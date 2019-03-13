// Traffic Stats of the entire Web site By baidu end
document.write("<scr"+"ipt src='https://g.csdnimg.cn/notification/1.3.6/notify.js'></scr"+"ipt>");
document.write("<scr"+"ipt src='https://g.csdnimg.cn/notification/1.3.6/main.js'></scr"+"ipt>");
var _gaq = [];
var userAgent = navigator.userAgent.toLowerCase();
    // Traffic Stats of the entire Web site By baidu
    var _hmt = _hmt || [];
    (function() {
      var getCookie =function (objName){//获取指定名称的cookie的值
        var arrStr = document.cookie.split("; ");
        for(var i = 0;i < arrStr.length;i ++){
          var temp = arrStr[i].split("=");
          if(temp[0] == objName && objName=="UD") return decodeURIComponent(temp[1]);
          if(temp[0] == objName) return decodeURI(temp[1]);
        }
      }
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?6bcd52f51e9b3dce32bec4a3997715ac";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
    // Traffic Stats of the entire Web site By baidu end



    /**
     * 判断是否是博客网站
     * @return {Boolean} [description]
     */
    function is_blog(){
      var loac_host = window.location.host;
      if(loac_host.indexOf("blog")=== 0)return true;
      else return false;
    }

    function siteId(){
      var loac_host = window.location.host;
      // loac_host ='www.csdn.net'
      var siteId;
      if(loac_host.indexOf("blog")=== 0){
        siteId = 'UA-127895514-2'
      }else if(loac_host.indexOf("download")=== 0){
        siteId = 'UA-127895514-8'
      } else if(loac_host.indexOf("edu")=== 0){
        siteId = 'UA-127895514-9'
      } else if(loac_host.indexOf("bbs")=== 0){
        siteId = 'UA-127895514-4'
      }else if(loac_host.indexOf("ask")=== 0){
        siteId = 'UA-127895514-5'
      }else if(loac_host.indexOf("gitbook")=== 0){
        siteId = 'UA-127895514-10'
      }else if(loac_host.indexOf("iteye")=== 0){
        siteId = 'UA-127895514-6'
      }else if(loac_host.indexOf("passport")=== 0){
        siteId = 'UA-127895514-7'
      }else if(loac_host.indexOf("so")=== 0){
        siteId = 'UA-127895514-3'
      }else if(loac_host.indexOf("www")=== 0){
        if (loac_host.indexOf("iteye")>0){
          siteId = 'UA-127895514-6'
        }else {
          siteId = 'UA-127895514-1'
        }
      }else {
        siteId = ''
      }
      return siteId
    }
    /**
     * 确定是否博客并有用户名
     * @return {Boolean} [description]
     */
    function is_bloger() {
      try {
        if(is_blog() && username){
          return true;
        }
      } catch (e) {
        return false;
      }
    }

!(function($){
  var currUser={
      userName:"",
      userNick:'<a class="set-nick" href="https://passport.csdn.net/account/profile">设置昵称<span class="write-icon"></span></a>',
      desc : '<a class="fill-dec" href="//my.csdn.net" target="_blank">编辑自我介绍，让更多人了解你<span class="write-icon"></span></a>',
      avatar:"//csdnimg.cn/public/common/toolbar/images/100x100.jpg"
    };
  var prodLogo = "none";
  var $oScriptTag =$("#toolbar-tpl-scriptId");
  var skin =$oScriptTag.attr("skin")=="black"?" csdn-toolbar-skin-black ":"";
  var fixed = $oScriptTag.attr("fixed")=="top"?" navbar-fixed-top ":"";
  var prodIndex= $oScriptTag.attr("domain")?$oScriptTag.attr("domain"):window.location.protocol+"//"+window.location.host;
      prodIndex+='_logo';
  var getCookie =function (objName){//获取指定名称的cookie的值
      var arrStr = document.cookie.split("; ");
      for(var i = 0;i < arrStr.length;i ++){
      var temp = arrStr[i].split("=");
      if(temp[0] == objName && objName=="UD") return decodeURIComponent(temp[1]);
      if(temp[0] == objName) return decodeURI(temp[1]);
      }
  }
  var setCookie = function (name,value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();// + ";domain=.csdn.net;path=/";
  }
  var HTMLEncode =function(str) {
      var s = "";
      if(str.length == 0) return "";
      s = str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\'/g, "&#39;").replace(/\"/g, "&quot;");
      return s;
    }
  var AUtoAvatar = function(AU){
    if(!AU||!currUser.userName){
      return false;
    }
    var _AUPath = AU.split("").join("/");
    var userName = currUser.userName&&currUser.userName.toLowerCase();
    return "//profile.csdnimg.cn/"+_AUPath+"/2_"+userName;
  }
  var hasLogin = false;
  var loginMark ="unlogin";
  function checkLogin(callback) {
          currUser.userNick = getCookie("UserNick") ||currUser.userNick;
          currUser.userName = getCookie("UserName") || currUser.userName;
          currUser.avatar = AUtoAvatar(getCookie("AU")) || currUser.avatar;
          currUser.desc = getCookie("UD") || currUser.desc;
          if(getCookie("UD")){
            currUser.desc = HTMLEncode(currUser.desc.replace(/\+/g," "));
          }
          callback(currUser);
    }
  checkLogin(function(currUser){
    if(currUser.userName){
        hasLogin = true;
        _hmt.push(['_setUserTag', '5744', currUser.userName]);
    }
    loginMark = hasLogin?"":"unlogin";
  })
  _hmt.push(['_setUserTag', '6525', getCookie("uuid_tt_dd")]);
  /*
  * init pord logo
  */
  var prodJSON = {
      "blog" : "blog-icon",
      "download" : "down-icon",
      "bbs" : "bbs-icon",
      "my" :"space-icon",
      "code" : "code-icon",
      "share" : "share-icon",
      "tag" : "tag-icon",
      "dashboard":"dashboard-icon",
      "news" : "news-icon",
      "tag" : "tag-icon",
      "ask" : "ask-icon",
      "notify" : "notify-icon"
  }
  if(prodJSON[$oScriptTag.attr("prod")]){
    prodLogo=prodJSON[$oScriptTag.attr("prod")]||$oScriptTag.attr("prod");
  }

/**
 * 二级域名 getSecondaryDomain
    * @return {string} 二级域名
    */
    var getSecondaryDomain = (function  () {
      var host = window.location.host;
      return host.split('.')[0]
    })()
  //  festival customize
    var start_time = 1546012800, // toolbar 特殊样式起始时间戳new Date(2018,11,29).getTime()/1000
        end_time = 1546358400,  // toolbar 特殊样式结束时间戳
        now_time = Math.floor(Date.now() / 1000), // 当前时间戳
        logo_tpl = '<svg class="toolbar-icon" aria-hidden="true"><use xlink:href="#gonggong_csdnlogo_"></use></svg>',  // 正常样式
        logo_title = 'CSDN首页', // 正常提示
        logo_href= 'https://www.csdn.net';
        /**
         * 当前时间在起始与结束时间内时触发
         */
        // if(now_time > start_time && now_time<end_time ){
        //   logo_tpl = '<img src="//csdnimg.cn/cdn/content-toolbar/dw.jpg">';
        //   logo_title = '端午节';
        // }
        // if(now_time > start_time && now_time < end_time ){
        //   logo_tpl = '<img src="//csdnimg.cn/cdn/content-toolbar/zq-logo.jpg">';
        //   logo_title = '中秋节';
        // }
        if(now_time > start_time && now_time < end_time ){
          logo_tpl = '<img src="//csdnimg.cn/cdn/content-toolbar/csdn-newyear.gif">';
          logo_title = 'csdn-newyear';
        }


    var input_placeholder = "搜CSDN";
    if (is_bloger()) {
      input_placeholder = "搜博主文章"
    }

// 因为 logo 是SVG 的，所以必须把 iconfont.js 加入到代码里面
    var protocol = location.protocol.substr(0, 4) === 'http' ? '' : 'http:';
  document.write('<script class="toolbar-s" type="text/javascript" src='+protocol+'//csdnimg.cn/cdn/content-toolbar/iconfont.js?_=4567"></script>');

  // 注册url，https://passport.csdn.net/account/register
  // <li><a href="//www.csdn.net/" target="_blank">首页</a></li>\
  // <li class="tb_is1440hide"><a href="http://www.csto.com" title="CSTO" target="_blank">CSTO</a></li>\
  //<li><a href="//gitbook.cn/?ref=csdn" title="GitChat" target="_blank">GitChat</a></li>\
  // <div><a href="//bbs.csdn.net/forums/Service" target="_blank">反馈</a></div>\
  //<p style="text-align: center;margin-top: 4px;margin-bottom: -5px;">扫码下载安卓版</p>\
  //<li><a href="https://www.tinymind.cn/?utm_source=csdnbar" target="_blank">TinyMind</a></li>
  //<li><a href="http://www.iteye.com" title="ITeye" target="_blank">ITeye</a></li>\
  var tpl ='\<div id="csdn-toolbar" class="csdn-toolbar tb_disnone '+skin+fixed+'">\
        <div class="container row center-block ">\
          <ul class="pull-left left-menu clearfix" id="nav-left-menu">\
            <li>\
              <a href="'+ logo_href +'" title="'+logo_title+'">'+logo_tpl+'</a>\
            </li>\
            <li><a href="//www.csdn.net/" class="toolbar_to_feed" title="首页" style="margin-left:0">首页</a></li>\
            <li><a href="//blog.csdn.net/" class="toolbar_to_feed" title="博客">博客</a></li>\
            <li><a href="//edu.csdn.net" title="学院">学院</a></li>\
            <li><a href="//download.csdn.net" title="下载">下载</a></li>\
            <li><a href="https://gitchat.csdn.net/?utm_source=csdn_toolbar" title="图文课">图文课</a></li>\
            <li><a href="//bbs.csdn.net" title="论坛">论坛</a></li>\
            <li class="app-btn"><a href="https://www.csdn.net/app/" title="APP">APP</a>\
              <div class="appControl">\
              <span>\
                <img src="https://csdnimg.cn/public/common/toolbar/images/csdnqr@2x.png" />\
                <em>CSDN</em>\
              </span>\
              <span class="eduwx">\
                <img src="https://csdnimg.cn/public/common/toolbar/images/eduwxfix.png" />\
                <em>CSDN学院</em>\
              </span>\
              </div>\
            </li>\
            <li><a href="//ask.csdn.net" title="问答">问答</a></li>\
            <li><a href="//mall.csdn.net" title="商城">商城</a></li>\
            <li class="vip-caise"><a href="https://mall.csdn.net/vip_code" title="VIP会员">VIP会员</a></li>\
            <li><a href="//huiyi.csdn.net/" title="活动">活动</a></li>\
            <li><a href="http://job.csdn.net" title="招聘">招聘</a></li>\
            <li><a href="http://www.iteye.com" title="ITeye">ITeye</a></li>\
            <li><a href="https://gitbook.cn/?ref=csdn" title="GitChat">GitChat</a></li>\
          </ul>\
          <div class="pull-right login-wrap '+loginMark+'">\
            <ul class="btns">\
            <li class="toolbar-tracking csdn-tracking-statistics tracking-click" data-mod="popu_369"><a href="#" style="padding:0"></a></li>\
              <li>\
                <div class="search_bar csdn-tracking-statistics tracking-click" data-mod="popu_366">\
                  <input type="text" class="input_search" name="" id="toolber-keyword" placeholder='+ input_placeholder +'>\
                  <a href="//so.csdn.net/so/" target="_blank" class="btn-nobg-noborder btn-search"><svg class="toolbar-icon" aria-hidden="true"><use xlink:href="#sousuo"></use></svg></a>\
                </div>\
              </li>\
              <li class="write-bolg-btn csdn-tracking-statistics tracking-click" data-mod="popu_370"><a class="" href="//mp.csdn.net/postedit" target="_blank" id="blogClick"><svg class="toolbar-icon" aria-hidden="true"><use xlink:href="#xieboke1"></use></svg><span>写博客</span></a>\
              <li class="gitChat"><a class="" href="//gitbook.cn/new/gitchat/activity?utm_source=csdnblog1" target="_blank"><img src="https://csdnimg.cn/public/common/toolbar/images/money.png" alt="" class="money"><span>赚零钱</span></a></li>\
              <li class="gitChat upload msgBox" id="msgBox">\
              <a target="_blank" class="" href="//i.csdn.net/#/msg/index"><img src="https://csdnimg.cn/public/common/toolbar/images/message-icon.png" alt="" class="message"><span>消息</span><div class="toolbar-circle" id="msg-circle"></div></a>\
                <div class="msgList" id="msgList">\
                  <a href="//i.csdn.net/#/msg/index">评论<em class="comment"></em></a>\
                  <a href="//i.csdn.net/#/msg/attention">关注<em class="follow"></em></a>\
                  <a href="//i.csdn.net/#/msg/like">点赞<em class="thumb_up"></em></a>\
                  <a href="//i.csdn.net/#/msg/answer">回答<em class="invitation"></em></a>\
                  <a href="//i.csdn.net/#/msg/notice">系统通知<em class="system"></em></a>\
                </div>\
              </li>\
              <li class="userinfo"><a href="https://passport.csdn.net/account/login" target="_blank">登录</a><span></span><a href="https://passport.csdn.net/account/login" target="_blank">注册</a></li>\
              <li class="userLogin">\
                <div class="loginCenter"><a href="//i.csdn.net" target="_blank"><img class="login_img" src="'+currUser.avatar+'"></a></div>\
                <div class="userControl">\
                <div class="bord">\
                <div><i class="pull_icon pull_icon1"></i><a href="https://www.csdn.net/nav/watchers" target="_blank">我的关注</a></div>\
                <div><i class="pull_icon pull_icon2"></i><a href="https://i.csdn.net/#/uc/favorite-list" target="_blank">我的收藏</a></div>\
                <div><i class="pull_icon pull_icon4"></i><a href="https://i.csdn.net/#/uc/profile" target="_blank">个人中心</a></div>\
                <div><i class="pull_icon pull_icon7"></i><a href="https://i.csdn.net/#/account/index" target="_blank">帐号设置</a></div>\
                </div>\
                  <div class="bord">\
                  <div><i class="pull_icon pull_icon5"></i><a href="https://blog.csdn.net/'+currUser.userName+'" target="_blank">我的博客</a></div>\
                  <div><i class="pull_icon pull_icon6"></i><a href="https://mp.csdn.net/" target="_blank">管理博客</a></div>\
                  <div><i class="pull_icon pull_icon12"></i><a href="https://edu.csdn.net/mycollege" target="_blank">我的学院</a></div>\
                  <div><i class="pull_icon pull_icon13"></i><a href="https://download.csdn.net/my/downloads" target="_blank">我的下载</a></div>\
                  </div>\
                  <div class="bord">\
                  <div><i class="pull_icon pull_icon8"></i><a href="https://my.csdn.net/my/score" target="_blank">我的C币</a></div>\
                  <div><i class="pull_icon pull_icon9"></i><a href="https://order.csdn.net/myorder" target="_blank">订单中心</a></div>\
                  </div>\
                  <div class="bord">\
                  <div><i class="pull_icon pull_icon10"></i><a href="https://blog.csdn.net/home/help.html" target="_blank">帮助</a></div>\
                  <div><i class="pull_icon pull_icon11"></i><a href="javascript:void(0);" class="logout">退出</a></div>\
                  </div>\
                </div>\
                <div class="guo_tip_box">关注和收藏在这里</div>\
              </li>\
            </ul>\
          </div>\
        </div>\
    </div>';
  $(document.body).prepend($(tpl));

    $('#csdn-toolbar .logout').click(function(){
        window.location.href = 'https://passport.csdn.net/account/logout?from='+encodeURIComponent(window.location.href);
    })

  var timeOut = 1;
  if((window.location.host.indexOf('bbs.csdn.net')>-1 && window.location.pathname.indexOf('home')>-1)||(window.location.host.indexOf('bbs.csdn.net')>-1 && window.location.pathname.indexOf('forums')>-1)){
    $('.csdn-toolbar').addClass('csdn-toolbarbbshome')
  }
  function istracking(){
    if(timeOut>10){
      return false;
    }
    try {
      if(typeof(csdn.trackingAd) === 'function'){
        bindTracking();
      }else{
        timeOut++;
        setTimeout(istracking,1000);
      }
    } catch (e) {
      timeOut++;
      setTimeout(istracking,1000);
    }
  }
  istracking();
  function bindTracking(){
    csdn.trackingAd('.toolbar-tracking', {
        pid: 'toolbar',
        mod: 'popu_366',
        mtp: '1'
    });
    // search_bar
    csdn.trackingAd('.search_bar', {
        pid: 'toolbar',
        mod: 'popu_369',
        mtp: '1'
    });

    csdn.trackingAd('.write-bolg-btn', {
        pid: 'toolbar',
        mod: 'popu_370',
        mtp: '1'
    });
  }
  // 消息新接口
  (function() {
    if(!hasLogin) return ;
    var url = '//msg.csdn.net/v1/web/message/view/unread';
    var maxNum = 10;
    var elems = $('#msgList a em');
    var msgCircle = $('#msg-circle');
    var docTitle = document.head.getElementsByTagName('title');
    var docTitleText;
    docTitle = docTitle && docTitle[0];
    docTitle && (docTitleText = docTitle.innerHTML);
    $.ajax({
      url: url,
      type: 'post',
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      success: function (json) {
        var len;
        if (json.status) {
          json = json.data;
          // || {
          //   comment: 100,
          //   follow: 5,
          //   invitation: 8,
          //   system: 0,
          //   thumb_up: 2,
          //   totalCount: 9
          // };
          if((len = json.totalCount) > 0){
            if (len >= maxNum){
              len = '9+';
            }
            len && docTitle && (docTitle.innerHTML = '(' + len + '条消息)' + docTitleText)
            msgCircle.html(len).fadeIn();
            elems.each(function (i, el) {
              var oClass = $(el).attr('class');
              var strNum = Number(json[oClass], 10);
              if (strNum >= maxNum) {
                strNum = (maxNum-1) + '+'
              }
              strNum && $(el).html(strNum);
            })
          }
        }
      },
      error: function (err) {
        console.error('消息服务错误!', err, err.responseText)
      }
    });
  })()



/*
   全站新添修改
   @description
     toolbar调整
     gitchat全站通栏广告添加
*/
$(function(){

  // control bbs toolbar
  function controlToolBarBBs(){
    if($('#main-home').hasClass('open')){
      $('#csdn-toolbar').addClass('tb_bbs');
    }
    $('.left_side > span').on('click',function(){
        if($(this).hasClass('btn_toggle_yc')){
          $('#csdn-toolbar').removeClass('tb_bbs');
        }
        if($(this).hasClass('btn_toggle_xs')){
          $('#csdn-toolbar').addClass('tb_bbs');
        }
    });
  }
  // gitchat 广告全站添加
  function advert(){
    var t = 2000,
        cookieTime = 60*60*24,
        cookieKey = 'is_advert',
        domain = '.csdn.net',
        isStart = false,
        advertDate = {
          start: '2018/06/07 06:00:00',
          end: '2018/06/30 00:00:00'
        };

    // contronl start
    isStart = adverControlDate();
    // clear cookie
    if(!isStart){
       // clear is_advert
       if(getCookie(cookieKey)){
          setCookie(cookieKey, '', 0);
       }
       return false;
    }

    dynamicTpl({
      posDom: $('#csdn-toolbar')
    },function(opts){
      var optDom, adDom, closeDom, adDomLink;
      optDom = opts.optDom;
      adDom = opts.tplDom;
      closeDom = $('#js_advert_close');
      adDomLink = adDom.find('.advert-a').get(0);

      // 点击关闭
      closeDom.on('click', function(){
        advertClose(adDom);
        return false;
      })

      // 验证是否存在
      if(getCookie(cookieKey)){
        adDom.addClass('advert-cur');
        return false;
      }else{
        adDom.addClass('advert-ex');
      }

      // start animation
      setMove(function(){
        // 缩小
        adDom.removeClass('advert-ex');
        return true;
      },function(flg){
        setMove(function(){
          adDom.addClass('advert-cur');
          return true;
        },200)
        // 写入cookie
        if(flg){
          if(!getCookie(cookieKey)){
            setCookie(cookieKey, '', cookieTime);
          }
        }
      }, t);
    });

    // 控制动画流程
    function setMove(beforeCallback, callback, t){
      var flg;
      if(!beforeCallback){
        return false;
      }
      if(callback && typeof callback === 'number' && Number(callback, 10) > 0){
        t = callback;
      }

      if(!t){
        return false;
      }

      setTimeout(function(){
        flg = beforeCallback();
        if(flg){
          typeof callback === 'function' && callback(flg);
        }
      }, t);
    }

    // cookie设定
    function setCookie(key, value, t){
       var oDate = new Date();
       var dayTime = oDate.getDate() + t;
       var v;
       oDate.setDate(dayTime);
       v = value || oDate.toGMTString();
       document.cookie = key + '=' + encodeURIComponent(v) + '; max-age=' + t + '; domain=' + domain;
       return true;
    }

    // 获得cookie
    function getCookie(key){
      var cookies = document.cookie;
      var a = cookies.split('; '), b, c;
      for(var i=0; i<a.length; i++){
         if(a[i]){
            b = a[i].split('=');
            if(b[0] === key){
               c = b[1];
               break;
            }
         }
      }
      return c;
    }

    // close
    function advertClose(adDom){
      adDom.addClass('hide');
    }

    // control show advert date
    function adverControlDate(){
      var iDate, curTime, advertStart, advertEnd, flg = false;
      iDate = new Date();
      curTime = iDate.getTime();
      advertStart = new Date(advertDate.start).getTime();
      advertEnd = new Date(advertDate.end).getTime();
      if(curTime >= advertStart && curTime < advertEnd){
        flg = true;
      }
      return flg;
    }

    // control tpl
    function dynamicTpl(options, callback){
      var opts, optDom, adDom, styl, tpl;
      opts = options ? options : null;
      if(!opts){
        return false;
      }
      optDom = opts.posDom ? opts.posDom instanceof jQuery ? opts.posDom : $(opts.posDom) : null;
      if(!optDom){
        return false;
      }
      styl = '<style type="text/css">' +
        '.hide{display:none;}' +
        '.advert-bg{background-color:#000;}' +
        '.advert{width: 1200px;margin: 0 auto;position:relative;line-height: 0;font-size: 0;}' +
        '.advert .advert-log{display:none;width:90px;height:22px;background-image: url("//csdnimg.cn/public/publick_img/gitchat_logo.png");background-repeat: no-repeat;background-size: contain;position:absolute;top:25px;left:27px;z-index: 10;}' +
        '.advert .advert-a{display:inline-block;width:100%;height:80px;background-repeat: no-repeat;background-size: contain;background-position: 50% 0;opacity:0;-webkit-transition: all 0.2s linear;-o-transition: all 0.2s linear;-ms-transition: all 0.2s linear;-moz-transition: all 0.2s linear;transition: all 0.2s linear;}' +
        '.advert-cur .advert-a{background-image: url("//csdnimg.cn/public/publick_img/ad_20180622_toolbar80.jpg") !important;opacity:1;}' +
        '.advert-ex .advert-a{height:200px;opacity:1;}' +
        '.csdn-bbs .advert .advert-close, .advert .advert-close{position:absolute;z-index:10;right: 2%;top: 15%;color: #fff;font-size: 22px;}' +
      '</style>';
      tpl = '<div class="advert-bg"><div class="advert" id="advert"><a href="https://edu.csdn.net/promotion_activity?id=3&utm_source=618qztt" class="advert-log"></a><a href="https://edu.csdn.net/promotion_activity?id=3&utm_source=618qztt" style="background-image: url(//csdnimg.cn/public/publick_img/ad_20180622_toolbar200.jpg);" class="advert-a"></a><a href="" class="advert-close" id="js_advert_close">&times</a></div></div>';

      if(optDom.length<=0){
        return false;
      }

      optDom = optDom[0];
      adDom = $(tpl);
      document.head.insertBefore($(styl)[0], document.head.getElementsByTagName('title')[0]);
      document.body.insertBefore(adDom[0], optDom);
      callback && typeof callback === 'function' && callback({optDom: $(optDom), tplDom: adDom});
    }
  }
  // run
  controlToolBarBBs();
  advert();
})

// hover
$(function(){
  var moreHover={
    showMore: function(){
      var $dom = $('.show-more .more');
      if($dom.is(":animated")){
        $dom.stop(true,true).fadeIn(200);
      }
      $dom.stop(true,true).fadeIn(200);
    },
    hideMore:function(){
      var $dom = $('.show-more .more');
      if($dom.is(":animated")){
        $dom.stop(true,true).fadeIn(200);
      }
      $dom.stop(true,true).fadeOut(300);
    }
  }
  var userHover={
    showMore: function(tagBox, tag){
      var $dom = $(tagBox);
      if($dom.is(":animated")){
        $dom.stop(true,true).fadeIn(200);
      }
      $dom.stop(true,true).fadeIn(200);
      tag && $(tag).hide()
    },
    hideMore:function(tagBox){
      var $dom = $(tagBox);
      if($dom.is(":animated")){
        $dom.stop(true,true).fadeIn(200);
      }
      $dom.stop(true,true).fadeOut(300);
    }
  }
  var appHover={
    showMore: function(){
      var $dom = $('.appControl');
      if($dom.is(":animated")){
        $dom.stop(true,true).fadeIn(200);
      }
      $('.app-btn a').css('color','#C03A3A')
      $dom.stop(true,true).fadeIn(200);
    },
    hideMore:function(){
      var $dom = $('.appControl');
      if($dom.is(":animated")){
        $dom.stop(true,true).fadeIn(200);
      }
      $('.app-btn a').css('color','')
      $dom.stop(true,true).fadeOut(300);
    }
  }
  $('.show-more').hover(moreHover.showMore,moreHover.hideMore)
  $('.userLogin').hover(function () {
    userHover.showMore('.userControl', '.guo_tip_box')
  }, function () {
    userHover.hideMore('.userControl')
  })
  $('.app-btn').hover(appHover.showMore,appHover.hideMore)
  $('.guo_tip_box').hover(function(){
    $(this).css('display','none')
    $.get('https://statistic.csdn.net/toolbar/followTipsclose');
  })
  // 消息下拉
  $('#msgBox').hover(function () {
    hasLogin && userHover.showMore('#msgList')
  }, function () {
    hasLogin && userHover.hideMore('#msgList')
  })
  if(!getCookie('c_adb')){
    var importCheck = document.createElement('script');
    importCheck.type="text/javascript";
    importCheck.src = 'https://g.csdnimg.cn/check-adb/1.0.3/check-adb.js';
    document.head.appendChild(importCheck);
  }
})
// search
$(function(){
  //获取 网站位置
  function getT(){
    var title = window.location.host.split( ".",1)[0];
    var t = ''
        switch (title) {
          case 'www':
            t = ' '
            break;
          case 'blog':
            t = 'blog'
            break;
          case 'blog':
            t = 'codes_snippet'
            break;
          case 'bbs':
            t = 'discuss'
            break;
          case 'download':
            t = 'doc'
            break;
          case 'ask':
            t = 'ask'
            break;
          case 'gitchat':
            t = 'gitchat'
            break;
          case 'geek':
            t = 'news'
            break;
          case 'edu':
            t = 'course'
            break;
          default:
            t = ' '
        }
    return t;
  }

  /**
   * 尝试获取用户名，拼接搜索链接
   * @return {[type]} [description]
   */
  function get_user_name(){
    var addusername = "&u=";
    if(is_bloger()){
      addusername += username;
    }
    return addusername;
  }
  function goFn(obj,txt){

      var searchTxt = encodeURIComponent(txt),
              url = "//so.csdn.net/so/search/s.do?q="+searchTxt + "&t="+getT()+get_user_name();
      if(searchTxt == ''){
          return false;
      }else{
        window.open(url)
      }

  }
    var searchBtn = $(".btn-search"),
            searchInpt = $(".input_search"),
            _this = this;

      //高亮当前导航
    var myNav=document.getElementById("nav-left-menu").getElementsByTagName("a");
    var currenturl = document.location.href;
        currenturl = currenturl.substr(currenturl.indexOf('/'));
        if(currenturl.indexOf('//edu.csdn.net')!=-1){
          input_placeholder = "搜索学院课程"
          $('#toolber-keyword')[0].setAttribute('placeholder',input_placeholder)
        }
    //学院搜索
    function eduSearch(obj,txt){
      var searchTxt = encodeURIComponent(txt),
          url = "https://so.csdn.net/so/search/s.do?q="+searchTxt+"&t=course"
      if(searchTxt == ''){
          return false;
      }else{
        window.open(url)
      }
    }
    for(var i=0;i<myNav.length;i++){
        var aurl=myNav[i].getAttribute("href");
            aurl = aurl.substr(aurl.indexOf('/'));
        if(currenturl.indexOf(aurl)!=-1 && aurl != '//mall.csdn.net'){
              // 包含vip_code
              myNav[i].className="active";
              myNav[0].className="";
        }
        if(currenturl.indexOf('//mall.csdn.net')!=-1 && currenturl.indexOf('vip_code') ==-1){
          myNav[9].className="active";
        }
        if(currenturl.indexOf('//gitbook.cn')!=-1 && currenturl.indexOf('vip_code') ==-1){
          myNav[9].className="active";
        }
        if(currenturl.indexOf('//www.csdn.net')!=-1 && currenturl.indexOf('app') !==-1){
          myNav[1].className="";
        }
    }
    searchBtn.on("click", function (e) {
        if(currenturl.indexOf('//edu.csdn.net')!=-1){
          eduSearch($(this), $(this).prev("input").val())
        }
        goFn($(this), $(this).prev("input").val());
        e.preventDefault();
    });

    searchInpt.keyup(function (event) {
        var evCode = event.keyCode;
        if(evCode == 13) {
          var searchTxt = encodeURIComponent($(this).val()),
              url = "//so.csdn.net/so/search/s.do?q=" + searchTxt + "&t="+getT()+get_user_name();
          window.open(url)
        }
    });
    $('#toolber-keyword')[0].addEventListener('focus',function(){
      $('#toolber-keyword')[0].removeAttribute('placeholder')
    });
    $('#toolber-keyword')[0].addEventListener('blur',function(){
      $('#toolber-keyword')[0].setAttribute('placeholder',input_placeholder)
    });
    // 绑定删除方法
    $(document).on('click','.toolbar_delete_bar',function(){
      $('.input_search').val(' ')
    })

    $(document).on('click','.btn_clear',function(){
      $('.input_search').val('');
      transform_search_icon()
      isInputSearch = false
    })
    if(hasLogin){
      $("#layerd").find("div.layer_close").click(function(){
        if(confirm("成为CSDN VIP会员,即刻享受全站免广告特权,前往购买?")){window.location.href="https://download.csdn.net/vip_code"}
        return false
      })
      var boxDefault = $("div.box-box-default"),
      boxLarge = $("div.box-box-large"),
      boxAways = $("div.box-box-aways");
      boxDefault.find("a.btn-remove").click(function() {
        if(confirm("成为CSDN VIP会员,即刻享受全站免广告特权,前往购买?")){window.location.href="https://download.csdn.net/vip_code"}
        return false
      });
      boxLarge.find("a.btn-remove").click(function() {
        if(confirm("成为CSDN VIP会员,即刻享受全站免广告特权,前往购买?")){window.location.href="https://download.csdn.net/vip_code"}
        return false
      });
      boxAways.find("a.btn-remove").click(function() {
        if(confirm("成为CSDN VIP会员,即刻享受全站免广告特权,前往购买?")){window.location.href="https://download.csdn.net/vip_code"}
        return false
      });
    }
    function transform_search_icon(){
      if($('.btn-search').length>0){
        $('.btn-search').detach();
        $('.search_bar').append('<a href="javascript:;" class="btn-nobg-noborder btn_clear"><i class="iconfont-toolbar toolbar-guanbi1"></i></a>');
      }else{
        $('.btn_clear').detach();
        $('.search_bar').append('<a href="//so.csdn.net/so/" target="_blank" class="btn-nobg-noborder btn-search"><i class="iconfont-toolbar toolbar-sousuo"></i></a>');
      }
    }
    // toolbar_prompt_hover($('.write-bolg-btn a'),'写博客')
    // toolbar_prompt_hover($('.gitChat a'),'发布Chat')
    // toolbar_prompt_hover($('.search_bar'),'err')
    function toolbar_prompt_hover(e,text){
      e = e instanceof jQuery ? e:$(e);
      e.css({'position':'relative'})
      var con = {
                e:e,
                text:text,
                isbind:false
       },
        isHave = false;
      this.events = this.events ? this.events : [con];
      for (var i = 0; i < this.events.length; i++) {
        if(this.events[i].e[0] == e[0]){
          this.events[i].text = text;
          this.events[i].e.children('.toolbar-prompt-box').text(this.events[i].text);
          isHave = true;
        }
      }
      if(!isHave){
        this.events.push(con);
        isHave= false
      }
      if(!this.events[this.events.length-1].isbind){
        toolbar_binding(this.events[this.events.length-1])
        this.events[this.events.length-1].isbind = true;
      }
    }
    function toolbar_binding(evens){
          var even = evens.e;
              t = evens.text;
          even.append(toolbar_tpls(t))
          var even_children = even.children('.toolbar-prompt-box'),
              even_children_w = even_children.width(),
              even_width = even.width();
          even_children.css({'left':-((even_children_w+16-even_width)/2)}).children().css({'left':(even_children_w+16)/2-5})
          even.hover(toolbar_prompt_show,toolbar_prompt_hide)
    }
    function toolbar_tpls(t) {
      var tpl = '<div class="toolbar-prompt-box">\
                  <div class="arrow"></div>\
                  <span>'+t+'</span>\
                </div>';
                return tpl;
    }
    function children_show(t){
      $(t).children('.toolbar-prompt-box').fadeIn(500)
    }
    var clearTime;//计时器id
    function toolbar_prompt_show() {
      if($(this).children('.toolbar-prompt-box').is(":animated")){
        $(this).children('.toolbar-prompt-box').stop(true,true).css({'display':'block'})
      }else{
        clearTime = setTimeout(children_show,1000,this);
      }
    }
    function toolbar_prompt_hide() {
      clearTimeout(clearTime)
      $(this).children('.toolbar-prompt-box').fadeOut(500)

    }
  // 阻止
  $(document).on('click','.prevent_a',function(e){
    e.preventDefault();
  });
  function loadScript(url, callback){
      var script = document.createElement ("script")
      script.type = "text/javascript";
      if (script.readyState){ //IE
          script.onreadystatechange = function(){
              if (script.readyState == "loaded" || script.readyState == "complete"){
                  script.onreadystatechange = null;
                  callback();
              }
          };
      } else { //Others
          script.onload = function(){
              callback();
          };
      }
      script.src = url;
      document.getElementsByTagName("head")[0].appendChild(script);
  }

  loadScript("//csdnimg.cn/search/baidu_opensug-1.0.0.js",function(){
      BaiduSuggestion.bind("toolber-keyword",{
          "XOffset":"0",
          "YOffset":"-5",
          "fontSize":"14px",		//文字大小
          "width" : 200,
          "line-height" : "35px",
          "padding" : "0 10px",
          "borderColor":"#e2e2e2", 	//提示框的边框颜色
          "bgcolorHI":"#f44444",		//提示框高亮选择的颜色
          "sugSubmit":false		//在选择提示词条是是否提交表单
      });
  });
})
})(jQuery);
