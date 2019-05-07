package cz.czechitas.janhanak.czechitas8lekce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Hawk.init(this).build();

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(mEmailView.getText().toString(),mPasswordView.getText().toString());
            }
        });
        mProgressView = findViewById(R.id.login_progress);

        if(Hawk.contains("logged")){
            startMainActivityAndFinish();
        }
    }

    private void attemptLogin(String login, String password) {
        ApiService apiService = RetrofitInstance.getRetrofitInstance().
                create(ApiService.class);
        Call<LoginAnswer> call;
        if(isPasswordValid(password)) {
            call = apiService.login(login, password);
        } else {
            call = apiService.loginError(login, password);
        }
        call.enqueue(new Callback<LoginAnswer>() {
            @Override
            public void onResponse(Call<LoginAnswer> call, Response<LoginAnswer> response) {
                if(response.code()==HttpURLConnection.HTTP_OK){
                    Hawk.put("logged",response.body().user);
                    Toast.makeText(LoginActivity.this,"jsem prihalsen",Toast.LENGTH_SHORT).show();
                    startMainActivityAndFinish();

                }
                else{
                    Toast.makeText(LoginActivity.this,"nenene", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LoginAnswer> call, Throwable t) {

            }
        });
        if(isPasswordValid(mPasswordView.getText().toString())) {

        } else {

        }
    }


    private void startMainActivityAndFinish(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isPasswordValid(String password) {
        return password.equals("czechitas");
    }
}

