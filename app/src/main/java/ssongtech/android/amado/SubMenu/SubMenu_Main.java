package ssongtech.android.amado.SubMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.Sb_Myinfo.Myinfo_Nick;
import ssongtech.android.amado.SubMenu.Sb_MusicUp.MusicUp_Main;
import ssongtech.android.amado.SubMenu.Sb_MyPage.MyPage_Main;

public class SubMenu_Main extends AppCompatActivity {
    private TextView Sub_Mypage;
    TextView Sub_Registration,Sub_Calculate;
    ImageView Sub_Back1,Sub_Setting,Sub_imageview1;

    public static Context SubMenuctx;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu_main);
        SubMenuctx=this;
        Sub_Mypage = (TextView) findViewById(R.id.submypage);
        Sub_Registration = (TextView) findViewById(R.id.Registration);
        Sub_Calculate = (TextView) findViewById(R.id.calculate);
        Sub_Back1 = (ImageView) findViewById(R.id.Back1);
        Sub_Setting = (ImageView) findViewById(R.id.Setting);
        Sub_imageview1 = (ImageView) findViewById(R.id.imageView1);
        TextView abc = (TextView)findViewById(R.id.Mypage_Birtday);
        TextView cde = (TextView)findViewById(R.id.Gender);
        TextView cd = (TextView)findViewById(R.id.Fire);
        TextView Pw_Change = (TextView)findViewById(R.id.Pw_Change);

//        Key_Chack();





        Sub_Mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubMenu_Main.this, MyPage_Main.class);
                startActivity(intent);
            }
        });

        Sub_Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SubMenu_Main.this, MusicUp_Main.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);
            }
        });

        Sub_Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SubMenu_Main.this, Myinfo_Nick.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);
            }
        });

        Sub_Back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SubMenu_Main.this, MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);
            }
        });

//        Sub_Setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(SubMenu_Main.this, Setting_Main.class);
//                startActivity(intent1);
//                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);
//            }
//        });

    }

//
//    protected void Key_Chack(){
//        String SnsChack = "1";
//        String provide ="";
//        String email ="";
//        String login_pw="";
//        Context Sub_MenuCtx = SubMenu_Main.this;
//        String login_id = Login_Main.Lg_login_id;  //ID값 불러오기
//        String  login_key = Login_Main.Chack_Key; //로그인 했던 키값 불러오기
//        Login_Check loginChack = new Login_Check();
//        loginChack.Login(login_id,login_pw,login_key,SnsChack,provide,email,Sub_MenuCtx);
//    }
}

