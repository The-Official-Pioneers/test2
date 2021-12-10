package it.uniba.pioneers.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

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

        this.setOnline(false);
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







}
