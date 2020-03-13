package ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import ssongtech.android.amado.R;
/*************************************************************
 클래스명   : Local_Birthday
 설명       : 성별,생년월일 입력
 생성일     : 2020.03.10
 생성자     : 박기태
 1차 수정일 :
 1차 수정자 :
 param      :
 return     :
 **************************************************************/
public class Local_Birthday extends AppCompatActivity {
    /*******************************************서버에 보낼 값들*******************************************/
    protected static String gender,join_birthday;
    protected static String Sns_Gender; //젠더 성별 확인

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_birthday);

        ImageView NaverJoin_GenderNext = findViewById(R.id.Birthday_Btn);
        EditText Join_Birthday = findViewById(R.id.Join_Birthday);
        TextView Birthday_Err = findViewById(R.id.Birthday_Err);
        Spinner Gender = findViewById(R.id.NaverJoin_Gender);

        /*******************************************텍스트 출력*******************************************/
        TextView Birthday_Text = findViewById(R.id.Birthday_Text);
        Birthday_Text.setText(Html.fromHtml("<b>출생년도</b>"+"와"+"<b>성별</b>"+"을 입력해주세요."));

        /*******************************************스피너 설정******************************************/
        ArrayAdapter GenderAdapter = ArrayAdapter.createFromResource(this,R.array.NaverJoin_Gender, android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(GenderAdapter);

        /*******************************************다음 버튼 클릭******************************************/
        NaverJoin_GenderNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        /*******************************************현재 년도 구하기기******************************************/
                Calendar calendar = new GregorianCalendar(Locale.KOREA);
                Integer Year = calendar.get(Calendar.YEAR);
        /*******************************************서버에 보낼 변수 담기******************************************/
                gender = Gender.getSelectedItem().toString();
                join_birthday = Join_Birthday.getText().toString();
                Integer Birthdy_Compare= Integer.valueOf(join_birthday); // 미래에서 오셨습니까? 출력할려고 Integer 변경
                if(Birthdy_Compare > Year){
                    Birthday_Err.setText(getString(R.string.Birtday_up));
                }else if(Birthdy_Compare < Year-100) {
                    Birthday_Err.setText(getString(R.string.Birtday_down));
                } else {
                    if(gender.equals(getString(R.string.Men))){
                        Sns_Gender=getString(R.string.Gender_001);
                        startActivity(new Intent(Local_Birthday.this,Local_Nick.class));
                    }else if (gender.equals(getString(R.string.Female))){
                        Sns_Gender=getString(R.string.Gender_002);
                        startActivity(new Intent(Local_Birthday.this,Local_Nick.class));
                    }else if (gender.equals(getString(R.string.unresponsive))){
                        Sns_Gender=getString(R.string.Gender_003);
                        startActivity(new Intent(Local_Birthday.this,Local_Nick.class));
                    }else {
                        Toast.makeText(Local_Birthday.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
