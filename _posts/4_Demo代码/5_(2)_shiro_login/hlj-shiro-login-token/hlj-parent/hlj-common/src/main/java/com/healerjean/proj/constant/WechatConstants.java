package com.healerjean.proj.constant;

/**
 * @author HealerJean
 * @ClassName WechatConstants
 * @date 2019/9/30  14:21.
 * @Description
 */
public class WechatConstants {

    /**  消息类型   */
    public static final String MSG_TYPE = "MsgType";
    /**  微信公众号号 */
    public static final String MSG_TO_USERNAME = "ToUserName";
    /** 来源 openId */
    public static final String MSG_FROM_USERNAME = "FromUserName";
    /** 点击事件 */
    public static final String MSG_EVENT_KEY = "EventKey";
    /** 消息内容 */
    public static final String MSG_CONTENT = "Content";



    /**  文本消息 */
    public static final String MSG_TEXT = "text";
    /**  图片消息 */
    public static final String MSG_IMAGE = "image";
    /**  图文消息 */
    public static final String MSG_NEWS = "news";
    /**  语音消息   */
    public static final String MSG_VOICE = "voice";
    /** 视频消息  */
    public static final String MSG_VIDEO = "video";
    /** 小视频消息 */
    public static final String MSG_SHORTVIDEO = "shortvideo";
    /** 地理位置消息  */
    public static final String MSG_LOCATION = "location";
    /**  链接消息 */
    public static final String MSG_LINK = "link";
    /**  事件推送消息 */
    public static final String MSG_EVENT = "event";
    /**  事件推送消息中,事件类型，subscribe(订阅)  */
    public static final String MSG_EVENT_SUBSCRIBE = "subscribe";
    /**  事件推送消息中,事件类型，unsubscribe(取消订阅)  */
    public static final String MSG_EVENT_UNSUBSCRIBE = "unsubscribe";
    /** 事件推送消息中,上报地理位置事件 */
    public static final String MSG_EVENT_LOCATION_UP = "LOCATION";
    /** 事件推送消息中,自定义菜单事件,点击菜单拉取消息时的事件推送  */
    public static final String MSG_EVENT_CLICK = "CLICK";
    /** 事件推送消息中,自定义菜单事件,点击菜单跳转链接时的事件推送  */
    public static final String MSG_EVENT_VIEW = "VIEW";


    /** 帮助图片  */
    public static final String MSG_HELP_IMAGE = "帮助中心";
    /**  关键词回复  */
    public static final String MSG_KEYWORDS_REPLY = "关键词回复";
    /**  关注回复  */
    public static final String MSG_SUBSCRIBE_REPLAY = "关注回复";
    /** 关注回复口令 */
    public static final String MSG_SUBSCRITE_REPLY_KOULING = "关注回复口令";


    /**
     * 创建菜单url
     */
    public static final  String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
    public static final  String CLICK = "click"; // click菜单
    public static final  String VIEW = "view"; // url菜单
    public static final  String SCANCODE_WAITMSG = "scancode_waitmsg"; // 扫码带提示
    public static final  String SCANCODE_PUSH = "scancode_push"; // 扫码推事件
    public static final  String PIC_SYSPHOTO = "pic_sysphoto"; // 系统拍照发图
    public static final  String PIC_PHOTO_OR_ALBUM = "pic_photo_or_album"; // 拍照或者相册发图
    public static final  String PIC_WEIXIN = "pic_weixin"; // 微信相册发图
    public static final  String LOCATION_SELECT = "location_select"; // 发送位置



}
