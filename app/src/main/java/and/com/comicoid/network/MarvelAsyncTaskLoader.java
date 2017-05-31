package and.com.comicoid.network;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import and.com.comicoid.data.MarvelContract;
import and.com.comicoid.model.Image;

/**
 * Created by dell on 29-05-2017.
 */

public class MarvelAsyncTaskLoader extends AsyncTaskLoader<List<Image>> {

    private static final String baseurl = "gateway.marvel.com/v1/public";
    private static String sort;
    private static String ts;
    private static String apiKey;
    private static String hash;

    public MarvelAsyncTaskLoader(Context context, String sort, String ts, String apiKey, String hash) {
        super(context);
        this.sort = sort;
        this.ts = ts;
        this.apiKey = apiKey;
        this.hash = hash;
    }

    private static URL createUrl(){
        URL url = null;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .encodedAuthority(baseurl)
                .appendPath(sort)
                .appendQueryParameter("ts",ts)
                .appendQueryParameter("apikey", apiKey)
                .appendQueryParameter("hash",hash)
                .appendQueryParameter("limit","100");
        String marvelUrl = builder.build().toString();
        try {
            return new URL(marvelUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    @Override
    public List<Image> loadInBackground() {
        List<Image> marvelList= null;
        URL url = createUrl();
        String jsonResponse = null;
        try {
            jsonResponse = makehttpRequest(url);
        }catch (IOException e){}
        marvelList = extractMarvel(jsonResponse);
        return marvelList;
    }

    private static String makehttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
            }
        }catch (IOException e){
        }finally{
            urlConnection.disconnect();
            inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Image> extractMarvel(String moviesJSON){
        ArrayList<Image> marvelArrayList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);
            JSONArray json_result_array = baseJsonResponse.getJSONObject("data").getJSONArray("results");

            for (int i = 0; i < json_result_array.length(); i++) {
                try {
                    JSONObject object = json_result_array.getJSONObject(i);
                    Image image = new Image();

                    image.setId(object.getInt("id"));

                    if(sort == "characters"){
                        image.setName(object.getString("name"));
                    }else{
                        image.setName(object.getString(MarvelContract.MarvelEntry.COLUMN_TITLE));
                    }

                    JSONObject url = object.getJSONObject("thumbnail");
                    image.setThumbnail(url.getString("path")+"."+url.getString("extension"));
                    image.setDescription(object.getString(MarvelContract.MarvelEntry.COLUMN_DETAIL));
                    marvelArrayList.add(image);

                } catch (JSONException e) {
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return marvelArrayList;
    }
}
