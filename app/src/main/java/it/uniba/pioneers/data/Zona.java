package it.uniba.pioneers.data;

import android.content.Context;
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

public class Zona {

    private int id;
    private String tipo;
    private String denominazione;
    private String  descrizione;
    private double latitudine;
    private double longitudine;
    private String luogo;

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

    //online state
    private boolean online;

    public Zona() {
        this.setId(0);
        this.setTipo("");
        this.setDenominazione("");
        this.setDescrizione("");
        this.setLatitudine(0.0);
        this.setLongitudine(0.0);
        this.setLuogo("");

        this.setOnline(true);
    }

    //con questa funzione andiamo a prendere degli elementi e gli andiamo a trasformare in oggetti JSON
    public Zona(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt("id"));
        this.setTipo(data.getString("tipo"));
        this.setDenominazione(data.getString("denominazione"));
        this.setDescrizione(data.getString("descrizione"));
        this.setLatitudine(data.getDouble("latitudine"));
        this.setLongitudine(data.getDouble("longitudine"));
        this.setLuogo(data.getString("luogo"));

    }
    public void setDataFromJSON(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt("id"));
        this.setTipo(data.getString("tipo"));
        this.setDenominazione(data.getString("denominazione"));
        this.setDescrizione(data.getString("descrizione"));
        this.setLatitudine(data.getDouble("latitudine"));
        this.setLongitudine(data.getDouble("longitudine"));
        this.setLuogo(data.getString("luogo"));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject tmp = new JSONObject();

        tmp.put("id", this.id);
        tmp.put("tipo", this.tipo);
        tmp.put("denominazione", this.denominazione);
        tmp.put("descrizione", this.descrizione);
        tmp.put("latitudine", this.latitudine);
        tmp.put("longitudine", this.longitudine);
        tmp.put("luogo", this.luogo);

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

    public void readDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/zona/read/";

            JSONObject data = new JSONObject();
            try {
                data.put("id", 10   );
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
            //TODO SQLITE3
        }
    }

    public void createDataDb(Context context){
        if(isOnline()){
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Server.getUrl() + "/zona/create/";

            JSONObject data = new JSONObject();
            try {
                data.put("tipo", getTipo());
                data.put("denominazione", getDenominazione());
                data.put("descrizione", getDescrizione());
                data.put("latitudine", getLatitudine());
                data.put("longitudine", getLongitudine());
                data.put("luogo", getLuogo());

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
                data.put("tipo", getTipo());
                data.put("denominazione", getDenominazione());
                data.put("descrizione", getDescrizione());
                data.put("latitudine", getLatitudine());
                data.put("longitudine", getLongitudine());
                data.put("luogo", getLuogo());

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
            //TODO SQLITE3
        }
    }
}
