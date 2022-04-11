package uqac.dim.recipeplus;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowRecipeActivity extends AppCompatActivity {

    //Gestion Image: Picasso / Glide

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_template);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setPageData((Recipe) getIntent().getSerializableExtra("Recipe"));
    }

    private void setPageData (Recipe r){
        if(r != null){
            ((TextView)findViewById(R.id.drinkName)).setText(r.getName());
            //((ImageView)view.findViewById(R.id.drinkImage)).setImageResource();
            ((TextView)findViewById(R.id.drinkDesc)).setText(r.getDesc());

            for(Ingredient ing:r.getIngredients()){
                View tempView = getLayoutInflater().inflate(R.layout.ingredient_template, null,false);
                ((TextView)tempView.findViewById(R.id.ingredientName)).setText("- " + ing.getName() + " :");
                ((TextView)tempView.findViewById(R.id.ingredientDesc)).setText("[" + ing.getDesc() + "] :");
                ((TextView)tempView.findViewById(R.id.ingredientPrice)).setText(Float.toString(ing.getAvgPrice()) + "$");

                ((LinearLayout)findViewById(R.id.drinkIngredient)).addView(tempView);
            }
        }
    }
}
