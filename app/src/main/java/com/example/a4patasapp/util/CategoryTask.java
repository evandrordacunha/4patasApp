package com.example.a4patasapp.util;


import android.os.AsyncTask;

import com.example.a4patasapp.model.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {

    private final FirebaseFirestore db;

    /*INSTANCIANDO O CONTEXTO*/
    public CategoryTask( FirebaseFirestore db) {
        this.db = db;
      //  this.context = new WeakReference<>(context);
    }

    @Override
    protected List<Category> doInBackground(String... strings) {
        return null;
    }

}
