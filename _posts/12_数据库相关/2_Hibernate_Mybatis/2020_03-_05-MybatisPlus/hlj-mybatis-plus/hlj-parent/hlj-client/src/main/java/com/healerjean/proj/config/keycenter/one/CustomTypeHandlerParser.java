// package com.healerjean.proj.config.keycenter.one;
//
// import com.healerjean.proj.config.keycenter.one.AESTypeHandler;
// import org.apache.ibatis.session.SqlSessionFactory;
// import org.apache.ibatis.type.TypeHandlerRegistry;
// import org.springframework.beans.BeansException;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.ApplicationContextAware;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author HealerJean
//  * @ClassName CustomTypeHandlerParser
//  * @date 2020/4/9  18:02.
//  * @Description
//  */
// @Component
// public class CustomTypeHandlerParser implements ApplicationContextAware {
//
//
//
//     @Override
//     public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//         //从spring容器获取sqlSessionFactory
//         SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
//         //获取typeHandler注册器
//         TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
//         //注册List的typeHandler
//         typeHandlerRegistry.register(String.class, AESTypeHandler.class);
//     }
// }
