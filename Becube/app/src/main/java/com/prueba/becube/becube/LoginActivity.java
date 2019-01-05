package com.prueba.becube.becube;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity{
    private EditText mUsuarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox checkSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("PreferenciasBecube",getApplicationContext().MODE_PRIVATE);
        if(prefs.contains("login") && prefs.getBoolean("login", false)){
            Intent intentMainActivity = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(intentMainActivity);
            finish();
        }

        setContentView(R.layout.activity_login);
        checkSesion = (CheckBox) findViewById(R.id.check_sesion);
        mUsuarioView = (EditText) findViewById(R.id.user);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        mUsuarioView.setError(null);
        mPasswordView.setError(null);
        String usuario = mUsuarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancelByPass = false;
        boolean cancelByUser = false;
        View focusViewPass = null;
        View focusViewUser = null;

        if (TextUtils.isEmpty(password) && password.trim().length()==0) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusViewPass = mPasswordView;
            cancelByPass = true;
        }

        if (TextUtils.isEmpty(usuario) && usuario.trim().length()==0) {
            mUsuarioView.setError(getString(R.string.error_invalid_user));
            focusViewUser = mUsuarioView;
            cancelByUser = true;
        }

        if (cancelByPass || cancelByUser) {
            if(cancelByPass){
                focusViewPass.requestFocus();
            }
            if(cancelByUser){
                focusViewUser.requestFocus();
            }
        } else {
            showProgress(true);

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            String url ="http://45.58.62.228/api/SanPablo/Login_UserDelivery/"+usuario+"/"+password;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Login[] login = gson.fromJson(response, Login[].class);
                            if(login.length == 1){
                                if (login[0] != null && login[0].getUsersDeliveryAvailability()) {
                                    if(checkSesion.isChecked()){
                                        guardaInicioAutomatico();
                                    }
                                    Intent intentMainActivity = new Intent(LoginActivity.this, MapsActivity.class);
                                    startActivity(intentMainActivity);
                                    finish();
                                } else {
                                    showProgress(false);
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showProgress(false);
                }
            });
            queue.add(stringRequest);
        }
    }

    private void guardaInicioAutomatico(){
        SharedPreferences prefs = getSharedPreferences("PreferenciasBecube",getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("login", true);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

