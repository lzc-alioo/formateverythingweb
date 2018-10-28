/**
 *
 */
package com.alioo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    /**
     * 根据Cookie名获取指定的Cookie值
     *
     * @param request
     * @param name
     * @return
     * @author mahaiyuan
     * @date 2015年11月6日 下午1:41:04
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();    //获取所有的Cookie值
        if (null == cookies) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;    //返回获取到的Cookie值
            }
        }
        return null;
    }

    /**
     * 获取指定名称Cookie的值
     *
     * @param request
     * @param name    cookie名
     * @return
     * @author mahaiyuan
     * @date 2015年11月6日 下午1:41:55
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (null == cookie) {
            return null;
        }
        return cookie.getValue();
    }

    public static void setCookie(HttpServletResponse response, String domain, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String domain, String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void setCookieArr(HttpServletResponse response, String domain, String name, String value, int
        expiry) {
        String[] domains = domain.split(",");
        for (String domainString : domains) {
            setCookie(response, domainString, name, value, expiry);
        }
    }

    public static void deleteCookie(HttpServletResponse response, String domain, String name) {
        String[] domains = domain.split(",");
        for (String domainString : domains) {
            Cookie cookie = new Cookie(name, "");
            cookie.setMaxAge(0);    //删除cookie
            cookie.setDomain(domainString);
            cookie.setPath("/");
            response.addCookie(cookie);
            logger.info("[deleteCookieFromDomain] domain:{}", domainString);
        }

    }

}
