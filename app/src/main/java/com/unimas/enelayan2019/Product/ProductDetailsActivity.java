package com.unimas.enelayan2019.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
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
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimas.enelayan2019.Model.Cart;
import com.unimas.enelayan2019.Model.Product;
import com.unimas.enelayan2019.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailsActivity extends AppCompatActivity{
    private TextView productName, productPrice, sellerName, sellingArea;
    private CircleImageView sellerImage;
    private ImageView productImage;
    private Dialog addToCartDialog;
    private Button addToCartBtn;

//    for dialog
    private Spinner paymentOption;
    private TextView amountAvailable;
    private Button addBtn;
    private EditText quantityBought;
    private CheckBox isCod;
    private CheckBox isPickup;
    String prodName="";
    String prodPrice="";
    String prodImg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        sellerImage = findViewById(R.id.sellerImage);
        sellingArea = findViewById(R.id.sellingArea);
        sellerName = findViewById(R.id.sellerName);
        productPrice = findViewById(R.id.productPricePerKg);
        addToCartBtn = findViewById(R.id.addToCartBtn);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartDialog.show();
            }
        });

        prodImg = getIntent().getExtras().getString("productImage");
        prodName = getIntent().getExtras().getString("productName");
        prodPrice = getIntent().getExtras().getString("productPrice");
        String sName = getIntent().getExtras().getString("sellerName");
        String sArea = getIntent().getExtras().getString("sellingArea");
        String sImage = getIntent().getExtras().getString("sellerImage");
        String prodId = getIntent().getExtras().getString("productId");

        Glide.with(ProductDetailsActivity.this).load(prodImg).into(productImage);
        Glide.with(ProductDetailsActivity.this).load(sImage).into(sellerImage);
        productName.setText(prodName);
        sellerName.setText(sName);
        productPrice.setText("RM "+prodPrice+" /Kg");
        sellingArea.setText(sArea);

        initAddToCart(prodId);

    }

    private void initAddToCart(String prodId) {
        addToCartDialog = new Dialog(this);
        addToCartDialog.setContentView(R.layout.add_product_to_cart);
        addToCartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addToCartDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        addToCartDialog.getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL;

        addBtn = addToCartDialog.findViewById(R.id.addButton);
        quantityBought = addToCartDialog.findViewById(R.id.amountAdded);
        amountAvailable = addToCartDialog.findViewById(R.id.amountAvailable);
        isCod = addToCartDialog.findViewById(R.id.isCod);
        isPickup = addToCartDialog.findViewById(R.id.isPickup);
        final double[] parsedAmount = {0.00};


        final FirebaseDatabase database = FirebaseDatabase    .getInstance();
        final DatabaseReference productRef = database.getReference().child("Products").child(prodId);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                amountAvailable.setText("Amount available: "+product.getAmountAvailable()+"kg");
                String availableFromDb =  product.getAmountAvailable();
                parsedAmount[0] = Double.parseDouble(availableFromDb);
                if (dataSnapshot.child("cod").getValue().equals(true)){
                    isCod.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("pickup").getValue().equals(true)){
                    isPickup.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean pickup = false;
                Boolean cod = false;

                if (isCod.isChecked()){
                    cod = true;
                }
                if (isPickup.isChecked()){
                    pickup = true;
                }


                if (!quantityBought.getText().toString().isEmpty()){
//                    Toast.makeText(ProductDetailsActivity.this, "Please specify the amount!", Toast.LENGTH_SHORT).show();
                    String oAmount = quantityBought.getText().toString();
                    double pricePerKg = Double.parseDouble(prodPrice);
                    double fOAmount = Double.parseDouble(oAmount);
                    double price = fOAmount * pricePerKg;
                    String oPrice = Double.toString(price);

                    if (fOAmount > parsedAmount[0]){
                        Toast.makeText(ProductDetailsActivity.this, "Quantity selected cannot be more than available quantity."+parsedAmount[0], Toast.LENGTH_SHORT).show();
                    }else if (isCod.isChecked() && isPickup.isChecked()){
                        Toast.makeText(ProductDetailsActivity.this, "Pick only one payment method!", Toast.LENGTH_SHORT).show();
                    }else {
                        DatabaseReference cartRef = database.getReference().child("Cart").push();
                        Cart cart = new Cart(
                                FirebaseAuth.getInstance().getUid(),
                                prodName,
                                prodImg,
                                oAmount,
                                oPrice,
                                cod,
                                pickup
                                );

                        cartRef.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ProductDetailsActivity.this, "Product added to cart!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(ProductDetailsActivity.this, "Failed to add product to cart.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }else {
                    Toast.makeText(ProductDetailsActivity.this, "Please specify orders information!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
