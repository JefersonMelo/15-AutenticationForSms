package com.jefmelo.autenticationforsms.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfigFirebase {

    private static DatabaseReference databaseReference;

    public static DatabaseReference getDatabaseReference(){

        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
