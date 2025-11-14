package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.DAO.ProductDAO;
import com.example.myapplication.DTO.ProductDTO;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView rcProduct;
    private ProductAdapter productAdapter;
    private ArrayList<ProductDTO> productList;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        rcProduct = findViewById(R.id.rc_product);
        productDAO = new ProductDAO(this);

        // Add sample data
        ProductDTO product1 = new ProductDTO();
        product1.setName("iPhone 15 Pro Max");
        product1.setPrice(1299.99);
        product1.setId_cat(1);
        productDAO.addProduct(product1);

        ProductDTO product2 = new ProductDTO();
        product2.setName("MacBook Pro M3");
        product2.setPrice(2499.00);
        product2.setId_cat(2);
        productDAO.addProduct(product2);

        productList = productDAO.getAllProducts();
        productAdapter = new ProductAdapter(this, productList);

        rcProduct.setAdapter(productAdapter);
    }
}
