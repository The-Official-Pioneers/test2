package it.uniba.pioneers.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "e_culture_perm.db";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbContract.AreaEntry.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.CuratoreMusealeEntry.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.GuidaEntry.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.VisitatoreEntry.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.OperaEntry.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.VisitaEntry.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.VisitaOperaEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DbContract.AreaEntry.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.CuratoreMusealeEntry.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.GuidaEntry.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.VisitatoreEntry.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.OperaEntry.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.VisitaEntry.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DbContract.VisitaOperaEntry.SQL_DELETE_ENTRIES);

        onCreate(sqLiteDatabase);
    }
}
