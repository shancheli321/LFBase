package com.lf.net;

public class AppHttpConstants {

    // 成功
    public static final int ZYBHttpCode_Success = 0;

    // 无网络
    public static final int ZYBHttpCode_Nonetwork = 10000;

    // 秘钥无效
    public static final int ZYBHttpCode_SecretExprise = 10001;

    // 强制退出
    public static final int ZYBHttpCode_Logout = 10002;

    // 密码已修改，请重新登录
    public static final int ZYBHttpCode_Psd_Change_Logout = 10003;

    // 被管理员强制退出
    public static final int ZYBHttpCode_forced_Logout = 10004;

    // token失效
    public static final int ZYBHttpCode_TokenExprise = 401;



/***************************************  自定义  ***************************************/

    /**
     * 网络连接失败  无网
     */
    public static final int NETWORK_ERROR = 100000;

    /**
     * 未知错误
     */
    public static final int ZYBHttpCode_Unknow = 1001;

    /**
     * 连接超时
     */
    public static final int ZYBHttpCode_TimeOut = 1005;

    /**
     * 连接错误
     */
    public static final int ZYBHttpCode_ConnectError = 1006;

    /**
     * 网络问题
     */
    public static final int ZYBHttpCode_HttpError = 1007;

    /**
     * 解析数据失败
     */
    public static final int ZYBHttpCode_ParseError = 1008;







}
