package uqac.dim.recipeplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        generateViewData(recipes.get(0));
    }

    private List<Recipe> loadRecipes(){
        List<Recipe> data = new ArrayList<Recipe>();
        data.add(generateRecipe());
        return data;
    }

    private Recipe generateRecipe(){
        Ingredient tempIng1 = new Ingredient(0,"Vodka","Alcohol",20.7f);
        Ingredient tempIng2 = new Ingredient(1,"Rum","Alcohol",25.5f);
        Ingredient tempIng3 = new Ingredient(2,"Coca Cola","Soft Drink",2.45f);

        List<Ingredient> data = new ArrayList<Ingredient>();
        data.add(tempIng1);
        data.add(tempIng2);
        data.add(tempIng3);

        Recipe tempRec = new Recipe(0,"Test Drink","- Add One oz of Rum \n- Add One oz of Vodka \n- Add One can of Coca Cola \n- Mix and Drink with ice",data);

        return tempRec;
    }

    private void generateViewData(Recipe data){

        View view = super.getLayoutInflater().inflate(R.layout.small_item_template, null);
        ((TextView)view.findViewById(R.id.drinkName)).setText(data.getName());
        view.setId(data.getId());
        view.setOnClickListener(favoriteListener);
        ((LinearLayout)findViewById(R.id.favorites)).addView(view);
    }

    private View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe temp = findRecipe(view.getId());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = new Intent(RecipeActivity.this,ShowRecipeActivity.class);
            intent.putExtra("Recipe",temp);
            startActivity(intent);

        }
    };

    private Recipe findRecipe(int id){
        for (Recipe r: recipes) {
            if(r.getId() == id){
                return r;
            }
        }
        return null;
    }

}
