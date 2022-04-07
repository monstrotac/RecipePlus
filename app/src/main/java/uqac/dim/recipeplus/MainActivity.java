package uqac.dim.recipeplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void LoginPage(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void RegisterPage(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public  void ContinueGuest(View v){
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }
}