package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShopItemDetailsActivity extends AppCompatActivity {

    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Intent intent = getIntent();
        if(intent != null){
            String name = intent.getStringExtra("name");
            Double price = intent.getDoubleExtra("price",0);
            pos  = intent.getIntExtra("position",-1);
            if(name!=null){
                EditText editTextName = findViewById(R.id.edittext_item_name);
                editTextName.setText(name);
                EditText editTextPrice = findViewById(R.id.edittext_item_price);
                editTextPrice.setText(price.toString());
            }
        }

        Button buttonOk = findViewById(R.id.button_item_detail);//下面是完成添加逻辑
        buttonOk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText editTextItemName = findViewById(R.id.edittext_item_name);
                EditText editTextItemPrice = findViewById(R.id.edittext_item_price);

                Intent intent = new Intent();
                intent.putExtra("name",editTextItemName.getText().toString());
                intent.putExtra("price",editTextItemPrice.getText().toString());
                intent.putExtra("position",pos);
                setResult(Activity.RESULT_OK,intent);
                ShopItemDetailsActivity.this.finish();
            }
        });
    }
}