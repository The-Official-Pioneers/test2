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


public class CuratoreMuseale {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ITALY);
    public static SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

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

    //MODIFICATO DA IVAN
    public void setDataNascita(String dataNascita) throws ParseException {
        this.dataNascita = CuratoreMuseale.format.parse(dataNascita);
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    //Usato per l'area personale degli utenti
    public String getShorterDataNascita(){
        return output.format(MainActivity.curatore.getDataNascita());
    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }
    //MODIFICATO DA IVAN


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

    public long getZona() {
        return zona;
    }

    public void setZona(long zona) {
        this.zona = zona;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatusComputation(boolean status) {
        this.statusComputation = status;
    }

    public boolean getStatusComputation() {
        return this.statusComputation;
    }

    private long id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String email;
    private String password;
    private String propic;
    private long zona;
    boolean statusComputation = false;

    //ONLINE STATE
    private boolean online;

    public CuratoreMuseale(){
        setId(0);
        setNome("");
        setCognome("");
        setEmail("");
        setPassword("");
        setPropic(null);
        setZona(0);

        setOnline(true);
    }

    public CuratoreMuseale(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt(DbContract.CuratoreMusealeEntry.COLUMN_ID));
        this.setNome(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_NOME));
        this.setCognome(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME));
        this.setDataNascita(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA));
        this.setEmail(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL));
        this.setPassword(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD));
        this.setPropic(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC));
        this.setZona(data.getInt(DbContract.CuratoreMusealeEntry.COLUMN_ZONA));
        this.setOnline(true);
    }

    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt(DbContract.CuratoreMusealeEntry.COLUMN_ID));
        this.setNome(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_NOME));
        this.setCognome(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME));
        this.setDataNascita(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA));
        this.setEmail(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL));
        this.setPassword(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD));
        this.setPropic(data.getString(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC));
        this.setZona(data.getInt(DbContract.CuratoreMusealeEntry.COLUMN_ZONA));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_ID, getId());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_NOME, getNome());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME, getCognome());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA, getDataNascita());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC, getPropic());
        tmp.put(DbContract.CuratoreMusealeEntry.COLUMN_ZONA, getZona());

        return tmp;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    //DB METHOD
    public void readDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/curatore-museale/read/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CuratoreMuseale self = this;

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
                    DbContract.CuratoreMusealeEntry.COLUMN_ID,
                    DbContract.CuratoreMusealeEntry.COLUMN_NOME,
                    DbContract.CuratoreMusealeEntry.COLUMN_COGNOME,
                    DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA,
                    DbContract.CuratoreMusealeEntry.COLUMN_EMAIL,
                    DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD,
                    DbContract.CuratoreMusealeEntry.COLUMN_PROPIC,
                    DbContract.CuratoreMusealeEntry.COLUMN_ZONA
            };

            String selection = DbContract.CuratoreMusealeEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            String sortOrder =
                    DbContract.CuratoreMusealeEntry.COLUMN_ID + " DESC";

            Cursor cursor = db.query(
                    DbContract.CuratoreMusealeEntry.TABLE_NAME,   // The table to query
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
                        DbContract.CuratoreMusealeEntry.COLUMN_ID
                    )
            );

            String nome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_NOME
                    )
            );

            String cognome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_COGNOME
                    )
            );

            long data_nascita = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA
                    )
            );

            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_EMAIL
                    )
            );

            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD
                    )
            );

            String propic = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_PROPIC
                    )
            );

            long zona = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.CuratoreMusealeEntry.COLUMN_ZONA
                    )
            );

            setId(id);
            setNome(nome);
            setCognome(cognome);
            setDataNascita(new Date(data_nascita));
            setEmail(email);
            setPassword(password);
            setPropic(propic);
            setZona(zona);

            cursor.close();
        }
    }

    public void createDataDb(Context context, Response.Listener<JSONObject> responseListener){

        if(isOnline()){
            try{
                System.out.println("Sono arrivato");
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = Server.getUrl() + "/curatore-museale/create/";

                JSONObject data = new JSONObject();
                try {
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_NOME, getNome());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME, getCognome());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA, getDataNascita());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC, getPropic().toString());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_ZONA, getZona());

                    System.out.println(data.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CuratoreMuseale self = this;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                        responseListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Il server non risponde", Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                });
                queue.add(jsonObjectRequest);

            }catch(Exception e){}
        }else{
            Toast.makeText(context, "Entred", Toast.LENGTH_SHORT).show();
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_NOME, getNome());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME, getCognome());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA, getDataNascita().getTime());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC, getPropic().toString());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_ZONA, getZona());

            long newRowId = db.insert(DbContract.CuratoreMusealeEntry.TABLE_NAME, null, values);
            setId(newRowId);
        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/curatore-museale/update/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_ID, getId());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_NOME, getNome());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME, getCognome());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA, getDataNascita());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC, getPropic());
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_ZONA, getZona());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CuratoreMuseale self = this;

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

            values.put(DbContract.CuratoreMusealeEntry.COLUMN_ID, getId());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_NOME, getNome());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_COGNOME, getCognome());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_DATA_NASCITA, getDataNascita().getTime());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_PROPIC, getPropic());
            values.put(DbContract.CuratoreMusealeEntry.COLUMN_ZONA, getZona());

            String selection = DbContract.CuratoreMusealeEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int count = db.update(
                    DbContract.CuratoreMusealeEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/curatore-museale/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.CuratoreMusealeEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CuratoreMuseale self = this;

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

            String selection = DbContract.CuratoreMusealeEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int deletedRows = db.delete(DbContract.CuratoreMusealeEntry.TABLE_NAME, selection, selectionArgs);
        }
    }

    public void login(Context context, Response.Listener<JSONObject> responseListener) {

        if (isOnline()) {
            try {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = Server.getUrl() + "/curatore-museale/login/";

                JSONObject data = new JSONObject();
                try {
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CuratoreMuseale self = this;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                        responseListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Il server non risponde", Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                });
                queue.add(jsonObjectRequest);

            } catch (Exception e) {
            }
        }
    }
}
