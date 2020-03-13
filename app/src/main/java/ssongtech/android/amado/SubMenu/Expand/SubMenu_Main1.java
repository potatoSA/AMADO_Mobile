package ssongtech.android.amado.SubMenu.Expand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ssongtech.android.amado.DataBase.Asynctask.Sb_Menu_Profile;
import ssongtech.android.amado.Function.Check.AppPreferences;
import ssongtech.android.amado.Function.Check.Login_Check;
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.Community.Community_Board;
import ssongtech.android.amado.SubMenu.Sb_Menu_Setting.SubMenu_Setting;

public class SubMenu_Main1 extends AppCompatActivity implements View.OnClickListener{

    public static Context SubMenuCtx;
    AppPreferences app;
    ImageView SubMenu_Background,SubMenu_Profile,Sub_Menu_Profile_Befor;
    Button Sub_Logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu_main1);
        SubMenuCtx=this;
        TextView SubMenu_Setting = findViewById(R.id.SubMenu_Setting);
        ImageButton SubMenu_Upload = findViewById(R.id.SubMenu_Upload);
        TextView textView = findViewById(R.id.textView);
        Button SubMenu_LoginBtn = findViewById(R.id.Sub_Menu_LoginBtn);
        Sub_Menu_Profile_Befor =findViewById(R.id.Sub_Menu_Profile_Befor);
        Sub_Logout = findViewById(R.id.Sub_Logout);
        SubMenu_Background = findViewById(R.id.Sub_Menu_Background);
        SubMenu_Profile = findViewById(R.id.Sub_Menu_Profile);

        Sub_Logout.setOnClickListener(this::onClick);
        SubMenu_Setting.setOnClickListener(this::onClick);
        SubMenu_LoginBtn.setOnClickListener(this::onClick);

        Key_Chack();

        if(app.Sharde_getType().equals("1")||app.Sharde_getType().equals("2")){
            SubMenu_Profile.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            SubMenu_LoginBtn.setVisibility(View.GONE);
            Sub_Menu_Profile_Befor.setVisibility(View.VISIBLE);
            Profile();
            Background();
        }else {
            SubMenu_Profile.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            SubMenu_LoginBtn.setVisibility(View.VISIBLE);
            Sub_Menu_Profile_Befor.setVisibility(View.GONE);
        }






        /**
         * 서브메뉴 쏭, 커뮤니티 리스트
         */
        Display newDisplay = getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        ArrayList<SubMenu_Grop> DataList = new ArrayList<SubMenu_Grop>();
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.mylist);
        SubMenu_Grop temp = new SubMenu_Grop("SSONG");
        temp.child.add("차트");
        temp.child.add("장르");
        temp.child.add("테마");
        DataList.add(temp);
        temp = new SubMenu_Grop("커뮤니티");
        temp.child.add("장르별 게시판");
        temp.child.add("공지사항");
        temp.child.add("자유게시판");
        temp.child.add("프로듀스");
        DataList.add(temp);
        ExpandAdapter adapter = new ExpandAdapter(getApplicationContext(),R.layout.submenu_group_row,R.layout.submenu_child_row,DataList);
        listView.setIndicatorBounds(width-50, width); //이 코드를 지우면 화살표 위치가 바뀐다.
        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //차트
                if(groupPosition == 0 && childPosition == 0){
                    Toast.makeText(SubMenu_Main1.this, "1", Toast.LENGTH_SHORT).show();
                }else if(groupPosition == 0 && childPosition == 1){
                    Toast.makeText(SubMenu_Main1.this, "1", Toast.LENGTH_SHORT).show();
                }else if(groupPosition == 0 && childPosition == 2){
                    Toast.makeText(SubMenu_Main1.this, "1", Toast.LENGTH_SHORT).show();
                }
                //커뮤니티
                else if(groupPosition == 1 && childPosition == 0){
//                    startActivity(SubMenu_Main1.this,Community_Board.class);
                    startActivity(new Intent(SubMenu_Main1.this,Community_Board.class));
                }else if(groupPosition == 1 && childPosition == 1){
                    Toast.makeText(SubMenu_Main1.this, "1", Toast.LENGTH_SHORT).show();
                }else if(groupPosition == 1 && childPosition == 2){
                    Toast.makeText(SubMenu_Main1.this, "1", Toast.LENGTH_SHORT).show();
                }else if(groupPosition == 1 && childPosition == 3){
                    Toast.makeText(SubMenu_Main1.this, "1", Toast.LENGTH_SHORT).show();
                }



                return false;
            }
        });

    }

    private void Profile(){
        Sb_Menu_Profile sbMenuProfile = new Sb_Menu_Profile();
        sbMenuProfile.execute("http://192.168.0.101/Album7.png");
        Sub_Menu_Profile_Befor.setImageBitmap(sbMenuProfile.bmImg);
    }

    private void Background(){
        Sb_Menu_Profile sbMenuProfile = new Sb_Menu_Profile();
        sbMenuProfile.execute("http://192.168.0.101/Album2.png");
        SubMenu_Background.setImageBitmap(sbMenuProfile.bmImg);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.SubMenu_Setting:
                startActivity(new Intent(SubMenuCtx, SubMenu_Setting.class));
                break;
            case R.id.Sub_Logout:
                String sns = Login_Main.SNS;
                if(sns.equals("201")){
                    ((Login_Main)Login_Main.Login_Context).Google_Logout();
                    Logout();
                    startActivity(new Intent(SubMenuCtx,MainActivity.class));
                    finish();
                    break;
                }else if(sns.equals("202")){
                    Logout();
                    ((Login_Main)Login_Main.Login_Context).Facebook_Logout();
                    startActivity(new Intent(SubMenuCtx,MainActivity.class));
                    finish();
                    break;
                }else if(sns.equals("203")){
                    Logout();
                    ((Login_Main)Login_Main.Login_Context).NaverLogout();
                    startActivity(new Intent(SubMenuCtx,MainActivity.class));
                    finish();
                    break;
                }else {
                    Logout();



//                    startActivity(new Intent(Main2Context,MainActivity.class));
//                    MainActivity.first2="";
//                    finish();


                    break;
                }
            case R.id.Sub_Menu_LoginBtn:
                startActivity(new Intent(SubMenuCtx,Login_Main.class));
        }
    }


    protected void Key_Chack(){
        app = new AppPreferences(SubMenuCtx);
        String Type = app.Sharde_getType();
        String Id = app.Sharde_getEmail();  //ID값 불러오기
        String Key = app.Sharde_getKey(); //로그인 했던 키값 불러오기
        String provide ="";
        String email ="";
        String login_pw="";
        Context Sub_MenuCtx = SubMenuCtx;
        Login_Check loginChack = new Login_Check();
        loginChack.Login(Id,login_pw,Key,Type,provide,email,Sub_MenuCtx);
    }



    public void Logout(){
        AppPreferences._prefsEditor = AppPreferences._sharedPrefs.edit();
        AppPreferences._prefsEditor.clear();
        AppPreferences._prefsEditor.commit();
    }




}
