package ssongtech.android.amado.App_Main.BaseActivity;
/****************************************************************
 클래스명    : BaseActivity.java
 설명       : 만들 ToolBar를 모든 액티비티에서 사용할 수 있도록 작업
 생성일     : 2020.03.05
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.SubMenu_Main;
import ssongtech.android.amado.SubMenu.Expand.SubMenu_Main1;

public class BaseActivity extends AppCompatActivity {
    // ToolBar 관련 변수
    private BottomNavigationView Main_BottomNavigationView;
    private Toolbar Main_ToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_baseactivity);
    }

    @Override
    public void setContentView(int layoutResID) {
        CoordinatorLayout ToolBarView = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.appbar_baseactivity, null);
        FrameLayout activityContainer = (FrameLayout) ToolBarView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(ToolBarView);


        // ToolBar/Navigation Init
        Main_BottomNavigationView = findViewById(R.id.Main_BottomNavigationView);

        // bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가한다_김선아
        Main_BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.BottomHome:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.BottomCommu:
                    case R.id.BottomFind:
                    case R.id.BottomLike:
                        Toast.makeText(getApplicationContext(), "준비중입니당~", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        Main_ToolBar = (Toolbar) findViewById(R.id.Main_ToolBar);

        if (useToolbar()) {
            setSupportActionBar(Main_ToolBar);
        } else {
            Main_ToolBar.setVisibility(View.GONE);
        }
    }

    //툴바를 사용할지 말지 정함
    protected boolean useToolbar() {
        return true;
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_amado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 툴바에 옵션 메뉴를 만들 수 있다.
        switch (item.getItemId()) {
            case R.id.Login:
                Intent intent = new Intent(this, Login_Main.class);
                startActivity(intent);
                return true;
            case R.id.submenu_option:
                Intent intent1 = new Intent(this, SubMenu_Main1.class);
                startActivity(intent1);
                return true;
                /*
            case R.id.amado_logo:
                Intent intent2 = new Intent(MainActivity_amado.this, MainActivity.class);
                startActivity(intent2);
                return true;
                 */
            default:
                return false;

        }
    }
}
