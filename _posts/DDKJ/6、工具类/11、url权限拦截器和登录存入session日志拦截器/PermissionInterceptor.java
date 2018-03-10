package com.duodian.admore.config.interceptors;

import com.duodian.admore.admin.login.service.LoginService;
import com.duodian.admore.admin.utils.AppSessionHelper;
import com.duodian.admore.constants.AppConstants;
import com.duodian.admore.entity.db.admin.SysAdminUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * 类描述：
 * 创建人： j.sh
 * 创建时间： 2016/9/13
 * version：1.0.0
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);


    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String uri = httpServletRequest.getRequestURI();

        SysAdminUser adminUser = AppSessionHelper.getSessionUser();
        if(adminUser.getIsSuper() != null && adminUser.getIsSuper().intValue() == 1){
            return true;
        }

        Set<String> sysProtects = loginService.getSystemProtectUrl();
        Set<String> admProtects = loginService.getAdmUserProtectUrl(adminUser.getId());

        for (String c : sysProtects){
            if(StringUtils.isNotBlank(c)){
                if (uri.startsWith(c)){
                    if (admProtects.contains(c)){
                        return true;
                    } else {
                        logger.info("prefix:" + uri + ":" + c);
                        nopermission(httpServletRequest,httpServletResponse);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


    private void invalidate(HttpServletRequest request,HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        String ajax = request.getHeader("X-Requested-With");
        if (StringUtils.equals(ajax,"XMLHttpRequest")) {
            //设置登陆超时header
            response.addHeader("sessionout","true");
        } else {
            response.sendRedirect("/public/sessionout");
        }
    }

    private void nopermission(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String ajax = request.getHeader("X-Requested-With");
        if (StringUtils.equals(ajax,"XMLHttpRequest")){
            response.addHeader("nopermission","true");
        } else {
            response.sendRedirect("/public/nopermission");
        }
    }
}
