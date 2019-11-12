package com.hlj.quartz.ddkj.monitor.quartz.listener;

import com.hlj.quartz.ddkj.monitor.AdmoreScheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * Created by j.sh on 2015/4/9
 * 容器初始化quartz
 */
@WebListener
public class QuartzListener extends QuartzInitializerListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        ServletContext ctx = servletContextEvent.getServletContext();
        StdSchedulerFactory schedulerFactory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);

        AdmoreScheduler.initialise(schedulerFactory);
    }

}
