package com.hlj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeCotroller {

    @GetMapping("")
    public String home(){
       return  "success";
    }
}
