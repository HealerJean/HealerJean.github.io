---
title: AngularJs-watch监听input-value-并请求数据
date: 2018-03-06 21:33:00
tags: 
- GitHub
category: 
- GitHub
description: AngularJs-watch监听input-value-并请求数据
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
-->

### 效果展示

![angularJswatch](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/angularJswatch.gif)

## 1、html input菜单和隐藏的控件
---

```
<div class="form-divider"></div>
<div class="form-item form-remark">
    <div class="item-title"><span class="adError">*</span> 投放产品</div>
    <input name="app" ng-model="chance.appName" type="text" class="item-select" placeholder="请填写产品名称">
</div>
<div ng-if="ishaveName==true" class="margin-top-1 adError">
    该应用已被商务"{{haveName}}"录入国，可保持沟通
</div>

```
## 2、watch开始使用
---
### 1、JS 必须初始化`$watch`控件 ，`ng-model="chance.appName" ` 

```
$scope.chance={
    appName:null
};
```
### 2、开始watch代码
#### 1、一定注意是`$watch`，前面有美元符。里面是变量，变量前面没有`$scope`
#### 2、在没有获取到值的情况下，提示控件，应该不显示。所以设置了变量`$scope.ishaveName= false`,注意`JS头部`初始化为false，只有当true的时候才会显示提示控件
#### 3、在进入`$watch`的时候，就将上面2中的`$scope.ishaveName`设置为false
---

```
$scope.chance={
    appName:null
};
$scope.ishaveName= false;
```
---
```
$scope.$watch('chance.appName',function () {
    $scope.ishaveName= false;
     if($scope.chance.appName!=null&&$scope.chance.appName!=undefined&&$scope.chance.appName!=''){
         $http.get(ctx+'/chance/findAdminByAppName?appName='+$scope.chance.appName)
             .success(function (data,status) {
                 if(data.result!=null){
                     $scope.haveName= data.result.name;
                     $scope.ishaveName= true;

                 }
             })
     }

});


```
## 3、后端代码
--- 
### 1、controller
---
```
@Controller
@RequestMapping("chance")
@ResourceFolder(folder = "module/chance/")
public class ChanceController {


	@GetMapping("findAdminByAppName")
	@ResponseBody
	public ResponseBean findAdminByAppName(String appName){
	        try {
	           return ResponseBean.buildSuccess(customerChanceService.findAdminByAppName(appName));
	        }catch (AppException e){
	            return ResponseBean.buildFailure(e.getMessage());
	        }
	}
}

```

## 2、service，当如果为登录人员自己设置的产品的时候，不需要提示自己，尽可能的提示其他人（傻子知道）。
---

```
//根据产品名字 ，查看是否有商务经理已经添加了该产品,选则一个不是自己的产品经理
@Override
public SysAdminUser findAdminByAppName(String appName) {
    if("".equals(appName)||appName==null){
        return null;
    }
    Long adminId = CasConfig.RemoteUserUtil.getRemoteUserId();

    List<SysAdminUser> sysAdminUsers = customerMapper.findAdminByAppName(appName);
    //有可能会出现多个人，那么首先如果是自己创建的话，就排除掉
    if(sysAdminUsers.size()>0){
        if(adminId.compareTo(sysAdminUsers.get(0).getId())==0){
            sysAdminUsers.remove(0);
        }
        if(sysAdminUsers.size()>0){
            return sysAdminUsers.get(0);
        }
    }

    return null;
}

```
### 3、mapper
#### 1、首先应该明确，我们这里获取的是管理人，所以要以管理人为核心。
---

```
<!--根据产品，名字，看出是否已经有商务经理在维护了，根据产品名字 ，查看是否有商务经理已经添加了该产品,如果前台传入的参数我空，则通过java判断，不能进入本sql，`否则会出错`-->
    <select id="findAdminByAppName" resultType="com.duodian.admore.entity.db.admin.SysAdminUser">
        SELECT s.*
        FROM `sys_admin_user`  s
        left JOIN  crm_customer_chance c on c.adminId = s.id
        where c.isVisible = 1 and c.adminId is not NULL
        <if test="_parameter != null and _parameter!= ''">
            and c.appName = #{_parameter}
        </if>
        GROUP by id
    </select>

</mapper>

```



<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'GitHub评论Gitalk插件',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

