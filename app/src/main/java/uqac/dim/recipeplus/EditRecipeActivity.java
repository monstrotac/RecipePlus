package uqac.dim.recipeplus;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import uqac.dim.recipeplus.database.DatabaseObject;

public class EditRecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseObject database;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Recipe currentRecipe;
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();

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

        currentRecipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        drawerLayout = findViewById(R.id.nav_drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setIngredients();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){

            case R.id.nav_recipe:
                intent = new Intent(EditRecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_myrecipe:
                intent = new Intent(EditRecipeActivity.this,RecipeActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("MyRecipe",true);
                startActivity(intent);
                break;
            case R.id.nav_addrecipe:
                intent = new Intent(EditRecipeActivity.this,AddRecipeActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(EditRecipeActivity.this,ProfileActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(EditRecipeActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return  true;
    }

    private void setIngredients (){
        try {
            ResultSet rs = database.getAllIngredients();

            while (rs.next()){
                ingredients.add(new Ingredient(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getFloat(4)));
            }
            showData();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void showData(){

        ((TextView)findViewById(R.id.recipeName)).setText(currentRecipe.getName());
        //((ImageView)view.findViewById(R.id.drinkImage)).setImageResource();
        ((TextView)findViewById(R.id.recipeInst)).setText(currentRecipe.getInstruc());
        ((TextView)findViewById(R.id.recipeDesc)).setText(currentRecipe.getDesc());

        try {
            byte[] byteArray = database.getRecipeThumbnail(currentRecipe.getId());
            ((ImageView)findViewById(R.id.userPirture)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for(Ingredient ing:ingredients){
            View tempView = getLayoutInflater().inflate(R.layout.list_layout, null,false);
            tempView.setId(ing.getId());

            ((CheckBox)tempView.findViewById(ing.getId())).setText(ing.getName() + " : " + ing.getDesc() + " : " + ing.getAvgPrice() + "$");
                ((LinearLayout)findViewById(R.id.radio_ingredient)).addView(tempView);

                for(Ingredient i : currentRecipe.getIngredients()){
                    if(ing.getId() == i.getId()){
                        ((CheckBox)tempView).setChecked(true);
                    }
                }
        }

    }

    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

        Intent chooser = Intent.createChooser(galleryIntent, "Some text here");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePictureIntent });

        try {
            startActivityForResult(chooser, REQUEST_IMAGE_CAPTURE);
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

    public void editRecipe(View view) throws IOException {
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

        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

        for(Ingredient ing:ingredients){
            if(((CheckBox)findViewById(ing.getId())).isChecked()){
                ingredientList.add(ing);
            }
        }

        Recipe data = new Recipe(name, desc, inst, ingredientList);
        try {
            database.updateRecipe(data,bytesData,new ArrayList<byte[]>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Intent intent = new Intent(EditRecipeActivity.this, RecipeActivity.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }
}
