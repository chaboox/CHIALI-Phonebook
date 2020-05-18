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
                new AdvancedExampleCountryPOJO("Tubes", R.drawable.pipe6),
                new AdvancedExampleCountryPOJO("Academie", R.drawable.graduated),
                new AdvancedExampleCountryPOJO("Groupe", R.drawable.tie),
                new AdvancedExampleCountryPOJO("Services", R.drawable.hammer),
                new AdvancedExampleCountryPOJO("Profiplast", R.drawable.door),
                new AdvancedExampleCountryPOJO("Nawafid", R.drawable.window),
                new AdvancedExampleCountryPOJO("Trading", R.drawable.sale),
                new AdvancedExampleCountryPOJO("Altim", R.drawable.farm),
                new AdvancedExampleCountryPOJO("Huilerie", R.drawable.food)
        );
    }
}
