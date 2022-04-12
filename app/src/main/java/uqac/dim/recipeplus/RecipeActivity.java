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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uqac.dim.recipeplus.database.DatabaseObject;

public class RecipeActivity extends AppCompatActivity {
    private List<Recipe> recipes = new ArrayList<Recipe>();
    private DatabaseObject database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        try {
            database = new DatabaseObject();
            recipes = loadRecipes();
            generateViewData(recipes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private List<Recipe> loadRecipes(){
        ResultSet rs = null;
        List<Recipe> data = new ArrayList<Recipe>();
        try {
            rs = database.getAllRecipes(false);

            while(rs.next()){

                data.add(new Recipe(rs.getInt(1),rs.getString(2),rs.getString(3),new ArrayList<Ingredient>()));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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

    private void generateViewData(List<Recipe> data){

        for(Recipe r:data){
            View view = super.getLayoutInflater().inflate(R.layout.small_item_template, null);
            ((TextView)view.findViewById(R.id.drinkName)).setText(r.getName());
            view.setId(r.getId());
            view.setOnClickListener(favoriteListener);
            ((LinearLayout)findViewById(R.id.favorites)).addView(view);
        }
    }

    private View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe temp = findRecipe(view.getId());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = new Intent(RecipeActivity.this,ShowRecipeActivity.class);
            intent.putExtra("Recipe",temp);
            intent.putExtra("Database",database);
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
