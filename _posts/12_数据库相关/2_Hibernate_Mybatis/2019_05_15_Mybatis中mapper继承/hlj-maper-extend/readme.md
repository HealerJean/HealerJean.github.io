
## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



### 1、mapper

#### 1.1、父mapper

```java
public interface FatherMapper {

    DemoEntity findById1();

    DemoEntity extendMethod();

}
```

#### 1.2、子mapper

```java
public interface SonMapper extends FatherMapper{

    /**
     * 覆盖父类的方法，这里的id为4
     * @return
     */
    DemoEntity extendMethod();

    DemoEntity findById3();

}

```



### 2、xml

#### 2.1、父xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.hlj.dao.mybatis.demo.FatherMapper">

    <select id="findById1" resultType="com.hlj.entity.db.demo.DemoEntity">
        select * from demo_entity d where d.id = 1
    </select>

    <select id="extendMethod" resultType="com.hlj.entity.db.demo.DemoEntity">
        select * from demo_entity d where d.id = 2
    </select>

</mapper>
```



#### 2.2、子xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.hlj.dao.mybatis.demo.SonMapper">

    <select id="extendMethod" resultType="com.hlj.entity.db.demo.DemoEntity">
        select * from demo_entity d where d.id = 4
    </select>

    <select id="findById3" resultType="com.hlj.entity.db.demo.DemoEntity">
        select * from demo_entity d where d.id = 3
    </select>

</mapper>
```



### 3、service

```java
public interface DemoEntityService {

    List<DemoEntity> mapperExtend();
}




@Service
@Slf4j
public class DemoEntityServiceImpl implements DemoEntityService {

    @Resource
    private FatherMapper fatherMapper;

    @Resource
    private SonMapper sonMapper;


    @Override
    public List<DemoEntity> mapperExtend() {
        List<DemoEntity> demoEntities = new ArrayList<>();
        demoEntities.add(fatherMapper.findById1());
        demoEntities.add(fatherMapper.extendMethod());

        demoEntities.add(sonMapper.findById1()); //继承的父类
        demoEntities.add(sonMapper.extendMethod());//重写的方法
        demoEntities.add(sonMapper.findById3());//自己的方法

        return demoEntities;
    }
}
```



### 4、controller

```java
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "demo控制器")
@Controller
@RequestMapping("mapper")
public class DemoController {


    @Autowired
    private DemoEntityService  demoEntityService;

    @ApiOperation(notes = "mapper继承",
            value = "mapper继承",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoEntity.class)
    @GetMapping("extend")
    @ResponseBody
    public ResponseBean  mapperExtend(){
        try {
            return ResponseBean.buildSuccess(demoEntityService.mapperExtend());
        }catch (AppException e){
            ExceptionLogUtils.log(e,this.getClass() );
            return  ResponseBean.buildFailure(e.getCode(),e.getMessage());
        }catch (Exception e){
            ExceptionLogUtils.log(e,this.getClass() );
            return  ResponseBean.buildFailure(e.getMessage());
        }
    }

}
```



### 5、测试

```json
{
    "success": true,
    "result": [
        {
            "id": 1,
            "name": "HealerJean",
            "age": null,
            "cdate": "2019-05-14T18:00:39.000+0000",
            "udate": "2019-05-14T10:00:39.000+0000"
        },
        {
            "id": 2,
            "name": "DemoEntity(id=null, name=HealerJean, age=null, cdate=null, udate=null)1",
            "age": null,
            "cdate": "2019-05-14T18:00:39.000+0000",
            "udate": "2019-05-14T10:00:39.000+0000"
        },
        {
            "id": 1,
            "name": "HealerJean",
            "age": null,
            "cdate": "2019-05-14T18:00:39.000+0000",
            "udate": "2019-05-14T10:00:39.000+0000"
        },
        {
            "id": 4,
            "name": "DemoEntity(id=null, name=HealerJean, age=null, cdate=null, udate=null)1",
            "age": null,
            "cdate": "2019-05-14T18:01:05.000+0000",
            "udate": "2019-05-14T10:01:06.000+0000"
        },
        {
            "id": 3,
            "name": "HealerJean",
            "age": null,
            "cdate": "2019-05-14T18:01:05.000+0000",
            "udate": "2019-05-14T10:01:06.000+0000"
        }
    ],
    "message": "",
    "code": "200",
    "date": "1557889965987"
}
```





<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |

