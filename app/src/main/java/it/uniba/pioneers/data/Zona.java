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
import java.util.ArrayList;

import it.uniba.pioneers.data.server.Server;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.sqlite.DbHelper;

public class Zona {

    private int id;
    private String tipo;
    private String denominazione;
    private String  descrizione;
    private double latitudine;
    private double longitudine;
    private String luogo;
    private ArrayList<Integer> areeZona;
    boolean statusComputation = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void setAreeZona(ArrayList<Integer> areeZona) { this.areeZona = areeZona; }

    public ArrayList<Integer> getAreeZona() { return areeZona; }

    public void setStatusComputation(boolean status) {
        this.statusComputation = status;
    }

    public boolean getStatusComputation() {
        return this.statusComputation;
    }

    //online state
    private boolean online;

    public Zona() {
        setId(0);
        setTipo("");
        setDenominazione("");
        setDescrizione("");
        setLatitudine(0.0);
        setLongitudine(0.0);
        setLuogo("");
        setAreeZona(null);

        setOnline(true);
    }

    //con questa funzione andiamo a prendere degli elementi e gli andiamo a trasformare in oggetti JSON
    public Zona(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt(DbContract.ZonaEntry.COLUMN_ID));
        setTipo(data.getString(DbContract.ZonaEntry.COLUMN_TIPO));
        setDenominazione(data.getString(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE));
        setDescrizione(data.getString(DbContract.ZonaEntry.COLUMN_DESCRIZIONE));
        setLatitudine(data.getDouble(DbContract.ZonaEntry.COLUMN_LATITUDINE));
        setLongitudine(data.getDouble(DbContract.ZonaEntry.COLUMN_LONGITUDINE));
        setLuogo(data.getString(DbContract.ZonaEntry.COLUMN_LUOGO));

        setOnline(true);

    }
    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt(DbContract.ZonaEntry.COLUMN_ID));
        setTipo(data.getString(DbContract.ZonaEntry.COLUMN_TIPO));
        setDenominazione(data.getString(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE));
        setDescrizione(data.getString(DbContract.ZonaEntry.COLUMN_DESCRIZIONE));
        setLatitudine(data.getDouble(DbContract.ZonaEntry.COLUMN_LATITUDINE));
        setLongitudine(data.getDouble(DbContract.ZonaEntry.COLUMN_LONGITUDINE));
        setLuogo(data.getString(DbContract.ZonaEntry.COLUMN_LUOGO));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.ZonaEntry.COLUMN_ID, getId());
        tmp.put(DbContract.ZonaEntry.COLUMN_TIPO,getTipo());
        tmp.put(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE, getDenominazione());
        tmp.put(DbContract.ZonaEntry.COLUMN_DESCRIZIONE, getDescrizione());
        tmp.put(DbContract.ZonaEntry.COLUMN_LATITUDINE, getLatitudine());
        tmp.put(DbContract.ZonaEntry.COLUMN_LONGITUDINE, getLongitudine());
        tmp.put(DbContract.ZonaEntry.COLUMN_LUOGO, getLuogo());

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
            String url = Server.getUrl() + "/zona/read/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.ZonaEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Zona self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Il server non risponde", Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    DbContract.ZonaEntry.COLUMN_ID,
                    DbContract.ZonaEntry.COLUMN_TIPO,
                    DbContract.ZonaEntry.COLUMN_DENOMINAZIONE,
                    DbContract.ZonaEntry.COLUMN_DESCRIZIONE,
                    DbContract.ZonaEntry.COLUMN_LATITUDINE,
                    DbContract.ZonaEntry.COLUMN_LONGITUDINE,
                    DbContract.ZonaEntry.COLUMN_LUOGO
            };

            String selection = DbContract.ZonaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String .valueOf(getId()) };

            String sortOrder =
                    DbContract.ZonaEntry.COLUMN_ID + "DESC";

            Cursor cursor = db.query(
                    DbContract.ZonaEntry.TABLE_NAME,   // The table to query
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
                          DbContract.ZonaEntry.COLUMN_ID
                    )
            );
            setId((int) id);

            String tipo = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DbContract.ZonaEntry.COLUMN_TIPO
                )
            ) ;
            setTipo(tipo);

            String denominazione = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.ZonaEntry.COLUMN_DENOMINAZIONE
                    )
            ) ;
            setDenominazione(denominazione);

            String descrizione = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.ZonaEntry.COLUMN_DESCRIZIONE
                    )
            ) ;
            setDescrizione(descrizione);

            double latitudine  = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                            DbContract.ZonaEntry.COLUMN_LATITUDINE
                    )
            );
            setLatitudine(latitudine);

            double longitudine  = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                            DbContract.ZonaEntry.COLUMN_LONGITUDINE
                    )
            );
            setLongitudine(longitudine);

            String luogo = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.ZonaEntry.COLUMN_LUOGO
                    )
            ) ;
            setLuogo(luogo);

            cursor.close();

            /*
            String[] projection2 = {DbContract.AreaEntry.COLUMN_ID};
            String selection2 = DbContract.AreaEntry.COLUMN_ZONA + " = ?";
            String[] selectionArgs2 = { String.valueOf(getId()) };
            String sortOrder2 = DbContract.AreaEntry.COLUMN_ID + "DESC";

             Cursor cursor2 = db.query(
                    DbContract.AreaEntry.TABLE_NAME,   // The table to query
                    projection2,             // The array of columns to return (pass null to get all)
                    selection2,              // The columns for the WHERE clause
                    selectionArgs2,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder2               // The sort order
            );

            cursor2.moveToFirst();
            for(int i=0;i<cursor2.getColumnCount();i++){
                this.areeZona.add(cursor2.getInt(cursor2.getColumnIndexOrThrow(DbContract.AreaEntry.COLUMN_ID)));
                cursor2.moveToNext();
            }
            cursor2.close();
            */
        }
    }

    public void createDataDb(Context context, Response.Listener<JSONObject> responseListener){

        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/zona/create/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.ZonaEntry.COLUMN_TIPO,getTipo());
                data.put(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE, getDenominazione());
                data.put(DbContract.ZonaEntry.COLUMN_DESCRIZIONE, getDescrizione());
                data.put(DbContract.ZonaEntry.COLUMN_LATITUDINE, getLatitudine());
                data.put(DbContract.ZonaEntry.COLUMN_LONGITUDINE, getLongitudine());
                data.put(DbContract.ZonaEntry.COLUMN_LUOGO, getLuogo());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Zona self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    responseListener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Il server non risponde", Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.ZonaEntry.COLUMN_TIPO,getTipo());
            values.put(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE, getDenominazione());
            values.put(DbContract.ZonaEntry.COLUMN_DESCRIZIONE, getDescrizione());
            values.put(DbContract.ZonaEntry.COLUMN_LATITUDINE, getLatitudine());
            values.put(DbContract.ZonaEntry.COLUMN_LONGITUDINE, getLongitudine());
            values.put(DbContract.ZonaEntry.COLUMN_LUOGO, getLuogo());

            long newRowId = db.insert(DbContract.ZonaEntry.TABLE_NAME, null, values);

        }

    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/zona/update/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.ZonaEntry.COLUMN_ID, getId());
                data.put(DbContract.ZonaEntry.COLUMN_TIPO,getTipo());
                data.put(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE, getDenominazione());
                data.put(DbContract.ZonaEntry.COLUMN_DESCRIZIONE, getDescrizione());
                data.put(DbContract.ZonaEntry.COLUMN_LATITUDINE, getLatitudine());
                data.put(DbContract.ZonaEntry.COLUMN_LONGITUDINE, getLongitudine());
                data.put(DbContract.ZonaEntry.COLUMN_LUOGO, getLuogo());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Zona self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Il server non risponde", Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DbContract.ZonaEntry.COLUMN_ID, getId());
            values.put(DbContract.ZonaEntry.COLUMN_TIPO,getTipo());
            values.put(DbContract.ZonaEntry.COLUMN_DENOMINAZIONE, getDenominazione());
            values.put(DbContract.ZonaEntry.COLUMN_DESCRIZIONE, getDescrizione());
            values.put(DbContract.ZonaEntry.COLUMN_LATITUDINE, getLatitudine());
            values.put(DbContract.ZonaEntry.COLUMN_LONGITUDINE, getLongitudine());
            values.put(DbContract.ZonaEntry.COLUMN_LUOGO, getLuogo());

            String selection = DbContract.ZonaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId())};

            int count = db.update(
                    DbContract.ZonaEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/zona/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Zona self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Il server non risponde", Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = DbContract.ZonaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int deletedRows = db.delete(DbContract.ZonaEntry.TABLE_NAME, selection, selectionArgs);
        }
    }
}
