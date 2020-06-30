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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.unimas.enelayan2019.AccountActivity;
import com.unimas.enelayan2019.MainActivity;
import com.unimas.enelayan2019.Model.Fisherman;
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
    private Boolean pickImage = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productImage = (ImageView) findViewById(R.id.productImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                submitButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
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
                String price= pricePerKg.getText().toString();

                if (pickImage.equals(false)){
                    Toast.makeText(AddProductActivity.this, "Please upload an image for the product!", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (!isCod.isChecked() && !isSelfPickup.isChecked()){
                    Toast.makeText(AddProductActivity.this, "Please select at least one payment method!", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (pCategory.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "Please select the category", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (pName.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "Please enter your product name!", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (pArea.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "Please enter your selling area!", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (amount.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "Please enter amount available for the product!", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (price.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "Please enter the price for each kilogram!", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else {
                    addProduct(pCategory, pName, pArea, amount, price);
                }
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermissions();
            }
        });
    }

    private void addProduct(final String pCategory, final String pName, final String pArea, final String amount, final String price) {
        final DatabaseReference productReference = database.getReference().child("Products").push();
        DatabaseReference userReference = database.getReference().child("Users").child(firebaseUser.getUid());

        final String sId = firebaseUser.getUid();
        final String sImage = firebaseUser.getPhotoUrl().toString();
        final String productKey = productReference.getKey();

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);
                final String sName = users.getName();
                final String sPhone = users.getPhone();
                final String sAddress = users.getAddress();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("product_images").child(firebaseUser.getUid());
                final StorageReference imageFilePath = storageReference.child(pName);
                imageFilePath.putFile(ImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageDownloadLink = uri.toString();

                                product = new Product(sId, sImage, sName, sPhone, sAddress, pCategory, pName, price, amount,
                                        checkCod, checkPick, imageDownloadLink, pArea);
                                product.setProductId(productKey);

                                productReference.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(AddProductActivity.this, "Product inserted successfully!", Toast.LENGTH_SHORT).show();
                                            submitButton.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            finish();
                                        }else {
                                            String e = task.getException().toString();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            submitButton.setVisibility(View.VISIBLE);
                                            Toast.makeText(AddProductActivity.this, "Product fail to be inserted. Problem :" + e, Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySelected = parent.getItemAtPosition(position).toString();

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
                pickImage = true;
                productImage.setImageURI(ImageURI);
                selectImageText.setVisibility(View.INVISIBLE);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(AddProductActivity.this, "Possible error occurred is " +e, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
