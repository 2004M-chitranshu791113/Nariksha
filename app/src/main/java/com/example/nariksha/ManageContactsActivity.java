package com.example.nariksha;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import androidx.annotation.NonNull;


import java.util.ArrayList;

public class ManageContactsActivity extends AppCompatActivity {

    private EditText editName, editPhone;
    private Button btnAdd;
    private ListView listContacts;

    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayAdapter<Contact> adapter;
    private ContactsDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contacts);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Emergency Contacts"); // optional, sets title
        }

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnAdd = findViewById(R.id.btnAdd);
        listContacts = findViewById(R.id.listContacts);

        dbHelper = new ContactsDBHelper(this);
        loadContacts();

        btnAdd.setOnClickListener(v -> addContact());
        listContacts.setOnItemLongClickListener(deleteOnLongPress());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // closes this activity and goes back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadContacts() {
        contacts = dbHelper.getAllContacts();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        listContacts.setAdapter(adapter);
    }

    private void addContact() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Enter both name and phone", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() < 8) {
            Toast.makeText(this, "Phone number looks too short", Toast.LENGTH_SHORT).show();
            return;
        }

        Contact contact = new Contact(name, phone);
        dbHelper.addContact(name, phone);

        contacts.add(contact);
        adapter.notifyDataSetChanged();

        editName.setText("");
        editPhone.setText("");
        hideKeyboard();
    }

    private AdapterView.OnItemLongClickListener deleteOnLongPress() {
        return (parent, view, position, id) -> {
            Contact contact = contacts.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Delete contact?")
                    .setMessage(contact.toString())
                    .setPositiveButton("Delete", (d, w) -> {
                        dbHelper.deleteContact(contact.getPhone());
                        contacts.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        };
    }

    private void hideKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
