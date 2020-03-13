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

public class AgrView_002 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agr_view_002);

        TextView Agr_tv2 = (TextView)findViewById(R.id.textView02); //약관 출력 부분
        String Agr_view2 = getString(R.string.Agree_view002);

        final Button agree02_Sucess = (Button) findViewById(R.id.Agree02_Sucess);  //

        // 줄은 300줄 까지
        for(int i=0; i<300; i++) {
            Agr_tv2.setText(Agr_view2);
        }

        //버튼 클릭시 전 화면으로 돌아감
        agree02_Sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agree02_Sucess.isClickable()){
                    Intent intent = new Intent(AgrView_002.this,Agr_Main.class);
                    startActivity(intent);
                }
            }
        });
    }
}


