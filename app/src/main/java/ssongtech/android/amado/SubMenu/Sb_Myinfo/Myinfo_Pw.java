package ssongtech.android.amado.SubMenu.Sb_Myinfo;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class Myinfo_Pw extends AppCompatActivity {

    public static String TAG = "Myprofile_PwChange";
    protected static String PwChange_JS;
    protected static String PwChange_Value;
    protected static String IP_ADDRESS = "192.168.0.100";

    SharedPreferences a;

    String email,type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_pw);




        EditText Myprofile_Pw = findViewById(R.id.Myprofile_Pw);
        EditText Myprofile_Pw2 = findViewById(R.id.Myprofile_Pw2);
        TextView Myprofile_PwErr = findViewById(R.id.Myprofile_PwErr);
        Button Myprofile_PwBtn = findViewById(R.id.Myprofile_PwBtn);

        a = getSharedPreferences("a",MODE_PRIVATE);
        String first2 = a.getString("Gender","");
        email=a.getString("Email","");
        type=a.getString("type","");




        Myprofile_PwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validatePassword(Myprofile_Pw.getText().toString())){
                    if(Myprofile_Pw.getText().toString().equals(Myprofile_Pw2.getText().toString())){
                        Myprofile_PwErr.setText("");
                        NickChange nickChange = new NickChange();
                        nickChange.execute("http://" + IP_ADDRESS + "/homepage/mobile/update_mobileprofile.php",email,type,Myprofile_Pw.getText().toString());
                    } else {
                        Myprofile_PwErr.setText("비밀번호가 올바르지 않습니다");
                    }
                }else {
                    Myprofile_PwErr.setText("비밀번호 형식이 다릅니다");
                    Log.d(TAG,"회원가입 중 : 실패 ");
                }
            }
        });






    }

    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9]).{8,15}.$"); // 8자리 ~ 16자리까지 가능

    // 비밀번호 검사
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }


    class NickChange extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // 처음에 클릭시 로딩중
            progressDialog = ProgressDialog.show(Myinfo_Pw.this,
                    "Please Wait", null, true, true);
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss(); //로딩창 지우기
            if (result == null) {
            } else {
                PwChange_JS = result;
                NickResult1();
                if(PwChange_Value.equals("1")){
                    //Myprofile_Gender_Spinner.getSelectedItem().toString();
//                    SharedPreferences.Editor editor = a.edit();
//                    editor.putString("Gender",Myprofile_Gender_Spinner.getSelectedItem().toString());
//                    editor.apply();
//                    startActivity(new Intent(test2.this,SubMenu_Main.class));
                    Toast.makeText(Myinfo_Pw.this, "성별 변경 완료", Toast.LENGTH_SHORT).show();
                }else if(PwChange_Value.equals("-1")) {
                    Toast.makeText(Myinfo_Pw.this, "닉네임 변경 실패 ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Myinfo_Pw.this, "잠시 후 다시시도해주세요 ", Toast.LENGTH_SHORT).show();
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
            postParameters ="&mb_mobile_id="+email+"&type="+type+"&mb_mobile_password="+change; // &mb = 키값저장

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

    public static void NickResult1() {
        String TAG_JSON, TAG_Value, tag_value;
        TAG_JSON = "appamado_profile";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(PwChange_JS); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                PwChange_Value = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG, "Nick_Change = " + PwChange_Value);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
