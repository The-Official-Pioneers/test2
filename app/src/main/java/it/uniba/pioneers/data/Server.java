package it.uniba.pioneers.data;

public class Server {
    static public final String PORT = "3000";
    static public final String IP = "172.29.36.161";
    static public final String PROTOCOL = "http";


    static public String getUrl(){
        return "http://172.29.36.161:3000";
    }
}