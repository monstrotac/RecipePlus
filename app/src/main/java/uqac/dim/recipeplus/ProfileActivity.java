package uqac.dim.recipeplus;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
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

        ((TextView)findViewById(R.id.name)).setText(user.getFirstname() + " " + user.getLastname());

        try {
            byte[] byteArray = database.getUserImage(user.getId());
            ((ImageView)findViewById(R.id.userPicture)).setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { galleryIntent });

        Intent[] intentArray = { takePictureIntent };
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        try {
            someActivityResultLauncher.launch(chooser);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ImageView imageView = (ImageView)findViewById(R.id.userPicture);
                        imageView.setImageBitmap(imageBitmap);


                        try {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] bytesData = stream.toByteArray();
                            stream.close();

                            database.updateUserProfilePicture(bytesData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


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
