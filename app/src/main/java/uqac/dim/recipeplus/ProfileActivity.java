package uqac.dim.recipeplus;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import uqac.dim.recipeplus.database.DatabaseObject;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseObject database;
    private User user;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadData(user);
    }

    private void loadData(User user){
        ((TextView)findViewById(R.id.email)).setText(user.getEmail());

        ((TextView)findViewById(R.id.name)).setText(user.getFirstname());

    }

}
