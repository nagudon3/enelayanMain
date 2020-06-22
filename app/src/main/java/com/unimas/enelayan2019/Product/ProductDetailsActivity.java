package com.unimas.enelayan2019.Product;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unimas.enelayan2019.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView productName, productPrice, sellerName, sellingArea;
    private CircleImageView sellerImage;
    private ImageView productImage;

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

        String prodImg = getIntent().getExtras().getString("productImage");
        String prodName = getIntent().getExtras().getString("productName");
        String prodPrice = getIntent().getExtras().getString("productPrice");
        String sName = getIntent().getExtras().getString("sellerName");
        String sArea = getIntent().getExtras().getString("sellingArea");
        String sImage = getIntent().getExtras().getString("sellerImage");

        Glide.with(ProductDetailsActivity.this).load(prodImg).into(productImage);
        Glide.with(ProductDetailsActivity.this).load(sImage).into(sellerImage);
        productName.setText(prodName);
        sellerName.setText(sName);
        productPrice.setText(prodPrice);
        sellingArea.setText(sArea);

    }
}
