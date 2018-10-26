package no.hiof.informatikk.gruppe6.rusletur.ApiCalls;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiNasjonalturbase{
    public static String TAG = "APICall";

    public static RequestQueue mQueue;

    public static void jsonFetchTripList(Context k) {

        final Context kont = k;
        mQueue = Volley.newRequestQueue(k);

        String url = "http://dev.nasjonalturbase.no/turer?limit%5000&api_key%{cb93d09a566a0ea1e6499a2be18beed87d7e2bb2&fbclid}";

        Log.d(TAG, "jsonFetchTripList: DOOOONE Start of jsonFetchTripList");
        
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("documents");
                    Log.d(TAG, "onResponse: DOOOONE In respons");
                    int antall = 0;
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject turer = jsonArray.getJSONObject(i);

                        Log.d(TAG, "onResponse: DOOOONE inside for");
                        
                        String id = turer.getString("_id");
                        /*String status = turer.getString("status");
                        String endret = turer.getString("endret");
                        String tilbyder = turer.getString("tilbyder");
                        String lisens = turer.getString("lisens");
                        String navn = turer.getString("navn");*/
                        antall++;
                        Log.d(TAG, "onResponse: DOOOONE id: " + id);
                        //ApiNasjonalturbase.jsonFetchIdInfo(kont, id);

                    }
                    Log.d(TAG, "onResponse: DOOOONE Antall: " + antall);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }

    public static void jsonFetchIdInfo(Context k, String id){
        RequestQueue idQueue = Volley.newRequestQueue(k);

        String url = "http://dev.nasjonalturbase.no/turer/" + id;

        Log.d(TAG, "jsonFetchIdInfo: DOOOONE DOOOONE2 Inside fetchIdInfo");
        
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: DOOOONE DOOOONE2 Inside onResponse");
                try {

                    JSONArray array = new JSONArray();

                    array.put(response.getString("_id"));
                    array.put(response.getString("beskrivelse"));

                    String id  = array.getString(0);
                    String beskrivelse = array.getString(1);
                    Log.d(TAG, "onResponse: DOOOONE DOOOONE2 Id: " + id + ". Beksirvelse: " + beskrivelse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse DOOOONE DOOOONE2: Error when retrieving id info");
                error.printStackTrace();
            }
        });


        mQueue.add(request);
    }

}