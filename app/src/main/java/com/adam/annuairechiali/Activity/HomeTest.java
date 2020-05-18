package com.adam.annuairechiali.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.adam.annuairechiali.R;

public class HomeTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EditText editText = findViewById(R.id.search);
        try {
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_gray_24dp, 0, 0, 0);
        }
        catch (Exception e){
            Log.d("TAG", "initContactAdapter: ");
        }
    }
}
