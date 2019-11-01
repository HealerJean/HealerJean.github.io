package com.healerjean.proj.constant;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/5/28  下午3:18.
 */
public class WechatApiUrlConstants {


    /**
     * token获取
     */
    public static final String WECHAT_TOKEN_GET_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 新增其他类型永久素材
     */
    public static final String UPLOAD_FORVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * 微信临时上传url api
     */
    public static final String UPLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=";

    /**
     * 微信媒体 下载 url
     */
    public static final String DOWNLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";


    /**
     * 添加客服账号
     */

    public static final String CALLPERSON_ADD_ACCOUNT_URL = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";


    /**
     * 获取所有客服账号
     */
    public static final String CALLPERSION_GET_ALL_ACCOUNT_LIST_URL = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN";

    /**
     * 微信客服发送消息url
     * POST请求
     */
    public static final String CALLPERSION_SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";


    /**
     * 获取用户列表
     * 一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。
     */

    public static final String USER_GET_LIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

    /**
     * 获取公众号已经获取的标签
     * GET
     */
    public static final String USER_TAGS_GET_URL = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN";


    /**
     * 获取标签下粉丝列表
     * POST 微信接口文档写错了
     */
    public static final String USER_TAGS_GET_PERSONS_URL = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=ACCESS_TOKEN";

    /**
     * 批量为用户打标签
     */
    public static final String USER_SET_TAG_URL = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN";


    /**
     * 获取用户基本信息
     */
    public static final String USER_GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";


    //2、 微信认证登录

    /**
     * 第一步：用户同意授权，获取code
     */
    public static final String WEB_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";


    /**
     * 第二步：通过code换取网页授权access_token
     */
    public static final String WEB_AUTHORIZE_GET_ACCESSTOKEN_BY_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";


    /**
     * 第三步：刷新access_token（如果需要）
     */

    public static final String WEB_AUTHORIZE_REFRESH_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

    /**
     * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
     */

    public static final String WEB_AUTHORIZE_GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 检验授权凭证（access_token）是否有效
     */
    public static final String WEB_AUTHORIZE_CHECK_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";


    //3、 微信支付

    /**
     * 企业付款
     */
    public static final String PAY_QIYE_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


    /**
     * 查询企业付款
     */
    public static final String PAY_QIYE_QUERY_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

    /**
     * 二维码生成
     */
    public static final String QR_CODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

    /**
     * 二维码下载
     */
    public static final String QR_IMAGE_DOWNLOAD_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";


    /**
     * 长连接转短链接
     */

    public static final String LONG_TO_SHORT_RUL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";


    /**
     * 微信模板列表
     */
    public static final String TEMPLATE_MESSAGE_LIST_GET_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /**
     * 删除模板
     */

    public static final String TEMPLATE_MESSAGE_DEL_URL = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";

    /**
     * 发送模板消息
     */
    public static final String TEMPLATE_MESSAGE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";


    /**
     * 获取模板id
     */
    public static final String TEMPLATE_MESSAGE_GET_ID_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";

}
