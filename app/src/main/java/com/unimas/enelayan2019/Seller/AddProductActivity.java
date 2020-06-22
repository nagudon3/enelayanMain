package com.unimas.enelayan2019.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.unimas.enelayan2019.AccountActivity;
import com.unimas.enelayan2019.MainActivity;
import com.unimas.enelayan2019.Model.Post;
import com.unimas.enelayan2019.Model.Product;
import com.unimas.enelayan2019.Model.Users;
import com.unimas.enelayan2019.PostActivity;
import com.unimas.enelayan2019.R;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView productImage, backBtn;
    private EditText productName, pricePerKg, sellingArea, amountAvailable;
    private Spinner category;
    private CheckBox isCod, isSelfPickup;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private Product product;
    private Users users;
    String categorySelected = "";
    private Boolean checkCod = false;
    private Boolean checkPick = false;
    private TextView selectImageText;
    private Uri ImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productImage = (ImageView) findViewById(R.id.productImage);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        productName = (EditText) findViewById(R.id.productName);
        pricePerKg = (EditText) findViewById(R.id.pricePerKg);
        sellingArea = (EditText) findViewById(R.id.sellingArea);
        amountAvailable = (EditText) findViewById(R.id.amountAvailable);
        category = (Spinner) findViewById(R.id.categorySpinner);
        isCod = (CheckBox) findViewById(R.id.isCod);
        isSelfPickup = (CheckBox) findViewById(R.id.isPickup);
        submitButton = (Button) findViewById(R.id.submitProduct);
        selectImageText = (TextView) findViewById(R.id.selectImageText);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        category.setOnItemSelectedListener(this);
        List<String>categories = new ArrayList<String>();
        categories.add("Fresh Water Fish");
        categories.add("Sea Fish");
        categories.add("Prawn");
        categories.add("Variants of Seafood");
        categories.add("Dried Seafood");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
            }
        });
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCod.isChecked()){
                    checkCod = true;
                }
                if (isSelfPickup.isChecked()){
                    checkPick = true;
                }
                String pCategory = categorySelected;
                String pName = productName.getText().toString();
                String pArea = sellingArea.getText().toString();
                String amount = amountAvailable.getText().toString();
                String imageURL = ImageURI.toString();

//                addProduct();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermissions();
            }
        });
    }

    private void checkAndRequestForPermissions() {
        if (ContextCompat.checkSelfPermission(AddProductActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(), "Please accept the permission to access your storage", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(AddProductActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                openGallery();
            }
        }else {
            openGallery();

    }
    }

    private void openGallery(){
        CropImage.activity().start(AddProductActivity.this);
    }


    private void addProduct() {
        DatabaseReference reference = database.getReference().child("Products").child(firebaseUser.getUid()).push();



//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("product_images").child(firebaseUser.getUid());
//        final StorageReference imageFilePath = storageReference.child(pName);
//        imageFilePath.putFile(ImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String imageDownloadLink = uri.toString();

//                        Post post = new Post(postTitle.getText().toString(),
//                                postDescription.getText().toString(),
//                                imageDownloadLink,
//                                user.getUid(),
//                                user.getDisplayName(),
//                                user.getPhotoUrl().toString());
//
//                        addPost(post);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
////                        addPostButton.setVisibility(View.VISIBLE);
////                        postProgress.setVisibility(View.INVISIBLE);
//                    }
//                });
////        reference.setValue()
//            }
//        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, categorySelected, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                ImageURI = result.getUri();
                productImage.setImageURI(ImageURI);
                selectImageText.setVisibility(View.INVISIBLE);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(AddProductActivity.this, "Possible error occurred is " +e, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
