package it.uniba.pioneers.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Visitatore {
    public static String dtStart = "2010-10-15T09:27:37Z";
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getDataNascita() {
        return dataNascita;
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

    public Uri getPropic() {
        return propic;
    }

    public void setPropic(Uri propic) {
        this.propic = propic;
    }

    public void setDataNascita(String dataNascita) throws ParseException {
        this.dataNascita = Visitatore.format.parse(dataNascita);
    }

    private int id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String email;
    private String password;
    private Uri propic;

    //ONLINE STATE
    private boolean online;

    public Visitatore(){
        this.setId(0);
        this.setNome("");
        this.setCognome("");
        this.setEmail("");
        this.setPassword("");
        this.setPropic(null);

        this.setOnline(false);
    }

    public Visitatore(JSONObject data) throws JSONException, ParseException {
        this.setId(data.getInt("id"));
        this.setNome(data.getString("nome"));
        this.setCognome(data.getString("cognome"));
        this.setDataNascita(data.getString("data_nascita"));
        this.setEmail(data.getString("email"));
        this.setPassword(data.getString("password"));
        this.setPropic(Uri.parse(data.getString("propic")));
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put("id", this.id);
        tmp.put("nome", this.nome);
        tmp.put("cognome", this.cognome);
        tmp.put("data_nascita", this.dataNascita);
        tmp.put("email", this.email);
        tmp.put("password", this.password);
        tmp.put("propic", this.propic);

        return tmp;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
