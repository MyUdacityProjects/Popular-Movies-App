package com.example.android.popular_movies_app.services;

import com.example.android.popular_movies_app.BuildConfig;
import com.example.android.popular_movies_app.utils.APIConstants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * @author harshita.k
 */
public class MovieClient {

    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(APIConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl url = original.url()
                        .newBuilder()
                        .addQueryParameter(APIConstants.APPKEY_QUERY_PARAM, BuildConfig.THE_MOVIEDB_API_KEY)
                        .build();
                Request request = original.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
