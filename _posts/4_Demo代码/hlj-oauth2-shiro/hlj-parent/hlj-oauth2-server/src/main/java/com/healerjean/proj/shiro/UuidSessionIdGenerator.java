package com.healerjean.proj.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

public class UuidSessionIdGenerator implements SessionIdGenerator {
    @Override
    public Serializable generateId(Session session) {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
