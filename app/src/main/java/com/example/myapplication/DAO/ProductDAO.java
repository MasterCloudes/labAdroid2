package com.example.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DTO.ProductDTO;
import com.example.myapplication.DpHelper.MyDbHelper;

import java.util.ArrayList;

public class ProductDAO {
    private MyDbHelper dbHelper;
    private SQLiteDatabase db;

    public ProductDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long addProduct(ProductDTO productDTO) {
        ContentValues values = new ContentValues();
        values.put("name", productDTO.getName());
        values.put("price", productDTO.getPrice());
        values.put("id_cat", productDTO.getId_cat());
        return db.insert("tb_product", null, values);
    }

    public ArrayList<ProductDTO> getAllProducts() {
        ArrayList<ProductDTO> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_product", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ProductDTO productDTO = new ProductDTO();
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int priceIndex = cursor.getColumnIndex("price");
                int idCatIndex = cursor.getColumnIndex("id_cat");

                if (idIndex != -1) {
                    productDTO.setId(cursor.getInt(idIndex));
                }
                if (nameIndex != -1) {
                    productDTO.setName(cursor.getString(nameIndex));
                }
                if (priceIndex != -1) {
                    productDTO.setPrice(cursor.getDouble(priceIndex));
                }
                if (idCatIndex != -1) {
                    productDTO.setId_cat(cursor.getInt(idCatIndex));
                }
                list.add(productDTO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int updateProduct(ProductDTO productDTO) {
        ContentValues values = new ContentValues();
        values.put("name", productDTO.getName());
        values.put("price", productDTO.getPrice());
        values.put("id_cat", productDTO.getId_cat());
        return db.update("tb_product", values, "id=?", new String[]{String.valueOf(productDTO.getId())});
    }

    public int deleteProduct(int id) {
        return db.delete("tb_product", "id=?", new String[]{String.valueOf(id)});
    }
}
