```log
2019-07-12 18:44:40.732 [main] INFO  com.hlj.proj.HljLogbakApplication - Starting HljLogbakApplication on MI-201902210704 with PID 17416 (started by HealerJean in D:\study\HealerJean.github.io\_posts\DDKJ\6、工具类\23、Logger日志\2_logbak\hlj-logbak) 
2019-07-12 18:44:40.736 [main] INFO  com.hlj.proj.HljLogbakApplication - No active profile set, falling back to default profiles: default 
2019-07-12 18:44:41.758 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8888"] 
2019-07-12 18:44:41.766 [main] INFO  org.apache.catalina.core.StandardService - Starting service [Tomcat] 
2019-07-12 18:44:41.767 [main] INFO  org.apache.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/9.0.21] 
2019-07-12 18:44:41.882 [main] INFO  o.a.c.core.ContainerBase.[Tomcat].[localhost].[/] - Initializing Spring embedded WebApplicationContext 
2019-07-12 18:44:42.217 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8888"] 
2019-07-12 18:44:42.247 [main] INFO  com.hlj.proj.HljLogbakApplication - Started HljLogbakApplication in 1.945 seconds (JVM running for 3.056) 
2019-07-12 18:44:48.236 [http-nio-8888-exec-1] INFO  o.a.c.core.ContainerBase.[Tomcat].[localhost].[/] - Initializing Spring DispatcherServlet 'dispatcherServlet' 
2019-07-12 18:44:48.267 [http-nio-8888-exec-1] INFO  com.hlj.proj.controller.LogbackController - info日志================== 
2019-07-12 18:44:48.267 [http-nio-8888-exec-1] WARN  com.hlj.proj.controller.LogbackController - warn日志==================== 
2019-07-12 18:44:48.268 [http-nio-8888-exec-1] ERROR com.hlj.proj.controller.LogbackController - error日志===================== 
2019-07-12 18:44:48.276 [http-nio-8888-exec-1] ERROR o.a.c.c.C.[.[localhost].[/].[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.ArithmeticException: / by zero] with root cause 
java.lang.ArithmeticException: / by zero
	at com.hlj.proj.controller.LogbackController.logback(LogbackController.java:27)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:897)

```
