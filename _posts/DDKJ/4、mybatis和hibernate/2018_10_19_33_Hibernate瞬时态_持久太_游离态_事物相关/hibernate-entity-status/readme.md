
## 前言

总结一个多年以前自己犯的错误，就是Hibernate持久化状态下，查出来的数据，只要set过修改过数据之后， 不用save也会自动更新.


```java
@Service
@Slf4j
public class DemoEntityServiceImpl implements DemoEntityService {

    @Resource
    DemoEntityRepository demoEntityRepository ;

    @Override
    public void testNoHaveSaveButSaveSuccess() {


DemoEntity demoEntity =demoEntityRepository.findOne(1L);
demoEntity.setBalance(12L);
demoEntity.setName("张宇晋"); //观察到下面没有save方法，但是这里实实在在显示到数据库中去了

DemoEntity copy = demoEntity ;
copy.setName("copyEntity"); //最终数据id为1的数据库中 name为 copyEntity ，因为是浅复制


DemoEntity demoEntityT = new DemoEntity();
demoEntityT.setName("z1");
demoEntityRepository.save(demoEntityT);

demoEntityT.setBalance(12L); //会对上面的进行更新


    }
}


```


## 解释：

### 1.瞬态：
  一个实体通过new操作符创建后，没有和Hibernate的Session建立关系，也没有手动赋值过该实体的持久化 标识(持久化标识可以认为是映射表的主键)。  此时该实体中任何属性的更新都不会反映到数据库表中。
### 2.持久化：
  当一个实体和Hibernate的Session创建了关系，并获取了持久化标识，而且在Hibernate的Session生命周期内 存在。  此时针对该实体任何属性的更改都会直接影响到数据库表中一条记录对应字段的更新，即与数据库表同步。
 
### 3.脱管：
  当一个实体和Hibernate的Session创建了关系，并获取了持久化标识，而此时Hibernate的Session生命周期结 束，实体的持久化标识没有被改动过。  针对该实体任何属性的修改都不会及时反映到数据库表中。

## 解决方案

如果我们只是想使用这个查出来的实体，并且在某种情况下，如果需要里面的值，并且要对它进行暂时的修改，却不是修改之后要保存到数据库中

情景：淘宝的appkey和appSecret保存了，默认给提供一些渠道数据，当定时器在修改某个商品的渠道的时候，淘宝信息是我们需要的，但是它提供的渠道却不是我们需要的，所以时候是要set渠道一下一下。

### 1、重新new一个出来


```java
BeanUtils.copyProperties(, );或者下面的


public static TaobaoUserInfo getNewTaobaoUserInfo(TaobaoUserInfo source){
    TaobaoUserInfo target = new TaobaoUserInfo();
    target.setId(source.getId());
    target.setCouponAdzoneId(source.getCouponAdzoneId());
    target.setUserId(source.getUserId());
    target.setUserName(source.getUserName());
    target.setAppkey(source.getAppkey());
    target.setSecret(source.getSecret());
    target.setHaodankuApiKey(source.getHaodankuApiKey());
    target.setCdate(source.getCdate());
    target.setUdate(source.getUdate());
    target.setStatus(source.getStatus());
    return target;

}

```


## 2、数据库表逻辑修改

针对上面的情况，我们可以将，淘宝信息中默认的渠道保存到另一张表中。也就是说制作两张表<br/>
一张渠道表（针对淘宝信息id提供默认值）、一张是淘宝表、表中添加注解Transient，这样即使将渠道set上也不会持久化保存了）


## 2、关于hibernate中`entityManager`一些方法的使用


```java
   
package com.hlj.springboot.dome.common.moudle.service.impl;

import com.hlj.springboot.dome.common.entity.DemoEntity;
import com.hlj.springboot.dome.common.entity.repository.DemoEntityRepository;
import com.hlj.springboot.dome.common.moudle.service.DemoEntityService;
import com.hlj.springboot.dome.common.moudle.service.PostConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * @Desc: 方法中全部开启了aop 也就是启动了session控制
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
@Service
@Slf4j
public class DemoEntityServiceImpl implements DemoEntityService {

    @Resource
    DemoEntityRepository demoEntityRepository ;

    @Resource
    private EntityManager entityManager ;



    @Override
    public void testNoHaveSaveButSaveSuccess() {


        DemoEntity demoEntity =demoEntityRepository.findOne(1L);
        demoEntity.setBalance(12L);
        demoEntity.setName("张宇晋"); //观察到下面没有save方法，但是这里实实在在显示到数据库中去了

//        DemoEntity copy = demoEntity ;
//        copy.setName("copyEntity");

        entityManager.persist(demoEntity);


        DemoEntity demoEntityT = new DemoEntity();
        demoEntityT.setName("z1");
        demoEntityRepository.save(demoEntityT);

        demoEntityT.setBalance(12L); //会进行更新

        System.out.println(demoEntity);
        System.out.println(demoEntityT);


    }

    /**
     * 托管状态
      临时状态在调用persist()后，即可将一般的JavaBean做为了托管状态的Bean，该Bean的任何属性改动都会牵涉到数据库记录的改动。

       一旦该记录flush到数据库之后，并且事务提交了，那么此对象不在持久化上下文中，即：变为了游离（没人管的孩子）状态了。

      在游离状态的时候调用更新、刷新方法后，游离状态对象就变为了在持久化上下文的托管状态了。

      通过管理器的find方法，将实体从数据库查询出来后，该实体也就变为了托管形态。


      persist方法：使对象由临时状态变为持久化状态,就是执行INSERT操作。

      1.如果传递进persist()方法的参数不是实体Bean，会引发IllegalArgumentException
      2.和hibernate的save()方法有些不同：如果对象有id，则不能执行insert操作，会抛出异常
     */

    @Override
    public void persist() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("persistNoId");
        entityManager.persist(demoEntity); //会向数据库中插入数据，可以获取到id
        System.out.println(demoEntity); //DemoEntity(id=1, name=persistNoId, balance=null)
    }

    @Override
    public void persistHaveId() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("persist");
        demoEntity.setId(1L);
        entityManager.persist(demoEntity); //本来以为可以：更新id为1的数据。但是没有成功，失败。抛出异常，传入id就会抛出异常

    }

    /**
     * merge 简单来说，就是更新操作。
     *
     * 1.      传入的对象没有id
     * 在这种情况下，调用merge方法，将返回一个新的对象（有id），并对这个新的对象执行insert操作。
     *
     * 2.  传入的对象有id，entityManager的缓存中没有该对象，数据库中没有该记录:
     *
     * 在这种情况下，调用merge方法，将返回一个新的对象，并对该对象执行insert操作。
     *
     * 新对象的id是数据库中这条记录的id（比如自增长的id），而不是我们自己传入的id。（其实和情况1的结果是一样的）
     *
     * 3.      传入的对象有id，entityManager的缓存没有该对象，数据库中有该记录
     *
     * 在这种情况下，调用merge方法，将会从数据库中查询对应的记录，生成新的对象，然后将我们传入的对象复制到新的对象，最后执行update操作。
     *
     *
     * ---------------------

     */
    @Override
    public void merge() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("merge");
        entityManager.merge(demoEntity);////会向数据库中插入数据,但是获取不到id
        System.out.println(demoEntity); //DemoEntity(id=null, name=merge, balance=null)

    }
    @Override
    public void mergeHaveId() {
        DemoEntity demoEntity110 = new DemoEntity();
        demoEntity110.setName("demoEntity110");
        demoEntity110.setId(110L); //id在数据库中不存在
        entityManager.merge(demoEntity110);////会向数据库中插入数据, 新对象的id是数据库中这条记录的id（比如自增长的id），而不是我们自己传入的id。

        DemoEntity demoEntityID = new DemoEntity();
        demoEntityID.setName("demoEntityID");
        demoEntityID.setId(1L); //id在数据库中存在
        entityManager.merge(demoEntityID);//，将会从数据库中查询对应的记录，生成新的对象，然后将我们传入的对象复制到新的对象，最后执行update操作。

        System.out.println(demoEntity110); //DemoEntity(id=110, name=demoEntity110, balance=null) (并没有显示数据库中的数据)
        System.out.println(demoEntityID); //DemoEntity(id=1, name=demoEntityID, balance=null) 并没有显示数据库中的数据
    }

    /**
     * remove对象必须是数据库中获取的
     */
    @Override
    public void remove() {
        DemoEntity demoEntity110 = new DemoEntity();
        demoEntity110.setName("demoEntity110");
        demoEntity110.setId(4L); //id在数据库中不存在

//        entityManager.remove(demoEntity110); //异常

        DemoEntity data = demoEntityRepository.findOne(4l);
        entityManager.remove(data);//从数据库中直接删除

        System.out.println(data); //DemoEntity(id=4, name=4, balance=4)
    }

    @Override
    public void refresh() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("refresh");
//        entityManager.merge(demoEntity);//没有显示数据库中的数据
//        entityManager.refresh(demoEntity); 报错，必须是数据库中直接获取的

        demoEntity = demoEntityRepository.findOne(1l);
        demoEntity.setName("refresh");
        entityManager.refresh(demoEntity); //表示和数据库中同步，呈现的是数据库中的数据，注意下面的name不是refresh
        System.out.println(demoEntity); //DemoEntity(id=1, name=demoEntityID, balance=null)

        DemoEntity persist = new DemoEntity();
        persist.setName("persist");
        entityManager.persist(persist);//最终显示的是数据库中的数据
        entityManager.refresh(persist); //表示和数据库中同步，注意下面出现了数据库中刚刚插入的id
        System.out.println(persist); //DemoEntity(id=6, name=persist, balance=null)



    }
    
}



```





<br/><br/><br/><br/>
<font color="red"> 感兴趣的，欢迎添加博主微信， </font><br/>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|


