package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CatAdapter;
import com.example.myapplication.DAO.CatDAO;
import com.example.myapplication.DTO.CatDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CatDAO catDAO;
    static String TAG = "zzzzzzzzzzzzzz";
    RecyclerView rc_cat;
    ArrayList<CatDTO> list;
    CatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //ánh xạ view
       rc_cat = findViewById(R.id.rc_cat);
       catDAO = new CatDAO(this);
       
       list = catDAO.getAllCat();
       adapter = new CatAdapter(this, list);
       rc_cat.setAdapter(adapter);

    }
}