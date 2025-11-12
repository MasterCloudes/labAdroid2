package com.example.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DTO.CatDTO;
import com.example.myapplication.DpHelper.MyDbHelper;

import java.util.ArrayList;

public class CatDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase db;


    public CatDAO(Context context){
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    // viết hàm thêm dữ liệu
    public int addCat (CatDTO catDTO){
        ContentValues values = new ContentValues();
        values.put("name", catDTO.getName());
        return (int) db.insert("tb_cat", null, values);
    }

    public ArrayList<CatDTO> getAllCat() {
        ArrayList<CatDTO> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_cat", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                CatDTO catDTO = new CatDTO();
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");

                if (idIndex != -1) {
                    catDTO.setId(cursor.getInt(idIndex));
                }
                if (nameIndex != -1) {
                    catDTO.setName(cursor.getString(nameIndex));
                }
                list.add(catDTO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
