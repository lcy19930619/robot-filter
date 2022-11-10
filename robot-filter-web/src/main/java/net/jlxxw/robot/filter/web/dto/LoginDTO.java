package net.jlxxw.robot.filter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author chunyang.leng
 * @date 2022-11-10 12:57 PM
 */
@Schema(name = "login data object")
public class LoginDTO {
    @Schema(description = " login name ",required = true)
    private String loginName;
    @Schema(description = " password ",required = true)
    private String password;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
