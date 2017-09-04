package com.myrecipebook.myrecipebook.models;
import java.util.List;

/**
 * Created by Sonia on 11/08/17.
 */

public class RecipeFilter {

    public boolean isApetizer;
    public boolean isFDish;
    public boolean isSDish;
    public boolean isDessert;
    public boolean intolerances;
    public boolean isUnique;

    public int selectedTime = 0;

    public int selectedDifficulty = 0;

    public String recipeName;

    public List<String> ingredientsList;
    public List<String> intolerance;
}
