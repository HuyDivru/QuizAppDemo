package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quizapp.adapter.CategoryAdapter;
import com.example.quizapp.databinding.ActivityMainBinding;
import com.example.quizapp.model.CategoryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    CircleImageView categoryImage;
    EditText inputCategoryName;
    Button uploadCategory;

    View fecthImage;
    Dialog dialog;
    Uri imageUri;
    int i = 0;
    ProgressDialog progressDialog;

    //
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;


    boolean isDialogShow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        //
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        //

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_category_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("please wait");

        uploadCategory = dialog.findViewById(R.id.button_upload);
        inputCategoryName = dialog.findViewById(R.id.edit_cateegory);
        categoryImage = dialog.findViewById(R.id.profile_image);
        fecthImage = dialog.findViewById(R.id.view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        list = new ArrayList<>();
        binding.recyclerview.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(this, list);

        //lấy dữ liệu của firebase
        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(new CategoryModel(
                                dataSnapshot.child("categoryName").getValue().toString(),
                                dataSnapshot.child("categoryImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Category not exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDialogShow) {
                    dialog.show();
                    isDialogShow=true;
                }
            }
        });

        fecthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /**
                 * Content Provider: Trong Android, Content Provider là một thành phần quan trọng
                 * của cơ sở dữ liệu liên quan đến ứng dụng. Nó cung cấp giao diện để truy cập và
                 * chia sẻ dữ liệu giữa các ứng dụng khác nhau.
                 * Content Provider thường được sử dụng để lưu trữ và chia sẻ dữ liệu như danh bạ
                 * , tin nhắn văn bản, ảnh và video.
                 */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputCategoryName.getText().toString();
                if (imageUri == null) {
                    Toast.makeText(MainActivity.this, "please upload category image", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    inputCategoryName.setError("Enter category name");
                } else {
                    if (!isFinishing()) {
                        progressDialog.show();
                        uploadData();
                    }
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogShow=false;
            }
        });
        binding.recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void uploadData() {
        if(imageUri!=null) {
            final StorageReference reference = storage.getReference().child("category").child(new Date().getTime() + "");

            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            CategoryModel categoryModel = new CategoryModel();

                            categoryModel.setCategoryName(inputCategoryName.getText().toString());
                            categoryModel.setSetNum(0);
                            categoryModel.setCategoryImage(uri.toString());
                            String uniqueKey=database.getReference().child("category").push().getKey();
//                            database.getReference().child("categories").child("category"+i++)
                            database.getReference().child("categories").child(uniqueKey)
                                    .setValue(categoryModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Uploaded Sucesss", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 )  {
            if(data != null){
                imageUri = data.getData();
                categoryImage.setImageURI(imageUri);
            }


        }
    }
}
