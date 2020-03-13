package ssongtech.android.amado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import ssongtech.android.amado.App_Main.Tab_Setting_Main.SectionsPagerAdapter;
import ssongtech.android.amado.Function.Check.AppPreferences;
import ssongtech.android.amado.Function.Check.Login_Check;
import ssongtech.android.amado.HeadMenu.Hd_Find.Find_Main;
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.HeadMenu.Hd_Ticket.Ticket_Main;
import ssongtech.android.amado.SubMenu.SubMenu_Main;
import com.google.android.material.tabs.TabLayout;

public class Main2Activity extends AppCompatActivity {

    //업데이트
    ImageView Main_logo,Main_Ticket,Main_Login,Main_Find,Main_Sub, Main_Logout;

    public static Context Main2Context;

    public static String first2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // 메뉴 셋팅 Start
        Main_logo = findViewById(R.id.logo);
        //Main_Login = findViewById(R.id.Login);
        Main_Logout = findViewById(R.id.Logout);
        Main_Ticket = findViewById(R.id.Ticket);
        Main_Find = findViewById(R.id.Serach);
        Main_Sub = findViewById(R.id.sub_menu);

        Main_Logout.setOnClickListener(this::onClick);
        Main2Context = this;


//        AppPreferences app = new AppPreferences(Main2Context);
//        String SNS = app.getSmsBody();
//
//        Log.d("테스트",SNS);

        Main_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);
            }
        });

        Main_Ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main2Activity.this, Ticket_Main.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);

            }
        });
        /*Main_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main2Activity.this, Login_Main.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);

            }
        });*/

        Main_Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main2Activity.this, Find_Main.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);

            }
        });
        Main_Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main2Activity.this, SubMenu_Main.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fadeinxml, R.anim.fadeout);
            }
        });
    }

    protected void onClick(View v){
        switch (v.getId()) {
            case R.id.Logout:
                String sns = Login_Main.SNS;
                if(sns.equals("201")){
                    ((Login_Main)Login_Main.Login_Context).Google_Logout();
                    startActivity(new Intent(Main2Context,MainActivity.class));
                    finish();
                }else if(sns.equals("202")){
                    ((Login_Main)Login_Main.Login_Context).Facebook_Logout();
                    startActivity(new Intent(Main2Context,MainActivity.class));
                    finish();
                }else if(sns.equals("203")){
                    Logout();
                    ((Login_Main)Login_Main.Login_Context).NaverLogout();
                    startActivity(new Intent(Main2Context,MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(Main2Context,MainActivity.class));
                    MainActivity.first2="";
                    finish();
                }
                break;
        }
    }
    public void Logout(){
        SharedPreferences app = getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor= app.edit();
        editor.clear();
        editor.commit();
    }
}
