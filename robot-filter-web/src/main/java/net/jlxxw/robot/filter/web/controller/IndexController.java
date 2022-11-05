package net.jlxxw.robot.filter.web.controller;

import java.util.List;
import net.jlxxw.robot.filter.core.data.DataCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chunyang.leng
 * @date 2022-11-05 10:55 PM
 */
@Controller
@RequestMapping("ui")
public class IndexController {

    /**
     * all data
     */
    @Autowired(required = false)
    private List<DataCore> dataCoreList;

    @GetMapping
    public String index(){
        return "index";
    }

    /**
     * todo check and ................
     * @param loginName
     * @param password
     * @return
     */
    @PostMapping("login")
    public String login(String loginName, String password){
        return "";
    }


}
