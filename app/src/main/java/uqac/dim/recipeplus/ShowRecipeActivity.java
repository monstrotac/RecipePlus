package uqac.dim.recipeplus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import uqac.dim.recipeplus.database.DatabaseObject;

public class ShowRecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Gestion Image: Picasso / Glide

    private DatabaseObject database;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Recipe currentRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_template);
        try {
            database = new DatabaseObject();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        user = (User) getIntent().getSerializableExtra("User");
        database.setUser(user);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        currentRecipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        setPageData(currentRecipe);

        drawerLayout = findViewById(R.id.nav_drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setPageData (Recipe r){
        if(r != null){
            ((TextView)findViewById(R.id.drinkName)).setText(r.getName());
            //((ImageView)view.findViewById(R.id.drinkImage)).setImageResource();
            ((TextView)findViewById(R.id.drinkInstruct)).setText(r.getInstruc());
            ((TextView)findViewById(R.id.drinkDesc)).setText(r.getDesc());
            r.setIngredients(getIngredients(r.getId()));

            try {
                byte[] byteArray = database.getRecipeThumbnail(r.getId());
                ((ImageView)findViewById(R.id.drinkImage)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            for(Ingredient ing:r.getIngredients()){
                View tempView = getLayoutInflater().inflate(R.layout.ingredient_template, null,false);
                ((TextView)tempView.findViewById(R.id.ingredientName)).setText("- " + ing.getName() + " :");
                ((TextView)tempView.findViewById(R.id.ingredientDesc)).setText("[" + ing.getDesc() + "] :");
                ((TextView)tempView.findViewById(R.id.ingredientPrice)).setText(Float.toString(ing.getAvgPrice()) + "$");

                ((LinearLayout)findViewById(R.id.drinkIngredient)).addView(tempView);
            }
            try {
                ResultSet rs = database.getRecipeCreator(r.getCreatorId());
                while (rs.next()){
                    ((TextView)findViewById(R.id.username)).setText(rs.getString(1) + " " + rs.getString(2));
                }

                byte[] byteArray = database.getUserImage(r.getCreatorId());
                ((ImageView)findViewById(R.id.userImage)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if(user.getId() == r.getCreatorId()){
                View tempView = getLayoutInflater().inflate(R.layout.button_layout, null,false);
                LinearLayout v = (LinearLayout) findViewById(R.id.extraButton);
                v.addView(tempView);
            }
        }
    }

    public void editRecipe(){

    }

    public void deleteRecipe(){
        try {
            database.deleteRecipeWithId(currentRecipe.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private ArrayList<Ingredient> getIngredients(int id){


        System.out.println(id);

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        try {
            ResultSet ing = database.getRecipeIngredients(id);

            while (ing.next()){

                System.out.println(ing.getInt(1));
                ingredients.add(new Ingredient(ing.getInt(1),ing.getString(2),ing.getString(3),ing.getFloat(4)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println(ingredients);

        return ingredients;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){

            case R.id.nav_recipe:
                intent = new Intent(ShowRecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_myrecipe:
                intent = new Intent(ShowRecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("MyRecipe",true);
                startActivity(intent);
                break;
            case R.id.nav_addrecipe:
                intent = new Intent(ShowRecipeActivity.this,AddRecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(ShowRecipeActivity.this,ProfileActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(ShowRecipeActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return  true;
    }
}
