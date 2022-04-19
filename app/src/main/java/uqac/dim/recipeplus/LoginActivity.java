package uqac.dim.recipeplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import uqac.dim.recipeplus.database.DatabaseObject;

public class LoginActivity extends AppCompatActivity {

    private DatabaseObject database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            database = new DatabaseObject();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void Login(View v){
        EditText temp = (EditText)findViewById(R.id.email);
        String email = temp.getText().toString();

        temp = (EditText)findViewById(R.id.password);
        String password = temp.getText().toString();

        try {
            if(database.attemptUserLogIn(email,password)){
                Intent intent = new Intent(this, RecipeActivity.class);
                intent.putExtra("User",database.getLoggedInUserObject());
                startActivity(intent);
            }
            else{

                View view = super.getLayoutInflater().inflate(R.layout.error_message_template, null);

                ((LinearLayout)findViewById(R.id.errorMessage)).addView(view);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
