package com.myrecipebook.myrecipebook;

import java.util.List;

/**
 * Created by Sonia on 11/08/17.
 */

public class  InfoDto {

    boolean isApetizer;
    boolean isFDish;
    boolean isSDish;
    boolean isDessert;
    boolean intolerances;

    int selectedTime = 0;

    int selectedDifficulty = 0;

    String recipeName;

    List<String> ingredientsList;
    List<String> intolerance;

    List<Recipe> resultRecipes;
}
