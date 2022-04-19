package uqac.dim.recipeplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import uqac.dim.recipeplus.database.DatabaseObject;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseObject database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            database = new DatabaseObject();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void Register(View v){

        EditText temp = (EditText)findViewById(R.id.email);
        String email = temp.getText().toString();

        temp = (EditText)findViewById(R.id.password);
        String password = temp.getText().toString();

        temp = (EditText)findViewById(R.id.firstname);
        String firstname = temp.getText().toString();

        temp = (EditText)findViewById(R.id.lastname);
        String lastname = temp.getText().toString();

        if(!email.equals("") && !password.equals("") && !firstname.equals("") && !lastname.equals("")){
            try {
                if(database.attemptUserRegister(firstname,lastname,password,email)){
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            View view = super.getLayoutInflater().inflate(R.layout.error_message_template, null);
            ((TextView)view.findViewById(R.id.errorText)).setText("All fields need to be filled");
            ((LinearLayout)findViewById(R.id.errorMessage)).addView(view);
        }

    }

}
