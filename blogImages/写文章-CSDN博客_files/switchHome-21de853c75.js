(function() {
	var blogname = 'write',
		address = window.location.host.split(".")[0], // 获取二级域名
		homeAdress = {
			feed: 'https://mp.csdn.net/postlist',
			blog: 'http://write.blog.csdn.net/'
		},

		isFeed = false, // 是否feed首页
		popu_content = {
			popuNUm: 'popu_583',
			pid: 'write',
			push: 'get'
		}
	// 判断二级域名是否显示切换开关
	// if ((address == blogname)) return;
	// 判断是否是详情页
	if ((address === blogname)) {
		// 如果是博客首页的子页面或详情页则不显示
		if (!(address == blogname)) return;
		// 博客首页判断是否不在需要直接跳转到feed首页
		var searchStr = window.location.search.substring(1).split("&").forEach(function(s) {
			if (s.indexOf('mp') > -1) {
				if (s.split("=")[1] === '0') localStorage.setItem('switchHome', 0)
			}
		});
		var switchHome = localStorage.getItem('switchHome');
		// 判断localStorage 查看是否需要直接跳转到feed首页
		if (parseInt(switchHome)) window.location.href = homeAdress.feed;
		// feed首页 和迁移后的blog首页 显示欢迎文字
	} else if (address === 'mp') {

		isFeed = true;
		popu_content = {
			popuNUm: 'popu_584',
			pid: 'mp',
			push: 'get'
		}
	}
	// 使用onload 事件加载
	function addLoadEvent(func) {
		var oldonload = window.onload;
		if (typeof window.onload != 'function') {
			window.onload = func;
		} else {
			window.onload = function() {
				oldonload();
				func();
			}
		}
	};

	function isSwitchAddress() {
		var switchButtonNew = '新版';
		var text = 'CSDN创作中心alpha版已经发布，欢迎体验',
			activationDom = '.switch-old';
		if (isFeed) text = '<span style="">对新版有任何意见或建议，请前往<a style="color:#349EDF;" target="_blank"href="https://bbs.csdn.net/forums/BlogQuestion">专区</a>进行反馈</span>';
		var mainLeft = 0;
		try {
			mainLeft = $('main').length > 0 ? $('main').offset().left : $('.blog_l').offset().left;
		} catch (e) {
			mainLeft = 150;
		}
		var switchDOMText = '<div class="switchDOM " data-poputype="feed" data-feed-show="false" style ="display:none;height:35px;background: #FAFAFA;box-shadow: 0 1px 2px 0 rgba(0,0,0,0.1);margin-bottom:1px;">\
                      <div class="switch-text" style="padding-left:150px;padding-right:16px;"><span style="line-height:35px;font-size: 14px;color: #4F4F4F;">' + text + '</span>\
                        <div class="switch-bottom csdn-tracking-statistics" style="float:right">\
                          <a class="switch-old" href="javascript:void(0);" style="padding-bottom: 5px;padding-top: 5px;line-height:35px;font-size:12px;padding-left:10px;padding-right:10px;" target="_self">去旧版</a>\
                          <a class="switch-new" href="javascript:void(0);" style="padding-bottom: 5px;padding-top: 5px;line-height:35px;font-size:12px;padding-left:10px;padding-right:10px;" target="_self">' + switchButtonNew + '</a>\
                        </div>\
                      </div>\
                      <style>\
                      .switch-old,.switch-new,.switch-new:link,.switch-new:visited,.switch-old:link,.switch-old:visited{color:#999;}\
                      body .switch-activation{border-radius: 16px;background-color: #5B5B5B;color:#fff !important}\
                      body .switch-old:hover,body .switch-new:hover{color: #4F4F4F;}\
                      body .switch-activation:hover{color:#999;} \
                      </style>\
                    </div>'
		var bodyDOM = $('body');
		bodyDOM.prepend(switchDOMText);
		csdn.trackingAd('.switch-bottom', {
			pid: popu_content.pid,
			mod: popu_content.popuNUm,
			dsm: popu_content.push,
			mtp: '1'
		});
		if (isFeed) activationDom = '.switch-new';
		$(activationDom).addClass('switch-activation');
		$('.switchDOM').show(500);
		$('.switch-old').on('click', function() {
			if (address === blogname) return
			window.location.href = homeAdress.blog + "?feed=0";
		})
		$('.switch-new').on('click', function() {
			if (address === 'mp') return
			localStorage.setItem('switchHome', 1);
			window.location.href = homeAdress.feed;
		})
	}
	addLoadEvent(isSwitchAddress);
})();