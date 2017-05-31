package and.com.comicoid.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dell on 29-05-2017.
 */

public class ApiClient {

    private static final String PRIVATE_KEY = "179e29e3f6447cedbb508940761ff0abc35d1884";
    private static final String PUBLIC_KEY = "b136328c81adf02eeb8cbfce3e0ef2a7";
    public static String ts = null;
    public static String hash = null;

    public void creatUrl(){
        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();
        String toHash = ts +  PRIVATE_KEY + PUBLIC_KEY;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashe = md.digest(toHash.getBytes("UTF-8"));
            StringBuffer hex = new StringBuffer(2*hashe.length);
            for (byte b : hashe) {
                hex.append(String.format("%02x", b&0xff));
            }
            hash = hex.toString();
        }
        catch(NoSuchAlgorithmException e) {
        }
        catch(UnsupportedEncodingException e) {
        }
    }

    public String getTs() {
        return ApiClient.ts;
    }

    public String getApiKey() {
        return ApiClient.PUBLIC_KEY;
    }

    public String getHash() {
        return ApiClient.hash;
    }
}
