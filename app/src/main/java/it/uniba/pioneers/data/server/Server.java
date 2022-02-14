package it.uniba.pioneers.data.server;

public class Server {
    static public final String PROTOCOL = "http";
    static public final String IP = "172.29.21.220";
    static public final String PORT = "3000";

    static public String getUrl(){
        return PROTOCOL+"://"+IP+":"+PORT;
    }
}
