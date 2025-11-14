package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CatAdapter;
import com.example.myapplication.DAO.CatDAO;
import com.example.myapplication.DTO.CatDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView rcCategory;
    private CatAdapter catAdapter;
    private ArrayList<CatDTO> categoryList;
    private CatDAO catDAO;
    private FloatingActionButton fabAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rcCategory = findViewById(R.id.rc_category);
        fabAddCategory = findViewById(R.id.fab_add_category);
        catDAO = new CatDAO(this);

        categoryList = catDAO.getAllCat();
        catAdapter = new CatAdapter(this, categoryList);

        rcCategory.setAdapter(catAdapter);

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_add, null);
        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        EditText edName = view.findViewById(R.id.ed_name);
        Button btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
                    return;
                }

                CatDTO newCategory = new CatDTO();
                newCategory.setName(name);

                long result = catDAO.addCat(newCategory);
                if (result > 0) {
                    categoryList.clear();
                    categoryList.addAll(catDAO.getAllCat());
                    catAdapter.notifyDataSetChanged();
                    Toast.makeText(CategoryActivity.this, "Thêm thể loại thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(CategoryActivity.this, "Thêm thể loại thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
