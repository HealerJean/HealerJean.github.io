<#assign ctx=Request.request.getContextPath()>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>多点广告后台登录</title>
    <script type="text/javascript" src="${ctx}/assets/js/plugins/jquery/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/angularjs/base/angular.all.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/angularjs/postencode/angular-post-encode.js"></script>

    <!-- bootstarp--->
    <link href="${ctx}/assets/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="all" />
    <script type="text/javascript" src="${ctx}/assets/js/plugins/bootstrap/js/bootstrap.min.js"></script>
    <!--// bootstarp-css -->

</head>
<body>

<div class="row">
    <div class="col-md-4 col-sm-6 col-md-offset-4">
        <form action="${ctx}/login" method="post">
            <div class="form-group">
                <label for="exampleInputEmail1">账号</label>
                <input type="text" class="form-control" name="username"  placeholder="账号">
            </div>
            <div class="form-group">
                <label for="exampleInputPassword1">密码</label>
                <input type="password" class="form-control" name="password" placeholder="密码">
            </div>
            <button type="submit" class="btn btn-default">登录</button>
        </form>
    </div>
</div>


</body>
</html>