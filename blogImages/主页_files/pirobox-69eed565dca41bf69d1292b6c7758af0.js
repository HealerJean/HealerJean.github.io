/*
* Name: piroBox v.1.2.2
* download by www.97zzw.com
* Version: 1.2.2
*/
(function($) {
	$.fn.piroBox = function(opt) {
		opt = jQuery.extend({
		my_speed : null,
		close_speed : 300,
		bg_alpha : 0.5,
		close_all : '.piro_close,.piro_overlay',
		slideShow : null,
		slideSpeed : null
		}, opt);

		function start_pirobox() {
		  var corners = 
			  '<tr>'+					   
			  '<td colspan="3" class="pirobox_up"></td>'+
			  '</tr>'+	
			  '<tr>'+	
			  '<td class="t_l"></td>'+
			  '<td class="t_c"></td>'+
			  '<td class="t_r"></td>'+
			  '</tr>'+
			  '<tr>'+
			  '<td class="c_l"></td>'+
			  '<td class="c_c"><span><span></span></span><div></div></td>'+
			  '<td class="c_r"></td>'+
			  '</tr>'+
			  '<tr>'+
			  '<td class="b_l"></td>'+
			  '<td class="b_c"></td>'+
			  '<td class="b_r"></td>'+
			  '</tr>'+
			  '<tr>'+
			  '<td colspan="3" class="pirobox_down"></td>'+
			  '</tr>';
			var window_height =  $(document).height();
			var bg_overlay = $(jQuery('<div class="piro_overlay"></div>').hide().css({'opacity':+opt.bg_alpha,'height':window_height+'px'}));
			var main_cont = $(jQuery('<table class="pirobox_content" cellpadding="0" cellspacing="0"></table>'));
			var caption = $(jQuery('<div class="caption"></div>'));
			var piro_nav = $(jQuery('<div class="piro_nav"></div>'));
			var piro_close = $(jQuery('<a href="#close" class="piro_close" title="关闭"></a>'));
			var piro_play = $(jQuery('<a href="#play" class="play" title="播放"></a>'));
			var piro_stop = $(jQuery('<a href="#stop" class="stop" title="暂停"></a>'));
			var piro_prev = $(jQuery('<a href="#prev" class="piro_prev" title="previous"></a>'));
			var piro_next = $(jQuery('<a href="#next" class="piro_next" title="next"></a>'));				
			$('body').append(bg_overlay).append(main_cont);
			main_cont.append(corners);
			$('.pirobox_up').append(piro_close);
			$('.pirobox_down').append(piro_nav);
			$('.pirobox_down').append(piro_play);
			piro_play.hide();
			$('.pirobox_down').append(piro_prev).append(piro_next);
			piro_nav.append(caption);
			var my_nav_w = piro_prev.width();
			main_cont.hide();
			var my_gall_classes = $("a[class^='pirobox']");
			var map = new Object();
				for (var i=0; i<my_gall_classes.length; i++) {
					var it=$(my_gall_classes[i])
					map['a.'+it.attr('class')]=0;
				}
			var gall_settings = new Array();
				for (var key in map) {
					gall_settings.push(key);
				}
				for (var i=0; i<gall_settings.length; i++) {
					$(gall_settings[i]).each(function(rel){this.rel = rel+1+"/"+$(gall_settings[i]).length;});
						var add_first = $(gall_settings[i]+':first').addClass('first');
						var add_last = $(gall_settings[i]+':last').addClass('last');
				}						
			$(my_gall_classes).each(function(rev){this.rev = rev+0});
			var imgCache = $(my_gall_classes).each(function(){this.href});
			var hidden = $('body').append('<div id="imgCache" style="display:none"></div>').children('#imgCache');
			$.each(imgCache, function (i,val) {
				$('<div/>').css({'background':'url('+val+')'/*,'width':'600px','height':'200px'*/}).appendTo(hidden);
			});
			var piro_gallery = $(my_gall_classes);
			$.fn.fixPNG = function() {
				return this.each(function () {
					var image = $(this).css('backgroundImage');
					if (image.match(/^url\(["']?(.*\.png)["']?\)$/i)) {
						image = RegExp.$1;
						$(this).css({
							'backgroundImage': 'none',
							'filter': "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=" + ($(this).css('backgroundRepeat') == 'no-repeat' ? 'crop' : 'scale') + ", src='" + image + "')"
						}).each(function () {
							var position = $(this).css('position');
							if (position != 'absolute' && position != 'relative')
								$(this).css('position', 'relative');
						});
					}
				});
			};
			/*$.browser.msie6 =($.browser.msie && /MSIE 6\.0/i.test(window.navigator.userAgent));
			if( $.browser.msie6 && !/MSIE 8\.0/i.test(window.navigator.userAgent)) {
				$('.t_l,.t_c,.t_r,.c_l,.c_r,.b_l,.b_c,.b_r,a.piro_next, a.piro_prev,a.piro_prev_out,a.piro_next_out,.c_c,.piro_close,a.play,a.stop').fixPNG();
				var ie_w_h =  $(document).height();
				bg_overlay.css('height',ie_w_h+ 'px'); 
			}
			if( $.browser.msie) {
			opt.close_speed = 0;
			}*/
			$(window).resize(function(){
				var new_w_bg = $(document).height();
				bg_overlay.css({'visibility':'visible','height':+ new_w_bg +'px'});				  
			});	
			piro_prev.add(piro_next).bind('click',function(c) {
				c.preventDefault();
				var image_count = parseInt($(piro_gallery).filter('.item').attr('rev'));
				var start = $(this).is('.piro_prev_out,.piro_prev') ? $(piro_gallery).eq(image_count - 1) : $(piro_gallery).eq(image_count + 1);
				if(!start.size()) {
					start = $(this).is('.piro_prev_out,.piro_prev') ? $(piro_gallery).eq($(piro_gallery).size() - 1) : $(piro_gallery).eq(0);
				}
				start.click();
				piro_close.add(caption).add(piro_next).add(piro_prev).css('visibility','hidden');
			});
			$(piro_gallery).each(function(array) {
					var item = $(this);
					item.unbind(); 
					item.bind('click',function(c) {
						c.preventDefault();
						piro_open(item.attr('href'));
						var this_url = item.attr('href');
						//var descr = item.children('span').html();
						var descr = item.attr('title');	
						var number = item.attr('rel');
						if( descr == ""){
						caption.html('<p>'+ this_url+'<em class="number">' + number + '</em><a href='+ this_url +' class="link_to" target="_blank" title="查看原图"></a></p>');
						}else{
						caption.html('<p>'+ descr+'<em class="number">' + number + '</em><a href='+ this_url +' class="link_to" target="_blank" title="查看原图"></a></p>');
						}
						if(item.is('.last')){
							$('.number').css('text-decoration','underline');
							
						}else{
							$('.number').css('text-decoration','none');
							}				
						if(item.is('.first')){
							piro_prev.hide();
							piro_next.show();		
						}else{
							piro_next.add(piro_prev).show();		  
						}
						if(item.is('.last')){
							piro_prev.show();
							piro_next.hide();
							piro_play.css('width','0');	  
						}else{
							piro_play.css('width','40px');
							}
						if(item.is('.last') && item.is('.first') ){
							piro_prev.add(piro_next).hide();
							$('.number').hide();
							piro_play.remove();
						}					
							$(piro_gallery).filter('.item').removeClass('item');
							item.addClass('item');
							$('.c_c').removeClass('unique');		
					});
				});
				var piro_open = function(my_url) {
					piro_play.add(piro_stop).hide();
					piro_close.add(caption).add(piro_next).add(piro_prev).css('visibility','hidden');
					if(main_cont.is(':visible')) {
						$('.c_c div').children().fadeOut(300, function() {
							$('.c_c div').children().remove();
							load_img(my_url);
						});
					} else {
						$('.c_c div').children().remove();
						main_cont.show();
						bg_overlay.fadeIn(300,function(){
						load_img(my_url);
						});
					}
				}
				var load_img = function(my_url) {
				if(main_cont.is('.loading')) {return;}
				main_cont.addClass('loading');
				var img = new Image();
				img.onerror = function (){
					var main_cont_h = $(main_cont).height();
					main_cont.css({marginTop : parseInt($(document).scrollTop())-(main_cont_h/1.9)});
				  $('.c_c div').append('<p class="err_mess">原图不存在或路径不对:&nbsp;<a href="#close" class="close_pirobox">点击关闭</a></p>');
					$('.close_pirobox').bind('click',function(c) {
						c.preventDefault();
						piro_close.add(bg_overlay).add(main_cont).add(caption).add(piro_next).add(piro_prev).hide(0,function(){ img.src = '';});
						main_cont.removeClass('loading');
					});
				}
				img.onload = function() {
					var imgH = img.height;
					var imgW = img.width;
					var main_cont_h = $(main_cont).height();

					var w_H = $(window).height();
					var w_W = $(window).width();

					if(w_H<imgH){
						img_H=w_H*0.8;
						imgW=0.8*w_H*imgW/imgH;
						imgH=img_H;
					}

					if(w_W<imgW){
						img_W=w_W*0.8;
						imgH=0.8*imgH*w_W/imgW;
						imgW=img_W;
					}

					$(img).height(imgH).width(imgW).hide();
						$('.c_c div').animate({height:imgH+'px',width:imgW+'px'},opt.my_speed);
						var fix = imgH/w_H*2.3;
						if(w_H < imgH){h_fix = fix;}else{h_fix = 2;}
						main_cont.animate({
						height : (imgH+40) + 'px' ,
						width : (imgW+40) + 'px' , 
						marginLeft : '-' +((imgW)/2+20) +'px',
						marginTop : parseInt($(document).scrollTop())-(imgH/h_fix)},opt.my_speed, function(){
						$('.piro_nav,.caption').css({width:(imgW)+'px','margin-bottom':'10px'});
						$('.piro_nav').css('margin-left','-'+(imgW)/2+'px');
							var caption_height = caption.height();
							$('.c_c div').append(img);					
							piro_close.css('display','block');
							piro_next.add(piro_prev).add(piro_close).css('visibility','visible');
							caption.css({'visibility':'visible','display':'block','opacity':'0.8','overflow':'hidden'});
							main_cont.hover(function(){
								caption.stop().fadeTo(200,0.8);},
								function(){caption.stop().fadeTo(200,0);
								});
								$(img).fadeIn(300);
									main_cont.removeClass('loading');
									if(opt.slideShow === true){
									   piro_play.add(piro_stop).show();
									}else{
										 piro_play.add(piro_stop).hide();
									}	
							});			
						}
					img.src = my_url;
					$('html').bind("keyup", function (c) {
						 if(c.keyCode == 27) {
							c.preventDefault();
							if($(img).is(':visible') || $('.c_c>div>p>a').is('.close_pirobox')){
								$(piro_gallery).removeClass('slideshow').removeClass('item');
								piro_close.add(bg_overlay).add(main_cont).add(caption).add(piro_next).add(piro_prev).hide(0,function(){ img.src = '';});
								main_cont.removeClass('loading');
								clearTimeout(timer);
								$(piro_gallery).children().removeAttr('class');
								$('.stop').remove();
								$('.c_c').append(piro_play);
								$('.sc_menu').css('display','none');
								$('ul.sc_menu li a').removeClass('img_active').css('opacity','0.4');	
								piro_next.add(piro_prev).show().css({'top':'50%'});	
								$(piro_gallery).children().fadeTo(100,1);
							}
						}
					});
					$('html').bind("keyup" ,function(e) {
						 if ($('.item').is('.first')){
						}else if(e.keyCode == 37){
						e.preventDefault();
							if($(img).is(':visible')){
								clearTimeout(timer);
								$(piro_gallery).children().removeAttr('class');
								$('.stop').remove();
								$('.c_c').append(piro_play);
								piro_prev.click();
							}
						 }
					});
					$('html').bind("keyup" ,function(z) {
						if ($('.item').is('.last')){
						}else if(z.keyCode == 39){
						z.preventDefault();
							if($(img).is(':visible')){
								clearTimeout(timer);
								$(piro_gallery).children().removeAttr('class');
								$('.stop').remove();
								$('.c_c').append(piro_play);
								piro_next.click();
								//alert('click')
							}
						}
					});
					var win_h = $(window).height();
					piro_stop.bind('click',function(x){
						x.preventDefault();
						clearTimeout(timer);
						$(piro_gallery).removeClass('slideshow');
						$('.stop').remove();
						$('.pirobox_down').append(piro_play);
						piro_next.add(piro_prev).css('width',my_nav_w+'px');
					});
					piro_play.bind('click',function(w){
						w.preventDefault();
						clearTimeout(timer);
						if($(img).is(':visible')){
						$(piro_gallery).addClass('slideshow');
						$('.play').remove();
						$('.pirobox_down').append(piro_stop);
						}
						piro_next.add(piro_prev).css({'width':'0px'});
						return slideshow();
					});
				  $(opt.close_all).bind('click',function(c) {
					$(piro_gallery).removeClass('slideshow');
					clearTimeout(timer);
					if($(img).is(':visible')){
						c.preventDefault();
						piro_close.add(bg_overlay).add(main_cont).add(caption).add(piro_next).add(piro_prev).hide(0,function(){ img.src = '';});
						main_cont.removeClass('loading');
						$(piro_gallery).removeClass('slideshow');
						piro_next.add(piro_prev).css('width',my_nav_w+'px').hide();
						$('.stop').remove();
						$('.pirobox_down').append(piro_play);
						piro_play.hide();
					}
				  });	
					if(opt.slideShow === true){
						function slideshow(){
							if( $(piro_gallery).filter('.item').is('.last')){
							clearTimeout(timer);
							$(piro_gallery).removeClass('slideshow');
							$('.stop').remove();
							$('.pirobox_down').append(piro_play);
							piro_next.add(piro_prev).css('width',my_nav_w+'px');								 
							} else if($(piro_gallery).is('.slideshow' ) && $(img).is(':visible')){
								clearTimeout(timer);
								piro_next.click();
							}
						}					
						var timer = setInterval(slideshow,opt.slideSpeed*1000 );
					}


				}
			}

		start_pirobox();
	}
})(jQuery);