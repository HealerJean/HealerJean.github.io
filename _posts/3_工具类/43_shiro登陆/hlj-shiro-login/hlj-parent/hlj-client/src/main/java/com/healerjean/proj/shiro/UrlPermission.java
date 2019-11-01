package com.healerjean.proj.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;


/**
 * @author HealerJean
 * @ClassName UrlPermission
 * @Date 2019-11-02  00:38.
 * @Description URL权限管理
 */

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlPermission implements Permission {

    private String uri;
    private String method;

    @Override
    public boolean implies(Permission permission) {
        if(!(permission instanceof UrlPermission)){
            return false;
        }
        UrlPermission urlPermission = (UrlPermission)permission;
        PatternMatcher patternMatcher = new AntPathMatcher();

        boolean matches = patternMatcher.matches(this.uri,urlPermission.getUri());
        matches = matches &
                (StringUtils.isNotBlank(this.method) && StringUtils.isNotBlank(urlPermission.getMethod())
                && this.method.toUpperCase().equals(urlPermission.getMethod().toUpperCase()));
        return matches;
    }
}
