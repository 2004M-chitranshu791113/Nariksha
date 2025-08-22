package com.example.nariksha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class ContactsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contactsDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";

    public ContactsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public void deleteContact(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_PHONE + "=?", new String[]{phone});
        db.close();
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
                contactList.add(new Contact(name, phone));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }
}
