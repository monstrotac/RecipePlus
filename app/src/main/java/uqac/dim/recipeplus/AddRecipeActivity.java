package uqac.dim.recipeplus;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uqac.dim.recipeplus.database.DatabaseObject;

public class AddRecipeActivity  extends AppCompatActivity {
    private DatabaseObject database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        try {
            database = new DatabaseObject();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        user = (User) getIntent().getSerializableExtra("User");
        database.setUser(user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
