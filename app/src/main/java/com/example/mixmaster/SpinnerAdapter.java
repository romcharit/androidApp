package com.example.mixmaster;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.mixmaster.model.Model;

import java.util.List;


public class SpinnerAdapter {


    // Cocktail categories spinner adapter
    public static ArrayAdapter<String> setCocktailCategoriesSpinner(Context context)
    {
        List<String> cocktailCategories = Model.getInstance().getCocktailCategories();
        ArrayAdapter<String> CocktailCategoriesSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,cocktailCategories);
        CocktailCategoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        return CocktailCategoriesSpinnerAdapter;
    }


    public static void setCountrySpinner(Context context, Model.Listener<ArrayAdapter<String>> listener)
    {
        Model.getInstance().getCountries(countries-> {
            ArrayAdapter<String> countrySpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, countries);
            countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listener.onComplete(countrySpinnerAdapter);
        });
    }

}
