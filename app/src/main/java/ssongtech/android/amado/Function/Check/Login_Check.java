package ssongtech.android.amado.Function.Check;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;

public class Login_Check extends AppCompatActivity{

    protected String Lg_JsonString1;  //JSON 파싱
    private static String IP_ADDRESS = "192.168.0.100";
    private static String TAG = "ssong_login";
    public static String Chack_Value="";//제이슨 에서 넘오온 숫자값으로 로그인 돼었는지 확인  (ex 1 = 로그인성공  그외 로그인실패)
    protected Context CheckcContext;


    public SharedPreferences a;


    public void Login(String login_id,String login_pw,String login_key,String type,String provid,String email, Context context){

        CheckcContext = context;
        LoginCheck loginCheck = new LoginCheck();
        loginCheck.execute("http://" + IP_ADDRESS + "/homepage/mobile/login_mobile.php", login_id ,login_pw, login_key,type,provid,email); //id,pw,key값을 서버로 던져준다

    }

    class LoginCheck extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Lg_JsonString1=result;
            showResult();
            try {
                AppPreferences app = new AppPreferences(CheckcContext);
                String Login_State = app.Sharde_getType();

                if(Login_State.equals("1")){
                }




//                if(Chack_Value.equals("1")){
//                    Toast.makeText(CheckcContext, "로컬 성공", Toast.LENGTH_SHORT).show();
//                }else if(Login_State.equalsIgnoreCase("1")){
//                    Toast.makeText(CheckcContext, "로그인성공", Toast.LENGTH_SHORT).show();
//                }
//                else if(Login_State.equalsIgnoreCase("2")){
//                    //startActivity(new Intent(CheckcContext, Login_Main.class));
//                }else {
//                    Toast.makeText(CheckcContext, "로그인 안함", Toast.LENGTH_SHORT).show();
//                    //startActivity(new Intent(CheckcContext, Login_Main.class));
////                    Intent i = new Intent(CheckcContext,Login_Main.class);
////                    CheckcContext.startActivity(i);
////                    finish();
//                }
            }catch (Exception e){
                //startActivity(new Intent(CheckcContext, Login_Main.class));
                Toast.makeText(CheckcContext, "로그인에러 다시시도", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "로그인 체크 결과 - " + result);
        }
        @Override
        public String doInBackground(String... params) {

            String Login_id1,Login_pw1,key2,serverURL,postParameters; //excute 에 서 받은 값들을 params 변수에 저장

            Login_id1 = (String)params[1];
            Login_pw1 = (String)params[2];
            key2 = (String)params[3];
            String type = (String)params[4];
            String provide = (String)params[5];
            String email = (String)params[6];
            serverURL = (String)params[0];

            postParameters = "&mb_mobile_id="+Login_id1+"&mb_mobile_password="+Login_pw1+"&mb_mobile_key="+key2+"&type="+type+"&provide="+provide+"&mb_mobile_email="+email; // &mb = 키값저장
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
        TAG_JSON="appamado_login";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(Lg_JsonString1); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                Chack_Value = tag_value;  // value 정보저장 (ex 101,1,100....)
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}

