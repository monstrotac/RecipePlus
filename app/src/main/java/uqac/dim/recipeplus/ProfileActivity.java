package uqac.dim.recipeplus;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.sql.SQLException;
import java.util.Objects;

import uqac.dim.recipeplus.database.DatabaseObject;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseObject database;
    private User user;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            database = new DatabaseObject();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        user = (User) getIntent().getSerializableExtra("User");
        database.setUser(user);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        loadData(user);


        drawerLayout = findViewById(R.id.nav_drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadData(User user){
        
        ((TextView)findViewById(R.id.email)).setText(user.getEmail());

        ((TextView)findViewById(R.id.name)).setText(user.getFirstname());

        try {
            byte[] byteArray = database.getUserImage(user.getId());
            ((ImageView)findViewById(R.id.userPirture)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){

            case R.id.nav_recipe:
                intent = new Intent(ProfileActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_myrecipe:
                intent = new Intent(ProfileActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("MyRecipe",true);
                startActivity(intent);
                break;
            case R.id.nav_addrecipe:
                intent = new Intent(ProfileActivity.this,AddRecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(ProfileActivity.this,ProfileActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return  true;
    }
}
