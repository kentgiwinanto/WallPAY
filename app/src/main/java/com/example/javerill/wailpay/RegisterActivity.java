package com.example.javerill.wailpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final int REQUEST_LOGIN = 1;

    @InjectView(R.id.etFullName) EditText etFullName;
    @InjectView(R.id.etEmail) EditText etEmail;
    @InjectView(R.id.etUserID) EditText etUserID;
    @InjectView(R.id.etPassword) EditText etPassword;
    @InjectView(R.id.btnRegister) Button btnRegister;
    @InjectView(R.id.linkLogin) TextView linkLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        ButterKnife.inject(this);
        etFullName.requestFocus();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                // finish();

                // Start the Login Activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
            }
        });
    }

    public void register() {
        if (!validate()) {
            onRegisterFailed();
            return;
        }

        btnRegister.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String fullName = etFullName.getText().toString();
        final String email = etEmail.getText().toString();
        final String userID = etUserID.getText().toString();
        final String password = etPassword.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    // On complete call either onRegisterSuccess or onRegisterFailed
                    // depending on success
                    onRegisterSuccess(fullName, email, userID, password);
                    // onSignupFailed();
                    progressDialog.dismiss();
                }
            }, 3000
        );
    }


    public void onRegisterSuccess(String fullName, String email, String userID, String password) {
        btnRegister.setEnabled(true);
        setResult(RESULT_OK, null);

        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CREATE OPERATION
         * */

        // Inserting Users
        db.addUser(new User(fullName, email, userID, password));

        Toast.makeText(this, "Register Success!", Toast.LENGTH_LONG).show();

        etFullName.setText(null);
        etEmail.setText(null);
        etUserID.setText(null);
        etPassword.setText(null);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Failed to Login. Please try again!", Toast.LENGTH_LONG).show();
        btnRegister.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String userID = etUserID.getText().toString();
        String password = etPassword.getText().toString();

        if (fullName.isEmpty()) {
            etFullName.setError("Enter a valid Full Name!");
            valid = false;
        } else {
            etFullName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid Email Address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

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
