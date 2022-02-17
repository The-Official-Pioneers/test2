package it.uniba.pioneers.data.associazioni;

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


public class VisitaOpera {

    private int id;
    private int visita;
    private int opera;
    private int ordine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisita() {
        return visita;
    }

    public void setVisita(int visita) {
        this.visita = visita;
    }

    public int getOpera() {
        return opera;
    }

    public void setOpera(int opera) {
        this.opera = opera;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    //online state
    private boolean online;

    public VisitaOpera() {
        setId(0);
        setVisita(0);
        setOpera(0);
        setOrdine(0);

        setOnline(true);
    }

    //con questa funzione andiamo a prendere degli elementi e gli andiamo a trasformare in oggetti JSON
    public VisitaOpera(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt(DbContract.VisitaOperaEntry.COLUMN_ID));
        setVisita(data.getInt(DbContract.VisitaOperaEntry.COLUMN_VISITA));
        setOpera(data.getInt(DbContract.VisitaOperaEntry.COLUMN_OPERA));
        setOrdine(data.getInt(DbContract.VisitaOperaEntry.COLUMN_ORDINE));

        setOnline(true);
    }
    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt(DbContract.VisitaOperaEntry.COLUMN_ID));
        setVisita(data.getInt(DbContract.VisitaOperaEntry.COLUMN_VISITA));
        setOpera(data.getInt(DbContract.VisitaOperaEntry.COLUMN_OPERA));
        setOrdine(data.getInt(DbContract.VisitaOperaEntry.COLUMN_ORDINE));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.VisitaOperaEntry.COLUMN_ID, getId());
        tmp.put(DbContract.VisitaOperaEntry.COLUMN_VISITA, getVisita());
        tmp.put(DbContract.VisitaOperaEntry.COLUMN_OPERA, getOpera());
        tmp.put(DbContract.VisitaOperaEntry.COLUMN_ORDINE, getOrdine());

        return tmp;
    }

    //restituisce lo stato dell'utente
    public boolean isOnline(){
        return online;
    }

    //imposta true o false in base allo stato dell'utente
    public void setOnline(boolean online){
        this.online = online;
    }

    //metodi del database
    public void readDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita_opera/read/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.VisitaOperaEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            it.uniba.pioneers.data.associazioni.VisitaOpera self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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
                    DbContract.VisitaOperaEntry.COLUMN_ID,
                    DbContract.VisitaOperaEntry.COLUMN_VISITA,
                    DbContract.VisitaOperaEntry.COLUMN_OPERA,
                    DbContract.VisitaOperaEntry.COLUMN_ORDINE
            };

            String selection = DbContract.VisitaOperaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            String sortOrder =
                    DbContract.VisitaOperaEntry.COLUMN_ID + "DESC";

            Cursor cursor = db.query(
                    DbContract.VisitaOperaEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            cursor.moveToNext();

            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitaOperaEntry.COLUMN_ID
                    )
            );
            setId((int) id);

            int visita = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitaOperaEntry.COLUMN_VISITA
                    )
            ) ;
            setVisita(visita);

            int opera = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitaOperaEntry.COLUMN_OPERA
                    )
            ) ;
            setOpera(opera);

            int ordine = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitaOperaEntry.COLUMN_ORDINE
                    )
            ) ;
            setOrdine(ordine);

            cursor.close();
        }
    }

    public void createDataDb(Context context){

        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita_opera/create/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.VisitaOperaEntry.COLUMN_VISITA, getVisita());
                data.put(DbContract.VisitaOperaEntry.COLUMN_OPERA, getOpera());
                data.put(DbContract.VisitaOperaEntry.COLUMN_ORDINE, getOrdine());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            it.uniba.pioneers.data.associazioni.VisitaOpera self = this;

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
            values.put(DbContract.VisitaOperaEntry.COLUMN_VISITA, getVisita());
            values.put(DbContract.VisitaOperaEntry.COLUMN_OPERA, getOpera());
            values.put(DbContract.VisitaOperaEntry.COLUMN_ORDINE, getOrdine());

            long newRowId = db.insert(DbContract.VisitaOperaEntry.TABLE_NAME, null, values);
        }

    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita_opera/update/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.VisitaOperaEntry.COLUMN_ID, getId());
                data.put(DbContract.VisitaOperaEntry.COLUMN_VISITA, getVisita());
                data.put(DbContract.VisitaOperaEntry.COLUMN_OPERA, getOpera());
                data.put(DbContract.VisitaOperaEntry.COLUMN_ORDINE, getOrdine());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            it.uniba.pioneers.data.associazioni.VisitaOpera self = this;

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

            values.put(DbContract.VisitaOperaEntry.COLUMN_ID, getId());
            values.put(DbContract.VisitaOperaEntry.COLUMN_VISITA, getVisita());
            values.put(DbContract.VisitaOperaEntry.COLUMN_OPERA, getOpera());
            values.put(DbContract.VisitaOperaEntry.COLUMN_ORDINE, getOrdine());

            String selection = DbContract.VisitaOperaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId())};

            int count = db.update(
                    DbContract.VisitaOperaEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita_opera/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            it.uniba.pioneers.data.associazioni.VisitaOpera self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
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

            String selection = DbContract.VisitaOperaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int deletedRows = db.delete(DbContract.VisitaOperaEntry.TABLE_NAME, selection, selectionArgs);
        }
    }
}
