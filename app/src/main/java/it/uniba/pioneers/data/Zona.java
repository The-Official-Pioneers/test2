package it.uniba.pioneers.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class Zona {

    private int id;
    private String zona;
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

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
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
        this.setZona("");
        this.setDenominazione("");
        this.setDescrizione("");
        this.setLatitudine(0.0);
        this.setLongitudine(0.0);
        this.setLuogo("");

        this.setOnline(false);
    }

    //con questa funzione andiamo a prendere degli elementi e gli andiamo a trasformare in oggetti JSON
    public Zona(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt("id"));
        this.setZona(data.getString("zona"));
        this.setDenominazione(data.getString("denominazione"));
        this.setDescrizione(data.getString("descrizione"));
        this.setLatitudine(data.getDouble("latitudine"));
        this.setLongitudine(data.getDouble("longitudine"));
        this.setLuogo(data.getString("luogo"));

    }


    public JSONObject toJSON() throws JSONException{
        JSONObject tmp = new JSONObject();

        tmp.put("id", this.id);
        tmp.put("zona", this.zona);
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
}
