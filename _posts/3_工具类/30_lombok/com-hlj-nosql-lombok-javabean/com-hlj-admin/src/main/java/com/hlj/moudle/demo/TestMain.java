package com.hlj.moudle.demo;

import com.hlj.data.bean.demo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/3/19  2:41 下午.
 * @description
 */
@Slf4j
public class TestMain {


    /**
     * lombok
     * private String name;       // lombok  setName
     * private boolean isTmail;   // lombok  setTmail
     * private Long tVolumn ;     // lombok  setTVolumn
     * <p>
     * {
     * "TVolumn": 100,
     * "id": 1,
     * "name": "HealerJean",
     * "tmail": true
     * }
     */
    @Test
    public void demo01() {
        String json;
        DemoEntity01 demoEntity = new DemoEntity01() //lombok
                .setName("HealerJean")    //  lombok   name
                .setTmail(true)           //  lombok  isTmail
                .setTVolumn(100L)         //  lombok   tVolumn
                ;

        demoEntity.getName();
        demoEntity.getTVolumn();
        demoEntity.isTmail();

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
    }


    /**
     * {
     * "TVolumn": 100,
     * "isTmail": true,
     * "name": "HealerJean"
     * }
     * private String Name ;      // lombok  setName
     * private boolean IsTmail;   // lombok  setIsTmail
     * private Long TVolumn;      // lombok  setTVolumn
     */
    @Test
    public void demo02() {
        String json;
        DemoEntity02 demoEntity = new DemoEntity02() //lombok
                .setName("HealerJean")  //  lombok    name
                .setIsTmail(true)       //  lombok    IsTmail
                .setTVolumn(100L)       //  lombok    TVolumn
                ;

        demoEntity.getName();
        demoEntity.getTVolumn();
        demoEntity.isIsTmail();

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
    }


    /**
     * JavaBean
     * <p>
     * private String name;       //    setName     getName
     * private boolean isTmail;   //    setTmail     isTmail
     * private Long tVolumn ;     //    settVolumn  gettVolumn
     * <p>
     * {
     * "name": "HealerJean",
     * "tVolumn": 100,
     * "tmail": true
     * }
     */

    @Test
    public void demo03() {
        String json;
        DemoEntity03 demoEntity = new DemoEntity03(); //JavaBean

        demoEntity.setName("HealerJean");
        demoEntity.setTmail(true);
        demoEntity.settVolumn(100L);

        demoEntity.getName();
        demoEntity.isTmail();       // JavaBean isTmail
        demoEntity.gettVolumn();    // JavaBean  tVolumn

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
    }


    /**
     * JavaBean
     * <p>
     * private String Name;       //   setName      getName
     * private boolean IsTmail;   //   setTmail     isTmail
     * private Long TVolumn ;     //   setTVolumn   getTVolumn
     * <p>
     * {
     * "TVolumn": 100,
     * "name": "HealerJean",
     * "tmail": true
     * }
     */
    @Test
    public void demo04() {
        String json;
        DemoEntity04 demoEntity = new DemoEntity04(); //JavaBean

        demoEntity.setName("HealerJean");
        demoEntity.setTmail(true);
        demoEntity.setTVolumn(100L);

        demoEntity.getName();
        demoEntity.isTmail();       // JavaBean IsTmail
        demoEntity.getTVolumn();    // JavaBean  TVolumn

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
    }


    /**
     * 不加@Data注解打印数据为null
     */
    @Test
    public void testBuilder1(){
        DemoEntity05Builder demoEntity05Builder = DemoEntity05Builder.builder().name("HealerJean").build();
        log.info(JSONObject.fromObject(demoEntity05Builder).toString());
    }

    @Test
    public void testBuilder2(){
        DemoEntity05Builder demoEntity05Builder = new DemoEntity05Builder();
        demoEntity05Builder.setName("healerjean2");
        log.info(JSONObject.fromObject(demoEntity05Builder).toString());
    }


    @Test
    public void testToString(){
        DemoEntity06_ToString demoEntity05Builder = new DemoEntity06_ToString();
        demoEntity05Builder.setName("healejean");
        System.out.println(demoEntity05Builder);
        log.info(demoEntity05Builder.toString());
    }



}
