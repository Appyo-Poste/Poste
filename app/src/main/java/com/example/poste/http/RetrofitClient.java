package com.example.poste.http;

import com.example.poste.R;
import com.example.poste.models.PosteApp;

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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8000/api/";
    private static Retrofit retrofit;
    private static OkHttpClient.Builder client = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            client = new OkHttpClient.Builder()
                    .addInterceptor(logging);
            // adds the SSL to the httpClient so it can use https with TLS encoding
            // to use need to change URl to an https one
            //initSSL();

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
            sslContext = createCertificate(PosteApp.getResourcesStatic().openRawResource(R.raw.cert));
        } catch (CertificateException | IOException | KeyStoreException |KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(sslContext != null){
            client.sslSocketFactory(sslContext.getSocketFactory(), systemDefaultTrustManager());
        }
    }

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
