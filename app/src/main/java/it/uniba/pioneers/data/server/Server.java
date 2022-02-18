package it.uniba.pioneers.data.server;

public class Server {
    public static  final String PROTOCOL = "http";
    public static  final String IP = "pioneers.uno";
    public static  final String PORT = "3000";

    public static  String getUrl(){
        return PROTOCOL+"://"+IP+":"+PORT;
    }
}
