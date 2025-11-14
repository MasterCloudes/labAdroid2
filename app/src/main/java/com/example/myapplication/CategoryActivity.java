package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CatAdapter;
import com.example.myapplication.DAO.CatDAO;
import com.example.myapplication.DTO.CatDTO;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView rcCategory;
    private CatAdapter catAdapter;
    private ArrayList<CatDTO> categoryList;
    private CatDAO catDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rcCategory = findViewById(R.id.rc_category);
        catDAO = new CatDAO(this);

        categoryList = catDAO.getAllCat();
        catAdapter = new CatAdapter(this, categoryList);

        rcCategory.setAdapter(catAdapter);
    }
}
