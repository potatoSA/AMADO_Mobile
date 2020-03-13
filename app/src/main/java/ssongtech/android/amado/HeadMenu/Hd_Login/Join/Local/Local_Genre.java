package ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Local_Genre extends AppCompatActivity{

    CheckBox Genre_Ballade,Genre_Dance,Genre_Trot,Genre_Indie,Genre_Other;
    CheckBox Genre_Rap,Genre_Cover,Genre_RB,Genre_Blues,Genre_Classic,Genre_Edm,Genre_electronica,Genre_Jazz,Genre_Pop,Genre_Rock;
    String Genre_Rap1="",Genre_Cover1="",Genre_RB1="",Genre_Blues1="",Genre_Classic1="",Genre_Edm1="",Genre_electronica1="",Genre_Jazz1="",Genre_Pop1="",Genre_Rock1="",
            Genre_Ballade1="",Genre_Dance1="",Genre_Trot1="",Genre_Indie1="",Genre_Other1="";
    //, 삭제하기 위해 선언
    String[] split;
    String [] Genre = {Genre_Ballade1,Genre_Rap1,Genre_Dance1,Genre_Trot1,Genre_Indie1,Genre_Other1,Genre_Cover1,Genre_RB1,Genre_Rock1
            ,Genre_electronica1,Genre_Blues1,Genre_Pop1,Genre_Edm1,Genre_Jazz1,Genre_Classic1};

    //널인셉션 공백 선언
    String Genre_Result="",Thema_Result="";

    String TAG = "로컬 로그인 로그: ";

    //서버로 보낼 변수
    String Local_email = Local_Email.Local_Email;
    String Local_pw = Local_Email.Local_Pw;
    String Local_nick = Local_Nick.Local_Nick1;
    String Local_birthday = Local_Birthday.join_birthday;
    String Local_gender = Local_Birthday.Sns_Gender;
    String Local_type = "1";  //  회원가입 구별 (SNS , 로컬로그인)

    // Login 결과 받을 변수 , 제이슨 파싱 받을 변수
    String LoginResult,LoginResultJs;


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_genre);

        mContext = Login_Main.Login_Context;


        Button Join_Next = (Button) findViewById(R.id.Genre_Next);
        Genre_Cover = findViewById(R.id.Genre_Cover);
        Genre_Ballade = findViewById(R.id.Genre_Ballade);
        Genre_Dance = findViewById(R.id.Genre_Dance);
        Genre_Trot = findViewById(R.id.Genre_Trot);
        Genre_RB = findViewById(R.id.Genre_RB);
        Genre_Rap = findViewById(R.id.Genre_Rap);
        Genre_Indie = findViewById(R.id.Genre_Indie);
        Genre_Blues = findViewById(R.id.Genre_Blues);
        Genre_Classic = findViewById(R.id.Genre_Classic);
        Genre_Edm = findViewById(R.id.Genre_Edm);
        Genre_electronica = findViewById(R.id.Genre_electronica);
        Genre_Jazz = findViewById(R.id.Genre_Jazz);
        Genre_Pop = findViewById(R.id.Genre_Pop);
        Genre_Rock = findViewById(R.id.Genre_Rock);
        Genre_Other = findViewById(R.id.Genre_Other);



        Join_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Genre_data();
                Naver_transmission();
            }
        });

    }

    private void Genre_data() {
        if (Genre_Ballade.isChecked() == true){ Genre[0] = "001,"; }
        if (Genre_Rap.isChecked() == true) { Genre[1] = "002,"; }
        if (Genre_Dance.isChecked() == true){ Genre[2] = "009,"; }
        if (Genre_Trot.isChecked() == true) { Genre[3] = "004,"; }
        if (Genre_Indie.isChecked() == true) { Genre[4] = "003,"; }
        if (Genre_Other.isChecked() == true) { Genre[5] = "008,"; }


        if (Genre_Cover.isChecked() == true) { Genre[6] = "009,"; }
        if (Genre_RB.isChecked() == true) { Genre[7] = "010,"; }
        if (Genre_Rock.isChecked() == true) { Genre[8] = "011,"; }
        if (Genre_electronica.isChecked() == true) { Genre[9] = "012,"; }
        if (Genre_Blues.isChecked() == true) { Genre[10] = "013,"; }
        if (Genre_Pop.isChecked() == true) { Genre[11] = "014,"; }
        if (Genre_Edm.isChecked() == true) { Genre[12] = "015,"; }
        if (Genre_Jazz.isChecked() == true) { Genre[13] = "016,"; }
        if (Genre_Classic.isChecked() == true) { Genre[14] = "017,"; }


        String genre1 = Genre[0]+Genre[1]+Genre[2]+Genre[3]+Genre[4]+Genre[5]+Genre[6]+Genre[7]+Genre[8]+Genre[9]+Genre[10]+Genre[11]+Genre[12]+Genre[13]+Genre[14];

        split = genre1.split(",");
        for(int i=0; i<split.length; i++){
            Genre_Result += split[i]+",";
        }
        Genre_Result = Genre_Result.substring(0,Genre_Result.length()-1);

        Log.d(TAG,"로컬 회원가입 장르 결과값 :  " +Genre_Result);
        Genre = null;
    }


    private void Naver_transmission() {

        String email =Local_email;
        String pw = Local_pw;
        String nick = Local_nick;
        String birthday = Local_birthday;
        String id = Local_email;
        String gender = Local_gender;
        String provider = "";
        String type = Local_type;
        String genre = Genre_Result;
        String thema = Thema_Result;

        String Local_join = getString(R.string.LOCAL_JOIN);

        Local_Login naverdata = new Local_Login();
        naverdata.execute(Local_join,email,pw,nick,birthday,id,gender,provider,type,genre,thema);
        Log.d(TAG,email+","+pw+","+nick+","+birthday+","+id+","+gender+","+provider+","+type+","+genre+","+thema);

    }


    class Local_Login extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시
        String Wait = getString(R.string.Pleasewait); // 잠시후 다시시도해주세요
        String Success = getString(R.string.Join_Success); //회원가입을 축하합니다
        String one = getString(R.string.one);
        @Override
        protected void onPreExecute() { super.onPreExecute();
            progressDialog = ProgressDialog.show(Local_Genre.this, "plase", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LoginResultJs = result;
            KeyChack();
            if(LoginResult.equals(one)){
                startActivity(new Intent(Local_Genre.this, Login_Main.class));
                finish();
                Toast.makeText(mContext, Success, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Local_Genre.this, Wait, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Local_Genre.this, MainActivity.class));
                finish();
                return;
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String) params[0];
            String Naver_email = (String) params[1];
            String password=(String)params[2];
            String nick=(String)params[3];
            String birthday = (String)params[4];
            String Naver_Idkey = (String) params[5];
            String Naver_gender = (String) params[6];
            String provider=(String)params[7];
            String type = (String) params[8];
            String genre = (String)params[9];
            String thema = (String)params[10];

            String postParameters = "&mb_mobile_id="+Naver_email+
                    "&mb_mobile_password="+password+
                    "&mb_nick="+nick+
                    "&mb_birthday="+birthday+
                    "&mb_mobile_email="+Naver_Idkey+
                    "&mb_gender="+Naver_gender+
                    "&provider="+provider+
                    "&type="+type+
                    "&mb_genre="+genre+
                    "&mb_thema="+thema;

            Log.d(TAG,"로컬연동 회원가입 서버로 보낼 파라메타 : " + postParameters);

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode(); //http연결 돼었는지 확인하기위해 변숫너언
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream; //inputstream 선언

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();  // 연결 잘 돼었을때 inputstream에 url정보 전달
                } else{
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
                Log.d(TAG," 123  " + sb.toString());
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "로컬 회원가입 Task 에러 :  ", e);
                return ("로컬 회원가입 Task 에러 : " + e.getMessage());
            }
        }
    }

    public void KeyChack(){
        String TAG_JSON,TAG_Value,tag_value;
        TAG_JSON="appamado_join";
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        try {
            JSONObject jsonObject = new JSONObject(LoginResultJs); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                LoginResult = tag_value;  // value 정보저장 (ex 101,1,100....)
                Log.d(TAG,"회원가입 Json 결과  = " + LoginResult);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "회원가입 Json 파싱에러  : ", e);
        }
    }
}
