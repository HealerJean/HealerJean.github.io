package com.healerjean.proj.shiro;

import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

@Slf4j
public class UrlPermissionResolver implements PermissionResolver {

    @Override
    public Permission resolvePermission(String s) {
        UrlPermission urlPermission = JsonUtils.toObject(s, UrlPermission.class);
        return urlPermission;
    }
}
