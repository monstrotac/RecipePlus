package uqac.dim.recipeplus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import uqac.dim.recipeplus.database.DatabaseObject;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<Recipe> recipes = new ArrayList<Recipe>();
    private DatabaseObject database;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        user = (User) getIntent().getSerializableExtra("User");
        try {
            database = new DatabaseObject();
            database.setUser(user);
            recipes = loadRecipes();
            generateViewData(recipes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        drawerLayout = findViewById(R.id.nav_drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){

            case R.id.nav_recipe:
                intent = new Intent(RecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_myrecipe:
                intent = new Intent(RecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("MyRecipe",true);
                startActivity(intent);
                break;
            case R.id.nav_addrecipe:
                intent = new Intent(RecipeActivity.this,AddRecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(RecipeActivity.this,ProfileActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(RecipeActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return  true;
    }

    private List<Recipe> loadRecipes(){
        ResultSet rs = null;
        List<Recipe> data = new ArrayList<Recipe>();
        try {
            if(getIntent().getSerializableExtra("MyRecipe") != null){
                rs = database.getUserRecipes(user.getId());
            }
            else{
                rs = database.getAllRecipes(false);
            }

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

            try {
                byte[] byteArray = database.getRecipeThumbnail(r.getId());
                ((ImageView)view.findViewById(R.id.drinkImage)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe temp = findRecipe(view.getId());

            Intent intent = new Intent(RecipeActivity.this,ShowRecipeActivity.class);
            intent.putExtra("User",user);
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
