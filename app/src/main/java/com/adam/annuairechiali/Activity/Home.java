package com.adam.annuairechiali.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;

import com.adam.annuairechiali.Model.Company;
import com.adam.annuairechiali.R;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ArrayList<Company> companies;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    private void populateCompany(){

    }

    private void initView(){
        gridLayout = findViewById(R.id.mainGrid);
    }
}
