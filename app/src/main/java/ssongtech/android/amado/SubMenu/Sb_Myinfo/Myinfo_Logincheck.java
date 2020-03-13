package ssongtech.android.amado.SubMenu.Sb_Myinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;

public class Myinfo_Logincheck extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = "LoginCheck";

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_logincheck);
        mContext= this;
        TextView logincheck_email = findViewById(R.id.LoginCheck_Email);
        TextView logincheck_pw = findViewById(R.id.LoginCheck_Pw);
        Button logincheck_Btn = findViewById(R.id.LoginChekc_Btn);

        logincheck_Btn.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginChekc_Btn:

        }
    }






}
