package com.adam.annuairechiali.Activity;

import com.adam.annuairechiali.R;

import java.util.Arrays;
import java.util.List;

public class AdvancedExampleCountryPOJO {

    private String title;
    private int icon;

    public AdvancedExampleCountryPOJO(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static List<AdvancedExampleCountryPOJO> getExampleDataset() {
        return Arrays.asList(
                new AdvancedExampleCountryPOJO("Tout", R.drawable.filter),
                new AdvancedExampleCountryPOJO("GROUPE CHIALI", R.drawable.tie),
                new AdvancedExampleCountryPOJO("CHIALI TUBES", R.drawable.pipe6),
                new AdvancedExampleCountryPOJO("CHIALI SERVICES", R.drawable.hammer),
                new AdvancedExampleCountryPOJO("CHIALI PROFIPLAST", R.drawable.door),
                new AdvancedExampleCountryPOJO("CHIALI TRADING", R.drawable.sale),
                new AdvancedExampleCountryPOJO("CHIALI ACADEMIE", R.drawable.graduated),
                new AdvancedExampleCountryPOJO("ALTIM", R.drawable.farm),
                new AdvancedExampleCountryPOJO("CHIALI HUILERIE", R.drawable.food)
        );
    }
}
