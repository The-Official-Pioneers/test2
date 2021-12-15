package it.uniba.pioneers.data.users;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.uniba.pioneers.data.Server;


public class CuratoreMuseale {
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'", Locale.getDefault());

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

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) throws ParseException {
        this.dataNascita = CuratoreMuseale.format.parse(dataNascita);
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setDataNascita(int dataNascita) {
        this.dataNascita = new Date(dataNascita);
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

    public Uri getPropic() {
        return propic;
    }

    public void setPropic(Uri propic) {
        this.propic = propic;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String email;
    private String password;
    private Uri propic;
    private int zona;

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
        setId(data.getInt("id"));
        setNome(data.getString("nome"));
        setCognome(data.getString("cognome"));
        setDataNascita(data.getString("data_nascita"));
        setEmail(data.getString("email"));
        setPassword(data.getString("password"));
        setPropic(Uri.parse(data.getString("propic")));
        setZona(data.getInt("zona"));

        setOnline(true);
    }

    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        setId(data.getInt("id"));
        setNome(data.getString("nome"));
        setCognome(data.getString("cognome"));
        //this.setDataNascita(data.getString("data_nascita"));
        setEmail(data.getString("email"));
        setPassword(data.getString("password"));
        setPropic(Uri.parse(data.getString("propic")));
        setZona(data.getInt("zona"));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put("id", getId());
        tmp.put("nome", getNome());
        tmp.put("cognome", getCognome());
        tmp.put("data_nascita", getDataNascita());
        tmp.put("email", getEmail());
        tmp.put("password", getPassword());
        tmp.put("propic", getPropic());
        tmp.put("zona", getZona());

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
                data.put("id", getId());
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
            //TODO SQLITE3
        }
    }

    public void createDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/curatore-museale/create/";

            JSONObject data = new JSONObject();
            try {
                data.put("nome", getNome());
                data.put("cognome", getCognome());
                data.put("data_nascita", getDataNascita());
                data.put("email", getEmail());
                data.put("password", getPassword());
                data.put("propic", getPropic().toString());
                data.put("zona", getZona());

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
            //TODO SQLITE3
        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/curatore-museale/update/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
                data.put("nome", getNome());
                data.put("cognome", getCognome());
                //data.put("data_nascita", getDataNascita());
                data.put("email", getEmail());
                data.put("password", getPassword());
                data.put("propic", getPropic());
                data.put("zona", getZona());

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
            //TODO SQLITE3
        }
    }

    public void deleteDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/curatore-museale/delete/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
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
            //TODO SQLITE3
        }
    }
}
