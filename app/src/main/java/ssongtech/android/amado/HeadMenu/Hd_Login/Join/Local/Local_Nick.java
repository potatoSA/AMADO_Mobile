package ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Local_Nick extends AppCompatActivity {

    protected static EditText Local_Nick;
    protected static TextView Local_NickErr;
    ImageView Local_NextBtn;

    public static String Local_Nick1;
    private static String TAG = "로컬회원가입(닉네임)";
    private static String IP_ADDRESS = "192.168.0.100";

    protected static String NickChackJeson;
    protected static String Nick_Value;

    CheckBox checkBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.join_nick);

        Local_Nick = findViewById(R.id.Local_Nick);
        Local_NickErr = findViewById(R.id.Local_NickErr);
        Local_NextBtn = findViewById(R.id.Local_NextBtn);
        checkBox = findViewById(R.id.checkBox);


        /**
         *
         * 텍스트 출력
         *
         */

        TextView Nick_Text = findViewById(R.id.Nick_Text);
        Nick_Text.setText(Html.fromHtml("<b>닉네임</b>" + "을 입력해주세요."));



        //다음 버튼 클릭시
        Local_NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Local_Nick1 = Local_Nick.getText().toString();
                if(Local_Nick1.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){
                    if(Local_Nick1.length()<=1){
                        Local_NickErr.setText("글자수 2~10글자 사이 가능");
                        Log.d(TAG,"닉네임 : " +"글자수 작음");
                    }else{
                        Local_NickErr.setText("");
                        NickChack nickChack = new NickChack();
                        nickChack.execute("http://" + IP_ADDRESS +"/homepage/mobile/check_mobilename.php", Local_Nick1);
                        Log.d(TAG, "닉네임 : " + "사용 가능한 닉네임");
                    }
                }else {
                    Local_NickErr.setText("특수문자 사용 불가");
                    Log.d(TAG,"닉네임 : " +"사용 불가 닉네임");
                }

            }
        });

        CheckBox checkBox = findViewById(R.id.checkBox);


        checkBox.setText("가입함으로써 이용 약관에 동의합니다. 개인 정보 보호 정책 및 쿠키 정책을 읽고 이해했습니다");

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Pattern pattern = Pattern.compile("이용약관");
        Pattern pattern1 = Pattern.compile("개인 정보 보호 정책");
        Pattern pattern2 = Pattern.compile("쿠키 정책");

        Linkify.addLinks(checkBox,pattern,"http://naver.com",null,mTransform);
        Linkify.addLinks(checkBox,pattern1,"http://naver.com",null,mTransform);
        Linkify.addLinks(checkBox,pattern2,"http://naver.com",null,mTransform);



    }

    //닉네임 체크
    class NickChack extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // 처음에 클릭시 로딩중
//            progressDialog = ProgressDialog.show(ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Nick.this,
//                    "Please Wait", null, true, true);
        }
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss(); //로딩창 지우기
            if (result == null){
                Local_NickErr.setText("닉네임 확인후 다시 클릭하여 주세요"); }
            else {
                NickChackJeson = result;
                NickResult();
                if(checkBox.isChecked()){
                    if(Nick_Value.equals("1")){
                        startActivity(new Intent(ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Nick.this, Local_Genre.class));
                    }else{
                        Local_NickErr.setText("중복된아이디입니다");
                    }
                }else {
                    Local_NickErr.setText("약관을 체크해주세요");
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
                Log.d(TAG,"Nick_chack = " + Nick_Value);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


}
