package ssongtech.android.amado.SubMenu.Sb_Myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.Sb_Myinfo.Myinfo_Nick;

public class SubMenu_Set_Myinfo extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu_set_myinfo);


        ImageView Myinfo_Nick = findViewById(R.id.Myinfo_Nick);
        ImageView Myinfo_Pw = findViewById(R.id.Myinfo_Pw);
        ImageView Myinfo_Birtday = findViewById(R.id.Myinfo_Birtday);
        ImageView Myinfo_Gender = findViewById(R.id.Myinfo_Gender);
        ImageView Myinfo_Withdrawal= findViewById(R.id.Myinfo_Withdrawal);



        Myinfo_Nick.setOnClickListener(this);
        Myinfo_Pw.setOnClickListener(this);
        Myinfo_Birtday.setOnClickListener(this);
        Myinfo_Gender.setOnClickListener(this);
        Myinfo_Withdrawal.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Myinfo_Nick:
                startActivity(new Intent(SubMenu_Set_Myinfo.this, Myinfo_Nick.class));
                break;
            case R.id.Myinfo_Pw:
                startActivity(new Intent(SubMenu_Set_Myinfo.this, Myinfo_Pw.class));
                break;
            case R.id.Myinfo_Birtday:
                startActivity(new Intent(SubMenu_Set_Myinfo.this, Myinfo_Birtday.class));
                break;
            case R.id.Myinfo_Gender:
                startActivity(new Intent(SubMenu_Set_Myinfo.this, Myinfo_Gender.class));
                break;
            case R.id.Myinfo_Withdrawal:
                startActivity(new Intent(SubMenu_Set_Myinfo.this, Myinfo_Withdrawal.class));
                break;
        }
    }
}
