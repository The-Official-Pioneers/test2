package it.uniba.pioneers.sqlite;

import android.provider.BaseColumns;

public final class DbContract {
    private DbContract(){}

    public static class AreaEntry implements BaseColumns{
        public static final String TABLE_NAME = "area";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_ZONA = "zona";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + AreaEntry.TABLE_NAME + " (" +
                        AreaEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        AreaEntry.COLUMN_NOME + " TEXT," +
                        AreaEntry.COLUMN_ZONA + " INTEGER,"+
                        "FOREIGN KEY("+AreaEntry.COLUMN_ZONA+") REFERENCES "+ZonaEntry.TABLE_NAME+"("+ZonaEntry.COLUMN_ID+"))";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AreaEntry.TABLE_NAME;
    }

    public static class CuratoreMusealeEntry implements BaseColumns{
        public static final String TABLE_NAME = "curatore_museale";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_COGNOME = "cognome";
        public static final String COLUMN_DATA_NASCITA = "data_nascita";
        public static final String COLUMN_ZONA = "zona";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PROPIC = "propic";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CuratoreMusealeEntry.TABLE_NAME + " (" +
                        CuratoreMusealeEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        CuratoreMusealeEntry.COLUMN_NOME + " TEXT," +
                        CuratoreMusealeEntry.COLUMN_COGNOME + " TEXT," +
                        CuratoreMusealeEntry.COLUMN_DATA_NASCITA + " INTEGER," +
                        CuratoreMusealeEntry.COLUMN_ZONA + " INTEGER," +
                        CuratoreMusealeEntry.COLUMN_EMAIL + " TEXT," +
                        CuratoreMusealeEntry.COLUMN_PASSWORD + " TEXT," +
                        CuratoreMusealeEntry.COLUMN_PROPIC + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CuratoreMusealeEntry.TABLE_NAME;
    }

    public static class GuidaEntry implements BaseColumns{
        public static final String TABLE_NAME = "guida";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_COGNOME = "cognome";
        public static final String COLUMN_DATA_NASCITA = "data_nascita";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_SPECIALIZZAZIONE = "specializzazione";
        public static final String COLUMN_PROPIC = "propic";


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GuidaEntry.TABLE_NAME + " (" +
                        GuidaEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        GuidaEntry.COLUMN_NOME + " TEXT," +
                        GuidaEntry.COLUMN_COGNOME + " TEXT," +
                        GuidaEntry.COLUMN_DATA_NASCITA + " INTEGER," +
                        GuidaEntry.COLUMN_EMAIL + " TEXT," +
                        GuidaEntry.COLUMN_PASSWORD + " TEXT," +
                        GuidaEntry.COLUMN_SPECIALIZZAZIONE + " TEXT," +
                        GuidaEntry.COLUMN_PROPIC + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GuidaEntry.TABLE_NAME;
    }

    public static class VisitatoreEntry implements BaseColumns{
        public static final String TABLE_NAME = "visitatore";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_COGNOME = "cognome";
        public static final String COLUMN_DATA_NASCITA = "data_nascita";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PROPIC = "propic";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + VisitatoreEntry.TABLE_NAME + " (" +
                        VisitatoreEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        VisitatoreEntry.COLUMN_NOME + " TEXT," +
                        VisitatoreEntry.COLUMN_COGNOME + " TEXT," +
                        VisitatoreEntry.COLUMN_DATA_NASCITA + " INTEGER," +
                        VisitatoreEntry.COLUMN_EMAIL + " TEXT," +
                        VisitatoreEntry.COLUMN_PASSWORD + " TEXT," +
                        VisitatoreEntry.COLUMN_PROPIC + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + VisitatoreEntry.TABLE_NAME;
    }


    public static class OperaEntry implements BaseColumns{
        public static final String TABLE_NAME = "opera";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITOLO = "titolo";
        public static final String COLUMN_DESCRIZIONE = "descrizione";
        public static final String COLUMN_FOTO = "foto";
        public static final String COLUMN_QR = "qr";
        public static final String COLUMN_ALTEZZA = "altezza";
        public static final String COLUMN_LARGHEZZA = "larghezza";
        public static final String COLUMN_PROFONDITA = "profondita";
        public static final String COLUMN_AREA = "area";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + OperaEntry.TABLE_NAME + " (" +
                        OperaEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        OperaEntry.COLUMN_TITOLO + " TEXT," +
                        OperaEntry.COLUMN_DESCRIZIONE + " TEXT," +
                        OperaEntry.COLUMN_FOTO + " TEXT," +
                        OperaEntry.COLUMN_QR + " TEXT," +
                        OperaEntry.COLUMN_ALTEZZA + " INTEGER," +
                        OperaEntry.COLUMN_LARGHEZZA + " INTEGER," +
                        OperaEntry.COLUMN_PROFONDITA + " INTEGER," +
                        OperaEntry.COLUMN_AREA + " INTEGER,"+
                        "FOREIGN KEY("+OperaEntry.COLUMN_AREA+") REFERENCES "+AreaEntry.TABLE_NAME+"("+AreaEntry.COLUMN_ID+"))";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + OperaEntry.TABLE_NAME;
    }

    public static class VisitaEntry implements BaseColumns{
        public static final String TABLE_NAME = "visita";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CREATORE_VISITATORE = "curatore_visitatore";
        public static final String COLUMN_CREATORE_CURATORE = "curatore_curatore";
        public static final String COLUMN_TIPO_CREATORE = "tipo_creatore";
        public static final String COLUMN_ORARIO = "orario";
        public static final String COLUMN_GUIDA = "guida";
        public static final String COLUMN_DATA = "data";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + VisitaEntry.TABLE_NAME + " (" +
                        VisitaEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        VisitaEntry.COLUMN_CREATORE_VISITATORE + " INTEGER," +
                        VisitaEntry.COLUMN_CREATORE_CURATORE + " INTEGER," +
                        VisitaEntry.COLUMN_TIPO_CREATORE + " INTEGER," +
                        VisitaEntry.COLUMN_GUIDA + " INTEGER," +
                        VisitaEntry.COLUMN_DATA + " INTEGER," +
                        VisitaEntry.COLUMN_ORARIO + " INTEGER,"+
                        "FOREIGN KEY("+VisitaEntry.COLUMN_CREATORE_VISITATORE+") REFERENCES "+VisitatoreEntry.TABLE_NAME+"("+VisitatoreEntry.COLUMN_ID+"),"+
                        "FOREIGN KEY("+VisitaEntry.COLUMN_CREATORE_CURATORE+") REFERENCES "+CuratoreMusealeEntry.TABLE_NAME+"("+CuratoreMusealeEntry.COLUMN_ID+"),"+
                        "FOREIGN KEY("+VisitaEntry.COLUMN_GUIDA+") REFERENCES "+GuidaEntry.TABLE_NAME+"("+GuidaEntry.COLUMN_ID+"))";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + VisitaEntry.TABLE_NAME;
    }

    public static class ZonaEntry implements BaseColumns{
        public static final String TABLE_NAME = "zona";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TIPO = "tipo";
        public static final String COLUMN_DENOMINAZIONE = "denominazione";
        public static final String COLUMN_DESCRIZIONE = "descrizione";
        public static final String COLUMN_LATITUDINE = "latitudine";
        public static final String COLUMN_LONGITUDINE = "longitudine";
        public static final String COLUMN_LUOGO = "luogo";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ZonaEntry.TABLE_NAME + " (" +
                        ZonaEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        ZonaEntry.COLUMN_TIPO + " INTEGER," +
                        ZonaEntry.COLUMN_DENOMINAZIONE + " TEXT," +
                        ZonaEntry.COLUMN_DESCRIZIONE + " TEXT," +
                        ZonaEntry.COLUMN_LATITUDINE + " REAL," +
                        ZonaEntry.COLUMN_LONGITUDINE + " REAL," +
                        ZonaEntry.COLUMN_LUOGO + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ZonaEntry.TABLE_NAME;
    }


    public static class VisitaOperaEntry implements BaseColumns{
        public static final String TABLE_NAME = "visita_opera";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_VISITA = "visita";
        public static final String COLUMN_OPERA = "opera";
        public static final String COLUMN_ORDINE = "ordine";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + VisitaOperaEntry.TABLE_NAME + " (" +
                        VisitaOperaEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        VisitaOperaEntry.COLUMN_VISITA + " INTEGER," +
                        VisitaOperaEntry.COLUMN_OPERA + " INTEGER," +
                        VisitaOperaEntry.COLUMN_ORDINE + " INTEGER,"+
                        "FOREIGN KEY("+VisitaOperaEntry.COLUMN_VISITA+") REFERENCES "+VisitaEntry.TABLE_NAME+"("+VisitaEntry.COLUMN_ID+"),"+
                        "FOREIGN KEY("+VisitaOperaEntry.COLUMN_OPERA+") REFERENCES "+OperaEntry.TABLE_NAME+"("+OperaEntry.COLUMN_ID+"))";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + VisitaOperaEntry.TABLE_NAME;
    }

}
