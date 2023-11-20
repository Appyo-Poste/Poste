package com.example.poste.http;

import com.example.poste.R;
import com.example.poste.models.PosteApp;
import com.example.poste.utils.NullHostNameVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://10.0.2.2/api/";
    private static Retrofit retrofit;
    private static OkHttpClient.Builder client = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .hostnameVerifier(new NullHostNameVerifier());
            // adds the SSL to the httpClient so it can use https with TLS encoding
            // to use need to change URl to an https one
            initSSL();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initSSL() {
        SSLContext sslContext = null;
        try {
            sslContext = createCertificate(PosteApp.getResourcesStatic().openRawResource(R.raw.poste));
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        if(sslContext != null){
            client.sslSocketFactory(sslContext.getSocketFactory(), systemDefaultTrustManager());
        }
    }

    /**
     * creates the need info for https communication.
     * @param trustedCertificateIS the cert file of the api.
     * @return SSLContext for communicating with the api.
     * @throws CertificateException
     * @throws IOException
     * @throws KeyStoreException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    private static SSLContext createCertificate(InputStream trustedCertificateIS) throws CertificateException, IOException, KeyStoreException,
            KeyManagementException, NoSuchAlgorithmException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate;
        try {
            certificate = certificateFactory.generateCertificate(trustedCertificateIS);
        } finally {
            trustedCertificateIS.close();
        }

        // create keystore
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", certificate);

        // create a trustmanager
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // create a SSLSocketFactory
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    /**
     * creates a trust manager or the https connection.
     * @return a X509 trust manager.
     */
    private static X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError();
        }
    }
}
