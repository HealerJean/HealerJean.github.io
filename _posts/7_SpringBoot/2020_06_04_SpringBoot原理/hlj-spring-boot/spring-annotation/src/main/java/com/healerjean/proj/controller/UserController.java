// package com.healerjean.proj.controller;
//
// import com.healerjean.proj.dto.UserDTO;
// import com.healerjean.proj.service.UserService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @Slf4j
// @RequestMapping("api")
// public class UserController {
//
//     @Autowired
//     private UserService userService;
//
//     @GetMapping("user/{id}")
//     public UserDTO login(@PathVariable Long id) {
//         return userService.login(id);
//     }
// }
