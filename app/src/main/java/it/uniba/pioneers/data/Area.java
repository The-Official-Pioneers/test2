package it.uniba.pioneers.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class Area {

    private int id;
    private int zona;
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //online state
    private boolean online;

    public Area() {
        this.setId(0);
        this.setZona(0);
        this.setNome("");

        this.setOnline(false);
    }

    public Area(JSONObject data) throws JSONException, ParseException {

        this.setId(data.getInt("id"));
        this.setZona(data.getInt("zona"));
        this.setNome(data.getString("nome"));


    }

    public JSONObject toJSON() throws JSONException{

        JSONObject tmp = new JSONObject();

        tmp.put("id", this.id);
        tmp.put("zona", this.zona);
        tmp.put("nome", this.nome);

        return tmp;
    }

    public boolean isOnline(){

        return online;
    }

    public void setOnline(boolean online){

        this.online = online;

    }

}
