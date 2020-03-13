package ssongtech.android.amado.HeadMenu.Hd_Login.Join.Sns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.regex.Pattern;

import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.R;

public class Sns_Join extends AppCompatActivity {

    //주소
    protected static String IP_ADDRESS = "192.168.0.100";
    protected static String TAG = "Sns_Join";

    //회원가입시 이메일 보낼값
    protected static String Join_Nick;
    //네이버에서 받는 이메일
    protected static String getNaver_email = Login_Main.Naver_email; //Login_Main 에서 받아온 네이버 이메일값

    //구글에서 받는 값들
    public static String Google_Email = Login_Main.Google_Email;

    //닉체크 제이슨 파일
    public static String NickChackJeson;
    //중복닉 확인
    protected static String Nick_Value;


    String SNS = Login_Main.SNS;

    TextView SnsNick_Err;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_join);


        Button Sns_Next = findViewById(R.id.Sns_Next);
        EditText Sns_Nick = findViewById(R.id.Sns_Nick);
        TextView Sns_Email = findViewById(R.id.Sns_Email);
        SnsNick_Err = findViewById(R.id.SnsNick_Err);


    // 값에 따라 sns 구별
        if(SNS =="201"){  //구글
            Sns_Email.setText(Google_Email);
        }else if(SNS =="202"){ //페북
            Sns_Email.setText("SNS연동 이메일 입니다");
        }else if(SNS == "203"){ //네이버
            Sns_Email.setText(getNaver_email);
        }

        //다음 버튼 클릭시
        Sns_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Join_Nick = Sns_Nick.getText().toString();
                if(Join_Nick.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){
                    if(Join_Nick.length()<=1){
                        SnsNick_Err.setText("글자수 2~10글자 사이 가능");
                        Log.d(TAG,"SnsNick = 글자수 미달");
                    }else{
                        NickChack nickChack = new NickChack();
                        nickChack.execute("http://" + IP_ADDRESS +"/homepage/mobile/check_mobilename.php", Join_Nick);
                        Log.d(TAG, "SnsNick = 닉네임 중복확인 성공");
                    }
                }else {
                    SnsNick_Err.setText("사용불가 닉네임");
                    Log.d(TAG,"SnsNick = 사용불가 닉네임");
                }
            }
        });
    }


    //패턴
    public static final Pattern NickPatten = Pattern.compile(
            "([0-9].*[^!,@,#,^,&,*,(,)])|([^!,@,#,^,&,*,(,)].*[2,10])|[가-힣]*$"
    );
    public boolean NickPatten(String email) {
        return NickPatten.matcher(email).matches();
    }

    //닉네임 체크
    class NickChack extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시
    @Override
    public void onPreExecute() {
        super.onPreExecute();
        // 처음에 클릭시 로딩중
        progressDialog = ProgressDialog.show(Sns_Join.this,
                "Please Wait", null, true, true); }
    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss(); //로딩창 지우기
        if (result == null){
            SnsNick_Err.setText("닉네임 확인후 다시 클릭하여 주세요"); }
        else {
            NickChackJeson = result;
            NickResult();
            if(Nick_Value.equals("1")){
                startActivity(new Intent(Sns_Join.this, Sns_Birthday.class));
            }else{
                SnsNick_Err.setText("중복된아이디입니다");
            }
        }
    }
    @Override
    public String doInBackground(String... params) {
        String serverURL,snsnick,postParameters; //excute 에 서 받은 값들을 params 변수에 저장

        snsnick = (String)params[1];
        serverURL = (String)params[0];

        postParameters = "&mb_nick=" + snsnick; // &mb = 키값저장

        Log.d(TAG,"NickChack :  "+postParameters);
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
            Log.d(TAG,"NickChack_Result : " +sb);
            return sb.toString();  //sb로 반환
        } catch (Exception e) {
            Log.d(TAG, "NickChack_Error", e);
            return new String("NickChack_Error" + e.getMessage());
        }
    }
}
    public static void NickResult(){
        String TAG_JSON,TAG_Value,tag_value;
        TAG_JSON="appamado_namecheck";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(NickChackJeson); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                Nick_Value = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG,"Nick_chackValue = " + Nick_Value);
            }
        } catch (JSONException e) {
            Log.d(TAG, "NickResultErr : ", e);
        }
    }
}

