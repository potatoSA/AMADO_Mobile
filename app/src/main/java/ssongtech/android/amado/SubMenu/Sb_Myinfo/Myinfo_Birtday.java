package ssongtech.android.amado.SubMenu.Sb_Myinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import ssongtech.android.amado.Function.Check.AppPreferences;
import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.SubMenu_Main;

public class Myinfo_Birtday extends AppCompatActivity {

    public static String TAG = "생년월일 변경";
    private static String IP_ADDRESS = "192.168.0.100";
    public static String Birtday_JS;
    public static String Birtday_Value;
    String getEmail, getType, getAge;
    TextView Myprofile_afterBirthday, Myprofile_RecentBirtday;
    Context myinfobirt_ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_birtday);
        myinfobirt_ctx=this;

        Myprofile_RecentBirtday = (TextView) findViewById(R.id.Myprofile_RecentBirtday);
        TextView Myprofile_BirtdayErr = (TextView) findViewById(R.id.Myprofile_BirtdayErr);
        Button Myprofile_BirtdayBtn = (Button) findViewById(R.id.Myprofile_BirtdayBtn);
        Myprofile_afterBirthday = (TextView) findViewById(R.id.Myprofile_afterBirthday);

        AppPreferences app = new AppPreferences(myinfobirt_ctx);
        getAge = app.Sharde_getAge();
        getEmail = app.Sharde_getEmail();
        getType = app.Sharde_getType();


        Myprofile_RecentBirtday.setText(getAge);


        Myprofile_BirtdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adsd = Myprofile_afterBirthday.getText().toString();
                Integer abc = Integer.valueOf(adsd);
                if (abc > 2020) {
                    Myprofile_BirtdayErr.setText("미래에서 오셨습니까?");
                } else if (abc < 1920) {
                    Myprofile_BirtdayErr.setText("정말입니까?");
                } else {
                    NickChange nickchange1 = new NickChange();
                    nickchange1.execute("http://" + IP_ADDRESS + "/homepage/mobile/update_mobileprofile.php", getEmail, getType, adsd);
                }
            }
        });
    }


    class NickChange extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // 처음에 클릭시 로딩중
            progressDialog = ProgressDialog.show(myinfobirt_ctx,
                    "Please Wait", null, true, true);
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss(); //로딩창 지우기
            if (result == null) {
            } else {
                Birtday_JS = result;
                NickResult1();
                if (Birtday_Value.equals("1")) {
                    AppPreferences app = new AppPreferences(myinfobirt_ctx);
                    app.Sharde_setAge(Myprofile_afterBirthday.getText().toString());
                    startActivity(new Intent(myinfobirt_ctx, SubMenu_Main.class));
                    finish();
                } else if (Birtday_Value.equals("-1")) {
                    Toast.makeText(myinfobirt_ctx, "출생년도 변경 실패 잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(myinfobirt_ctx, "잠시 후 다시시도해주세요 ", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public String doInBackground(String... params) {
            String serverURL, email, postParameters, type, change; //excute 에 서 받은 값들을 params 변수에 저장

            change = (String) params[3];
            type = (String) params[2];
            email = (String) params[1];
            serverURL = (String) params[0];

            postParameters = "&mb_mobile_id=" + email + "&type=" + type + "&mb_age=" + change; // &mb = 키값저장


            Log.d(TAG, "키값" + postParameters);

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
                return ("NickChack_Error" + e.getMessage());
            }
        }
    }

    public static void NickResult1() {
        String TAG_JSON, TAG_Value, tag_value;
        TAG_JSON = "appamado_profile";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(Birtday_JS); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                Birtday_Value = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG, "Nick_Change = " + Birtday_Value);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
