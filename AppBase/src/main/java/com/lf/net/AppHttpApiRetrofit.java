package com.lf.net;


import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lf.net.cookie.AppHttpCookieManger;
import com.lf.net.file.AppHttpProgressResponseBody;
import com.lf.net.gson.AppHttpDoubleDefaultAdapter;
import com.lf.net.gson.AppHttpIntegerDefaultAdapter;
import com.lf.net.gson.AppHttpLongDefaultAdapter;
import com.lf.net.gson.AppHttpStringNullAdapter;
import com.lf.net.listener.AppHttpBaseNetListener;
import com.lf.net.utils.AppHttpApiDns;
import com.lf.net.utils.AppNetUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * File descripition:   创建Retrofit
 *
 * @date 2020/8/20
 */

public class AppHttpApiRetrofit {
    private String TAG = "HTTP %s";

    private static final int DEFAULT_TIMEOUT = 30;

    private static AppHttpApiRetrofit mHttpApiRetrofit = null;

    private Retrofit retrofit;

    private OkHttpClient okHttpClient;

    private Gson gson;

    public static String mBaseUrl = AppHttpUtils.getInstance().getBaseUrl();

    private static AppHttpBaseNetListener mZYBHttpBaseNetListener = null;

    public AppHttpApiRetrofit() {

        initOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))//添加json转换框架(正常转换框架)
//                .addConverterFactory(MyGsonConverterFactory.create(buildGson()))//添加json自定义（根据需求，此种方法是拦截gson解析所做操作）
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(okHttpClient)
                .build();
    }

    /**
     * 默认使用方式
     *
     * @return
     */
    public static synchronized AppHttpApiRetrofit getInstance() {
        if (mHttpApiRetrofit == null) {
            mHttpApiRetrofit = new AppHttpApiRetrofit();
        }
        return mHttpApiRetrofit;
    }

    /**
     * 动态改变baseUrl使用方式
     *
     * @param baseUrl
     * @return
     */
    public static AppHttpApiRetrofit getBaseUrlInstance(String baseUrl) {
        mZYBHttpBaseNetListener = null;
        if (!TextUtils.isEmpty(baseUrl)) {
            mBaseUrl = baseUrl;
        } else {
            mBaseUrl = AppHttpUtils.getInstance().getBaseUrl();
        }
        return mHttpApiRetrofit;
    }


    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setZYBHttpBaseNetListener(AppHttpBaseNetListener listener) {

        mZYBHttpBaseNetListener = listener;
    }

    /**
     * 文件处理
     *
     * @param httpClientBuilder
     */
    public void initFileClient(OkHttpClient.Builder httpClientBuilder) {
        /**
         * 处理文件下载进度展示所需
         */
        httpClientBuilder.addNetworkInterceptor(new ProgressInterceptor());
    }


    private void initOkHttpClient() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);//错误重联

        /**
         * 处理一些识别识别不了 ipv6手机，如小米  实现方案  将ipv6与ipv4置换位置，首先用ipv4解析
         */
        httpClientBuilder.dns(new AppHttpApiDns());

        /**
         * 添加cookie管理
         * 方法2：手动封装cookie管理
         */
        httpClientBuilder.cookieJar(new AppHttpCookieManger(AppHttpUtils.getInstance().getmContext()));

        /**
         * 处理文件下载进度展示所需
         */
        httpClientBuilder.addNetworkInterceptor(new ProgressInterceptor());

        // 添加公参
        httpClientBuilder.addInterceptor(new HttpParamsInterceptor());

        /**
         * 添加日志拦截
         */
        httpClientBuilder.addInterceptor(new JournalInterceptor());

        /**
         * 添加请求头
         */
        httpClientBuilder.addInterceptor(new HeadUrlInterceptor());

        okHttpClient = httpClientBuilder.build();
    }



    /**
     * 增加后台返回""和"null"的处理,如果后台返回格式正常
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     * 4.String=>""
     *
     * @return
     */
    public Gson buildGson() {

        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new AppHttpIntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new AppHttpIntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new AppHttpDoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new AppHttpDoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new AppHttpLongDefaultAdapter())
                    .registerTypeAdapter(long.class, new AppHttpLongDefaultAdapter())
                    .registerTypeAdapter(String.class, new AppHttpStringNullAdapter())
                    .create();
        }
        return gson;
    }

    /**
     * 请求访问quest    打印日志
     * response拦截器
     */
    public class JournalInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            try {
                Response response = chain.proceed(request);
                MediaType mediaType = response.body().contentType();
                String content = response.body().string();

                if (request.url().toString().contains("/label/group/list") || request.url().toString().contains("/addressbook/org/list")) {

                } else {
                    long startTime = System.currentTimeMillis();
                    if (response == null) {
                        return chain.proceed(request);
                    }

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                }


                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, content))
                        .build();

            } catch (Exception e) {

                e.printStackTrace();
                return chain.proceed(request);
            }
        }
    }

    /**
     * 请求参数日志打印
     *
     * @param body
     */
    private void printParams(RequestBody body) {
        if (body != null) {
            Buffer buffer = new Buffer();
            try {
                body.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                String params = buffer.readString(charset);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加  请求头
     */
    public class HeadUrlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            String phone = Build.MANUFACTURER + "-" + Build.MODEL;
            String version = Build.VERSION.RELEASE;
            String user_agent = phone + version + ":" + getDeviceUUID(AppHttpUtils.getInstance().getmContext());
            Request.Builder builder = chain.request()
                    .newBuilder();

                    if (TextUtils.isEmpty(AppHttpUtils.getInstance().getToken())) {
                        builder.addHeader("User-Agent", user_agent);
                    } else {
                        builder.addHeader("Authorization", AppHttpUtils.getInstance().getToken())
                                .addHeader("User-Agent", user_agent);
                    }
            Request request = builder.build();
            return chain.proceed(request);
        }
    }


    /**
     * 文件下载进度拦截
     */
    public class ProgressInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            if (mZYBHttpBaseNetListener != null) {
                Response response = chain.proceed(request);

                return response.newBuilder().body(new AppHttpProgressResponseBody(response.body(), new AppHttpProgressResponseBody.ProgressListener() {

                    @Override
                    public void onProgress(long totalSize, long downSize) {

                        int progress = (int) (downSize * 100 / totalSize);
                        if (mZYBHttpBaseNetListener != null) {
                            mZYBHttpBaseNetListener.onProgress(progress);
                        }
                    }
                })).build();

            } else {

                request = chain.request().newBuilder().addHeader("Connection", "close").build();

                return chain.proceed(request);
            }
        }
    }

    /**
     * 获取HTTP 添加公共参数的拦截器
     * 暂时支持get、head请求&Post put patch的表单数据请求
     *
     * @return
     */
    public class HttpParamsInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            long random = (long) (Math.random() * 100000);

            Request request = chain.request();

            if (request.method().equalsIgnoreCase("GET") || request.method().equalsIgnoreCase("HEAD")) {

                HttpUrl httpUrl = request.url().newBuilder()
                        .addQueryParameter("clientId", "1")
                        .addQueryParameter("appVersion", AppHttpUtils.getInstance().getAppVersion())
                        .addQueryParameter("osVersion", AppHttpUtils.getInstance().getOsVersion())
                        .addQueryParameter("netRequestID", String.valueOf(random))
                        .build();
                request = request.newBuilder().url(httpUrl).build();

            } else {
                RequestBody originalBody = request.body();
                if (originalBody instanceof FormBody) {
                    FormBody.Builder builder = new FormBody.Builder();
                    FormBody formBody = (FormBody) originalBody;
                    for (int i = 0; i < formBody.size(); i++) {
                        builder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }

                    FormBody newFormBody = builder
                            .add("clientId", "1")
                            .add("appVersion", AppHttpUtils.getInstance().getAppVersion())
                            .add("osVersion", AppHttpUtils.getInstance().getOsVersion())
                            .add("netRequestID", String.valueOf(random))
                            .build();

                    if (request.method().equalsIgnoreCase("POST")) {

                        request = request.newBuilder().post(newFormBody).build();
                    } else if (request.method().equalsIgnoreCase("PATCH")) {

                        request = request.newBuilder().patch(newFormBody).build();
                    } else if (request.method().equalsIgnoreCase("PUT")) {

                        request = request.newBuilder().put(newFormBody).build();
                    }

                } else if (originalBody instanceof MultipartBody) {

                }

            }
            return chain.proceed(request);
        }
    }

    /**
     * 获得HTTP 缓存的拦截器
     *
     * @return
     */
    public class HttpCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            // 无网络时，始终使用本地Cache
            if (!AppNetUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (AppNetUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {

                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                return response.newBuilder()
                        //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    /**
     * 特殊返回内容  处理方案
     */
    public class MockInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Gson gson = new Gson();
            Response response = null;
            Response.Builder responseBuilder = new Response.Builder()
                    .code(200)
                    .message("")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .addHeader("content-type", "application/json");
            Request request = chain.request();
            if (request.url().toString().contains(AppHttpUtils.getInstance().getBaseUrl())) { //拦截指定地址
                String responseString = "{\n" +
                        "\t\"success\": true,\n" +
                        "\t\"data\": [{\n" +
                        "\t\t\"id\": 6,\n" +
                        "\t\t\"type\": 2,\n" +
                        "\t\t\"station_id\": 1,\n" +
                        "\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\"factors\": [{\n" +
                        "\t\t\t\"id\": 11,\n" +
                        "\t\t\t\"history_id\": 6,\n" +
                        "\t\t\t\"station_id\": 1,\n" +
                        "\t\t\t\"factor_id\": 6,\n" +
                        "\t\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\t\"value_check\": 2.225,\n" +
                        "\t\t\t\"value_span\": 5.0,\n" +
                        "\t\t\t\"value_standard\": 4.0,\n" +
                        "\t\t\t\"error_difference\": -1.775,\n" +
                        "\t\t\t\"error_percent\": -44.38,\n" +
                        "\t\t\t\"accept\": false\n" +
                        "\t\t}, {\n" +
                        "\t\t\t\"id\": 12,\n" +
                        "\t\t\t\"history_id\": 6,\n" +
                        "\t\t\t\"station_id\": 1,\n" +
                        "\t\t\t\"factor_id\": 7,\n" +
                        "\t\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\t\"value_check\": 1.595,\n" +
                        "\t\t\t\"value_span\": 0.5,\n" +
                        "\t\t\t\"value_standard\": 1.6,\n" +
                        "\t\t\t\"error_difference\": -0.005,\n" +
                        "\t\t\t\"error_percent\": -0.31,\n" +
                        "\t\t\t\"accept\": true\n" +
                        "\t\t}, {\n" +
                        "\t\t\t\"id\": 13,\n" +
                        "\t\t\t\"history_id\": 6,\n" +
                        "\t\t\t\"station_id\": 1,\n" +
                        "\t\t\t\"factor_id\": 8,\n" +
                        "\t\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\t\"value_check\": 8.104,\n" +
                        "\t\t\t\"value_span\": 20.0,\n" +
                        "\t\t\t\"value_standard\": 8.0,\n" +
                        "\t\t\t\"error_difference\": 0.104,\n" +
                        "\t\t\t\"error_percent\": 1.3,\n" +
                        "\t\t\t\"accept\": true\n" +
                        "\t\t},null]\n" +
                        "\t}],\n" +
                        "\t\"additional_data\": {\n" +
                        "\t\t\"totalPage\": 0,\n" +
                        "\t\t\"startPage\": 1,\n" +
                        "\t\t\"limit\": 30,\n" +
                        "\t\t\"total\": 0,\n" +
                        "\t\t\"more_items_in_collection\": false\n" +
                        "\t},\n" +
                        "\t\"related_objects\": [{\n" +
                        "\t\t\"id\": 6,\n" +
                        "\t\t\"name\": \"氨氮\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"nh3n\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}, {\n" +
                        "\t\t\"id\": 7,\n" +
                        "\t\t\"name\": \"总磷\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"tp\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}, {\n" +
                        "\t\t\"id\": 8,\n" +
                        "\t\t\"name\": \"总氮\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"tn\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}, {\n" +
                        "\t\t\"id\": 9,\n" +
                        "\t\t\"name\": \"CODMn\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"codmn\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}],\n" +
                        "\t\"request_time\": \"2019-06-05T16:40:14.915+08:00\"\n" +
                        "}";
                responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()));//将数据设置到body中
                response = responseBuilder.build(); //builder模式构建response
            } else {
                response = chain.proceed(request);
            }
            return response;
        }
    }


    /**
     * 获取设备uuid
     *
     * @param context
     * @return
     */
    private String getDeviceUUID(Context context) {
        String deviceId = "";
        String androidId = "";
        String SerialNum = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SerialNum = Build.SERIAL;
        String packageName = context.getPackageName();
        String uuid = deviceId + androidId + SerialNum + packageName;

        try {
            return SHA1(SHA1(uuid));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    private String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("UTF-8"));
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    /**
     * 转换成哈希
     *
     * @param data
     * @return
     */
    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
