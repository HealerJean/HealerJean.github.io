---
title: SpringBoot源码阅读之依赖注入
date: 2020-06-23 03:33:00
tags: 
-  SpringBoot
category: 
-  SpringBoot
description: SpringBoot源码阅读之依赖注入
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



IOC容器对bean标签进行解析之后存入`Map`中的过程，这些`bean`只是以`BeanDefinition`为载体单纯的存储起来了，并没有转换成一个个的对象，今天继续进行跟踪，看一看IOC容器是怎样实例化对象的。

​    



前面我们对IoC容器的初始化过程进行了详细的分析，这个初始化过程完成的主要工作是在IoC容器中建立`BeanDefinition`数据映射。在此过程中并没有看到IoC容器对`Bean`依赖关系进行注入，接下来分析一下`IoC`容器是怎样对`Bean`的依赖关系进行注入的。              

前面在`refresh()`-->`invokeBeanFactoryPostProcessors(beanFactory`;方法中已经完成了IoC容器的初始化并已经载入了我们定义的Bean的信息（`BeanDefinition`），     

现在我们开始分析依赖注入的原理。首先需要说明的是依赖注入在用户第一次向`IoC`容器索要Bean时触发，当然也有例外，这个例外不介绍   





# 1、SpringBoot源码阅读之依赖注入  

## 1.1、`getBean()`

```java
// AbstractApplicationContext类
@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        ...
        try {
            ...
            // Instantiate all remaining (non-lazy-init) singletons.
            finishBeanFactoryInitialization(beanFactory);
            ...
        }
        ...
    }
}


// AbstractApplicationContext类
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    ...
    // Instantiate all remaining (non-lazy-init) singletons.
    // 实例化所有剩余的(non-lazy-init)单例。
    beanFactory.preInstantiateSingletons();
}




// DefaultListableBeanFactory类
@Override
public void preInstantiateSingletons() throws BeansException {
    ...
    // Iterate over a copy to allow for init methods which in turn register new bean definitions.
    // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
    List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
    // Trigger initialization of all non-lazy singleton beans...
    for (String beanName : beanNames) {
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
            if (isFactoryBean(beanName)) {
                Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
                if (bean instanceof FactoryBean) {
                    final FactoryBean<?> factory = (FactoryBean<?>) bean;
                    boolean isEagerInit;
                    if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                        isEagerInit = AccessController.doPrivileged((PrivilegedAction<Boolean>)
                                        ((SmartFactoryBean<?>) factory)::isEagerInit,
                                getAccessControlContext());
                    } else {
                        isEagerInit = (factory instanceof SmartFactoryBean &&
                                ((SmartFactoryBean<?>) factory).isEagerInit());
                    }
                    if (isEagerInit) {
                        getBean(beanName);
                    }
                }
            } else {
                // 这里就是触发依赖注入的地方
                getBean(beanName);
            }
        }
    }
    ...
}
```





![image-20201116163559163](/Users/healerjean/Desktop/Now/HealerJean.github.io/blogImages/image-20201116163559163.png)





跟踪其调用栈，`getBean(beanName);`方法，我们再梳理一下`getBean()`方法，前面总结过该方法在`IoC`容器的顶层接口`BeanFactory`中定义，然后在IoC容器的具体产品`DefaultListableBeanFactory`类的基类`AbstractBeanFactory`实现了`getBean()`方法。接着看代码。    



​    

```java
// AbstractBeanFactory类
@Override
public Object getBean(String name) throws BeansException {
    return doGetBean(name, null, null, false);
}
@Override
public <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException {
    return doGetBean(name, requiredType, null, false);
}
@Override
public Object getBean(String name, Object... args) throws BeansException {
    return doGetBean(name, null, args, false);
}
public <T> T getBean(String name, @Nullable Class<T> requiredType, @Nullable Object... args)
        throws BeansException {
    return doGetBean(name, requiredType, args, false);
}
```



从上面代码可知大致可分为两种获取Bean的参数，一种是按名获取，一种是按类获取。但是最终都进入到了`doGetBean()`方法。  

如果`#getSingleton()`为 null继续往下看，会在当前的`BeanFactory`中获取`BeanDefinition`，    

也就是这行方法代码：`final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);`在这行代码拿到`BeanDefinition`后，首先判断是不是`singleton Bean`，如果是的话，开始执行创建`Bea`n，    

正是`return createBean(beanName, mbd, args);`这行代码。   

如果是原型`(Prototype)Bean`我们就不分析了。原`型bean`每次执行`getBean()`都会创建一个实例。接下来我们看`createBean()`方法。



```java
// AbstractBeanFactory类
protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
        @Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {

    // bean获取过程：先获取bean名字
    // 会把带有&前缀的去掉，或者去aliasMap中找这个是不是别名，最终确定bean的id是什么
    final String beanName = transformedBeanName(name);
    Object bean;

    // 1.检查缓存中或者实例工厂中是否有对应的实例，将ObjectFactory加入到缓存中，一旦下一个要创建的bean需要依赖上个bean则直接使用ObjectFactory
    // 2.spring 默认是单例的，如果能获取到直接返回，提高效率。
    // Eagerly check singleton cache for manually registered singletons.
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        if (logger.isDebugEnabled()) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                        "' that is not fully initialized yet - a consequence of a circular reference");
            }
            else {
                logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
            }
        }
      
      
        // 用于检测bean的正确性，同时如果获取的是FactoryBean的话还需要调用getObject()方法获取最终的那个bean实例
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }

  
  
    else {

        // We're assumably within a circular reference.
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }

        // Check if bean definition exists in this factory.
        //这里对IoC容器中的BeanDefinition是否存在进行检查，检查是否能在当前的BeanFactory中取得需要的Bean。
        // 如果当前的工厂中取不到，则到双亲BeanFactory中去取。如果当前的双亲工厂取不到，那就顺着双亲BeanFactory
        // 链一直向上查找。
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            // Not found -> check parent.
            String nameToLookup = originalBeanName(name);
            if (parentBeanFactory instanceof AbstractBeanFactory) {
                // 递归调用父bean的doGetBean查找
                return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
                        nameToLookup, requiredType, args, typeCheckOnly);
            }
            else if (args != null) {
                // Delegation to parent with explicit args.
                return (T) parentBeanFactory.getBean(nameToLookup, args);
            }
            else {
                // No args -> delegate to standard getBean method.
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
        }

        if (!typeCheckOnly) {
            markBeanAsCreated(beanName);
        }

        try {
            //这里根据Bean的名字取得BeanDefinition
            final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            checkMergedBeanDefinition(mbd, beanName, args);

            // Guarantee initialization of beans that the current bean depends on.
            //获取当前Bean的所有依赖Bean，这里会触发getBean的递归调用。知道取到一个没有任何依赖的Bean为止。
            String[] dependsOn = mbd.getDependsOn();
            if (dependsOn != null) {
                for (String dep : dependsOn) {
                    if (isDependent(beanName, dep)) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                    }
                    registerDependentBean(dep, beanName);
                    try {
                        getBean(dep);
                    }
                    catch (NoSuchBeanDefinitionException ex) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                    }
                }
            }

            // 这里通过createBean方法创建singleton Bean的实例 这里还有一个回调函数
            // Create bean instance.
            if (mbd.isSingleton()) {
                sharedInstance = getSingleton(beanName, () -> {
                    try {
                        // 最后在getSingleton中又会调用这个方法
                        // TODO createBean的入口
                        return createBean(beanName, mbd, args);
                    }
                    catch (BeansException ex) {
                        // Explicitly remove instance from singleton cache: It might have been put there
                        // eagerly by the creation process, to allow for circular reference resolution.
                        // Also remove any beans that received a temporary reference to the bean.
                        destroySingleton(beanName);
                        throw ex;
                    }
                });
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }
            // 这里是创建prototype bean的地方
            else if (mbd.isPrototype()) {
                // It's a prototype -> create a new instance.
                Object prototypeInstance = null;
                try {
                    beforePrototypeCreation(beanName);
                    prototypeInstance = createBean(beanName, mbd, args);
                }
                finally {
                    afterPrototypeCreation(beanName);
                }
                bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
            }

            else {
                String scopeName = mbd.getScope();
                final Scope scope = this.scopes.get(scopeName);
                if (scope == null) {
                    throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                }
                try {
                    Object scopedInstance = scope.get(beanName, () -> {
                        beforePrototypeCreation(beanName);
                        try {
                            return createBean(beanName, mbd, args);
                        }
                        finally {
                            afterPrototypeCreation(beanName);
                        }
                    });
                    bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                }
                catch (IllegalStateException ex) {
                    throw new BeanCreationException(beanName,
                            "Scope '" + scopeName + "' is not active for the current thread; consider " +
                            "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                            ex);
                }
            }
        }
        catch (BeansException ex) {
            cleanupAfterBeanCreationFailure(beanName);
            throw ex;
        }
    }

    // Check if required type matches the type of the actual bean instance.
    //这里对创建的Bean进行类型检查，如果没有问题，就返回这个新创建的Bean，这个Bean已经是包含了依赖关系的Bean
    if (requiredType != null && !requiredType.isInstance(bean)) {
        try {
            T convertedBean = getTypeConverter().convertIfNecessary(bean, requiredType);
            if (convertedBean == null) {
                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
            return convertedBean;
        }
        catch (TypeMismatchException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to convert bean '" + name + "' to required type '" +
                        ClassUtils.getQualifiedName(requiredType) + "'", ex);
            }
            throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
        }
    }
    return (T) bean;
}
```





这个就是依赖注入的入口了，依赖注入是在容器的`BeanDefinition`数据已经建立好的前提下进行的。    

前面我们详细介绍了`BeanDefinition`的注册过程，`BeanDefinition`就是数据。如上面代码所示，`doGetBean()`方法不涉及复杂的算法，但是这个过程也不是很简单，因为我们都知道，对于IoC容器的使用，Spring提供了很多的配置参数，每一个配置参数实际上就代表了一个IoC容器的实现特征，这些特征很多都需要在依赖注入的过程或者对Bean进行生命周期管理的过程中完成。虽然我们可以简单的将IoC容器描述成一个ConcurrentHashMap，ConcurrentHashMap只是它的数据结构而不是IoC容器的全部。





## 1.2、`createBean()`的过程



> 前面说了`getBean()`是依赖注入的起点，之后会调用`createBean()`，下面通过`createBean()`代码来了解这个过程。在这个过程中，Bean对象会根据`BeanDefinition`定义的要求生成。



```java
// AbstractAutowireCapableBeanFactory类
@Override
protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
        throws BeanCreationException {
    ...
    try {
        // 验证以及准备override的方法
        mbdToUse.prepareMethodOverrides();
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(),
                beanName, "Validation of method overrides failed", ex);
    }
    try {
        // createBean之前调用BeanPostProcessor子类的InstantiationAwareBeanPostProcessor的postProcessBeforeInitialization和postProcessAfterInitialization方法
        // 默认不做任何处理所以会返回null
        // 但是如果我们重写了这两个方法，那么bean的创建过程就结束了，这里就为以后的annotation自动注入提供了钩子
        Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
        if (bean != null) {
            return bean;
        }
    }catch (Throwable ex) {
        throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName,
                "BeanPostProcessor before instantiation of bean failed", ex);
    }
    try {
        // 实际执行createBean的是doCreateBean()方法
        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
        if (logger.isDebugEnabled()) {
            logger.debug("Finished creating instance of bean '" + beanName + "'");
        }
        return beanInstance;
    }
    catch (BeanCreationException | ImplicitlyAppearedSingletonException ex) {
        // A previously detected exception with proper bean creation context already,
        // or illegal singleton state to be communicated up to DefaultSingletonBeanRegistry.
        throw ex;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                mbdToUse.getResourceDescription(), beanName, "Unexpected exception during bean creation", ex);
    }
}
```



```java
	@Nullable
	protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
		for (BeanPostProcessor bp : getBeanPostProcessors()) {
			if (bp instanceof InstantiationAwareBeanPostProcessor) {
				InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
				Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	
	
	
		@Override
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(result, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}
```



 　接着往下看doCreateBean()方法。

```java
// AbstractAutowireCapableBeanFactory类
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
        throws BeanCreationException {
    // BeanWrapper是用来持有创建出来的Bean对象
    // Instantiate the bean.
    BeanWrapper instanceWrapper = null;
    // 如果是单例，先把缓存中的同名Bean清除
    if (mbd.isSingleton()) {
        instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
    }
  
  
    // 这里是创建Bean的地方，由createBeanInstance完成。
    // TODO 完成Bean初始化过程的第一步：创建实例
    if (instanceWrapper == null) {
        instanceWrapper = createBeanInstance(beanName, mbd, args);
    }
    final Object bean = instanceWrapper.getWrappedInstance();
    Class<?> beanType = instanceWrapper.getWrappedClass();
    if (beanType != NullBean.class) {
        mbd.resolvedTargetType = beanType;
    }

    // Allow post-processors to modify the merged bean definition.
    synchronized (mbd.postProcessingLock) {
        if (!mbd.postProcessed) {
            try {
                applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
            }
            catch (Throwable ex) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                        "Post-processing of merged bean definition failed", ex);
            }
            mbd.postProcessed = true;
        }
    }

    // Eagerly cache singletons to be able to resolve circular references
    // even when triggered by lifecycle interfaces like BeanFactoryAware.
    // 是否自动解决循环引用
    // 当bean条件为： 单例&&允许循环引用&&正在创建中这样的话提早暴露一个ObjectFactory
    boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
            isSingletonCurrentlyInCreation(beanName));
    if (earlySingletonExposure) {
        if (logger.isDebugEnabled()) {
            logger.debug("Eagerly caching bean '" + beanName +
                    "' to allow for resolving potential circular references");
        }
        // 把ObjectFactory放进singletonFactories中
        // 这里在其他bean在创建的时候会先去singletonFactories中查找有没有beanName到ObjectFactory的映射
        // 如果有ObjectFactory就调用它的getObject方法获取实例
        // 但是在这里就可以对一个bean进行保证，代理等等AOP就可以在getEarlyBeanReference这里实现
        addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
    }

    // Initialize the bean instance.
    Object exposedObject = bean;
    try {
        // TODO 完成Bean初始化过程的第二步：为Bean的实例设置属性
        // Bean依赖注入发生的地方
        // 对bean进行属性填充，如果存在依赖于其他的bean的属性，则会递归的调用初始化依赖的bean
        populateBean(beanName, mbd, instanceWrapper);
        // TODO 完成Bean初始化过程的第三步：调用Bean的初始化方法（init-method）
        // 调用初始化方法，比如init-method方法指定的方法
        exposedObject = initializeBean(beanName, exposedObject, mbd);
    }
    catch (Throwable ex) {
        if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
            throw (BeanCreationException) ex;
        }
        else {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
        }
    }

    if (earlySingletonExposure) {
        Object earlySingletonReference = getSingleton(beanName, false);
        if (earlySingletonReference != null) {
            if (exposedObject == bean) {
                exposedObject = earlySingletonReference;
            }
            else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
                String[] dependentBeans = getDependentBeans(beanName);
                Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
                for (String dependentBean : dependentBeans) {
                    if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
                        actualDependentBeans.add(dependentBean);
                    }
                }
                if (!actualDependentBeans.isEmpty()) {
                    throw new BeanCurrentlyInCreationException(beanName,
                            "Bean with name '" + beanName + "' has been injected into other beans [" +
                            StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
                            "] in its raw version as part of a circular reference, but has eventually been " +
                            "wrapped. This means that said other beans do not use the final version of the " +
                            "bean. This is often the result of over-eager type matching - consider using " +
                            "'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
                }
            }
        }
    }

    // Register bean as disposable.
    try {
        // 注册销毁方法,比如：可以在配置bean的时候指定destory-method方法
        registerDisposableBeanIfNecessary(beanName, bean, mbd);
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanCreationException(
                mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
    }

    return exposedObject;
}
```



## 1.3、`createBeanInstance`

```java
// AbstractAutowireCapableBeanFactory类
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
    // Make sure bean class is actually resolved at this point.
    // 确认需要创建的Bean的实例的类可以实例化
    Class<?> beanClass = resolveBeanClass(mbd, beanName);

    if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
    }

    Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
    if (instanceSupplier != null) {
        return obtainFromSupplier(instanceSupplier, beanName);
    }

    // 当有工厂方法的时候使用工厂方法初始化Bean，就是配置的时候指定FactoryMethod属性，类似注解中的@Bean把方法的返回值作为Bean
    if (mbd.getFactoryMethodName() != null)  {
        return instantiateUsingFactoryMethod(beanName, mbd, args);
    }

    // Shortcut when re-creating the same bean...
    boolean resolved = false;
    boolean autowireNecessary = false;
    if (args == null) {
        synchronized (mbd.constructorArgumentLock) {
            if (mbd.resolvedConstructorOrFactoryMethod != null) {
                resolved = true;
                autowireNecessary = mbd.constructorArgumentsResolved;
            }
        }
    }
    if (resolved) {
        if (autowireNecessary) {
            // 如果有有参数的构造函数，构造函数自动注入
            // 这里spring会花费大量的精力去进行参数的匹配
            return autowireConstructor(beanName, mbd, null, null);
        }
        else {
            // 如果没有有参构造函数，使用默认构造函数构造
            return instantiateBean(beanName, mbd);
        }
    }

    // Need to determine the constructor...
    // 使用构造函数进行实例化，正常是这里
    Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
    if (ctors != null ||
            mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
            mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
        return autowireConstructor(beanName, mbd, ctors, args);
    }

    // No special handling: simply use no-arg constructor.
    // 使用默认的构造函数对Bean进行实例化
    return instantiateBean(beanName, mbd);
}
```



我们可以看到在`instantiateBean()`方法中生成了`Bean`所包含的Java对象，这个对象的生成有很多种不同的方式，可以通过工厂方法生成，也可以通过容器的`autowire`特性生成，这些生成方式都是由`BeanDefinition`决定的。对于上面我们的`WebController`和`WebService`两个类是通过最后一行，使用默认的构造函数进行Bean的实例化。

```java
// AbstractAutowireCapableBeanFactory类
protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
    // 使用默认的实例化策略对Bean进行实例化，默认的实例化策略是CglibSubclassingInstantiationStrategy，
    // 也就是常说的CGLIB来对Bean进行实例化。PS：面试官常问的字节码增强
    try {
        Object beanInstance;
        final BeanFactory parent = this;
        if (System.getSecurityManager() != null) {
            beanInstance = AccessController.doPrivileged((PrivilegedAction<Object>) () ->
                    getInstantiationStrategy().instantiate(mbd, beanName, parent),
                    getAccessControlContext());
        }
        else {
            // getInstantiationStrategy()会返回CglibSubclassingInstantiationStrategy类的实例
            beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
        }
        BeanWrapper bw = new BeanWrapperImpl(beanInstance);
        initBeanWrapper(bw);
        return bw;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
    }
}
```





这里使用`CGLIB`进行`Bean`的实例化。`CGLIB`是一个常用的字节码生成器的类库，其提供了一系列的API来提供生成和转换Java字节码的功能。    

在Spring AOP中同样也是使用的CGLIB对Java的字节码进行增强。在IoC容器中，使用`SimpleInstantiationStrategy`类。这个类是Spring用来生成Bean对象的默认类，它提供了两种实例化Java对象的方法，一种是通过`BeanUtils`，它使用的是JVM的反射功能，一种是通过`CGLIB`来生成。`getInstantiationStrategy()`方法获取到`CglibSubclassingInstantiationStrategy`实例，`instantiate()`是`CglibSubclassingInstantiationStrategy`类的父类`SimpleInstantiationStrategy`实现的。



```java
// SimpleInstantiationStrategy类
@Override
public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
    // Don't override the class with CGLIB if no overrides.
    // 如果BeanFactory重写了Bean内的方法，则使用CGLIB，否则使用BeanUtils，我们这里使用BeanUtils方式
    if (!bd.hasMethodOverrides()) {
        // 如果bean没有需要动态替换的方法就直接反射进行创建实例
        Constructor<?> constructorToUse;
        synchronized (bd.constructorArgumentLock) {
            constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;
            if (constructorToUse == null) {
                final Class<?> clazz = bd.getBeanClass();
                if (clazz.isInterface()) {
                    throw new BeanInstantiationException(clazz, "Specified class is an interface");
                }
                try {
                    if (System.getSecurityManager() != null) {
                        constructorToUse = AccessController.doPrivileged(
                                (PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
                    } else {
                        constructorToUse = clazz.getDeclaredConstructor();
                    }
                    bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                } catch (Throwable ex) {
                    throw new BeanInstantiationException(clazz, "No default constructor found", ex);
                }
            }
        }
        // 通过BeanUtils进行实例化，这个BeanUtils的实例化通过Constructor类实例化Bean
        // 在BeanUtils中可以看到具体的调用ctor.newInstances(args)
        return BeanUtils.instantiateClass(constructorToUse);
    } else {
        // Must generate CGLIB subclass.
        // TODO 使用CGLIB实例化对象
        return instantiateWithMethodInjection(bd, beanName, owner);
    }
}
```



## 1.4、`populateBean()`;属性设置（依赖注入）



```java
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
    if (bw == null) {
        if (mbd.hasPropertyValues()) {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
        }
        else {
            // Skip property population phase for null instance.
            return;
        }
    }
    // Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
    // state of the bean before properties are set. This can be used, for example,
    // to support styles of field injection.
    boolean continueWithPropertyPopulation = true;
    // 调用InstantiationAwareBeanPostProcessor  Bean的后置处理器，在Bean注入属性前改变BeanDefinition的信息
    if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
    }
    if (!continueWithPropertyPopulation) {
        return;
    }
    // 这里取得在BeanDefinition中设置的property值，这些property来自对BeanDefinition的解析
    // 用于在配置文件中通过<property>配置的属性并且显示在配置文件中配置了autowireMode属性
    PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null);
    if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME ||
            mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {
        MutablePropertyValues newPvs = new MutablePropertyValues(pvs);

        // Add property values based on autowire by name if applicable.
        // 这里对autowire注入的处理，autowire by name
        if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME) {
            autowireByName(beanName, mbd, bw, newPvs);
        }

        // Add property values based on autowire by type if applicable.
        // 这里对autowire注入的处理， autowire by type
        // private List<Test> tests;
        if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {
            autowireByType(beanName, mbd, bw, newPvs);
        }

        pvs = newPvs;
    }
    boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
    boolean needsDepCheck = (mbd.getDependencyCheck() != RootBeanDefinition.DEPENDENCY_CHECK_NONE);
    if (hasInstAwareBpps || needsDepCheck) {
        if (pvs == null) {
            pvs = mbd.getPropertyValues();
        }
        PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
        if (hasInstAwareBpps) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                    // TODO @Autowire（AutowiredAnnotationBeanPostProcessor类） @Resource @Value @Inject 等注解的依赖注入过程
                    pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                    if (pvs == null) {
                        return;
                    }
                }
            }
        }
        if (needsDepCheck) {
            checkDependencies(beanName, mbd, filteredPds, pvs);
        }
    }
  

  if (pvs != null) {
        // 注入配置文件中<property>配置的属性
        applyPropertyValues(beanName, mbd, bw, pvs);
    }
}
```





applyPropertyValues()方法基本都是用于SpringMVC中采用xml配置Bean的方法。所以我们不做介绍了。看注释知道干嘛的就行了。我们主要看的是pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);这行代码，这行代码是真正执行采用@Autowire @Resource @Value @Inject 等注解的依赖注入过程。

```java
// AutowiredAnnotationBeanPostProcessor类
@Override
public PropertyValues postProcessPropertyValues(
        PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {
    //findAutowiringMetadata(beanName, bean.getClass(), pvs);方法会寻找在当前类中的被@Autowire，@Resource，@Value，@Inject等具备注入功能的注解的属性。
    // 遍历，获取@Autowire，@Resource，@Value，@Inject等具备注入功能的注解
    InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
    try {
        // 属性注入
        metadata.inject(bean, beanName, pvs);
    } catch (BeanCreationException ex) {
        throw ex;
    } catch (Throwable ex) {
        throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
    }
    return pvs;
}
```



`AutowiredAnnotationBeanPostProcessor`类实现了`postProcessPropertyValues()`方法。`findAutowiringMetadata(beanName`, `bean.getClass(), pvs`);方法会寻找在当前类中的被@Autowire，@Resource，@Value，@Inject等具备注入功能的注解的属性。

![image-20201116191831014](/Users/healerjean/Desktop/Now/HealerJean.github.io/blogImages/image-20201116191831014.png)



`metadata.inject(bean, beanName, pvs);`方法开始执行注入的逻辑。



```java
	public void inject(Object target, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
		Collection<InjectedElement> checkedElements = this.checkedElements;
		Collection<InjectedElement> elementsToIterate =
				(checkedElements != null ? checkedElements : this.injectedElements);
		if (!elementsToIterate.isEmpty()) {
			for (InjectedElement element : elementsToIterate) {
				if (logger.isTraceEnabled()) {
					logger.trace("Processing injected element of bean '" + beanName + "': " + element);
				}
        //AutowiredAnnotationBeanPostProcessor类
				element.inject(target, beanName, pvs);
			}
		}
	}
```



```java
// AutowiredAnnotationBeanPostProcessor类
@Override
protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
    // 需要注入的字段
    Field field = (Field) this.member;
    // 需要注入的属性值
    Object value;
    if (this.cached) {
        value = resolvedCachedArgument(beanName, this.cachedFieldValue);
    } else {
        // @Autowired(required = false)，当在该注解中设置为false的时候，如果有直接注入，没有跳过，不会报错。
        DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
        desc.setContainingClass(bean.getClass());
        Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
        Assert.state(beanFactory != null, "No BeanFactory available");
        TypeConverter typeConverter = beanFactory.getTypeConverter();
        try {
            // 通过BeanFactory 解决依赖关系
            // 比如在webController中注入了webService，这个会去BeanFactory中去获取webService，也就是getBean()的逻辑。
            // 如果存在直接返回，不存在再执行createBean()逻辑。
            // 如果在webService中依然依赖，依然会去递归。
            // 这里是一个复杂的递归逻辑。
            value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
        } catch (BeansException ex) {
            throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
        }
        synchronized (this) {
            if (!this.cached) {
                if (value != null || this.required) {
                    this.cachedFieldValue = desc;
                    registerDependentBeans(beanName, autowiredBeanNames);
                    if (autowiredBeanNames.size() == 1) {
                        String autowiredBeanName = autowiredBeanNames.iterator().next();
                        if (beanFactory.containsBean(autowiredBeanName) &&
                                beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                            this.cachedFieldValue = new ShortcutDependencyDescriptor(
                                    desc, autowiredBeanName, field.getType());
                        }
                    }
                } else {
                    this.cachedFieldValue = null;
                }
                this.cached = true;
            }
        }
    }
    if (value != null) {
        ReflectionUtils.makeAccessible(field);
        field.set(bean, value);
    }
}
```



看这行代码：`value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);`注   

意beanFactory依旧是我们熟悉的IoC容器的具体产品，也就是实现类DefaultListableBeanFactory。见到就说一遍，方便大家记住它，很重要。

　　在resolveDependency()方法中经过一顿操作，最终又会来到上面的getBean()方法。以上就是依赖注入的整个过程。注意看代码中的注释哦。



## 1.5、`initializeBean()`：调用Bean的初始化方法

> `bean`实现`InitializingBean`接口重写afterPropertiesSet()方法。



```java
// AbstractAutowireCapableBeanFactory类
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
  if (System.getSecurityManager() != null) {
    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
      invokeAwareMethods(beanName, bean);
      return null;
    }, getAccessControlContext());
  }
  else {
    //在调用Bean的初始化方法之前，调用一系列的aware接口实现，把相关的BeanName，BeanClassLoader，以及BeanFactory注入到Bean中去。
    invokeAwareMethods(beanName, bean);
  }

  Object wrappedBean = bean;
  if (mbd == null || !mbd.isSynthetic()) {
    // 这些都是钩子方法，在反复的调用，给Spring带来了极大的可拓展性
    // 初始化之前调用BeanPostProcessor （包括 ApplicationContextAwareProcessor）
    wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
  }

  try {
    // 调用指定的init-method方法
    invokeInitMethods(beanName, wrappedBean, mbd);
  }
  catch (Throwable ex) {
    throw new BeanCreationException(
      (mbd != null ? mbd.getResourceDescription() : null),
      beanName, "Invocation of init method failed", ex);
  }
  if (mbd == null || !mbd.isSynthetic()) {
    // 这些都是钩子方法，在反复的调用，给Spring带来了极大的可拓展性
    // 初始化之后调用BeanPostProcessor 后置处理器
    wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
  }

  return wrappedBean;
}




@Override
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
  throws BeansException {

  Object result = existingBean; 
  //包括 ApplicationContextAwareProcessor
  for (BeanPostProcessor processor : getBeanPostProcessors()) {
    Object current = processor.postProcessBeforeInitialization(result, beanName);
    if (current == null) {
      return result;
    }
    result = current;
  }
  return result;
}




protected void invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
  throws Throwable {

  boolean isInitializingBean = (bean instanceof InitializingBean);
  if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
    if (logger.isTraceEnabled()) {
      logger.trace("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
    }
    if (System.getSecurityManager() != null) {
      try {
        AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
          ((InitializingBean) bean).afterPropertiesSet();
          return null;
        }, getAccessControlContext());
      }
      catch (PrivilegedActionException pae) {
        throw pae.getException();
      }
    }
    else {
      ((InitializingBean) bean).afterPropertiesSet();
    }
  }

  if (mbd != null && bean.getClass() != NullBean.class) {
    String initMethodName = mbd.getInitMethodName();
    if (StringUtils.hasLength(initMethodName) &&
        !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
        !mbd.isExternallyManagedInitMethod(initMethodName)) {
      invokeCustomInitMethod(beanName, bean, mbd);
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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 
