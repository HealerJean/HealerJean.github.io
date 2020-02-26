package com.healerjean.proj.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @ClassName AuthWebSessionManager
 * @Author TD
 * @Date 2019/1/28 15:31
 * @Description 管理session管理类
 */
@Slf4j
public class AuthWebSessionManager extends DefaultWebSessionManager {

    /**
     * 是否保存session到cookie中
     */
    private boolean sessionIdCookieEnabled;

    /**
     * 初始化session管理器
     * 1、初始化session的与系统关联
     * 2、设置sessionIdCookie，将sessionId存储到cookie中
     * @param authConfiguration
     */
    public AuthWebSessionManager(AuthConfiguration authConfiguration) {
        super();
        Cookie cookie = new SimpleCookie(authConfiguration.getClientName()+ "_SID");
        cookie.setHttpOnly(true);

        //设置sessionIdCookie
        super.setSessionIdCookie(cookie);
        this.setSessionIdCookie(cookie);
        //是否将sessionId存储到cookie中
        super.setSessionIdCookieEnabled(true);
        this.setSessionIdCookieEnabled(true);

        super.setSessionIdUrlRewritingEnabled(true);
        super.setSessionValidationSchedulerEnabled(false);
    }

    /**
     * 1、取出Session：当浏览器最开始访问的时候进入获取当前会话session(这个方法会被多次执行，反复执行)
     *  1.1、从浏览器/请求中中获取sessionId，如果浏览器中token为空的话session工厂中创建
     *  1.2、调用RedisSessionDao进行获取session   -》readSession
     *  1.3、如果获取不到session，抛出下面的异常
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        // 1.1、从浏览器/请求中中获取sessionId，如果浏览器中token为空的话session工厂中创建
        Serializable sessionId = getSessionId(sessionKey);
        if (sessionId == null) {
            log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a session could not be found.", sessionKey);
            return null;
        }
        // 1.2、调用RedisSessionDao进行获取session   -》readSession
        Session session = retrieveSessionFromDataSource(sessionId);

        // 1.3、如果获取不到session，抛出下面的异常
        if (session == null) {
            throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
        }
        return session;
    }



    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }


    /**
     * 从浏览器coolie中取sessionId
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = getSessionIdCookieValue(request, response);
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());
        return id;
    }

    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response) {
        if (!isSessionIdCookieEnabled()) {
            return null;
        }
        if (!(request instanceof HttpServletRequest)) {
            return null;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
    }










    @Override
    public boolean isSessionIdCookieEnabled() {
        return sessionIdCookieEnabled;
    }

    @Override
    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }


    @Override
    public void setGlobalSessionTimeout(long globalSessionTimeout) {
        super.setGlobalSessionTimeout(globalSessionTimeout);
    }


}
