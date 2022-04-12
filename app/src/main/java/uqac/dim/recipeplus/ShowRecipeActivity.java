package uqac.dim.recipeplus;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uqac.dim.recipeplus.database.DatabaseObject;

public class ShowRecipeActivity extends AppCompatActivity {

    //Gestion Image: Picasso / Glide

    private DatabaseObject database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_template);
        database = (DatabaseObject) getIntent().getSerializableExtra("Database");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setPageData((Recipe) getIntent().getSerializableExtra("Recipe"));
    }

    private void setPageData (Recipe r){
        if(r != null){
            ((TextView)findViewById(R.id.drinkName)).setText(r.getName());
            //((ImageView)view.findViewById(R.id.drinkImage)).setImageResource();
            ((TextView)findViewById(R.id.drinkDesc)).setText(r.getDesc());
            r.setIngredients(getIngredients(r.getId()));

            for(Ingredient ing:r.getIngredients()){
                View tempView = getLayoutInflater().inflate(R.layout.ingredient_template, null,false);
                ((TextView)tempView.findViewById(R.id.ingredientName)).setText("- " + ing.getName() + " :");
                ((TextView)tempView.findViewById(R.id.ingredientDesc)).setText("[" + ing.getDesc() + "] :");
                ((TextView)tempView.findViewById(R.id.ingredientPrice)).setText(Float.toString(ing.getAvgPrice()) + "$");

                ((LinearLayout)findViewById(R.id.drinkIngredient)).addView(tempView);
            }
        }
    }

    private ArrayList<Ingredient> getIngredients(int id){

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        try {
            ResultSet ing = database.getIngredientsAssociatedWithRecipe(id);

            while (ing.next()){
                ingredients.add(new Ingredient(ing.getInt(1),ing.getString(2),ing.getString(3),ing.getFloat(4)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ingredients;
    }
}
