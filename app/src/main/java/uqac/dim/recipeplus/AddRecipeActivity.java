package uqac.dim.recipeplus;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uqac.dim.recipeplus.database.DatabaseObject;

public class AddRecipeActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseObject database;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        try {
            database = new DatabaseObject();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        user = (User) getIntent().getSerializableExtra("User");
        database.setUser(user);

        drawerLayout = findViewById(R.id.nav_drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){

            case R.id.nav_recipe:
                intent = new Intent(AddRecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_myrecipe:
                intent = new Intent(AddRecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("MyRecipe",true);
                startActivity(intent);
                break;
            case R.id.nav_addrecipe:
                intent = new Intent(AddRecipeActivity.this,AddRecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(AddRecipeActivity.this,ProfileActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(AddRecipeActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return  true;
    }

    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView)findViewById(R.id.userPirture);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void saveRecipe(View view) throws IOException {
        EditText temp = (EditText)findViewById(R.id.recipeName);
        String name = temp.getText().toString();

        temp = (EditText)findViewById(R.id.recipeDesc);
        String desc = temp.getText().toString();

        temp = (EditText)findViewById(R.id.recipeInst);
        String inst = temp.getText().toString();

        ImageView image = (ImageView)findViewById(R.id.userPirture);

        Bitmap bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytesData = stream.toByteArray();
        stream.close();


        Recipe data = new Recipe(name, desc, inst, new ArrayList<Ingredient>());
        try {
            database.addNewRecipeFromLoggedInUser(data,bytesData,new ArrayList<byte[]>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Intent intent = new Intent(AddRecipeActivity.this, RecipeActivity.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }
}
