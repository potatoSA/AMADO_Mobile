package ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ssongtech.android.amado.R;

public class Local_Email extends AppCompatActivity {



    //변수
    String TAG = "Local_EmailCheck";  //TAG
    String EmailCheck_Value;
    String JS_EmailCheck;
    TextView Err_Chack;

    EditText Join_pw ;  //비밀번호
    EditText Join_pw2;  // 비밀번호확인
    EditText Join_id; //아이디

    ImageView Join_SuccesBtn;

    /*******************************************서버로 보낼 변수*******************************************/
    protected static String Local_Email,Local_Pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_join);

        //변수선언
        Join_pw = findViewById(R.id.JOIN_PW);  //비밀번호
        Join_pw2 = findViewById(R.id.JOIN_PW2);  // 비밀번호확인
        Join_id = findViewById(R.id.JOIN_ID);  // 아이디
        TextView Pw_ErrChack = findViewById(R.id.Pw_ErrChack);  //패스워드 에러확인
        Join_SuccesBtn = findViewById(R.id.JOIN_SUCBTN);    //회원가입
        Err_Chack = findViewById(R.id.bug);  //사용할수 있는아이디 없는아이디
        TextView textView21 =findViewById(R.id.textView21);

        /*******************************************텍스트 출력*******************************************/
        textView21.setText(Html.fromHtml("<b>아이디</b>" + "와"+"<b>비밀번호</b>"+"를 입력해주세요."));

        /*******************************************다음 버튼*******************************************/
        Join_SuccesBtn.setOnClickListener(new View.OnClickListener() {  //rgiBtn 버튼 클릭시 발생되는 이벤트
            @Override
            public void onClick(View v) {
                if(!checkEmail(Join_id.getText().toString())){  // 이메일정규화 랑 joindid 스트링이랑 문자 비교 정규화 문자랑 다를시 오류 표시
                    Err_Chack.setText(getString(R.string.Si_Email_check));  //에러 텍스트 출력
                } else {
                    if(validatePassword(Join_pw.getText().toString())){
                        if(Join_pw.getText().toString().equals(Join_pw2.getText().toString())){
                            Pw_ErrChack.setText("");
                            String Email = Join_id.getText().toString();
                            Email_Check email_check = new Email_Check();
                            email_check.execute(getString(R.string.LOCAL_EMAIL_CHECK),Email);
                            Local_Email = Join_id.getText().toString();
                            Local_Pw = Join_pw.getText().toString();
                            Err_Chack.setText("");
                        } else {
                            Pw_ErrChack.setText(getString(R.string.Pw_Err));
                        }
                    }else {
                        Pw_ErrChack.setText(getString(R.string.Pw_Err2));
                        Log.d(TAG,"회원가입 중 : 실패 ");
                    }
                }
            }
        });


        /*******************************************이메일 형식 확인*******************************************/
        Join_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!checkEmail(Join_id.getText().toString())){
                    Err_Chack.setText(getString(R.string.Email_Err));
                }else {
                    Err_Chack.setText(""); } }
            @Override public void afterTextChanged(Editable s) { }}); }

    /*******************************************이메일 형식 정규화*******************************************/
    public static final Pattern emailcheck = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    private boolean checkEmail(String email) {
        return emailcheck.matcher(email).matches();
    }
    /*******************************************비밀번호 형식 정규화*******************************************/
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9]).{8,15}.$"); // 8자리 ~ 16자리까지 가능
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }


    class Email_Check extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params){
            String serverURL = (String)params[0];  //task.exectu() 안에있는 값들을 각 배열로 선언 해준다
            String email = (String)params[1];
            String postParameters ="&mb_mobile_id= " + email; // emalie1 을 서버 키값으로 보내기 위한 곳
            try {
                URL url = new URL(serverURL); //url 선언
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //httpurl에 url 연결 준비

                httpURLConnection.setReadTimeout(5000);  // 5초뒤 종료 (옵션)
                httpURLConnection.setConnectTimeout(5000); // 5초뒤 종료 (옵션)
                httpURLConnection.setRequestMethod("POST");  //통신 방식은  post 방식
                httpURLConnection.connect();  //설정후 연결

                OutputStream outputStream = httpURLConnection.getOutputStream(); //클라이언트에서 서버로 보낼 준비
                outputStream.write(postParameters.getBytes("UTF-8"));  //utf-8 아닐시 글자가 깨질수가 있기때문에 utf8로 인코딩후 서버 전송
                outputStream.flush();  //서버보낸후 초기화
                outputStream.close(); //종료

                int responseStatusCode = httpURLConnection.getResponseCode(); //http연결 돼었는지 확인하기위해 변숫너언
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream; //inputstream 선언

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();  // 연결 잘 돼었을때 inputstream에 url정보 전달
                }
                else{
                    inputStream = httpURLConnection.getErrorStream(); //연결 안돼었을때 에러
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8"); //서버에서 정보 가져오기위한 준비 ,UTF-8로인코딩
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //서버에서 값 들고오면 버퍼리더에 저장
                StringBuilder sb = new StringBuilder(); //버퍼리더 값 출력하기 위해 준비

                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line); //버퍼리더 에있는 정보를 sb로 저장 출력하기위해
                }
                bufferedReader.close(); //버퍼리더 종료
                Log.d(TAG,"Local_Email_Chack Result : "+ sb.toString());
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Toast.makeText(ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Email.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Local_Email_Chack Error ", e);
                return new String("Local_Email_Chack  " + e.getMessage());
            }
        }
        //마지막 출력부분 스트링선언 백그라운드 에서 sb를 스트링으로 변환후 여기서 스트링으로 뿌려서 출력
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JS_EmailCheck=result;
            EmailJsResult();
            if(EmailCheck_Value.equalsIgnoreCase(getString(R.string.OneHundred_Five))){//equalsIgnoreCase 란 문자열 비교 , 서버 파일에 문자 열을 인식받아 조건에 맞는 함수 출력
                Err_Chack.setText(getString(R.string.Si_Id_Sucess_Error));  //이미 중복된아이디 텍스트 출력
                Log.d(TAG,"이미 사용된 사용자 이름입니다.");
            }else if(EmailCheck_Value.equalsIgnoreCase(getString(R.string.one))){
                startActivity(new Intent(ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Email.this, Local_Birthday.class));
            }else {
                Toast.makeText(ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Email.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void EmailJsResult(){
        String TAG_JSON,TAG_Value,tag_value;
        TAG_JSON="appamado_idcheck";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(JS_EmailCheck); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                EmailCheck_Value = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG,"EmailJsResult = " + EmailCheck_Value);
            }
        } catch (JSONException e) {
            Toast.makeText(this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "EmailJsResult Error  : ", e);
        }
    }

}
