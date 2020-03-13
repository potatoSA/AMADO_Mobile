package ssongtech.android.amado.SubMenu.Sb_Myinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import ssongtech.android.amado.Function.Check.AppPreferences;
import ssongtech.android.amado.Function.Check.Login_Check;
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.SubMenu_Main;

public class Myinfo_Nick extends AppCompatActivity {

    public static String TAG = "Myprofile_Nick";
    private static String IP_ADDRESS = "192.168.0.100";
    protected static String Myprofile_Nick_JS;
    TextView Myprofile_Nick;
    EditText MYprofile_NIckChange;
    TextView Myprofile_NickErr;
    Button Myprofile_NickBtn;
    protected static String NickCheck;
    protected String login_id;
    protected String login_pw;
    protected String login_key;


    protected static String Change_JS;
    public static String getEmail;
    public static String getType;

    public static Context SubMenu_Context = SubMenu_Main.SubMenuctx;
    protected static SharedPreferences Snsdata;

    String change,getNick;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_nick);
        Myprofile_Nick = findViewById(R.id.Myprofile_recentNick);
        MYprofile_NIckChange = findViewById(R.id.Myprofile_NickChanger);
        Myprofile_NickErr = findViewById(R.id.Myprofile_NNickErr);
        Myprofile_NickBtn = findViewById(R.id.Myprofile_NickBtn);

        Snsdata = getSharedPreferences("a",MODE_PRIVATE);
        Key_Chack();
        Myprofile_NickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Myprofile_Nick1 = MYprofile_NIckChange.getText().toString();
                if (Myprofile_Nick1.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|()]*")) {
                    if (Myprofile_Nick1.length() <= 1) {
                        Myprofile_NickErr.setText("글자수 2~10글자 사이 가능");
                        Log.d(TAG, "닉네임 : " + "글자수 작음");
                    } else {
                        //Myprofile_NickErr.setText("사용가능닉네임");
                        NickChack nickChack = new NickChack();
                        nickChack.execute("http://" + IP_ADDRESS + "/homepage/mobile/check_mobilename.php", Myprofile_Nick1);
                        Log.d(TAG, "닉네임 : " + "사용 가능한 닉네임");
                    }
                } else {
                    Myprofile_NickErr.setText("특수문자 사용 불가");
                    Log.d(TAG, "닉네임 : " + "사용 불가 닉네임");
                }
            }
        });


        AppPreferences app = new AppPreferences(Myinfo_Nick.this);
        getNick = app.Sharde_getNick();
        getEmail = app.Sharde_getEmail();
        getType = app.Sharde_getType();
        Myprofile_Nick.setText(getNick);

    }
    //중복확인
    class NickChack extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시

        @Override
        public void onPreExecute() {
            super.onPreExecute();
//            // 처음에 클릭시 로딩중
//            progressDialog = ProgressDialog.show(Myinfo_Nick.this,
//                    "Please Wait", null, true, true);
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss(); //로딩창 지우기
            if (result == null) {
                Myprofile_NickErr.setText("닉네임 확인후 다시 클릭하여 주세요");
            } else {
                Myprofile_Nick_JS = result;
                NickCheckResult();
                if (NickCheck.equals("1")) {
                    change = MYprofile_NIckChange.getText().toString();
                    NickChange nickchange1 = new NickChange();
                    nickchange1.execute("http://" + IP_ADDRESS + "/homepage/mobile/update_mobileprofile.php",getEmail,getType,change);
                } else {
                    Myprofile_NickErr.setText("중복된아이디입니다");
                }
            }
        }

        @Override
        public String doInBackground(String... params) {
            String serverURL, snsnick, postParameters; //excute 에 서 받은 값들을 params 변수에 저장

            snsnick = (String) params[1];
            serverURL = (String) params[0];

            postParameters = "&mb_nick=" + snsnick; // &mb = 키값저장

            Log.d(TAG, "NickChack :  " + postParameters);
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

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();  // 연결 잘 돼었을때 inputstream에 url정보 전달
                } else {
                    inputStream = httpURLConnection.getErrorStream(); //연결 안돼었을때 에러
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8"); //서버에서 정보 가져오기위한 준비 ,UTF-8로인코딩
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //서버에서 값 들고오면 버퍼리더에 저장
                StringBuilder sb = new StringBuilder(); //버퍼리더 값 출력하기 위해 준비

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line); //버퍼리더 에있는 정보를 sb로 저장 출력하기위해
                }
                bufferedReader.close(); //버퍼리더 종료
                Log.d(TAG, "NickChack_Result : " + sb);
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Log.d(TAG, "NickChack_Error", e);
                return new String("NickChack_Error" + e.getMessage());
            }
        }
    }

    public static void NickCheckResult() {
        String TAG_JSON, TAG_Value, tag_value;
        TAG_JSON = "appamado_namecheck";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(Myprofile_Nick_JS); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                NickCheck = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG, "Nick_chack = " + NickCheck);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    public static void NickChangeResult() {
        String TAG_JSON, TAG_Value, tag_value;
        TAG_JSON = "appamado_profile";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(Myprofile_Nick_JS); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                NickCheck = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG, "Nick_Change = " + NickCheck);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


    protected void Key_Chack(){
        String SnsChack = "1";
        String provide ="";
        String email ="";
        Context Cl_Context = Myinfo_Nick.this;
        login_id = Login_Main.Lg_login_id;  //ID값 불러오기
        login_key = Login_Main.Chack_Key; //로그인 했던 키값 불러오기
        Login_Check loginChack = new Login_Check();
        loginChack.Login(login_id,login_pw,login_key,SnsChack,provide,email,Cl_Context);
    }



    class NickChange extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시

        @Override
        public void onPreExecute() {
//            super.onPreExecute();
//            // 처음에 클릭시 로딩중
//            progressDialog = ProgressDialog.show(Myinfo_Nick.this,
//                    "Please Wait", null, true, true);
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss(); //로딩창 지우기
            if (result == null) {
            } else {
                Change_JS = result;
                NickChangeResult();
                if(NickCheck.equals("1")){
                    AppPreferences app = new AppPreferences(Myinfo_Nick.this);
                    app.Sharde_setNick(MYprofile_NIckChange.getText().toString());
                    startActivity(new Intent(SubMenu_Context,SubMenu_Main.class));
                    finish();
                    Toast.makeText(SubMenu_Context, "닉네임 변경 성공", Toast.LENGTH_SHORT).show();
                }else if(NickCheck.equals("-1")) {
                    Toast.makeText(Myinfo_Nick.this, "닉네임 변경 실패 ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Myinfo_Nick.this, "잠시 후 다시시도해주세요 ", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public String doInBackground(String... params) {
            String serverURL, email, postParameters,type,change; //excute 에 서 받은 값들을 params 변수에 저장
            change=(String)params[3];
            type = (String)params[2];
            email = (String) params[1];
            serverURL = (String) params[0];

            postParameters ="&mb_mobile_id="+email+"&type="+type+"&mb_nick="+change; // &mb = 키값저장
            Log.d(TAG,"키값" + postParameters);

            Log.d(TAG, "NickChack :  " + postParameters);
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

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();  // 연결 잘 돼었을때 inputstream에 url정보 전달
                } else {
                    inputStream = httpURLConnection.getErrorStream(); //연결 안돼었을때 에러
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8"); //서버에서 정보 가져오기위한 준비 ,UTF-8로인코딩
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //서버에서 값 들고오면 버퍼리더에 저장
                StringBuilder sb = new StringBuilder(); //버퍼리더 값 출력하기 위해 준비

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line); //버퍼리더 에있는 정보를 sb로 저장 출력하기위해
                }
                bufferedReader.close(); //버퍼리더 종료
                Log.d(TAG, "NickChange_Result : " + sb);
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Log.d(TAG, "NickChange_Error", e);
                return new String("NickChack_Error" + e.getMessage());
            }
        }
    }
}

