package com.cg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/errors")
public class ErrorController {
    @GetMapping("/404")
    public String showError(){
        return "error/404";
    }
}
