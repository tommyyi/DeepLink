package com.laser.tools;

import java.util.concurrent.TimeUnit;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * retrofit帮助类
 * 【1】使用自定义的okhttp客户端，此okhttp客户端可以自定义cache之类的特性
 * 【2】添加stetho的网络拦截器
 * Created by 易剑锋 on 2017/3/9.
 */

public class RetrofitHelper
{
    private static Retrofit retrofit;
    
    /*返回retrofit 网络接口，retrofit会依据interface动态生成实现了该interface的类的对象*/
    public static <T> T getInstance(Context context, Class<T> service)
    {
        if (retrofit == null)
        {
            synchronized (RetrofitHelper.class)
            {
                if (retrofit == null)
                {
                    //设置http客户端
                    retrofit = new Retrofit.Builder().client(new OkHttpFactory(context).getOkHttpClient())//设置http客户端
                                                     .baseUrl("http://www.fake.com")
                        .build();
                }
            }
        }
        return retrofit.create(service);//依据interface动态生成实现了该interface的类的对象
    }
    
    private static class OkHttpFactory
    {
        private OkHttpClient client;
        
        private static final int TIMEOUT_READ = 25;
        
        private static final int TIMEOUT_CONNECTION = 25;
        
        Cache mCache;
        
        private OkHttpFactory(Context context)
        {
            mCache = new Cache(context.getCacheDir(), 10 * 1024 * 1024);//缓存目录
            OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(mCache)
                .retryOnConnectionFailure(true) //失败重连
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)//读超时设置
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);//连接超时设置
            /*反射获取stetho网络拦截器*/
            Interceptor interceptor = StethoHelper.getStethoOkHttp3Interceptor();
            /*添加stetho的网络拦截器*/
            if (interceptor != null)
                builder.addNetworkInterceptor(interceptor);
            client = builder.build();
        }
        
        OkHttpClient getOkHttpClient()
        {
            return client;
        }
    }
    
    /**
     * retrofit接口定义example
     */
    public static interface INetworkPortExample
    {
        /**
         * 带query和field的post
         * @param data 需要提交的信息
         * @return
         */
        @POST
        @FormUrlEncoded
        public Call<Object> getUpdateList(@Url String url, @Query(value = "action", encoded = true) String action,
            @Field("data") String data);
        
        /**
         * 带query的get
         * @return 位置信息
         */
        @GET
        public Call<Object> getTopList(@Url String url, @Query(value = "action", encoded = true) String action);
        
        /**
         * 带url的get
         * @return 应用商店配置
         */
        @GET
        public Call<ResponseBody> store(@Url String url);
    }
}
