package it.uniba.pioneers.data.server;

public class Server {
    static public final String PROTOCOL = "http";
    static public final String IP = "192.168.1.15";
    static public final String PORT = "3000";

    static public String getUrl(){
        return PROTOCOL+"://"+IP+":"+PORT;
    }
}
