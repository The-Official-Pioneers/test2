package it.uniba.pioneers.data;

import android.content.ContentValues;
import android.content.Context;
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

public class Opera {

    private int id;
    private String titolo;
    private String descrizione;
    private String foto;
    private String qr;
    private int altezza;
    private int larghezza;
    private int profondita;
    private int area;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public int getAltezza() {
        return altezza;
    }

    public void setAltezza(int altezza) {
        this.altezza = altezza;
    }

    public int getLarghezza() {
        return larghezza;
    }

    public void setLarghezza(int larghezza) {
        this.larghezza = larghezza;
    }

    public int getProfondita() {
        return profondita;
    }

    public void setProfondita(int profondita) {
        this.profondita = profondita;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }



    //ONLINE STATE (dovrebbero essere tolti perchè l'online serve solo all'utente)
    private boolean online;
    public boolean isOnline() {
        return online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }


    public Opera(){
        this.setId(0);
        this.setTitolo("");
        this.setDescrizione("");
        this.setFoto(null);
        this.setQr("");
        this.setAltezza(0);
        this.setLarghezza(0);
        this.setProfondita(0);
        this.setArea(0);

        this.setOnline(true);
    }

    public Opera(JSONObject data) throws JSONException {
        this.setId(data.getInt("id"));
        this.setTitolo(data.getString("titolo"));
        this.setDescrizione(data.getString("descrizione"));
        this.setFoto(data.getString("foto"));
        this.setQr(data.getString("qr"));
        this.setAltezza(data.getInt("altezza"));
        this.setLarghezza(data.getInt("larghezza"));
        this.setProfondita(data.getInt("profondita"));
        this.setArea(data.getInt("area"));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put("id", this.id);
        tmp.put("titolo", this.titolo);
        tmp.put("descrizione", this.descrizione);
        tmp.put("foto", this.foto);
        tmp.put("qr", this.qr);
        tmp.put("altezza", this.altezza);
        tmp.put("larghezza", this.larghezza);
        tmp.put("profondita", this.profondita);
        tmp.put("area", this.area);

        return tmp;
    }

    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt("id"));
        this.setTitolo(data.getString("titolo"));
        this.setDescrizione(data.getString("descrizione"));
        this.setFoto(data.getString("foto"));
        this.setQr(data.getString("qr"));
        this.setAltezza(data.getInt("altezza"));
        this.setLarghezza(data.getInt("larghezza"));
        this.setProfondita(data.getInt("profondita"));
        this.setArea(data.getInt("area"));
    }

    public void readDataDb(Context context){

        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/opera/read/";

            JSONObject data = new JSONObject();
            try{
                data.put("id", getId());
            }catch(JSONException e){
                e.printStackTrace();
            }

             Opera self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                    //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
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

        }/*else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    DbContract.OperaEntry.COLUMN_ID,
                    DbContract.OperaEntry.COLUMN_TITOLO,
                    DbContract.OperaEntry.COLUMN_DESCRIZIONE,
                    DbContract.OperaEntry.COLUMN_FOTO,
                    DbContract.OperaEntry.COLUMN_QR,
                    DbContract.OperaEntry.COLUMN_ALTEZZA,
                    DbContract.OperaEntry.COLUMN_LARGHEZZA,
                    DbContract.OperaEntry.COLUMN_PROFONDITA,
                    DbContract.OperaEntry.COLUMN_AREA
            };

            String selection = DbContract.OperaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId())};
            String sortOrder = DbContract.OperaEntry.COLUMN_ID + "DESC";

            Cursor cursor = db.query(
                    DbContract.OperaEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder
            );

            cursor.moveToNext(); //FACCIO NEXT PERCHE MI ASPETTO SOLO UNA TUPLA

            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_ID
                    )
            );
            setId((int) id);

            String titolo = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_TITOLO
                    )
            );
            setTitolo(titolo);

            String descrizione = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_DESCRIZIONE
                    )

            );
            setDescrizione(descrizione);

            String foto = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_FOTO
                    )

            );
            setFoto(foto);

            String qr = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_QR
                    )

            );
            setQr(qr);

            long altezza = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_ALTEZZA
                    )

            );
            setAltezza((int) altezza);

            long larghezza = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_LARGHEZZA
                    )

            );
            setLarghezza((int) larghezza);

            long profondita = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_PROFONDITA
                    )

            );
            setProfondita((int) profondita);

            long area = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.OperaEntry.COLUMN_AREA
                    )

            );
            setArea((int) area);
            cursor.close();
        }*/   // SQLite
    }

    public void createDataDb(Context context){
        if(isOnline()){
            Toast.makeText(context, "entred", Toast.LENGTH_SHORT).show();
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/opera/create/";

            JSONObject data = new JSONObject();
            try {
                data.put("titolo", this.titolo);
                data.put("descrizione", this.descrizione);
                data.put("foto", this.foto);
                data.put("qr", this.qr);
                data.put("altezza", this.altezza);
                data.put("larghezza", this.larghezza);
                data.put("profondita", this.profondita);
                data.put("area", this.area);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Opera self = this;
            Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show();
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

            values.put(DbContract.OperaEntry.COLUMN_TITOLO, getTitolo());
            values.put(DbContract.OperaEntry.COLUMN_DESCRIZIONE, getDescrizione());
            values.put(DbContract.OperaEntry.COLUMN_FOTO, getFoto());
            values.put(DbContract.OperaEntry.COLUMN_QR, getQr());
            values.put(DbContract.OperaEntry.COLUMN_ALTEZZA, getAltezza());
            values.put(DbContract.OperaEntry.COLUMN_LARGHEZZA, getLarghezza());
            values.put(DbContract.OperaEntry.COLUMN_PROFONDITA, getProfondita());
            values.put(DbContract.OperaEntry.COLUMN_AREA, getArea());

            long newRowId = db.insert(
                    DbContract.OperaEntry.TABLE_NAME,
                    null,
                    values
            );

            if(newRowId != 0){
                Toast.makeText(context, String.valueOf(newRowId), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/opera/update/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
                data.put("titolo", getTitolo());
                data.put("descrizione", getDescrizione());
                data.put("foto", getFoto());
                data.put("qr", getQr());
                data.put("altezza", getAltezza());
                data.put("larghezza", getLarghezza());
                data.put("profondita", getProfondita());
                data.put("area", getArea());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Opera self = this;

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
            //TODO SQLITE3
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DbContract.OperaEntry.COLUMN_ID, getId());
            values.put(DbContract.OperaEntry.COLUMN_TITOLO, getTitolo());
            values.put(DbContract.OperaEntry.COLUMN_DESCRIZIONE, getDescrizione());
            values.put(DbContract.OperaEntry.COLUMN_FOTO, getFoto());
            values.put(DbContract.OperaEntry.COLUMN_QR, getQr());
            values.put(DbContract.OperaEntry.COLUMN_ALTEZZA, getAltezza());
            values.put(DbContract.OperaEntry.COLUMN_LARGHEZZA, getLarghezza());
            values.put(DbContract.OperaEntry.COLUMN_PROFONDITA, getProfondita());
            values.put(DbContract.OperaEntry.COLUMN_AREA, getArea());

            String selection = DbContract.OperaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int count = db.update(
                    DbContract.OperaEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/opera/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Opera self = this;

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
            //TODO SQLITE3
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = DbContract.OperaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int deletedRows = db.delete(
                    DbContract.OperaEntry.TABLE_NAME,
                    selection,
                    selectionArgs
            );
        }
    }
}
