package com.aliu.ev3.ev3.frame;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ALiu on 2018/4/3.
 */
public class RetrofitHelper {



    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    /**
     * 创建Retrofit
     *
     * @param baseUrl      基础BaseUrl
     * @param okHttpClient null则使用默认配置
     * @return Retrofit
     */
    public static Retrofit createRetrofit(String baseUrl, OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(13, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);
            /*// 证书
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
            if (sslSocketFactory != null) {
                builder.sslSocketFactory(sslSocketFactory).hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }*/
            okHttpClient = builder.build();
        }
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(new ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    /**
     * 创建新的证书
     *
     * @param sslKey 证书中的公钥
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSSLSocketFactory(final String sslKey) {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            //客户端并为对ssl证书的有效性进行校验
            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
                if (chain == null) {
                    throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                }

                if (!(chain.length > 0)) {
                    throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                }

                if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
                    throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                }

                // Perform customary SSL/TLS checks
                try {
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                    tmf.init((KeyStore) null);
                    for (TrustManager trustManager : tmf.getTrustManagers()) {
                        ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                    }
                } catch (Exception e) {
                    throw new CertificateException(e);
                }
                // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
                String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
                // Pin it!
                final boolean expected = sslKey.equalsIgnoreCase(encoded);

                if (!expected) {
                    throw new CertificateException("checkServerTrusted: Expected public key: "
                            + sslKey + ", got public key:" + encoded);
                }
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于string于response和requetbody之间的切换
     */
    public static class ToStringConverterFactory extends Converter.Factory {
        /**
         * Media类型
         */
        private final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (String.class.equals(type)) {
                return new Converter<ResponseBody, Object>() {
                    @Override
                    public Object convert(ResponseBody value) throws IOException {
                        return value.string();
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            if (String.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        return RequestBody.create(MEDIA_TYPE, value);
                    }
                };
            }
            return null;
        }
    }
}
