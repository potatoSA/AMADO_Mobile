package ssongtech.android.amado.SubMenu.Sb_Myinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;

public class Myinfo_Withdrawal extends AppCompatActivity {

    Login_Main loginMain;
    protected static String Withdrawal_Js;

    private static String value;
    private String TAG = "회원탈퇴";
    Context withdrawalCtx;
    private static String IP_ADDRESS = "192.168.0.100";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_withdrawal);

        withdrawalCtx = this;




        TextView Withdrawal_Id = (TextView) findViewById(R.id.Withdrawal_Id);
        TextView Withdrawal_Pw = (TextView) findViewById(R.id.Withdrawal_Pw);
        Button Withdrawal_Btn = (Button) findViewById(R.id.Withdrawal_Btn);

        AppPreferences app = new AppPreferences(withdrawalCtx);
        String abc = app.Sharde_getEmail();
        String type = app.Sharde_getType();
        Withdrawal_Id.setText(abc);

        if(type.equals("2")){
            Withdrawal_Pw.setVisibility(View.GONE);
        }






        Withdrawal_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(withdrawalCtx);

                builder.setTitle("알림!").setMessage("AMADO회원 계정 탈퇴시 해당 ID로는 재가입이 불가능 합니다. 그래도 탈퇴 하시겠습니까?");

                builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String pw = Withdrawal_Pw.getText().toString();
                        String id1 =app.Sharde_getEmail();
                        String type = app.Sharde_getType();
                        String provider = app.Sharde_getProvideer();
                        String key = app.Sharde_getKey();
                        Withdrawal1 localLoginIng = new Withdrawal1();
                        localLoginIng.execute("http://" + IP_ADDRESS + "/homepage/mobile/leave_mobile.php", id1, pw, type, provider, key);
                    }
                });
                builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    class Withdrawal1 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Myinfo_Withdrawal.this,
                    "Please Wait", null, true, true);
        }
        @Override
        public void onPostExecute(String result) {

            super.onPostExecute(result);
            Withdrawal_Js=result;
            showResult();
            progressDialog.dismiss();
            try {
                if(value.equals("1")){
                    startActivity(new Intent(Myinfo_Withdrawal.this, MainActivity.class));
                    finish();
                    Toast.makeText(Myinfo_Withdrawal.this, "회원탈퇴 성공", Toast.LENGTH_SHORT).show();
                }
                    else if(value.equals("-1"))Toast.makeText(Myinfo_Withdrawal.this, "회원탈퇴 실패", Toast.LENGTH_SHORT).show();
                    else if(value.equals("102"))Toast.makeText(Myinfo_Withdrawal.this, "비밀번호를 다시 입력하여 주세요", Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                //startActivity(new Intent(CheckcContext, Login_Main.class));
                Toast.makeText(Myinfo_Withdrawal.this, "회원탈퇴 널값", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "" + result);
        }
        @Override
        public String doInBackground(String... params) {

            String serverURL,postParameters; //excute 에 서 받은 값들을 params 변수에 저장

            String id = (String)params[1];
            String pw = (String)params[2];
            String type = (String)params[3];
            String provider = (String)params[4];
            String key = (String)params[5];

            serverURL = (String)params[0];

            postParameters ="&mb_mobile_id="+id+"&mb_mobile_password="+pw+"&type="+type+"&provider="+provider+"&mb_mobile_key="+key; // &mb = 키값저장

            Log.d(TAG,"확인파라메타"+postParameters);

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
                Log.d(TAG,"서버값"+sb.toString());
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }



    public void showResult(){
        String TAG_JSON,TAG_Value,tag_value;
        TAG_JSON="appamado_common";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(Withdrawal_Js); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                value = tag_value;  // value 정보저장 (ex 101,1,100....)
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }






}
