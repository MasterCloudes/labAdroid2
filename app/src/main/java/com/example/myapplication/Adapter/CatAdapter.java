package com.example.myapplication.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DAO.CatDAO;
import com.example.myapplication.DTO.CatDTO;
import com.example.myapplication.R;


import java.util.ArrayList;


public class CatAdapter
       extends RecyclerView.Adapter<CatAdapter.MyViewHolder> {
   Context context;
   ArrayList<CatDTO> list;
   CatDAO catDAO;
   public CatAdapter(Context context, ArrayList<CatDTO> list) {
       this.context = context;
       this.list = list;
       catDAO = new CatDAO(context);
   }


   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //return null;
       LayoutInflater inflater = ((Activity) context).getLayoutInflater();
       View v = inflater.inflate(R.layout.item_category, parent, false);
       return new MyViewHolder(v);
   }


   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       //lấy thông tin của 1 dòng trong list
       CatDTO catDTO = list.get(position);
       // vắn vào holder
       holder.tv_id.setText(String.valueOf(catDTO.getId()));
       holder.tv_name.setText(catDTO.getName());
       // nút sửa, nút xoá
       holder.img_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // code xử lý khi click vào nút sửa
               updateRow(catDTO, position);
           }
       });
       holder.img_delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //code xử lý khi click vào nút xoá
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (catDAO.deleteRow(catDTO.getId())) {
                            list.remove(catDTO);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Hủy", null);
                builder.show();
           }
       });


   }


   @Override
   public int getItemCount() {
       return list.size();
   }

   void updateRow (CatDTO objCat, int index){
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
       // load layout cho dialog
       LayoutInflater inflater = ((Activity) context).getLayoutInflater();
       View v = inflater.inflate(R.layout.layout_dialog_edit, null);
       builder.setView(v);// gắn layout vào dialog
       builder.setCancelable(false);
// hien thi dialog
       AlertDialog dialog = builder.create();
       // anh xa view
       EditText ed_name = v.findViewById(R.id.ed_name);
       ed_name.setText(objCat.getName()); // hien thi du lieu truoc khi edit
       Button btn_save = v.findViewById(R.id.btn_save);
       btn_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // goi code update
               objCat.setName(ed_name.getText().toString()); // cap nhat lai ten cat
               if (catDAO.updateRow(objCat)) { // ghi vào csdl
                   list.set(index, objCat); // update danh sach tren recyclerview
                   notifyDataSetChanged(); // thong bao cho adapter update giao dien
                   Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
               }
               dialog.dismiss();// tat dialog
           }
       });

       dialog.show();
   }


   public static class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tv_id, tv_name;
        ImageView img_edit, img_delete; // khai báo biến để ánh xạ view 1 dòng
       public MyViewHolder(View itemView) {
           super(itemView);
           tv_id = itemView.findViewById(R.id.tv_id);
           tv_name = itemView.findViewById(R.id.tv_name);
           img_edit = itemView.findViewById(R.id.img_edit);
           img_delete = itemView.findViewById(R.id.img_delete);
       }
   }
}