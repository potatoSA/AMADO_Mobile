package ssongtech.android.amado.HeadMenu.Hd_Login.Join.Sns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.R;

public class Sns_Birthday extends AppCompatActivity {
    protected static String gender,join_birthday;
    protected static String Sns_Gender; //성별 값

    ImageView NaverJoin_GenderNext;
    TextView Birthday_Err; // 성별,생년월일 화면 에러 확인

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_birthday);

        EditText Join_Birthday = findViewById(R.id.Join_Birthday);
        NaverJoin_GenderNext = findViewById(R.id.Birthday_Btn);
        Birthday_Err=findViewById(R.id.Birthday_Err);
        Spinner Gender = (Spinner)findViewById(R.id.NaverJoin_Gender);

        ArrayAdapter GenderAdapter = ArrayAdapter.createFromResource(this,R.array.NaverJoin_Gender, android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(GenderAdapter);

        NaverJoin_GenderNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = Gender.getSelectedItem().toString();
                join_birthday = Join_Birthday.getText().toString();
                String adsd = Join_Birthday.getText().toString();
                Integer abc = Integer.valueOf(adsd);
                if(abc > 2020){
                    Birthday_Err.setText("미래에서 오셨습니까");
                }else if(abc < 1920) {
                    Birthday_Err.setText("정말이세요?");
                } else {
                    if(gender.equals("남성")){
                        Sns_Gender="002";
                        startActivity(new Intent(Sns_Birthday.this, Sns_Genre.class));
                    }else if(gender.equals("여성")){
                        Sns_Gender="001";
                        startActivity(new Intent(Sns_Birthday.this, Sns_Genre.class));
                    }else if(gender.equals("무응답")){
                        Sns_Gender="003";
                        startActivity(new Intent(Sns_Birthday.this, Sns_Genre.class));
                    }else {
                        Toast.makeText(Sns_Birthday.this, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
