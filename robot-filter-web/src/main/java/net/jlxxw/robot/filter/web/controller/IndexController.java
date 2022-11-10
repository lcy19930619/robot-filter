package net.jlxxw.robot.filter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import net.jlxxw.robot.filter.common.encrypt.DesEncryption;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.config.properties.ui.UiProperties;
import net.jlxxw.robot.filter.core.data.DataCore;
import net.jlxxw.robot.filter.core.exception.RuleException;
import net.jlxxw.robot.filter.core.vo.base.RequestResult;
import net.jlxxw.robot.filter.servlet.context.RobotServletFilterWebContext;
import net.jlxxw.robot.filter.web.aop.LoginCheck;
import net.jlxxw.robot.filter.web.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chunyang.leng
 * @date 2022-11-05 10:55 PM
 */
@Controller
@RequestMapping("ui")
public class IndexController {
    @Autowired
    private UiProperties properties;
    @Autowired
    private DesEncryption desEncryption;
    /**
     * all data
     */
    @Autowired(required = false)
    private List<DataCore> dataCoreList;

    @Operation(summary = "to index page")
    @GetMapping
    public String index() {
        return "index";
    }

    /**
     * login
     *
     * @param loginDTO form info
     * @return
     */
    @Operation(summary = "web ui login method")
    @ResponseBody
    @PostMapping(value = "login")
    public RequestResult<Boolean> login(@RequestBody LoginDTO loginDTO,
        @Parameter(hidden = true) HttpServletResponse response) throws Exception {
        String name = properties.getLoginName();
        String dbPassword = properties.getPassword();
        RuleProperties properties = new RuleProperties();
        properties.setReturnRejectMessage(true);
        properties.setContentType("application/json");
        properties.setHttpResponseCode(401);
        if (!name.equals(loginDTO.getLoginName())) {
            throw new RuleException("login failed", properties);
        }
        if (!securityEquals(dbPassword, loginDTO.getPassword())) {
            throw new RuleException("login failed", properties);
        }

        String format = LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String decrypt = desEncryption.encrypt(format);

        String host = RobotServletFilterWebContext.getHost();
        Cookie cookie = new Cookie("x-login", decrypt);
        cookie.setMaxAge(30 * 60);
        cookie.setDomain(host);
        cookie.setPath("/");
        response.addCookie(cookie);

        return RequestResult.success();
    }

    @LoginCheck
    @Operation(summary = "get current running information")
    @GetMapping("currentRunning")
    @ResponseBody
    public RequestResult getCurrentRunning(){

        return RequestResult.success();
    }

    @LoginCheck
    @Operation(summary = "get ip information")
    @GetMapping("info/ip/{ip}")
    @ResponseBody
    public RequestResult getIpInfo(@PathVariable("ip") String ip){

        return RequestResult.success();
    }

    @LoginCheck
    @Operation(summary = "get client id information")
    @GetMapping("info/clientId/{clientId}")
    @ResponseBody
    public RequestResult getClientIdInfo(@PathVariable("clientId") String clientId){

        return RequestResult.success();
    }


    private boolean securityEquals(String propertiesPassword, String password) {
        int length = propertiesPassword.length();

        boolean result = true;
        for (int i = 0; i < length; i++) {
            try {
                char o = propertiesPassword.charAt(i);
                char n = password.charAt(i);
                result = result && Objects.equals(o, n);
            } catch (IndexOutOfBoundsException e) {
                //ignore
                result = false;
            }
        }
        return result && length == password.length();
    }

}
