package it.uniba.pioneers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import it.uniba.pioneers.data.server.Server;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.sqlite.DbHelper;
import it.uniba.pioneers.testtool.R;

public class Area {

    private int id;
    private int zona;
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //online state
    private boolean online;

    public Area() {
        setId(0);
        setZona(0);
        setNome("");

        setOnline(true);
    }

    public Area(JSONObject data) throws JSONException, ParseException {

        setId(data.getInt(DbContract.AreaEntry.COLUMN_ID));
        setZona(data.getInt(DbContract.AreaEntry.COLUMN_ZONA));
        setNome(data.getString(DbContract.AreaEntry.COLUMN_NOME));

        setOnline(true);


    }

    public JSONObject toJSON() throws JSONException{

        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.AreaEntry.COLUMN_ID, getId());
        tmp.put(DbContract.AreaEntry.COLUMN_ZONA, getZona());
        tmp.put(DbContract.AreaEntry.COLUMN_NOME, getNome());

        return tmp;
    }

    public boolean isOnline(){

        return online;
    }

    public void setOnline(boolean online){

        this.online = online;

    }

    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException{

        setId(data.getInt(DbContract.AreaEntry.COLUMN_ID));
        setZona(data.getInt(DbContract.AreaEntry.COLUMN_ZONA));
        setNome(data.getString(DbContract.AreaEntry.COLUMN_NOME));

    }

    public static void areeZona(Context context, int idZona, Response.Listener<JSONObject> responseListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/area/areeZona/";

        JSONObject data = new JSONObject();
        try{
            data.put("id", idZona);
        }catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void readDataDb(Context context){

        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/area/read/";

            JSONObject data = new JSONObject();
            try{
                data.put("id", getId());
            }catch(JSONException e){
                e.printStackTrace();
            }

            Area self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {   // response.getJsonArray("data") ---> foreach... ogni elemento=JSONObject
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                }else{
                                    Toast.makeText(context, R.string.lettura_dati_non_riuscita, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();

                }
            });
            queue.add(jsonObjectRequest);

        }else{
            DbHelper dbHelper = new DbHelper(context);

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    DbContract.AreaEntry.COLUMN_ID,
                    DbContract.AreaEntry.COLUMN_ZONA,
                    DbContract.AreaEntry.COLUMN_NOME
            };

            String selection = DbContract.AreaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId())};

            String sortOrder =
                    DbContract.AreaEntry.COLUMN_ID + "DESC";

            Cursor cursor = db.query(
                    DbContract.AreaEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder

            );

            cursor.moveToNext();

            long id = cursor.getLong(

                    cursor.getColumnIndexOrThrow(
                            DbContract.AreaEntry.COLUMN_ID
                    )
            );
            setId((int) id);

            long zona = cursor.getLong(

                    cursor.getColumnIndexOrThrow(
                            DbContract.AreaEntry.COLUMN_ZONA
                    )
            );
            setId((int) zona);

            String nome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.AreaEntry.COLUMN_NOME
                    )

            );
            setNome(nome);

            cursor.close();
        }
    }

    public void createDataDb(Context context, Response.Listener<JSONObject> response){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/area/create/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.AreaEntry.COLUMN_ID, getId());
                data.put(DbContract.AreaEntry.COLUMN_ZONA, getZona());
                data.put(DbContract.AreaEntry.COLUMN_NOME, getNome());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Area self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                }else{
                                    Toast.makeText(context, R.string.creazione_non_riuscita, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();

                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.AreaEntry.COLUMN_ID, getId());
            values.put(DbContract.AreaEntry.COLUMN_ZONA, getZona());
            values.put(DbContract.AreaEntry.COLUMN_NOME, getNome());

            long newRowId = db.insert(DbContract.AreaEntry.TABLE_NAME, null, values);

        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/area/update/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.AreaEntry.COLUMN_ID, getId());
                data.put(DbContract.AreaEntry.COLUMN_ZONA, getZona());
                data.put(DbContract.AreaEntry.COLUMN_NOME, getNome());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Area self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                }else{
                                    Toast.makeText(context, R.string.aggiornamento_non_riuscito, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();

                }
            });
            queue.add(jsonObjectRequest);

        }else{

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DbContract.AreaEntry.COLUMN_ID, getId());
            values.put(DbContract.AreaEntry.COLUMN_ZONA, getZona());
            values.put(DbContract.AreaEntry.COLUMN_NOME, getNome());

            String selection = DbContract.AreaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int count = db.update(
                    DbContract.AreaEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/area/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Area self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    Toast.makeText(context, R.string.area_eliminata_successo, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, R.string.eliminazione_non_riuscita, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();

                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = DbContract.AreaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int deletedRows = db.delete(DbContract.AreaEntry.TABLE_NAME, selection, selectionArgs);
        }
    }

    public static void getAllPossibleChild(Context context,Area area,
                                           Response.Listener<JSONObject> responseListener,
                                           Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/area/child/";

        JSONObject data = new JSONObject();
        try {
            data.put(DbContract.VisitaEntry.COLUMN_ID, area.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
        queue.add(jsonObjectRequest);
    }

    public static void addOpera(Context context,int visita_id, int opera_id,
                               Response.Listener<JSONObject> responseListener,
                               Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/area/add/";

        JSONObject data = new JSONObject();
        try {
            data.put("visita_id", visita_id);
            data.put("opera_id", opera_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
        queue.add(jsonObjectRequest);
    }

    public static void removeOpera(Context context,int visita_id, int opera_id,
                                  Response.Listener<JSONObject> responseListener,
                                  Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/area/remove/";

        JSONObject data = new JSONObject();
        try {
            data.put("visita_id", visita_id);
            data.put("opera_id", opera_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
        queue.add(jsonObjectRequest);
    }

}
