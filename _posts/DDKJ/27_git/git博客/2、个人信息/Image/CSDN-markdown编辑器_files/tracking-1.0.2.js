// last motify 2017-12-19 14:46:10

/**
 *  @author  yanglw@csdn.net
 *  @version 1.0.3
 *  @description CSDN统一上报JS-SDK
 */

(function () {
    var CFG, exports, _fn, allParam;
    CFG = {
        SERVER_PV_URL: window.location.protocol+'//pv.csdn.net/csdnbi',
        SERVER_RE_URL: window.location.protocol+'//re.csdn.net/csdnbi'
    }

    allParam = {
        headers: {
            component: "enter",
            datatype: "pv",
            version : "v1"
        },
        body: {}
    }

    _fn = {
        /**
         *
         * @param datatype  re:曝光/点击;track:pv统计
         * @param params
         */
        buildReqParam : function(datatype,params){
            var body={
                "re":params
            };
            return "["+JSON.stringify($.extend(allParam, {
                    headers: {
                        component: "enterprise",
                        datatype: datatype,
                        version : "v1"
                    },
                    body: JSON.stringify(body)
                }))+"]";
        },
        serverUrl : function(datatype){
            if("track"==datatype){
                return CFG.SERVER_PV_URL;
            }else{
                return CFG.SERVER_RE_URL;
            }
        }
    }

    exports = {
        /**
         * CSDN 数据上报接口
         * @param datatype  数据类型:re( 曝光/点击);track( pv统计 )
         * @param params
         */
        trackReport : function(datatype,params){
            var data=_fn.buildReqParam(datatype,params);
            // console.log(data);
            $.ajax({
                url:_fn.serverUrl(datatype),
                type:'POST',
                async:true,
                crossDomain: true,
                xhrFields: {
                    withCredentials: true
                },
                contentType:'text/plain;charset=UTF-8',
                data:data,
                success:function(){

                },
                error : function () {
                    // console.error("csdn.track.report()",arguments);
                }
            });
        }
    };

    if (window.csdn === undefined) {
        window.csdn = {};
    }
    for (i in exports) {
        window.csdn[i] = exports[i];
    }
})();

/*
 @file 前端用户行为跟踪
 @author <caoyu#at#csdn.net>
 @version 20160323
 */

(function() {
    var slice = [].slice;

    (function(definition, undef) {
        var $, exports, global, i, t;
        global = this;
        $ = global.jQuery;
        exports = {};
        definition(global, exports, $);
        if (global.csdn === undef) {
            global.csdn = exports;
        }
        for (i in exports) {
            global[i] = global.csdn[i] = exports[i];

        }
        t = exports.tracking;
        t({
            '': [t.baseParams, t.tos, t.sessionId],
            'bbs.csdn.net': t.tags('/topics/', 'div.tag span'),
            'blog.csdn.net': t.tags('/article/details/', 'div.tag2box a'),
            'ask.csdn.net': t.tags('/questions/', 'div.tag_data a.tag span'),
            'download.csdn.net': t.tags('/detail/', 'div.info a[href^="/tag/"]'),
            'www.csdn.net': [t.tags('/article/', 'div.tag a'), t.cmsPid],
            'www.csto.com': t.tags('/p/', 'span.tech a'),
            'www.iteye.com': t.tags('/topic/', '#topic_tags a')
        });
    })(function(global, exports, $) {
        var iarr = [], _key=0;
        var crossdomainGet, doc, domReady, flush, fns, hack, loaded, loc, protocol, querySelectorAll, testEl, topDomain, tracking;
        doc = global.document;
        loc = global.location;
        protocol = loc.protocol.substr(0, 4) === 'http' ? '' : 'http:';
        fns = [];
        testEl = doc.documentElement;
        hack = testEl.doScroll;
        loaded = (hack ? /^loaded|^c/ : /^loaded|c/).test(doc['readyState']);
        flush = function() {
            var f;
            loaded = 1;
            while (f = fns.shift()) {
                f();
            }
        };
        if (typeof doc.addEventListener === "function") {
            doc.addEventListener('DOMContentLoaded', function() {
                doc.removeEventListener('DOMContentLoaded', arguments.callee, false);
                flush();
            });
        }
        if (hack) {
            doc.attachEvent('onreadystatechange', function() {
                if (/^c/.test(doc.readyState)) {
                    doc.detachEvent('onreadystatechange', arguments.callee);
                    flush();
                }
            });
        }
        domReady = hack ? function(fn) {
            if (global.self !== global.top) {
                if (loaded) {
                    return fn();
                } else {
                    return fns.push(fn);
                }
            } else {
                try {
                    testEl.doScroll('left');
                } catch (_error) {
                    global.setTimeout(function() {
                        domReady(fn);
                    }, 50);
                    return;
                }
                fn();
            }
        } : function(fn) {
            if (loaded) {
                fn();
            } else {
                fns.push(fn);
            }
        };
        topDomain = function(d) {
            return /\.?([a-z0-9\-]+\.[a-z0-9\-]+)(:\d+)?$/.exec(d)[1];
        };

        /*
         对外公开的跟踪函数tracking
         @param {Object} opts 定义了不同域名下要收集的信息
         */
        exports.tracking = tracking = function(opts) {
            domReady(function() {
                var data, i, j, k, len, len1, opt, ref;
                data = {};
                ref = [opts[loc.host], opts['']];
                for (j = 0, len = ref.length; j < len; j++) {
                    opt = ref[j];
                    if (opt) {
                        if (typeof opt === 'function') {
                            opt(data);
                        } else {
                            for (k = 0, len1 = opt.length; k < len1; k++) {
                                i = opt[k];
                                if (typeof i === "function") {
                                    i(data);
                                }
                            }
                        }
                    }
                }
                crossdomainGet(data);
            });
        };

        /*
         附加基本的参数到数据上，包括referrer user_name oid pid x-acl-token
         */
        tracking.baseParams = function(data) {
            var ref, ref1, ref2, ref3, ref4;
            data.user_name = /iteye.com$/.test(loc.host) ? ((ref = />欢迎([^<]+)<\/a>/.exec((ref1 = doc.getElementById('user_nav')) != null ? ref1.innerHTML : void 0)) != null ? ref[1] : void 0) || '' : ((ref2 = /(; )?(UserName)=([^;]+)/.exec(doc.cookie)) != null ? ref2[3] : void 0) || '';
            data['x-acl-token'] = 'status_js_dkuyqthzbajmncbsb_token';
            if (!data.pid) {
                data.pid = /iteye.com$/.test(loc.host) ? 'iteye' : doc.body.getAttribute('data-pid') || ((ref3 = /(\w+)\.\w+\.\w+$/.exec(loc.host)) != null ? ref3[1] : void 0);
            }
            data.oid = ((ref4 = querySelectorAll('.h-entry .p-author')[0]) != null ? ref4.innerHTML.replace(/^\s+|\s+$/g, '') : void 0) || '';
            //修改会议商品列表页嵌套的iframe的referrer开始
            function GetCurrenUrlString(){
                var name,value;
                var str= document.referrer; //取得整个地址栏
                var num=str.indexOf("?")
                str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]

                var arr=str.split("&"); //各个参数放到数组里
                for(var i=0;i < arr.length;i++){
                    num=arr[i].indexOf("=");
                    if(num>0){
                        name=arr[i].substring(0,num);
                        value=arr[i].substr(num+1);
                        this[name]=value;
                    }
                }
            }
            var Request = new GetCurrenUrlString();
            var huiyiProductId=Request.project_id;
            var curReferrer = global.document.referrer;
            //pareantsUrl;
            //var childiframeurl = document.goodsListIframe.pareantsUrl;
            //console.log(childiframeurl);
            //ref = global.document.referrer || '-';
            if(curReferrer.substr(0, 54)==='http://huiyi.csdn.net/api/activity_api/get_goods_list?'){
                var huiyiUrl = 'http://huiyi.csdn.net/activity/product/goods_list?'
                return data.referrer = huiyiUrl+'project_id='+huiyiProductId;
            }else{
                return data.referrer = doc.referrer;
            }
            //修改会议商品列表页嵌套的iframe的referrer结束

        };
        tracking.sessionId = function(data) {
            var ref, sid;
            sid = (ref = /\bdc_session_id=([^;]*)(?:$|;)/.exec(doc.cookie)) != null ? ref[1] : void 0;
            /*if (!/^https?:\/\/([\w-]+\.)*csdn\.net\//.test(doc.referrer)) {
             sid = void 0;
             }*/
            if (sid === void 0) {
                sid = new Date().getTime()+"_"+Math.random();
            }
            doc.cookie = "dc_session_id=" + sid + " ; path=/ ; domain=." + (topDomain(loc.host));
            return data.session_id = "" + sid;
        };

        /*
         附加上一页面及停留时间到数据上
         */
        tracking.tos = function(data) {
            var e, now, ref, t, tos;
            now = +new Date() / 1000 | 0;
            t = (ref = /\bdc_tos=([^;]*)(?:$|;)/.exec(doc.cookie)) != null ? ref[1] : void 0;
            try {
                tos = now - parseInt(t, 36);
            } catch (_error) {
                e = _error;
                tos = -1;
            }
            doc.cookie = "dc_tos=" + (now.toString(36)) + " ; expires=" + (new Date((now + 4 * 60 * 60) * 1000).toGMTString()) + " ; max-age=" + (4 * 60 * 60) + " ; path=/ ; domain=." + (topDomain(loc.host));
            return data.tos = tos;
        };

        /*
         返回附加tag参数到数据上的函数，只在指定的path中生效，具体的tags由selectors指定
         @param {String/RegExp} path 要匹配的地址路径片段
         @param {Array[String/Function]} selectors tag的选择器或者是返回tag数组的函数列表
         */
        tracking.tags = function() {
            var path, selectors;
            path = arguments[0], selectors = 2 <= arguments.length ? slice.call(arguments, 1) : [];
            return function(data) {
                var e, eles, i, j, k, l, len, len1, len2, ref, result, sel, t;
                if (typeof path === 'string' && !~loc.pathname.indexOf(path) || path instanceof RegExp && !path.test(loc.pathname)) {
                    return;
                }
                eles = [];
                for (j = 0, len = selectors.length; j < len; j++) {
                    sel = selectors[j];
                    ref = (typeof sel === 'string' ? querySelectorAll(sel) : (typeof sel === "function" ? sel() : void 0) || []);
                    for (k = 0, len1 = ref.length; k < len1; k++) {
                        i = ref[k];
                        eles.push(i);
                    }
                }
                result = {};
                for (l = 0, len2 = eles.length; l < len2; l++) {
                    e = eles[l];
                    result[e.innerHTML.replace(/^\s+|\s+$/g, '')] = 1;
                }
                data.tag = ((function() {
                    var results;
                    results = [];
                    for (t in result) {
                        results.push(t);
                    }
                    return results;
                })()).join();
            };
        };

        /*
         附加pid参数到数据上的函数，针对 www.csdn.net/article/ 下的文章页探测真实pid
         */
        tracking.cmsPid = function(data) {
            if (loc.pathname.indexOf('/article/') === -1) {
                return;
            }
            try {
                return data.pid = querySelectorAll('.brea_nav > a')[1].hostname.match(/(\w+)\.\w+\.\w+$/)[1];
            } catch (_error) {

            }
        };
        /*
         附加wechat参数到数据上的函数，针对微信浏览器用户增加参数
         */
        /*tracking.wechat=function(data){
         var ua = window.navigator.userAgent.toLowerCase();
         if(ua.match(/MicroMessenger/i) == 'micromessenger'){
         data.source='wechat';
         }else{
         return false;
         }
         }*/

        /*
         使用CSS选择器检索对应的DOM元素
         @param {String} selector - CSS选择器
         @returns {Array[HTMLElement]} HTML元素集合，如果浏览器不支持使用CSS选择器查找将返回 undefined，如果找不到任何元素返回0长度的近似数组
         */
        tracking.querySelectorAll = querySelectorAll = function(selector) {
            return (typeof doc.querySelectorAll === "function" ? doc.querySelectorAll(selector) : void 0) || (typeof $ === "function" ? $(selector) : void 0) || global.Prototype && (typeof global.$$ === "function" ? global.$$(selector) : void 0) || [];
        };

        /*
         发送跨域HTTP GET请求
         @param {String} url - 请求的Url，忽略将使用默认的行为跟踪地址
         @param {Object} data - 请求要提交的数据
         */
        return tracking.crossdomainGet = crossdomainGet = function(data) {
            csdn.trackReport("track",$.param(data));
        };
    });

}).call(this);
