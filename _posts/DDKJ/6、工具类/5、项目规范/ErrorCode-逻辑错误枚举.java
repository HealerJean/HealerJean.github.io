package com.duodian.admore.zhaobutong.bean;

/**
 * Created by mkoo on 2017/3/22.
 */
public enum ErrorCode {

    TODO(-1, "开发中"),
    OK(200, ""),
    NeedExit(300, "发现异常，请退出重新打开应用"),
    NeedUpdate(301, "当前版本已无法使用，请升级版本"),
    HaveUpdate(302, "发现新版本，是否要升级？"),


    ParamsError(400, "参数错误"),
    ForbidError(401, "权限错误"),
    //FlushError(401, "请刷新页面重试"),
    ServerError(500, "服务器忙请稍后重试"),

    DeviceExsit(1001, "设备已经被其他账号绑定"),
    DeviceForbid(1002, "设备已被禁用"),

    TokenExpired(1003, "token已过期，请重新登录"),
    TokenInvalid(1004, "token已失效，请重新登录"),
//    TokenInvalid(1004, "您已在其他设备上登录，请重新登录"),
    TokenForbide(1005, "您已被禁用"),

    AccountNotExsit(1005, "用户不存在"),
    PasswordError(1006, "密码错误"),
    AccountExsit(1007, "用户已存在"),

    MoneyNotEnough(2001, "余额不足"),

    TaskNotEnough(3001, "已经被抢光啦"),
    TaskDuplicate(3002, "重复领用"),
    IdfaError(3003, "您的信息发生变更，请刷新"),
    TaskExsited(3004, "您已下载过此应用，无法领取任务"),

    AlreadyPassed(3010, "已试玩成功"),

    PhoneError(4009, "请输入正确手机号码"),
    PhoneCodeNotExsit(4010, "请输入验证码"),
    PhoneCodeError(4011, "验证码错误"),
    PhoneCodeRequestTooMuch(4012, "获取验证码太频繁，请间隔30秒再请求"),
    ;

    public int code;
    public String msg;

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
