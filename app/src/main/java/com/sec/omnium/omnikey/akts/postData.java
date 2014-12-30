package com.sec.omnium.omnikey.akts;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class postData extends AsyncTask<JSONObject, JSONObject, JSONObject> {

    String url = "http://posttestserver.com/post.php";
//    String url = "http://162.243.86.70:8080/delete";

    @Override
    protected JSONObject doInBackground(JSONObject... data) {

        JSONObject json = data[0];
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

        JSONObject jsonResponse = null;
        HttpPost post = new HttpPost(url);
        try {

            StringEntity se = new StringEntity("" + json.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());


            Log.i("Tste aki: ", resFromServer);
            jsonResponse=new JSONObject(resFromServer);
            Log.i("Response from server", jsonResponse.getString("msg"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

}