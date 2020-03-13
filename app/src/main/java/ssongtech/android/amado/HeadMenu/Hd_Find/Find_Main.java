package ssongtech.android.amado.HeadMenu.Hd_Find;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.Function.Check.Login_Check;
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.R;

public class Find_Main extends AppCompatActivity {

    public static String login_id,login_key,login_pw="";
    public static Context Find_context; //로그인 체크 하귀위한 준비
    private static String TAG = "ssong_login";
    public static TextView kitae;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_main);
        Find_context = Find_Main.this; //로그인 체크 하귀위한 준비
        kitae = findViewById(R.id.kitae);
        Key_Chack();
    }

    protected void Key_Chack(){
        String SnsChack = "1";
        String provide ="";
        String email ="";
        Context Fi_Context = Find_Main.this;
        login_id = Login_Main.Lg_login_id;  //ID값 불러오기
        login_key = Login_Main.Chack_Key; //로그인 했던 키값 불러오기
        Login_Check loginChack = new Login_Check();
        loginChack.Login(login_id,login_pw,login_key,SnsChack,provide,email,Fi_Context);
    }
}

