package uqac.dim.recipeplus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RecipeActivity extends AppCompatActivity {
    private List<Recipe> recipes = new ArrayList<Recipe>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipes = loadRecipes();
        generateViewData(recipes.get(0));
        generateViewData(recipes.get(0));
        generateViewData(recipes.get(0));
    }

    private List<Recipe> loadRecipes(){
        List<Recipe> data = new ArrayList<Recipe>();
        data.add(generateRecipe());
        return data;
    }

    private Recipe generateRecipe(){
        Ingredient tempIng = new Ingredient(0,"Vodka","Strong Drink",20);
        List<Ingredient> data = new ArrayList<Ingredient>();
        data.add(tempIng);

        Recipe tempRec = new Recipe(0,"Shot","small shot",data);

        return tempRec;
    }

    private void generateViewData(Recipe data){

        View view = super.getLayoutInflater().inflate(R.layout.small_item_template, null);
        ((TextView)view.findViewById(R.id.drinkName)).setText(data.getName());

        ((LinearLayout)findViewById(R.id.favorites)).addView(view);
    }

}
