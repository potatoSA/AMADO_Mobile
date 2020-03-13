package ssongtech.android.amado.HeadMenu.Hd_Login.Login;
/*************************************************************
 클래스명   : Login_pwsch
 설명       : 비밀번호 찾기
 생성일     : 2020.02.26
 생성자     : 박기태
 1차 수정일 :
 1차 수정자 :
 param      :  email  서버로 보낼 이메일 값
 return     :  TAG_Value  // 결과 값
 **************************************************************/
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import ssongtech.android.amado.R;

public class Login_pwsch extends AppCompatActivity {

    private String TAG = "비밀번호 찾기 : ";
    String Pwsch_JS ;  // JSON 파싱 파일
    String Pwsch_Value; //벨류값
    private static String Email;
    private EditText Pwsch_Email;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_pwsch);

        Pwsch_Email = findViewById(R.id.Pwsch_Email);
        Button Pwsch_Button = findViewById(R.id.Pwsch_Button);


        Pwsch_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Pwsch_check_mobilepw = getString(R.string.Pwsch_check_mobilepw);

                String email = Pwsch_Email.getText().toString();
                Pwsch pwsch = new Pwsch();
                pwsch.execute(Pwsch_check_mobilepw, email); //id,pw,key값을 서버로 던져준다
            }
        });
    }


    class Pwsch extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // 처음에 클릭시 로딩중
            progressDialog = ProgressDialog.show(Login_pwsch.this, "잠시만 기다려 주세요", null, true, true);
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss(); //로딩창 지우기
            String one = getString(R.string.one);
            String Mone= getString(R.string.Mone);
            String Pwsch_Error = getString(R.string.Pwsch_Error);
            String OneHundred_Seven = getString(R.string.OneHundred_Seven);
            String Sever_Error = getString(R.string.Sever_Error);
            String Success = getString(R.string.Success);
            String Pleasewait = getString(R.string.Pleasewait);
            String Pwsch_EamilError =getString(R.string.Pwsch_EamilError);
            String OneHundred_Nine = getString(R.string.OneHundred_Nine);
            try {
                if (result == null) {
                    Toast.makeText(Login_pwsch.this, Pleasewait, Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Pwsch result_Error : "+result );
                } else {
                    Log.d(TAG,"Pwsch result : " + result);
                    Pwsch_JS = result;
                    PwschshowResult();
                    Email = Pwsch_Email.getText().toString();
                    if(Pwsch_Value.equals(one)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_pwsch.this);
                        builder.setMessage(Email+Pwsch_Error);
                        builder.setPositiveButton(Success, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else if(Pwsch_Value.equals(Mone)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_pwsch.this);
                        builder.setMessage(Sever_Error);
                        builder.setPositiveButton(Success, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else if(Pwsch_Value.equals(OneHundred_Seven)){
                        Log.d(TAG,"Pwsch_: 파라메타 오류");
                    }else if(Pwsch_Value.equals(OneHundred_Nine)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_pwsch.this);
                        builder.setMessage(Pwsch_EamilError);
                        builder.setPositiveButton(Success, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(Login_pwsch.this, Pleasewait, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public String doInBackground(String... params) {

            String serverURL,postParameters; //excute 에 서 받은 값들을 params 변수에 저장

            String email = (String)params[1];
            serverURL = (String)params[0];

            postParameters = "&mb_mobile_id="+email; // &mb = 키값저장

            Log.d(TAG,"Pwsch_postParametres "+postParameters);
            try {
                URL url = new URL(serverURL); //url 선언
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //httpurl에 url 연결 준비

//                httpURLConnection.setReadTimeout(5000);  // 5초뒤 종료 (옵션)
//                httpURLConnection.setConnectTimeout(5000); // 5초뒤 종료 (옵션)
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
                Log.d(TAG,"pwsch_값 :  "+sb);
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Log.d(TAG, "Pwsch_Error ", e);
                return new String("Pwsch_Error: " + e.getMessage());
            }
        }
    }


    //JS파싱
    public void PwschshowResult(){
        String TAG_JSON,TAG_Value,tag_value;
        TAG_JSON="appamado_idcheck";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        StringBuffer sb = new StringBuffer(); //sb에 stringBuffer 선언
        try {
            JSONObject jsonObject = new JSONObject(Pwsch_JS); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                Pwsch_Value = tag_value;  //value 정보 저장
            }
            Log.d(TAG,"Pwsch_Value = " + Pwsch_Value);
        } catch (JSONException e) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            Log.d(TAG, " PwschshowResult error : ", e);
        }
    }
}
