package ssongtech.android.amado.SubMenu.Sb_Menu_Setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.Sb_Myinfo.SubMenu_Set_Myinfo;

public class SubMenu_Setting extends AppCompatActivity implements View.OnClickListener {

    Context SubMenu_setCtx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu_setting);
        SubMenu_setCtx=this;

        ImageView Setting_Myinfo = findViewById(R.id.Setting_Myinfo);
        ImageView Setting_VersionBtn =findViewById(R.id.Setting_VersionBtn);
        Setting_Myinfo.setOnClickListener(this::onClick);
        Setting_VersionBtn.setOnClickListener(this::onClick);



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Setting_Myinfo:
                startActivity(new Intent(SubMenu_setCtx, SubMenu_Set_Myinfo.class));
                break;
            case R.id.Setting_VersionBtn:
                startActivity(new Intent(SubMenu_setCtx, SubMenu_Version.class));
                break;

        }
    }
}
