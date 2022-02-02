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

public class Visita {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatore_visitatore() {
        return creatore_visitatore;
    }

    public void setCreatore_visitatore(int creatore_visitatore) {
        this.creatore_visitatore = creatore_visitatore;
        this.setTipo_creatore(1);
    }

    public int getGuida() {
        return guida;
    }

    public void setGuida(int guida) {
        this.guida = guida;
    }

    public int getCreatore_curatore() {
        return creatore_curatore;
    }

    public void setCreatore_curatore(int creatore_curatore) {
        this.creatore_curatore = creatore_curatore;
        this.setTipo_creatore(0);
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public int getTipo_creatore() {
        return tipo_creatore;
    }

    public void setTipo_creatore(int tipo_creatore) {
        this.tipo_creatore = tipo_creatore;
    }

    //ATTRIBUTES
    private int id;
    private int creatore_visitatore;
    private int guida;
    private int creatore_curatore;
    private long data;
    private int tipo_creatore;

    //ONLINE STATE
    private boolean online;

    public Visita(){
        this.setId(0);
        this.setCreatore_visitatore(0);
        this.setGuida(0);
        this.setCreatore_curatore(0);
        this.setData(0);
        this.setTipo_creatore(-1);

        this.setOnline(true);
    }

    //Metodi per la gestione dello stato di Online
    public boolean isOnline() {
        return online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }

    //Crea un oggetto JSON
    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.VisitaEntry.COLUMN_ID, getId());
        tmp.put(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE, getCreatore_visitatore());
        tmp.put(DbContract.VisitaEntry.COLUMN_GUIDA, getGuida());
        tmp.put(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE, getCreatore_curatore());
        tmp.put(DbContract.VisitaEntry.COLUMN_DATA, getData());
        tmp.put(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE, getTipo_creatore());

        return tmp;
    }

    //Costruttore con oggetto JSON
    public Visita(JSONObject data) throws JSONException, ParseException{
        this.setId(data.getInt(DbContract.VisitaEntry.COLUMN_ID));
        this.setCreatore_visitatore(data.getInt(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE));
        this.setGuida(data.getInt(DbContract.VisitaEntry.COLUMN_GUIDA));
        this.setCreatore_curatore(data.getInt(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE));
        this.setData(data.getLong(DbContract.VisitaEntry.COLUMN_DATA));
        this.setTipo_creatore(data.getInt(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE));

        this.setOnline(true);
    }

    //Imposta i dati della visita tramite un oggetto di tipo JSON
    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException{

        setId(data.getInt(DbContract.VisitaEntry.COLUMN_ID));

        if( data.get(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE).equals(null) ){
            this.setCreatore_visitatore(0);
            this.setTipo_creatore(0);
        } else {
            this.setCreatore_visitatore(data.getInt(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE));
            this.setTipo_creatore(1);
        }

        if( data.get(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE).equals(null) ){
            this.setCreatore_curatore(0);
            this.setTipo_creatore(1);
        } else {
            this.setCreatore_curatore(data.getInt(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE));
            this.setTipo_creatore(0);
        }

        if( data.get(DbContract.VisitaEntry.COLUMN_GUIDA).equals(null) ){
            this.setGuida(0);
        } else {
            this.setGuida(data.getInt(DbContract.VisitaEntry.COLUMN_GUIDA));
        }

        setData(data.getInt(DbContract.VisitaEntry.COLUMN_DATA));
        setTipo_creatore(data.getInt(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE));

    }

    //Crea record dal DB in base ai dati forniti
    public void createDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita/create/";

            JSONObject data = new JSONObject();
            try {

                if( getCreatore_visitatore() != 0  ){
                    data.put(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE, getCreatore_visitatore());
                }
                if( getCreatore_curatore() != 0  ){
                    data.put(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE, getCreatore_curatore());
                }
                if( getGuida() != 0  ){
                    data.put(DbContract.VisitaEntry.COLUMN_GUIDA, getGuida());
                }
                data.put(DbContract.VisitaEntry.COLUMN_DATA, getData());
                data.put(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE, getTipo_creatore());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visita self = this;

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
            //SQLITE3 Necessario per gestire le richieste offline

            //Creazione oggetto di tipo dbHelper e lo istanzio come un WritableDatabase
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //Imposto i valori dell'istanza da creare all'interno del db
            ContentValues values = new ContentValues();
            values.put(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE, getCreatore_visitatore());
            values.put(DbContract.VisitaEntry.COLUMN_GUIDA, getGuida());
            values.put(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE, getCreatore_curatore());
            values.put(DbContract.VisitaEntry.COLUMN_DATA, getData());
            values.put(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE, getTipo_creatore());

            //Inserisco i valori dell'istanza da creare all'interno del db
            long newRowId = db.insert(DbContract.ZonaEntry.TABLE_NAME, null, values);
        }
    }

    //Legge record dal DB in base all'id fornito
    public void readDataDb(Context context){
        if( isOnline() ){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita/read/";

            JSONObject data = new JSONObject();
            try {

                data.put(DbContract.VisitaEntry.COLUMN_ID, 18);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visita self = this;

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
            //SQLITE3 Necessario per gestire le richieste offline

            //Creazione oggetto di tipo dbHelper e lo istanzio come un ReadableDatabase
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            //Definiamo quali colonne dovranno essere presenti all'interno della proiezione
            String[] projection = {
                DbContract.VisitaEntry.COLUMN_ID,
                DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE,
                DbContract.VisitaEntry.COLUMN_GUIDA,
                DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE,
                DbContract.VisitaEntry.COLUMN_DATA,
                DbContract.VisitaEntry.COLUMN_TIPO_CREATORE
            };

            //Troviamo la riga all'interno del db e ricaviamo i valori
            String selection = DbContract.VisitaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            //Impostiamo l'ordine che dovranno seguire i risultati
            String sortOrder = DbContract.VisitaEntry.COLUMN_ID + " DESC";

            //Effettuiamo la query che ci restituirà i valori richiesti
            Cursor c = db.query(
                    DbContract.VisitaEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            //Spostiamo l'oggetto Cursor c alla prima riga presente all'interno del RecordSet
            c.moveToNext();

            //Salvo i vari dati presenti in tale riga
            int id = c.getInt(
                    c.getColumnIndexOrThrow(
                            DbContract.VisitaEntry.COLUMN_ID
                    )
            );
            setId(id);

            int creatore_visitatore = c.getInt(
                    c.getColumnIndexOrThrow(
                            DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE
                    )
            );
            setCreatore_visitatore(creatore_visitatore);

            int cognome = c.getInt(
                    c.getColumnIndexOrThrow(
                            DbContract.VisitaEntry.COLUMN_GUIDA
                    )
            );
            setGuida(cognome);

            int creatore_curatore = c.getInt(
                    c.getColumnIndexOrThrow(
                            DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE
                    )
            );
            setCreatore_curatore(creatore_curatore);

            long data = c.getLong(
                    c.getColumnIndexOrThrow(
                            DbContract.VisitaEntry.COLUMN_DATA
                    )
            );
            setData(data);

            int tipo_creatore = c.getInt(
                    c.getColumnIndexOrThrow(
                            DbContract.VisitaEntry.COLUMN_TIPO_CREATORE
                    )
            );
            setTipo_creatore(tipo_creatore);

            //Chiusura del Cursor c poichè non più necessario
            c.close();

        }
    }

    //Aggiorna record dal DB in base all'id fornito
    public void updateDataDb(Context context){
        if( isOnline() ){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita/update/";

            JSONObject data = new JSONObject();
            try {

                data.put(DbContract.VisitaEntry.COLUMN_ID, getId());
                if( getCreatore_visitatore() != 0  ){
                    data.put(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE, getCreatore_visitatore());
                }
                if( getCreatore_curatore() != 0  ){
                    data.put(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE, getCreatore_curatore());
                }
                if( getGuida() != 0  ){
                    data.put(DbContract.VisitaEntry.COLUMN_GUIDA, getGuida());
                }
                data.put(DbContract.VisitaEntry.COLUMN_DATA, getData());
                data.put(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE, getTipo_creatore());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visita self = this;

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
            //SQLITE3 Necessario per gestire le richieste offline
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //Imposto i nuovi valori da passare al db
            ContentValues values = new ContentValues();
            values.put(DbContract.VisitaEntry.COLUMN_CREATORE_VISITATORE, getCreatore_visitatore());
            values.put(DbContract.VisitaEntry.COLUMN_GUIDA, getGuida());
            values.put(DbContract.VisitaEntry.COLUMN_CREATORE_CURATORE, getCreatore_curatore());
            values.put(DbContract.VisitaEntry.COLUMN_DATA, getData());
            values.put(DbContract.VisitaEntry.COLUMN_TIPO_CREATORE, getTipo_creatore());

            //Trovo la riga da aggiornare all'interno del db
            String selection = DbContract.VisitaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            //Eseguo l'update per tale riga con i nuovi valori presenti all'interno di values
            int c = db.update(
                    DbContract.VisitaEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );

        }
    }

    //Cancella record dal DB in base all'id fornito
    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visita/delete/";

            JSONObject data = new JSONObject();
            try {

                data.put(DbContract.VisitaEntry.COLUMN_ID, getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visita self = this;

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
            //SQLITE3 Necessario per gestire le richieste offline
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //Selezione la riga da eliminare
            String selection = DbContract.VisitaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            //Elimino la riga all'interno del db dopo la selection
            int deleteRows = db.delete(DbContract.VisitaEntry.TABLE_NAME,selection,selectionArgs);

        }
    }

    public static void getGraphData(Context context,Visita visita,
                                    Response.Listener<JSONObject> responseListener,
                                    Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Server.getUrl() + "/visita/all-data/";

        JSONObject data = new JSONObject();
        try {
            data.put(DbContract.VisitaEntry.COLUMN_ID, visita.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, data, responseListener, errorListener);
        queue.add(jsonObjectRequest);
    }
}
