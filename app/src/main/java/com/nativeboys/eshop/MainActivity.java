package com.nativeboys.eshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    /*Static fields are executed before the constructor (before an instance of this class)
    now we bypass the following exception -> "Calls to setPersistenceEnabled() must be made before any other usage of FirebaseDatabase instance"*/
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
