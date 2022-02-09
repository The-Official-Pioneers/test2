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
import it.uniba.pioneers.testtool.R;

public class Guida {

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

    //MODIFICATO DA IVAN
    public void setDataNascita(String dataNascita) throws ParseException {
        this.dataNascita = Guida.format.parse(dataNascita);
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    //Usato per l'area personale degli utenti
    public String getShorterDataNascita(){
        return output.format(MainActivity.guida.getDataNascita());
    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }
    //MODIFICATO DA IVAN

    public void setDataNascita(long dataNascita) {
        this.dataNascita = new Date(dataNascita);
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

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
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
    private String specializzazione;
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

    public Guida(){
        setId(0);
        setNome("");
        setCognome("");
        setEmail("");
        setPassword("");
        setPropic(null);
        setSpecializzazione("");

        setOnline(true);
    }

    public Guida(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt(DbContract.GuidaEntry.COLUMN_ID));
        setNome(data.getString(DbContract.GuidaEntry.COLUMN_NOME));
        setCognome(data.getString(DbContract.GuidaEntry.COLUMN_COGNOME));
        setDataNascita(data.getString(DbContract.GuidaEntry.COLUMN_DATA_NASCITA));
        setEmail(data.getString(DbContract.GuidaEntry.COLUMN_EMAIL));
        setPassword(data.getString(DbContract.GuidaEntry.COLUMN_PASSWORD));
        setPropic(data.getString(DbContract.GuidaEntry.COLUMN_PROPIC));
        setSpecializzazione(data.getString(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE));
        setOnline(true);
    }

    //CREATE JSON OBJECT
    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put(DbContract.GuidaEntry.COLUMN_ID, this.id);
        tmp.put(DbContract.GuidaEntry.COLUMN_NOME, this.nome);
        tmp.put(DbContract.GuidaEntry.COLUMN_COGNOME, this.cognome);
        tmp.put(DbContract.GuidaEntry.COLUMN_DATA_NASCITA, this.dataNascita);
        tmp.put(DbContract.GuidaEntry.COLUMN_EMAIL, this.email);
        tmp.put(DbContract.GuidaEntry.COLUMN_PASSWORD, this.password);
        tmp.put(DbContract.GuidaEntry.COLUMN_PROPIC, this.propic);
        tmp.put(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE, this.specializzazione);

        return tmp;
    }

    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt(DbContract.GuidaEntry.COLUMN_ID));
        setNome(data.getString(DbContract.GuidaEntry.COLUMN_NOME));
        setCognome(data.getString(DbContract.GuidaEntry.COLUMN_COGNOME));
        setDataNascita(data.getString(DbContract.GuidaEntry.COLUMN_DATA_NASCITA));
        setEmail(data.getString(DbContract.GuidaEntry.COLUMN_EMAIL));
        setPassword(data.getString(DbContract.GuidaEntry.COLUMN_PASSWORD));
        setPropic(data.getString(DbContract.GuidaEntry.COLUMN_PROPIC));
        setSpecializzazione(data.getString(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE));
    }

    //DB METHOD
    public void readDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/guida/read/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.GuidaEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Guida self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                }else{
                                    Toast.makeText(context, R.string.cambio_dati_no_validi, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    DbContract.GuidaEntry.COLUMN_ID,
                    DbContract.GuidaEntry.COLUMN_NOME,
                    DbContract.GuidaEntry.COLUMN_COGNOME,
                    DbContract.GuidaEntry.COLUMN_DATA_NASCITA,
                    DbContract.GuidaEntry.COLUMN_EMAIL,
                    DbContract.GuidaEntry.COLUMN_PASSWORD,
                    DbContract.GuidaEntry.COLUMN_PROPIC,
                    DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE
            };

            String selection = DbContract.GuidaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            String sortOrder =
                    DbContract.GuidaEntry.COLUMN_ID + " DESC";

            Cursor cursor = db.query(
                    DbContract.GuidaEntry.TABLE_NAME,   // The table to query
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
                            DbContract.GuidaEntry.COLUMN_ID
                    )
            );

            String nome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_NOME
                    )
            );

            String cognome = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_COGNOME
                    )
            );

            long data_nascita = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_DATA_NASCITA
                    )
            );

            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_EMAIL
                    )
            );

            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_PASSWORD
                    )
            );

            String propic = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_PROPIC
                    )
            );

            String specializzazione = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE
                    )
            );

            setId(id);
            setNome(nome);
            setCognome(cognome);
            setDataNascita(new Date(data_nascita));
            setEmail(email);
            setPassword(password);
            setPropic(propic);
            setSpecializzazione(specializzazione);

            cursor.close();
        }
    }

    public void createDataDb(Context context, Response.Listener<JSONObject> responseListener){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/guida/create/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.GuidaEntry.COLUMN_NOME, getNome());
                data.put(DbContract.GuidaEntry.COLUMN_COGNOME, getCognome());
                data.put(DbContract.GuidaEntry.COLUMN_DATA_NASCITA, getDataNascita());
                data.put(DbContract.GuidaEntry.COLUMN_EMAIL, getEmail());
                data.put(DbContract.GuidaEntry.COLUMN_PASSWORD, getPassword());
                data.put(DbContract.GuidaEntry.COLUMN_PROPIC, getPropic());
                data.put(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE, getSpecializzazione());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Guida self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    responseListener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.GuidaEntry.COLUMN_NOME, getNome());
            values.put(DbContract.GuidaEntry.COLUMN_COGNOME, getCognome());
            values.put(DbContract.GuidaEntry.COLUMN_DATA_NASCITA, getDataNascita().getTime());
            values.put(DbContract.GuidaEntry.COLUMN_EMAIL, getEmail());
            values.put(DbContract.GuidaEntry.COLUMN_PASSWORD, getPassword());
            values.put(DbContract.GuidaEntry.COLUMN_PROPIC, getPropic());
            values.put(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE, getSpecializzazione());


            long newRowId = db.insert(DbContract.GuidaEntry.TABLE_NAME, null, values);
            setId(newRowId);
        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/guida/update/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.GuidaEntry.COLUMN_ID, getId());
                data.put(DbContract.GuidaEntry.COLUMN_NOME, getNome());
                data.put(DbContract.GuidaEntry.COLUMN_COGNOME, getCognome());
                data.put(DbContract.GuidaEntry.COLUMN_DATA_NASCITA, getDataNascita());
                data.put(DbContract.GuidaEntry.COLUMN_EMAIL, getEmail());
                data.put(DbContract.GuidaEntry.COLUMN_PASSWORD, getPassword());
                data.put(DbContract.GuidaEntry.COLUMN_PROPIC, getPropic());
                data.put(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE, getSpecializzazione());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Guida self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    self.setDataFromJSON(response.getJSONObject("data"));
                                }else{
                                    Toast.makeText(context, R.string.cambio_dati_no_validi, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DbContract.GuidaEntry.COLUMN_ID, getId());
            values.put(DbContract.GuidaEntry.COLUMN_NOME, getNome());
            values.put(DbContract.GuidaEntry.COLUMN_COGNOME, getCognome());
            values.put(DbContract.GuidaEntry.COLUMN_DATA_NASCITA, getDataNascita().getTime());
            values.put(DbContract.GuidaEntry.COLUMN_EMAIL, getEmail());
            values.put(DbContract.GuidaEntry.COLUMN_PASSWORD, getPassword());
            values.put(DbContract.GuidaEntry.COLUMN_PROPIC, getPropic());
            values.put(DbContract.GuidaEntry.COLUMN_SPECIALIZZAZIONE, getSpecializzazione());

            String selection = DbContract.GuidaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int count = db.update(
                    DbContract.GuidaEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/guida/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put(DbContract.GuidaEntry.COLUMN_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Guida self = this;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, R.string.cambio_dati_no_validi, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(jsonObjectRequest);
        }else{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = DbContract.GuidaEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(getId()) };

            int deletedRows = db.delete(DbContract.GuidaEntry.TABLE_NAME, selection, selectionArgs);
        }
    }

    public void login(Context context, Response.Listener<JSONObject> responseListener) {

        if (isOnline()) {
            try {
                System.out.println("Sono arrivato");
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = Server.getUrl() + "/guida/login/";

                JSONObject data = new JSONObject();
                try {
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_EMAIL, getEmail());
                    data.put(DbContract.CuratoreMusealeEntry.COLUMN_PASSWORD, getPassword());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Guida self = this;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                        responseListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, R.string.server_no_risponde, Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                });
                queue.add(jsonObjectRequest);

            } catch (Exception e) {
            }
        }
    }
}
