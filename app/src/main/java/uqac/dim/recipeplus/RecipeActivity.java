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

                data.add(new Recipe(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),new ArrayList<Ingredient>()));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return data;
    }

    private void generateViewData(List<Recipe> data){

        for(Recipe r:data){
            View view = super.getLayoutInflater().inflate(R.layout.small_item_template, null);
            ((TextView)view.findViewById(R.id.drinkName)).setText(r.getName());
            ((TextView)view.findViewById(R.id.drinkDesc)).setText(r.getDesc());

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
