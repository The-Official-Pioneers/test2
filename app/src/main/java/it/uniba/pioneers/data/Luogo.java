package it.uniba.pioneers.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.data.server.Server;

public class Luogo {
    public static void all(Context context, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/luogo/all/";

        JSONObject data = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);

        queue.add(jsonObjectRequest);
    }

    public static void add(Context context, String luogo, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/luogo/add/";

        JSONObject data = new JSONObject();
        try {
            data.put("luogo", luogo);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Context context, String luogo, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/luogo/delete/";

        JSONObject data = new JSONObject();
        try {
            data.put("luogo", luogo);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void update(Context context, String oldLuogo, String newLuogo,
                              Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/luogo/update/";

        JSONObject data = new JSONObject();
        try {
            data.put("oldLuogo", oldLuogo);
            data.put("newLuogo", newLuogo);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

