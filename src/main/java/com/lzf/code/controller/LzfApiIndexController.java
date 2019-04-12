package com.lzf.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-16 22:54
 *
 * @author Li Zhenfeng
 */

@Controller
public class LzfApiIndexController {
    @GetMapping("/api")
    public String apiIndex() {
        return "api/index";
    }
}
