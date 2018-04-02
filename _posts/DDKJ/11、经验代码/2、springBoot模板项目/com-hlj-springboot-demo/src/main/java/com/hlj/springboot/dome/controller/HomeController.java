package com.hlj.springboot.dome.controller;


import com.hlj.springboot.dome.common.Format.ResponseBean;
import com.hlj.springboot.dome.common.bean.Person;
import com.hlj.springboot.dome.common.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  上午10:22.
 */
@Controller
public class HomeController {

    public static int i = 1;

    @Autowired
    private PersonRepository personRepository;

    //http://localhost:8080/insert?name=HealerJean
    @GetMapping("insert")
    public @ResponseBody
    ResponseBean insert(Person person){
        try {
            return  ResponseBean.buildSuccess(personRepository.save(person));
        }catch (Exception e){
            return  ResponseBean.buildFailure(e.getMessage());
        }
    }


    @GetMapping("findById")
    @ResponseBody
    public ResponseBean  findById(Long id){
        try {
            return ResponseBean.buildSuccess(personRepository.findById(id));
        }catch (Exception e){
            return  ResponseBean.buildFailure(e.getMessage());
        }
    }

    @GetMapping("static")
    @ResponseBody
    public ResponseBean  staticTest( ){
        i ++;

        try {
            return ResponseBean.buildSuccess(i);
        }catch (Exception e){
            return  ResponseBean.buildFailure(e.getMessage());
        }
    }
}
