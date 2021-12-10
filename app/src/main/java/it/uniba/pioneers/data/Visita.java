package it.uniba.pioneers.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Visita {


    //USED TO SET VISITA.DATA AND VISITA.ORARIO
    public static String dtStart = "2010-10-15T09:27:37Z";
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

    //GETTER AND SETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatore_visitatore() {
        return creatore_visitatore;
    }

    public void setCreatore_visitatore(int creatore_visitatore) {
        this.creatore_visitatore = creatore_visitatore;
    }

    public int getCreatore_curatore() {
        return creatore_curatore;
    }

    public void setCreatore_curatore(int creatore_curatore) {
        this.creatore_curatore = creatore_curatore;
    }

    public int getGuida() {
        return guida;
    }

    public void setGuida(int guida) {
        this.guida = guida;
    }

    public Date getData() {
        return data;
    }

    public void setData(String data) throws ParseException {
        this.data = Visita.format.parse(data);
    }


    public void setData(Date data) {
        this.data = data;
    }

    public Date getOrario() {
        return orario;
    }

    public void setOrario(String orario) throws ParseException {
        this.orario = Visita.format.parse(orario);
    }

    public void setOrario(Date orario) {
        this.orario = orario;
    }

    public String getTipo_creatore() {
        return tipo_creatore;
    }

    public void setTipo_creatore(String tipo_creatore) {
        this.tipo_creatore = tipo_creatore;
    }

    //ATTRIBUTES
    private int id;
    private int creatore_visitatore;
    private int creatore_curatore;
    private int guida;
    private Date data;
    private Date orario;
    private String tipo_creatore;

    //ONLINE STATE
    private boolean online;

    public Visita(){
        this.setId(0);
        this.setCreatore_visitatore(0);
        this.setCreatore_curatore(0);
        this.setGuida(0);
        //this.setData();
        //this.setOrario();
        this.setTipo_creatore("");

        this.setOnline(false);
    }

    //ONLINE STATUS METHODS

    //di regola vanno tolti, perchè l'online serve solo agli utenti
    public boolean isOnline() {
        return online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }

    public Visita(JSONObject data) throws JSONException, ParseException{
        this.setId(data.getInt("id"));
        this.setCreatore_visitatore(data.getInt("creatore_visitatore"));
        this.setCreatore_curatore(data.getInt("creatore_curatore"));
        this.setGuida(data.getInt("guida"));
        this.setData(data.getString("data"));
        this.setOrario(data.getString("orario"));
        this.setTipo_creatore(data.getString("tipo_creatore"));

        this.setOnline(false);
    }


    //CREATE JSON OBJECT
    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put("id", this.id);
        tmp.put("creatore_visitatore", this.creatore_visitatore);
        tmp.put("creatore_curatore", this.creatore_curatore);
        tmp.put("guida", this.guida);
        tmp.put("data", this.data);
        tmp.put("orario", this.orario);
        tmp.put("tipo_creatore", this.tipo_creatore);

        return tmp;
    }


}
