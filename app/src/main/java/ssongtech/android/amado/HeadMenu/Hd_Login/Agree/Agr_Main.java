package ssongtech.android.amado.HeadMenu.Hd_Login.Agree;
/*************************************************************
 클래스명   : Agr_Main
 설명       : 약관동의
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import ssongtech.android.amado.R;
import ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Email;

public class Agr_Main extends AppCompatActivity {


    // 체크박스 체크여부
    public int Agr_Agree_1 = 0; // No Check = 0, Check = 1
    public int Agr_Agree_2 = 0; // No Check = 0, Check = 1
    public int Agr_Agree_3 = 0; // No Check = 0, Check = 1
    // 체크박스
    CheckBox Agree01,Agree02,Agree03;; // 동의
    Button Agr_Nextbtn,agree01_Btn,agree02_Btn; // 다음버튼

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agr_main);

        final String Agree_Sucese=getString(R.string.Agree_sucess);

        //체크확인
        Agree01 = (CheckBox)findViewById(R.id.Agree01);  //1번 체크박스
        Agree02 = (CheckBox)findViewById(R.id.Agree02);  //2번 체크박스
        Agree03 = (CheckBox)findViewById(R.id.Agree03);  //3번 전체 체크박스

        Agr_Nextbtn = (Button)findViewById(R.id.NextBtn);   //다음단계
        agree01_Btn = (Button)findViewById(R.id.Agree01_Btn);  //1번 약관보기
        agree02_Btn= (Button)findViewById(R.id.Agree02_Btn);  //2번 약관보기


        agree01_Btn.setOnClickListener(new View.OnClickListener() {   //1번 약관버튼 클릭시 이벤트실행
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Agr_Main.this,AgrView_001.class);
                startActivity(intent);
            }
        });

        agree02_Btn.setOnClickListener(new View.OnClickListener() { //2번 약관버튼 클릭시 이벤트실행
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Agr_Main.this,AgrView_002.class);
                startActivity(intent);
            }
        });



        Agree01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {   //1번 버튼 클릭시 AGREE 값 1로 지정 (체크했느지 안햇는지 확인하기위해)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Agree01.isChecked() == true){
                    Agr_Agree_1 = 1;
                }else{
                    Agr_Agree_1 = 0;
                }
            }
        });

        Agree02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //2번 버튼 클릭시 AGREE 값 1로 지정 (체크했느지 안햇는지 확인하기위해)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Agr_Agree_2 = 1;
                }else {
                    Agr_Agree_2 = 0;
                }
            }
        });

        Agree03.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //3번 버튼 클릭시 AGREE 값 1로 지정 (체크했느지 안햇는지 확인하기위해)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Agree01.setChecked(true);
                    Agree02.setChecked(true);
                    Agr_Agree_3 = 1;
                }else {
                    Agree01.setChecked(false);
                    Agree02.setChecked(false);
                    Agr_Agree_3 = 0;
                }
            }
        });
        Agr_Nextbtn.setOnClickListener(new View.OnClickListener() {   // 3번에 값이 없을때 1,2번 어그리 동의 입력값 으로 체크 되었는지 확인
            @Override
            public void onClick(View v) {
                if(Agr_Agree_3 != 1){
                    if(Agr_Agree_2 == 1){
                        if(Agr_Agree_1 == 1){
                            Intent intent = new Intent(Agr_Main.this, Local_Email.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Agr_Main.this,Agree_Sucese,Toast.LENGTH_SHORT).show();  //약관 2번 동의안했을시 체크하라는 알람
                            return;
                        }
                    }else {
                        Toast.makeText(Agr_Main.this,Agree_Sucese,Toast.LENGTH_SHORT).show();  //약관 1번 동의안했을시 체크하라는 알람
                        return;
                    }
                } else {
                    Intent intent = new Intent(Agr_Main.this, Local_Email.class);
                    startActivity(intent);
                }
            }
        });
    }

}


