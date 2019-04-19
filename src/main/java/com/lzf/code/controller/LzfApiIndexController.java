package com.lzf.code.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-16 22:54
 *
 * @author Li Zhenfeng
 */

@Controller
public class LzfApiIndexController {
    @Value("${api-username:cleancode}")
    private String apiUsername;
    @Value("${api-password:cleancode}")
    private String apiPassword;

    @GetMapping("api")
    public String apiIndex(HttpSession session) {
        if (StringUtils.isEmpty(apiUsername) || StringUtils.isEmpty(apiPassword)) {
            return "api/index";
        }
        if (Objects.isNull(session.getAttribute("api-user-info"))) {
            return "api/login";
        }
        return "api/index";
    }

    @PostMapping("api/login")
    public String apiLogin(HttpSession session,  String username, String password, Model model) {
        if (apiUsername.equals(username) && apiPassword.equals(password)) {
            session.setAttribute("api-user-info", true);
            return "redirect:/api";
        }
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("msg", "用户名或密码错误");
        return "api/login";
    }
}
