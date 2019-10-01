package com.example.bakingapp.Utils;

import com.example.bakingapp.Models.Recipes;
import com.example.bakingapp.Models.RecipesIngredients;
import com.example.bakingapp.Models.RecipesSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<Recipes> parseRecipesJson(String json) throws JSONException {


        JSONArray results = new JSONArray(json);

        ArrayList<Recipes> recipesArrayList = new ArrayList<>();

            for (int i = 0; i <= 3; i++) {
                JSONObject movieData = results.getJSONObject(i);
                Recipes recipe = new Recipes(
                        movieData.optString("name"),
                        movieData.optString("sId")
                );
                recipesArrayList.add(recipe);

            }

        return recipesArrayList;


    }

    public static ArrayList<RecipesSteps> parseStepsJson(String json, int recipesId ) throws JSONException {

        JSONArray root = new JSONArray(json);
        JSONObject jsonObject = root.getJSONObject(recipesId);
        JSONArray results = jsonObject.optJSONArray("steps");

        ArrayList<RecipesSteps> stepsArrayList = new ArrayList<>();

        for (int i = 0; i <= 9; i++) {
            JSONObject stepsData = results.getJSONObject(i);
            RecipesSteps steps = new RecipesSteps(
                    stepsData.optString("sId"),
                    stepsData.optString("shortDescription"),
                    stepsData.optString("videoURL"),
                    stepsData.getString("description")
            );
            stepsArrayList.add(steps);

        }

        return stepsArrayList;


    }

    public static ArrayList<RecipesIngredients> parseIngredientsJson(String json, int recipesId ) throws JSONException {

        JSONArray root = new JSONArray(json);
        JSONObject jsonObject = root.getJSONObject(recipesId);
        JSONArray results = jsonObject.optJSONArray("ingredients");

        ArrayList<RecipesIngredients> IngredientsArrayList = new ArrayList<>();

        for (int i = 0; i <= 9; i++) {
            JSONObject IngredientsData = results.getJSONObject(i);
            RecipesIngredients Ingredients = new RecipesIngredients(
                    IngredientsData.optString("ingredient"),
                    IngredientsData.optString("quantity"),
                    IngredientsData.optString("measure")
            );
            IngredientsArrayList.add(Ingredients);

        }

        return IngredientsArrayList;


    }
}
