---
title: SpringBoot中Configuration和Component注解的区别
date: 2020-06-11 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot中Configuration和Component注解的区别
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、`@Configuration`和@`Component`区别源码解析

> 为什么这么搞呢？    
>
> `@Configuration`它本身就是基于配置的，内部肯定会有一些相互调用，为了保证是单例的，所以这样实现了。    
>
> 而`@Component`是基于类而已，大概意思就是只要使用`@Component`注释了，大概率上是希望被扫描到。至于是不是单例，不关心。  



## 1.1、找出带有`@Configuration`的的类，使它变成增强类  



> `ConfigurationClassPostProcessor `后置处理器  中的方法`enhanceConfigurationClasses`，使用`cglib`动态代理增强类替换原有的类      
>
> 第一个for循环中，查找到所有带有 `@Configuration` 注解的 bean 定义，然后放入`configBeanDefs ,Map`中：        
>
> 第二个for循环，对带有`@Configuration`的类进行增强，具体就是，返回返回 Enhancer类替代原有的列，内部加入一些拦截器等    



```java
public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
    Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap<String, AbstractBeanDefinition>();

    //在方法的第一个for循环中，查找到所有带有 @Configuration 注解的 bean 定义，然后放入configBeanDefs ,Map中：
    for (String beanName : beanFactory.getBeanDefinitionNames()) {
        BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
        //判断是类否有@Configuration注解
        if (ConfigurationClassUtils.isFullConfigurationClass(beanDef)) {
            //省略部分代码
            configBeanDefs.put(beanName, (AbstractBeanDefinition) beanDef);
        }
    }
    if (configBeanDefs.isEmpty()) {
        // nothing to enhance -> return immediately
        return;
    }




    //第二个for循环，对带有@Configuration的类进行增强，具体就是，返回返回 Enhancer类替代原有的列，内部加入一些拦截器等
    ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
    for (Map.Entry<String, AbstractBeanDefinition> entry : configBeanDefs.entrySet()) {
        AbstractBeanDefinition beanDef = entry.getValue();
        // If a @Configuration class gets proxied, always proxy the target class
        beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
        try {
            // Set enhanced subclass of the user-specified bean class
            Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
            //对配置类进行增强
            Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
            if (configClass != enhancedClass) {
                //省略部分代码
                beanDef.setBeanClass(enhancedClass);
            }
        }
        catch (Throwable ex) {
            throw new IllegalStateException(
                "Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
        }
    }
}

```





**对原有的类进行增强，形成增强类，然后使用增强后的类替换了原有的 `beanClass`：，当下面两行执行完成后带有@Configuration的类就变成了增强类**

```java
Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
```

```java
beanDef.setBeanClass(enhancedClass);
```



**类：`ConfigurationClassEnhancer`：包含增强类的实现，以及增强类内部的一些东西，比如cglib代理的类，会通过CallbackFilter会进行调用，这里会指定这个调用类的CallbackFilter**

```java
public Class<?> enhance(Class<?> configClass, @Nullable ClassLoader classLoader) {
    if (EnhancedConfiguration.class.isAssignableFrom(configClass)) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Ignoring request to enhance %s as it has " +
                                       "already been enhanced. This usually indicates that more than one " +
                                       "ConfigurationClassPostProcessor has been registered (e.g. via " +
                                       "<context:annotation-config>). This is harmless, but you may " +
                                       "want check your configuration and remove one CCPP if possible",
                                       configClass.getName()));
        }
        return configClass;
    }
    //创建代理类增强类
    Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
    if (logger.isTraceEnabled()) {
        logger.trace(String.format("Successfully enhanced %s; enhanced class name is: %s",
                                   configClass.getName(), enhancedClass.getName()));
    }
    return enhancedClass;
}
```





**构造一个cglib增强类，**

```java
//指定cglib代理的增强类 在调用方法的时候，会通过CallbackFilter 调用，我们这里放入该接口
enhancer.setCallbackFilter(CALLBACK_FILTER);
```




```java
private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(configSuperClass);
		enhancer.setInterfaces(new Class<?>[] {EnhancedConfiguration.class});
		enhancer.setUseFactory(false);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
		enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));
      //指定cglib代理的增强类 在调用方法的时候，会通过CallbackFilter 调用，我们这里放入该接口
		enhancer.setCallbackFilter(CALLBACK_FILTER);
		enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
		return enhancer;
	}
```



**`CallbackFilter`，具体的实现类是`ConditionalCallbackFilter`，同时它也是我们增强配置类`ConfigurationClassEnhancer`的内部类**

```java
	private static final Callback[] CALLBACKS = new Callback[] {
			new BeanMethodInterceptor(),
			new BeanFactoryAwareMethodInterceptor(),
			NoOp.INSTANCE
	};

	private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);

```



## 1.2、Clglib动态代理调用增强类，选择带有`@Bean`注释的方法，准备创建Bean 

> cglib动态代理调用增强类`Enhancer`，,增强类内部有`emitMethods`作为调用的入口。     
>
> 然后调用`ConditionalCallbackFilter的accept方法`，判断是否有`@Bean`注解（千万注意，这里就是正常的`@Bean`哦）并返回拦截器的 索引，这个拦截器将会在创建Bean类的时候会执行  
>
> 

```java
 int index = this.filter.accept(actualMethod);
```



```java
private void emitMethods(ClassEmitter ce, List methods, List actualMethods) {
    CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
    Map groups = new HashMap();
    final Map indexes = new HashMap();
    final Map originalModifiers = new HashMap();
    final Map positions = CollectionUtils.getIndexMap(methods);
    Map declToBridge = new HashMap();
    Iterator it1 = methods.iterator();
    Iterator it2 = actualMethods != null ? actualMethods.iterator() : null;

    while(it1.hasNext()) {
        MethodInfo method = (MethodInfo)it1.next();
        Method actualMethod = it2 != null ? (Method)it2.next() : null;
        //调用
        int index = this.filter.accept(actualMethod);
        if (index >= this.callbackTypes.length) {
            throw new IllegalArgumentException("Callback filter returned an index that is too large: " + index);
        }

        originalModifiers.put(method, actualMethod != null ? actualMethod.getModifiers() : method.getModifiers());
        indexes.put(method, index);
        List group = (List)groups.get(generators[index]);
        if (group == null) {
            groups.put(generators[index], group = new ArrayList(methods.size()));
        }

        ((List)group).add(method);
        if (TypeUtils.isBridge(actualMethod.getModifiers())) {
            Set bridges = (Set)declToBridge.get(actualMethod.getDeclaringClass());
            if (bridges == null) {
                bridges = new HashSet();
                declToBridge.put(actualMethod.getDeclaringClass(), bridges);
            }

            ((Set)bridges).add(method.getSignature());
        }
    }

    //具体初始化Bean逻辑
    ………………………………

        se.return_value();
    se.end_method();
}
```





`ConditionalCallbackFilter`,中的`accept`方法，判断是方法上否有@Bean定义，如果有的话，就返回，将来要初始化成Bean类。  



```java
private static class ConditionalCallbackFilter implements CallbackFilter {

    private final Callback[] callbacks;

    private final Class<?>[] callbackTypes;

    public ConditionalCallbackFilter(Callback[] callbacks) {
        this.callbacks = callbacks;
        this.callbackTypes = new Class<?>[callbacks.length];
        for (int i = 0; i < callbacks.length; i++) {
            this.callbackTypes[i] = callbacks[i].getClass();
        }
    }

    @Override
    public int accept(Method method) {
        for (int i = 0; i < this.callbacks.length; i++) {
            Callback callback = this.callbacks[i];
            if (!(callback instanceof ConditionalCallback) || ((ConditionalCallback) callback).isMatch(method)) {
                return i;
            }
        }
        throw new IllegalStateException("No callback available for method " + method.getName());
    }

    public Class<?>[] getCallbackTypes() {
        return this.callbackTypes;
    }
}
```



其中 `BeanMethodInterceptor` 匹配方法如下：



```java
@Override
public boolean isMatch(Method candidateMethod) {
    return (candidateMethod.getDeclaringClass() != Object.class &&
            !BeanFactoryAwareMethodInterceptor.isSetBeanFactory(candidateMethod) &&
            BeanAnnotationHelper.isBeanAnnotated(candidateMethod));
}





public static boolean isBeanAnnotated(Method method) {
    return AnnotatedElementUtils.hasAnnotation(method, Bean.class);
}
```



另一个 `BeanFactoryAwareMethodInterceptor` 匹配的方法如下，这个貌似在我们现在的测试中也用不到：

```java
@Override
public boolean isMatch(Method candidateMethod) {
    return isSetBeanFactory(candidateMethod);
}

public static boolean isSetBeanFactory(Method candidateMethod) {
    return (candidateMethod.getName().equals("setBeanFactory") &&
            candidateMethod.getParameterCount() == 1 &&
            BeanFactory.class == candidateMethod.getParameterTypes()[0] &&
            BeanFactoryAware.class.isAssignableFrom(candidateMethod.getDeclaringClass()));
}
```



## 1.3、拦截器开始创建Bean

> 上面我们知道带有`@Configuration` 以及@Bean的类是如何被识别的。现在我们开始关注`BeanMethodInterceptor`，开始带有@Bean注解的方法执行逻辑 ，也就是创建Bean类     



```java
private static class BeanMethodInterceptor implements MethodInterceptor, ConditionalCallback {

		/**
		 * Enhance a {@link Bean @Bean} method to check the supplied BeanFactory for the
		 * existence of this bean object.
		 * @throws Throwable as a catch-all for any exception that may be thrown when invoking the
		 * super implementation of the proxied method i.e., the actual {@code @Bean} method
		 */
		@Override
		@Nullable
		public Object intercept(Object enhancedConfigInstance, Method beanMethod, Object[] beanMethodArgs,
					MethodProxy cglibMethodProxy) throws Throwable {

            //首先通过反射从增强的 Configuration 注解类中获取 beanFactory
			ConfigurableBeanFactory beanFactory = getBeanFactory(enhancedConfigInstance);
			String beanName = BeanAnnotationHelper.determineBeanNameFor(beanMethod);

            //然后通过方法获取 beanName，默认为方法名，可以通过 @Bean 注解指定
			if (BeanAnnotationHelper.isScopedProxy(beanMethod)) {
				String scopedBeanName = ScopedProxyCreator.getTargetBeanName(beanName);
				if (beanFactory.isCurrentlyInCreation(scopedBeanName)) {
					beanName = scopedBeanName;
				}
			}

			//确定这个 bean 是否指定了代理的范围
			//默认下面 if 条件 false 不会执行
			if (factoryContainsBean(beanFactory, BeanFactory.FACTORY_BEAN_PREFIX + beanName) &&
					factoryContainsBean(beanFactory, beanName)) {
				Object factoryBean = beanFactory.getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
				if (factoryBean instanceof ScopedProxyFactoryBean) {
					// Scoped proxy factory beans are a special case and should not be further proxied
				}
				else {
					// It is a candidate FactoryBean - go ahead with enhancement
					return enhanceFactoryBean(factoryBean, beanMethod.getReturnType(), beanFactory, beanName);
				}
			}

            
            //重头戏来了，判断当前执行的方法是否为正在执行的 @Bean 方法，如果是的话，则执行，
            //appBean() 方法首次进入的时候，会执行下面的cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
            //appBean() 内部方法调用到dataBean()的时候，还会进来（请记住因为有拦截器的关系，dataBean()上面就有@Bean，如果是普通new就不会进来了），这个时候就是false，然后会执行 resolveBeanReference方法
			if (isCurrentlyInvokedFactoryMethod(beanMethod)) {
				// The factory is calling the bean method in order to instantiate and register the bean
				// (i.e. via a getBean() call) -> invoke the super implementation of the method to actually
				// create the bean instance.
				if (logger.isInfoEnabled() &&
						BeanFactoryPostProcessor.class.isAssignableFrom(beanMethod.getReturnType())) {
					logger.info(String.format("@Bean method %s.%s is non-static and returns an object " +
									"assignable to Spring's BeanFactoryPostProcessor interface. This will " +
									"result in a failure to process annotations such as @Autowired, " +
									"@Resource and @PostConstruct within the method's declaring " +
									"@Configuration class. Add the 'static' modifier to this method to avoid " +
									"these container lifecycle issues; see @Bean javadoc for complete details.",
							beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName()));
				}
               //直接调用原方法创建 bean
				return cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
			}

            //如果不满足上面 if，也就是在 appBean() 中调用的 dataBean() 方法
			return resolveBeanReference(beanMethod, beanMethodArgs, beanFactory, beanName);
		}
```



`obtainBeanInstanceFromFactory` 方法比较简单，就是通过 `beanFactory.getBean` 获取 `Country`，如果已经创建了就会直接返回，如果没有执行过，就会通过 `invokeSuper` 首次执行。   



```java
private Object resolveBeanReference(Method beanMethod, Object[] beanMethodArgs,
                                    ConfigurableBeanFactory beanFactory, String beanName) {

    boolean alreadyInCreation = beanFactory.isCurrentlyInCreation(beanName);
    try {
        if (alreadyInCreation) {
            beanFactory.setCurrentlyInCreation(beanName, false);
        }
        boolean useArgs = !ObjectUtils.isEmpty(beanMethodArgs);
        if (useArgs && beanFactory.isSingleton(beanName)) {
            
            for (Object arg : beanMethodArgs) {
                if (arg == null) {
                    useArgs = false;
                    break;
                }
            }
        }
        
        //通过beanFactory.getBean(beanName)); 获取Bean，如果Spring容器已经创建了就直接返回，如果没有执行过，则还是会调用上面的方法，再次进入拦截器进行创建，通过invokeSuper调用执行。创建出Bean来
        Object beanInstance = (useArgs ? beanFactory.getBean(beanName, beanMethodArgs) :
                               beanFactory.getBean(beanName));
        if (!ClassUtils.isAssignableValue(beanMethod.getReturnType(), beanInstance)) {
            // Detect package-protected NullBean instance through equals(null) check
            if (beanInstance.equals(null)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("@Bean method %s.%s called as bean reference " +
                                               "for type [%s] returned null bean; resolving to null value.",
                                               beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName(),
                                               beanMethod.getReturnType().getName()));
                }
                beanInstance = null;
            }
            else {
                String msg = String.format("@Bean method %s.%s called as bean reference " +
                                           "for type [%s] but overridden by non-compatible bean instance of type [%s].",
                                           beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName(),
                                           beanMethod.getReturnType().getName(), beanInstance.getClass().getName());
                try {
                    BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
                    msg += " Overriding bean of same name declared in: " + beanDefinition.getResourceDescription();
                }
                catch (NoSuchBeanDefinitionException ex) {
                    // Ignore - simply no detailed message then.
                }
                throw new IllegalStateException(msg);
            }
        }
        Method currentlyInvoked = SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod();
        if (currentlyInvoked != null) {
            String outerBeanName = BeanAnnotationHelper.determineBeanNameFor(currentlyInvoked);
            beanFactory.registerDependentBean(beanName, outerBeanName);
        }
        return beanInstance;
    }
    finally {
        if (alreadyInCreation) {
            beanFactory.setCurrentlyInCreation(beanName, true);
        }
    }
}
```









![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'NKlz4RvMU8gqBbIx',
    });
    gitalk.render('gitalk-container');
</script> 

