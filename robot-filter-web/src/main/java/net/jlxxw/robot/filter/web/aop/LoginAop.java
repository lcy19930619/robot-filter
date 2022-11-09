package net.jlxxw.robot.filter.web.aop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import net.jlxxw.robot.filter.common.encrypt.DesEncryption;
import net.jlxxw.robot.filter.web.exception.NoLoginException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author chunyang.leng
 * @date 2022-11-09 6:10 PM
 */
@Aspect
@Component
@Order(0)
public class LoginAop {
    @Autowired
    private DesEncryption desEncryption;
    @Pointcut("execution(public * net.jlxxw.robot.filter.web.controller.*.*.*(..))")
    public void pointcut() {

    }

    @Before(value = "pointcut()")
    public void validation() throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();

        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            throw new NoLoginException();
        }
        for (Cookie cookie : cookies) {
            if ("x-login".equals(cookie.getName())){
                String value = cookie.getValue();
                String decrypt = desEncryption.decrypt(value);
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime parse = LocalDateTime.parse(decrypt, df);
                LocalDateTime now = LocalDateTime.now();
                if (parse.isAfter(now)){
                    return;
                }
            }
        }
        throw new NoLoginException();
    }

}
