package com.hlj.springboot.dome.common.moudle.controller;

import com.hlj.springboot.dome.common.entity.DemoEntity;
import com.hlj.springboot.dome.common.moudle.service.IsolationService;
import com.hlj.springboot.dome.common.moudle.service.IsolationStartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/23  下午7:33.
 * 类描述：
 */
@Controller
@RequestMapping(value = "isolation")
public class IsolationController {

    @Resource
     private   IsolationService isolationService ;

    @Resource
    private IsolationStartService isolationStartService ;


    @GetMapping("startTransactional")
    @ResponseBody
    public DemoEntity startTransactional(Long id){
       return   isolationStartService.startTransactional(id);
    }

    @GetMapping("transRequirs")
    @ResponseBody
    public DemoEntity transRequirs(Long id){
        return   isolationService.transRequirs(id);
    }


    @GetMapping("transRequirsNew")
    @ResponseBody
    public DemoEntity transRequirsNew(Long id){
        return   isolationService.transRequirsNew(id);
    }


    @GetMapping("isoLationReadCommited")
    @ResponseBody
    public DemoEntity isoLationReadCommited(Long id){
        return   isolationService.isoLationReadCommited(id);
    }

}
