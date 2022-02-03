package it.uniba.pioneers.data.users;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.uniba.pioneers.data.server.Server;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.sqlite.DbHelper;
import it.uniba.pioneers.testtool.MainActivity;

public class Visitatore {
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ITALY);

    public static SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setDataNascita(String dataNascita) throws ParseException {
        this.dataNascita = Visitatore.format.parse(dataNascita);
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    //Usato per l'area personale degli utenti
    public String getShorterDataNascita(){
         return output.format(MainActivity.visitatore.getDataNascita());
    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public boolean getStatusComputation() {
        return this.statusComputation;
    }

    public void setStatusComputation(boolean status) {
        this.statusComputation = status;
    }


    private long id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String email;
    private String password;
    private String propic;
    boolean statusComputation = false;

    //ONLINE STATE
    private boolean online;

    //Method that checks if the user is online
    public boolean isOnline() {
        return online;
    }

    //Method that sets the online status to true or false depending on the user's actual connection
    public void setOnline(boolean online) {
        this.online = online;
    }

    public Visitatore(){
        this.setId(0);
        this.setNome("");
        this.setCognome("");
        this.setEmail("");
        this.setPassword("");
        this.setPropic(null);

        this.setOnline(true);
    }

    public Visitatore(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt(DbContract.VisitatoreEntry.COLUMN_ID));
        this.setNome(data.getString(DbContract.VisitatoreEntry.COLUMN_NOME));
        this.setCognome(data.getString(DbContract.VisitatoreEntry.COLUMN_COGNOME));
        setDataNascita(data.getString(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA));
        this.setEmail(data.getString(DbContract.VisitatoreEntry.COLUMN_EMAIL));
        this.setPassword(data.getString(DbContract.VisitatoreEntry.COLUMN_PASSWORD));
        this.setPropic(data.getString(DbContract.VisitatoreEntry.COLUMN_PROPIC));
        setOnline(true);
    }

    //CREATE JSON OBJECT
    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.VisitatoreEntry.COLUMN_ID, this.id);
        tmp.put(DbContract.VisitatoreEntry.COLUMN_NOME, this.nome);
        tmp.put(DbContract.VisitatoreEntry.COLUMN_COGNOME, this.cognome);
        tmp.put(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA, this.dataNascita);
        tmp.put(DbContract.VisitatoreEntry.COLUMN_EMAIL, this.email);
        tmp.put(DbContract.VisitatoreEntry.COLUMN_PASSWORD, this.password);
        tmp.put(DbContract.VisitatoreEntry.COLUMN_PROPIC, this.propic);

        return tmp;
    }

    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt(DbContract.VisitatoreEntry.COLUMN_ID));
        this.setNome(data.getString(DbContract.VisitatoreEntry.COLUMN_NOME));
        this.setCognome(data.getString(DbContract.VisitatoreEntry.COLUMN_COGNOME));
        this.setDataNascita(data.getString(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA));
        this.setEmail(data.getString(DbContract.VisitatoreEntry.COLUMN_EMAIL));
        this.setPassword(data.getString(DbContract.VisitatoreEntry.COLUMN_PASSWORD));
        this.setPropic(data.getString(DbContract.VisitatoreEntry.COLUMN_PROPIC));
    }

    public void readDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visitatore/read/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.VisitatoreEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visitatore self = this;

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
                    DbContract.VisitatoreEntry.COLUMN_ID,
                    DbContract.VisitatoreEntry.COLUMN_NOME,
                    DbContract.VisitatoreEntry.COLUMN_COGNOME,
                    DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA,
                    DbContract.VisitatoreEntry.COLUMN_EMAIL,
                    DbContract.VisitatoreEntry.COLUMN_PASSWORD,
                    DbContract.VisitatoreEntry.COLUMN_PROPIC,
            };

            String selection = DbContract.VisitatoreEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            String sortOrder =
                    DbContract.VisitatoreEntry.COLUMN_ID + " DESC";

            Cursor cursor = db.query(
                    DbContract.VisitatoreEntry.TABLE_NAME,   // The table to query
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
                            DbContract.VisitatoreEntry.COLUMN_ID
                    )
            );

            String nome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitatoreEntry.COLUMN_NOME
                    )
            );

            String cognome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitatoreEntry.COLUMN_COGNOME
                    )
            );

            long data_nascita = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA
                    )
            );

            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitatoreEntry.COLUMN_EMAIL
                    )
            );

            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitatoreEntry.COLUMN_PASSWORD
                    )
            );

            String propic = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.VisitatoreEntry.COLUMN_PROPIC
                    )
            );

            setId(id);
            setNome(nome);
            setCognome(cognome);
            setDataNascita(new Date(data_nascita));
            setEmail(email);
            setPassword(password);
            setPropic(propic);

            cursor.close();
        }
    }

    public void createDataDb(Context context, Response.Listener<JSONObject> responseListener){

        if(isOnline()){

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visitatore/create/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.VisitatoreEntry.COLUMN_NOME, getNome());
                data.put(DbContract.VisitatoreEntry.COLUMN_COGNOME, getCognome());
                data.put(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA, getDataNascita());
                data.put(DbContract.VisitatoreEntry.COLUMN_EMAIL, getEmail());
                data.put(DbContract.VisitatoreEntry.COLUMN_PASSWORD, getPassword());
                data.put(DbContract.VisitatoreEntry.COLUMN_PROPIC, getPropic().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visitatore self = this;

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
            values.put(DbContract.VisitatoreEntry.COLUMN_NOME, getNome());
            values.put(DbContract.VisitatoreEntry.COLUMN_COGNOME, getCognome());
            values.put(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA, getDataNascita().getTime());
            values.put(DbContract.VisitatoreEntry.COLUMN_EMAIL, getEmail());
            values.put(DbContract.VisitatoreEntry.COLUMN_PASSWORD, getPassword());
            values.put(DbContract.VisitatoreEntry.COLUMN_PROPIC, getPropic());

            long newRowId = db.insert(DbContract.VisitatoreEntry.TABLE_NAME, null, values);
            setId(newRowId);
        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visitatore/update/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
                data.put(DbContract.VisitatoreEntry.COLUMN_NOME, getNome());
                data.put(DbContract.VisitatoreEntry.COLUMN_COGNOME, getCognome());
                data.put(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA, getDataNascita());
                data.put(DbContract.VisitatoreEntry.COLUMN_EMAIL, getEmail());
                data.put(DbContract.VisitatoreEntry.COLUMN_PASSWORD, getPassword());
                data.put(DbContract.VisitatoreEntry.COLUMN_PROPIC, getPropic());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visitatore self = this;

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

            values.put(DbContract.VisitatoreEntry.COLUMN_ID, getId());
            values.put(DbContract.VisitatoreEntry.COLUMN_NOME, getNome());
            values.put(DbContract.VisitatoreEntry.COLUMN_COGNOME, getCognome());
            values.put(DbContract.VisitatoreEntry.COLUMN_DATA_NASCITA, getDataNascita().getTime());
            values.put(DbContract.VisitatoreEntry.COLUMN_EMAIL, getEmail());
            values.put(DbContract.VisitatoreEntry.COLUMN_PASSWORD, getPassword());
            values.put(DbContract.VisitatoreEntry.COLUMN_PROPIC, getPropic());

            String selection = DbContract.VisitatoreEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int count = db.update(
                    DbContract.VisitatoreEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }


    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/visitatore/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.VisitatoreEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Visitatore self = this;

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

            String selection = DbContract.VisitatoreEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            long deletedRows = db.delete(DbContract.VisitatoreEntry.TABLE_NAME, selection, selectionArgs);
        }
    }
}
