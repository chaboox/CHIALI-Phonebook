package com.adam.annuairechiali.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.adam.annuairechiali.R;
import com.adam.annuairechiali.Activity.DSListView;
//import com.adam.annuairechiali.Activity.DSListView;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AdvancedExampleActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar)
            supportActionBar.hide();

        setContentView(R.layout.advanced_example_activity);

        // Prepare dataset
        List<AdvancedExampleCountryPOJO> exampleDataSet = AdvancedExampleCountryPOJO.getExampleDataset();

        // Create adapter with our dataset
        ArrayAdapter<AdvancedExampleCountryPOJO> adapter = new AdvancedExampleCountryAdapter(
                this, R.layout.advanced_example_country_list_item, exampleDataSet);

        // Set adapter to our DSListView
        DSListView<AdvancedExampleCountryPOJO> pickerView = findViewById(R.id.ds_county_list);
        pickerView.setAdapter(adapter);

    }

}
