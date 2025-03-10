package com.healerjean.proj.shiro;

import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisSessionDao
 * @Author TD
 * @Date 2019/1/28 15:39
 * @Description Redis共享Session
 */
@Slf4j
public class RedisSessionDao extends AbstractSessionDAO {

    private RedisTemplate redisTemplate;
    private String keyPrefix;
    private long sessionExpire;
    private long sessionUserExpire;

    public RedisSessionDao(AuthConfiguration authConfiguration, RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.sessionExpire = authConfiguration.getSessionExpire();
        this.sessionUserExpire = authConfiguration.getSessionUserExpire();
        StringBuffer sb = new StringBuffer();
        sb.append(CommonConstants.REDIS_HLJ)
                .append(":")
                .append(authConfiguration.getClientName())
                .append(":Session:")
                .append(AuthConstants.SESSION_TYPE_COOKIE)
                .append(":");
        this.keyPrefix = sb.toString();
        super.setSessionIdGenerator(new UuidSessionIdGenerator());
    }

    /**
     * 对创建的sesion进行持久化
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = session.getId();
        if (session == null || sessionId == null || StringUtils.isBlank(sessionId.toString())) {
            //UuidSessionIdGenerator 进行创建
            sessionId = generateSessionId(session);

        }
        AuthSession authSession = toAuth(session);
        authSession.setId(sessionId.toString());
        this.update(authSession);
        return sessionId;
    }


    /**
     * SessionDAO中readSession实现; 通过sessionId从持久化设备获取session对象.
     * 子类doReadSession方法的代理，具体的获取细节委托给了子类的doReadSession方法.
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session s = doReadSession(sessionId);
        return s;
    }

    /**
     * 根据会话ID获取会话。
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        //这里需要判断是否需要去做Token的验证
        StringBuffer sessionPrefix = new StringBuffer();
        sessionPrefix.append(keyPrefix);
        sessionPrefix.append(sessionId);
        Object o = redisTemplate.opsForValue().get(sessionPrefix.toString());
        if (o instanceof Session) {
            return (Session) o;
        }
        return null;
    }


    /**
     * 更新session;
     * 如更新session最后访问时间/停止会话/设置超时时间/设置移除属性等会调用。
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }
        //这里对于session的保存需要把登陆和非登陆区分开来
        AuthSession authSession = toAuth(session);
        Serializable sessionId = authSession.getId();
        StringBuffer sessionPrefix = new StringBuffer();
        sessionPrefix.append(keyPrefix);
        sessionPrefix.append(sessionId);
        Long expire = sessionUserExpire;
        Object object = session.getAttribute(AuthConstants.AUTH_USER);
        if (object == null) {
            expire = sessionExpire;
        }
        redisTemplate.opsForValue().set(sessionPrefix.toString(), session, expire, TimeUnit.SECONDS);
    }


    /**
     * 删除session; 当会话过期/会话停止（如用户退出时）会调用。
     */
    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }
        AuthSession authSession = toAuth(session);
        Serializable sessionId = authSession.getId();
        StringBuffer sessionPrefix = new StringBuffer();
        sessionPrefix.append(keyPrefix);
        sessionPrefix.append(sessionId);
        redisTemplate.delete(sessionPrefix.toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        log.error("未重写方法：getActiveSessions 被执行");
        return null;
    }

    private AuthSession toAuth(Session session) {
        if (session instanceof AuthSession) {
            return (AuthSession) session;
        }
        throw new BusinessException(ResponseEnum.系统错误.code, "session实现类异常");
    }
}
