package com.example.javerill.wailpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by javerill on 9/22/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTER = 0;

    @InjectView(R.id.etUserID) EditText etUserID;
    @InjectView(R.id.etPassword) EditText etPassword;
    @InjectView(R.id.btnLogin) Button btnLogin;
    @InjectView(R.id.linkRegister) TextView linkRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        ButterKnife.inject(this);
        etUserID.requestFocus();

        // List User
        DatabaseHandler db = new DatabaseHandler(this);

        List<User> users = db.getAllUsers();

        for (User u : users) {
            Toast.makeText(this, "ID: " + u.getID() + " ,FullName: " + u.getFullName()
                    + " ,Email: " + u.getEmail() + " ,User ID: " + u.getUserID()
                    + " ,Password: " + u.getPassword() + System.getProperty("line.separator"),
                    Toast.LENGTH_LONG).show();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Register Activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_REGISTER);
            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String userID = etUserID.getText().toString();
        final String password = etPassword.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess(userID, password);
                    etUserID.setText(null);
                    etPassword.setText(null);

                    // onLoginFailed();
                    progressDialog.dismiss();
                }
            }, 3000
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful register logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String userID, String password) {
        btnLogin.setEnabled(true);

        DatabaseHandler db = new DatabaseHandler(this);

        boolean check = db.checkIfExist(userID, password);
        if(check == true) {
            User users = db.getUserByUID(userID, password);

            if (users.getUserID().equals(userID) && users.getPassword().equals(password)) {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this, "Invalid User ID or Password!", Toast.LENGTH_LONG).show();
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Failed to Login. Please try again!", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userID = etUserID.getText().toString();
        String password = etPassword.getText().toString();

        if (userID.isEmpty()) {
            etUserID.setError("Enter a valid User ID!");
            valid = false;
        } else {
            etUserID.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 6) {
            etPassword.setError("Password must be 6 characters!");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
}
