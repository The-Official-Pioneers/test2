package it.uniba.pioneers.data;

import android.content.Context;
import android.net.Uri;
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

import it.uniba.pioneers.data.serer.Server;

public class Opera {

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

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
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

    private int id;
    private String titolo;
    private String descrizione;
    private Uri foto;
    private String qr;
    private int altezza;
    private int larghezza;
    private int profondita;
    private int area;

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
        this.setFoto(Uri.parse(data.getString("foto")));
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
        this.setFoto(Uri.parse(data.getString("foto")));
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
            //TODO SQLITE3
        }
    }

    public void updateDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/opera/update/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", getId());
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
        }
    }
}
