package com.hlj.redis.project;

import com.hlj.redis.project.data.ProjectData;
import com.hlj.redis.project.utils.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/13  下午12:04.
 */
@RequestMapping("redis/project")
@Controller
public class ProjectController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private RedisTemplate<String,Object> redisWithTemplate;


    @GetMapping("redisWithTemplate")
    @ResponseBody
    public String lockRedis(){

        ProjectData projectData =new ProjectData().setGroup("group").setName("healerjean");
        redisWithTemplate.opsForValue().set("key",projectData );
        ProjectData data = JsonUtils.toObject(stringRedisTemplate.opsForValue().get("key"),ProjectData.class);
        System.out.println("data"+data.toString());

        return  data.toString();
    }



    @GetMapping("redisWithTemplate/set")
    @ResponseBody
    public String redisWithTemplateSet(){

        ProjectData projectData =new ProjectData().setGroup("group").setName("healerjean");
        ProjectData projectData2 =new ProjectData().setGroup("group2").setName("healerjean2");

        redisWithTemplate.opsForSet().add("keyAdd",projectData);
        redisWithTemplate.opsForSet().add("keyAdd",projectData2);
        redisWithTemplate.opsForSet().add("keyAdd",projectData2);

        Set<Object> projectDatas = redisWithTemplate.opsForSet().members("keyAdd");
        for(Object object: projectDatas){
            ProjectData setData =  (ProjectData)object;
            System.out.println(setData.toString());
        }

        //String类型只能得到String格式的，里面存放的其实就是Json，下面打印的也是json
        Set<String> projectDataString = stringRedisTemplate.opsForSet().members("keyAdd");
        System.out.println(projectDataString.toString());

        ProjectData projectData3 =new ProjectData().setGroup("group3").setName("healerjean3");
        ProjectData projectData4 =new ProjectData().setGroup("group4").setName("healerjean4");


        return "success";


        /**
         ProjectData(name=healerjean2, Group=group2)
         ProjectData(name=healerjean, Group=group)
         [{"@class":"com.hlj.redis.project.data.ProjectData","name":"healerjean2","group":"group2"}, {"@class":"com.hlj.redis.project.data.ProjectData","name":"healerjean","group":"group"}]

         */
    }


}
