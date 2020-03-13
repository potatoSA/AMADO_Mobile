package ssongtech.android.amado.HeadMenu.Hd_Login.Agree;
/*************************************************************
 클래스명   : Agr_Main
 설명       : 약관동의 보기
 생성일     : 2020.01.31
 생성자     : 박기태
 1차 수정일 :
 1차 수정자 :
 param      :
 return     :
 **************************************************************/
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.R;

public class AgrView_001 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agr_view_001);

        TextView Agr_tv1 = (TextView)findViewById(R.id.textView01);
        String Agr_view1 = getString(R.string.Agree_view001);

        final Button agree01_Sucess = (Button) findViewById(R.id.Agree01_Sucess);

        // 줄은 300줄 까지
        for(int i=0; i<300; i++) {
            Agr_tv1.setText(Agr_view1);
        }
        //버튼 클릭시 전 화면으로 돌아감
        agree01_Sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agree01_Sucess.isClickable()){
                    Intent intent = new Intent(AgrView_001.this,Agr_Main.class);
                    startActivity(intent);
                }
            }
        });
    }
}


