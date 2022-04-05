package uqac.dim.recipeplus;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipes = loadRecipes();
    }

    private List<Recipe> loadRecipes(){

        return null;
    }


}
