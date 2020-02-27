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
 * @author HealerJean
 * @ClassName AuthWebSessionManager
 * @Date 2020/2/26  21:13.
 * @Description 管理session管理类
 */
@Slf4j
public class AuthWebSessionManager extends DefaultWebSessionManager {

    /**
     * 1、取出Session：当浏览器最开始访问的时候进入获取当前会话session(这个方法会被多次执行，反复执行)
     * 1.1、从浏览器/请求中中获取sessionId，如果浏览器中token为空的话session工厂中创建
     * 1.2、调用RedisSessionDao进行获取session   -》readSession
     * 1.3、如果获取不到session，抛出下面的异常
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
        Serializable sessionId = key.getSessionId();
        if (sessionId == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            sessionId = getSessionId(request, response);
        }
        return sessionId;
    }

    /**
     * 从浏览器中获取token
     */
    @Override
    protected Serializable getSessionId(ServletRequest servletRequest, ServletResponse response) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader(AuthConstants.HEADER_TOKEN_NAME);
        if (token != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());
        return token;
    }


}
