package ssongtech.android.amado.HeadMenu.Hd_Login.Login;
/*************************************************************
 클래스명   : Login_Main
 설명       : 로그인 및 인증키를 체크한다.
 생성일     : 2020.01.31
 생성자     : 박기태
 1차 수정일 :
 1차 수정자 :
 param      : Chack_Value,Chack_Key,"mb_nick","mb_id","mb_genre","mb_gender","mb_age","mb_email";
 return     : email,type,provider,pw
 **************************************************************/
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ssongtech.android.amado.Function.Check.AppPreferences;
import ssongtech.android.amado.HeadMenu.Hd_Login.Join.Local.Local_Email;
import ssongtech.android.amado.HeadMenu.Hd_Login.Join.Sns.Sns_Join;
import ssongtech.android.amado.MainActivity;
import ssongtech.android.amado.R;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Login_Main extends AppCompatActivity {
    /*******************************************Context*******************************************/
    public static Context Login_Context;
    /*******************************************공통 *******************************************/
    public static String SNS="";
    String TAG = "Login_Main";
    /*******************************************로그인 상태 체크*******************************************/
    public static String Lg_login_id;
    public static String Chack_Value, Chack_Key, login_id, login_pw, login_key;
    /*******************************************JS*******************************************/
    private String Lg_JsonString;
    private String LoginResult;
    /*******************************************구글 로그인 변수*******************************************/
    SignInButton googlebtn;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    public static String Google_Email;
    /*******************************************네이버로그인 변수*******************************************/
    public static OAuthLogin mOAuthLoginModule;
    OAuthLoginButton mOAuthLoginButton;
    public static String Naver_Token;
    public static String Naver_email;  //네이버 연동 이메일
    public static String Naver_gender; //네이버 연동 젠더
    public static Integer Naver_Id; //네이버 연동 젠더
    public static String Naver_Email;
    /*******************************************페이스북 로그인 변수*******************************************/
    private LoginButton Facebook_Login;
    private CallbackManager mCallbackManager;
    public static String Facebook_Email;
    public static String Facebook_Id;
    /*******************************************데이터 저장 변수*******************************************/
    public static String Sharde_email,Sharde_nick,Sharde_gender,Sharde_age,Sharde_Type,
            Sharde_Provider,Sharde_Key;
    /*******************************************변수*******************************************/
    TextView Lg_Errorchack;  //로그인 에러 텍스트
    EditText Lg_id, Lg_pw;  //ID, PW
    CheckBox LoginState; //로그인 상태 유지
    ScrollView linearLayout2; //스낵바 사용하기위해 선언

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        /**Context 선언**/
        Login_Context = this;

        /**변수 선언**/
        Lg_id = findViewById(R.id.LOGIN); //id
        Lg_pw = findViewById(R.id.PW); //pw
        Lg_Errorchack = findViewById(R.id.ErrorCheck); //로그인화면에서 비밀번호가 잘못됐습니다,아이디,비밀번호 다시확인하세요 체크 하는것
        LoginState = findViewById(R.id.LoginState);
        linearLayout2 = findViewById(R.id.linearLayout2);
        mOAuthLoginButton = findViewById(R.id.button_naverlogin);  //네이버 로그인
        ImageButton Lg_btn = (ImageButton) findViewById(R.id.Login_Btn); //로그인 버튼
        TextView Lg_sch = (TextView) findViewById(R.id.IDSCH); // 아이디,비밀번호 찾기
        TextView Lg_sing = (TextView) findViewById(R.id.SINGUP);   //회원가입버튼

        /*******************************************페이스북 로그인*******************************************/
        Facebook_Login =findViewById(R.id.Facebook_Login);
        mCallbackManager = CallbackManager.Factory.create();
        Facebook_Login.setReadPermissions(Arrays.asList("email","public_profile"));
        Facebook_Login.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { }
            @Override
            public void onCancel() {
                Toast.makeText(Login_Main.this, "로그인 실패", Toast.LENGTH_SHORT).show(); }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login_Main.this, "아이디,비밀번호 오류", Toast.LENGTH_SHORT).show(); }});

        /*******************************************구글 로그인*******************************************/
        //프로필. ID 및 기본 프로필은 DEFAULT_SIGN_IN에 포함되어 있습니다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googlebtn = findViewById(R.id.sign_in_button);
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        Google_Login();
                        break;
                }
            }
        });

        /*******************************************로그인 클릭*******************************************/
        Lg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_mobile_check = getString(R.string.login_mobile_check);
                String Null = getString(R.string.Null);
                if(Lg_id.getText().toString().equals("") ){
                    Lg_Errorchack.setText(Null);
                }else if(Lg_pw.getText().toString().equals("")){
                    Lg_Errorchack.setText(Null);
                }else {
//                    login_id = Lg_id.getText().toString();  //lg_id값  > login_id 로 저장
//                    login_pw = Lg_pw.getText().toString(); //lg_pw값  > login_pw 로 저장
//                    login_key = ""; //lg_keynumber값  > login_key 로 저장

                    String id = Lg_id.getText().toString();
                    String pw = Lg_pw.getText().toString();
                    String email = "";
                    String provide = "";
                    String type = "1";
                    // 회원가입 클래스 정의
                    LoginData loginData = new LoginData();
                    loginData.execute(login_mobile_check, id,type,provide,email); //id,pw,key값을 서버로 던져준다
                }
            }
        });
        /******************************************* 비밀번호 찾기*******************************************/
        Lg_sch.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { startActivity(new Intent(Login_Main.this,Login_pwsch.class)); }});
        //네이버 실행
        setNaver();
        /******************************************* 로그인 메인창 텍스트 출력 및 링크*******************************************/
        TextView LoginMAin_Text2 =findViewById(R.id.LoginMAin_Text2);
        String Login_Main_Text = getString(R.string.Login_Main_Text);
        String Link = getString(R.string.Link);
        String Link_Text = getString(R.string.Link_Text);
        LoginMAin_Text2.setText(Login_Main_Text);
        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile(Link_Text);
        Linkify.addLinks(LoginMAin_Text2,pattern,Link,null,mTransform);


        Lg_sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Context, Local_Email.class));
            }
        });


    }
    /*******************************************네이버 로그인*******************************************/
    private void setNaver() {
        String OAUTH_CLIENT_ID = getString(R.string.OAUTH_CLIENT_ID);
        String OAUTH_CLIENT_SECRET = getString(R.string.OAUTH_CLIENT_SECRET);
        String OAUTH_CLIENT_NAME = getString(R.string.OAUTH_CLIENT_NAME);
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        mOAuthLoginButton = findViewById(R.id.button_naverlogin);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {

                Naver_Token = mOAuthLoginModule.getAccessToken(Login_Context);
                Naver_Profile task = new Naver_Profile();
                task.execute(Naver_Token);
                String refreshToken = mOAuthLoginModule.getRefreshToken(Login_Context);
                long expiresAt = mOAuthLoginModule.getExpiresAt(Login_Context);
                String tokenType = mOAuthLoginModule.getTokenType(Login_Context);
                Log.d(TAG,"네이버 로그인 토큰 값" + Naver_Token);
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(Login_Context).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(Login_Context);
                Toast.makeText(Login_Context, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };
    class Naver_Profile extends AsyncTask<String, Void, String> {
        String Pleasewait = getString(R.string.Pleasewait);
        String result;
        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                Snackbar.make(linearLayout2,Pleasewait,Snackbar.LENGTH_SHORT).show();
            }return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(result);
                Log.d(TAG,"Naver Login result :"+result);
                if (object.getString("resultcode").equals("00")) {
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    Naver_email = jsonObject.getString("email");
                    Naver_gender = jsonObject.getString("gender");
                    Naver_Id = jsonObject.getInt("id");
                    Naver_Email = String.valueOf(Naver_Email);
                    NaverLogin();
                    Log.d(TAG, "jsonObject"+jsonObject.toString());
                }
            } catch (Exception e) {
                Log.d(TAG, "Naver Login result Error : "+ e);
                Snackbar.make(linearLayout2,Pleasewait,Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    public void NaverLogout(){
        mOAuthLoginModule.logout(Login_Context);
    }
    /*******************************************로그인 이벤트******************************************/
    public void NaverLogin(){
        String login_mobile_check = getString(R.string.login_mobile_check);
        SNS =getString(R.string.TwoHundred_Three);
        Sharde_Type =getString(R.string.two);
        Sharde_Provider = getString(R.string.naver);
        loginsu naverdata = new loginsu();
        naverdata.execute(login_mobile_check,Naver_email, Naver_Email,Sharde_Type,Sharde_Provider);
    }
    public void facelogin(){
        String login_mobile_check = getString(R.string.login_mobile_check);
        SNS = getString(R.string.TwoHundred_Two);
        Sharde_Type =getString(R.string.two);
        Sharde_Provider =getString(R.string.facebook);
        loginsu facedata = new loginsu();
        facedata.execute(login_mobile_check,Facebook_Email,Facebook_Id,Sharde_Type,Sharde_Provider);
    }
    protected void googlelogin(){
        String login_mobile_check = getString(R.string.login_mobile_check);
        SNS =getString(R.string.TwoHundred_One);
        Sharde_Type =getString(R.string.two);
        Sharde_Provider =getString(R.string.google);
        loginsu googledata = new loginsu();
        googledata.execute(login_mobile_check,Google_Email,Google_Email,Sharde_Type,Sharde_Provider);
    }
    /*******************************************구글 데이터*******************************************/
    //구글 로그인 클릭시 아이디 선택 화면
    //사용자에게 인증 허가 장면 보여줌
    private void Google_Login(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            Google_Email = acct.getEmail();
            googlelogin();
        } catch (ApiException e) {
            Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void Google_Logout() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Login_Main.this, "Signed out successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }); }
    /*******************************************페이스북 데이터 정보*******************************************/
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null) {
            }else{
                userProfile(currentAccessToken);
            }
        }
    };
    protected void userProfile(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    try {
                        Facebook_Email = object.getString("email");
                        Facebook_Id = object.getString("id");
                    }catch (Exception e){
                        Facebook_Id = object.getString("id");
                    }
                    facelogin();
                    Log.d(TAG,"Facebook Login Success");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG,"Facebook Login Err");
                }
            }
        });
        Bundle parmeters = new Bundle();
        parmeters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parmeters);
        request.executeAsync();
    }
    public void Facebook_Logout(){
        LoginManager.getInstance().logOut();
    }

    /*******************************************로컬로그인 존재하는 아이디인지 아닌지 체크*******************************************/
    class LoginData extends AsyncTask<String, Void, String> {
        final String Lg_Restart=getString(R.string.Lg_Restart);
        final String Lg_Idpw_err = getString(R.string.Lg_Idpw_err);
        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null){
                Lg_Errorchack.setText(Lg_Restart); }
            else {
                try {
                    String login_mobile = getString(R.string.login_mobile);
                    Lg_JsonString = result;
                    showResult();
                    if(Chack_Value.equals(getString(R.string.OneHundred_Seven))){ //아이디 오류
                        Lg_Errorchack.setText(Lg_Idpw_err);
                    }else if(Chack_Value.equals(getString(R.string.OneHundred_Seven))){ //비밀번호 오류
                        Lg_Errorchack.setText(Lg_Idpw_err);
                        Lg_pw.setText("");
                    }else if(Chack_Value.equals(getString(R.string.OneHundred_Five))) { //로그인 체크 성공
                        String type = "1";
                        String id = Lg_id.getText().toString();
                        String pw = Lg_pw.getText().toString();
                        String email = "";
                        String provider = "";
                        String key = "";
                        LocalLoginIng localLoginIng = new LocalLoginIng();
                        localLoginIng.execute(login_mobile,id,pw,type,provider,email,key);
                    }else if(Chack_Value.equals(R.string.OneHundred_Eleven)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Context);
                        builder.setMessage(getString(R.string.Email_Check));
                        builder.setPositiveButton(getString(R.string.Success), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else if(Chack_Value.equals(getString(R.string.OneHundred_twelve))){
                        Toast.makeText(Login_Main.this, getString(R.string.secession), Toast.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        public String doInBackground(String... params) {
            String serverURL,postParameters; //excute 에 서 받은 값들을 params 변수에 저장
            String id = (String)params[1];
            String type = (String)params[2];
            String provide = (String)params[3];
            String email = (String)params[4];
            serverURL = (String)params[0];

            postParameters = "&mb_mobile_id="+id+"&type="+type+"&provide="+provide+"&mb_mobile_email="+email; // &mb = 키값저장

            Log.d(TAG,"LoginData Parameters : "+postParameters);
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
                Log.d(TAG, "Local Login Check POST response code - " + responseStatusCode);

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
                Log.d(TAG,"LoginDatak Success : "+sb);
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "LoginData: Error ", e);
                return new String("LoginData : Error " + e.getMessage());
            }
        }
    }
    public void showResult(){
        String TAG_JSON,TAG_Key,TAG_Value,tag_key,tag_value;
        TAG_JSON="appamado_login";
        TAG_Key = "key";  // 토큰값 들고오기
        TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)

        try {
            JSONObject jsonObject = new JSONObject(Lg_JsonString); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언

            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                tag_value = item.getString(TAG_Value);
                tag_key = item.getString(TAG_Key);
                Chack_Value = tag_value;  //value 정보 저장
                Chack_Key = tag_key;
            }
        } catch (JSONException e) {
            Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "showResult Err : ", e);
        }
    }
    /*******************************************SNS 로그인*******************************************/
    class loginsu extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() { super.onPreExecute(); }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login_Context);
                LoginResult =result;
                LocalData();
                if(Chack_Value.equals(getString(R.string.one))){ //SNS로그인 성공
                    Sharde_Type = getString(R.string.two);
                    save3();
                    startActivity(new Intent(Login_Main.this,MainActivity.class));
                    finish();
                }else if(Chack_Value.equals(getString(R.string.OneHundred_Nine))){
                    startActivity(new Intent(Login_Context, Sns_Join.class));  //회원가입 화면이동
                }else if(Chack_Value.equals(getString(R.string.Mone))){
                    Toast.makeText(Login_Main.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
                }else if(Chack_Value.equals(getString(R.string.OneHundred_Eight))){
                    builder.setMessage(getString(R.string.Member_Exist));
                    builder.setPositiveButton(R.string.Success, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if(Chack_Value.equals(getString(R.string.OneHundred_Five))){
                    builder.setMessage(getString(R.string.Member_Exist));
                    builder.setPositiveButton(R.string.Success, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if(Chack_Value.equals(getString(R.string.OneHundred_Seven))){
                    startActivity(new Intent(Login_Context, Sns_Join.class));  //회원가입 화면이동
                }else if(Chack_Value.equals(getString(R.string.OneHundred_twelve))){
                    Toast.makeText(Login_Main.this, getString(R.string.secession), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Login_Main.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Log.d(TAG,"loginsu err"+e);
                Toast.makeText(Login_Main.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String) params[0];
            String email = (String) params[1];
            String id=(String)params[2];
            String type=(String)params[3];
            String provide=(String)params[4];

            String postParameters ="&mb_mobile_email="+email+"&mb_mobile_id="+id+"&type="+type+"&provider="+provide;
            Log.d(TAG,"SNS Login Parameters :  " + postParameters);

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode(); //http연결 돼었는지 확인하기위해 변숫너언
                Log.d(TAG, "loginsu POST response code - " + responseStatusCode);

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
                Log.d(TAG,"loginsu result :" + sb.toString());
                return sb.toString();
            } catch (Exception e) {
                Toast.makeText(Login_Main.this, getString(R.string.Pleasewait), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "loginsu Error :",e);
                return ("loginsu Error :" + e.getMessage());
            }
        }
    }
    public void save3() {
        AppPreferences app = new AppPreferences(Login_Context);
        app.Sharde_setEmail(Sharde_email);
        app.Sharde_setGender(Sharde_gender);
        app.Sharde_setNick(Sharde_nick);
        app.Sharde_setAge(Sharde_age);
        app.Sharde_setValue(Chack_Value);
        app.Sharde_setKey(Sharde_Key);
        app.Sharde_setType(Sharde_Type);
        app.Sharde_setProvider(Sharde_Provider);
    }
    /*******************************************로컬 로그인 결과 출력*******************************************/
    class LocalLoginIng extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;  //서버랑 통신중일때 dialog 표시
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Login_Main.this,
                    "Please Wait", null, true, true); }
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss(); //로딩창 지우기
            if (result == null){
                Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
            }
            else {
                try {
                    LoginResult = result;
                    LocalData();
                    if(Chack_Value.equals(getString(R.string.one))){
                        Sharde_Type=getString(R.string.one);
                        save3();
                        startActivity(new Intent(Login_Main.this, MainActivity.class));
                        finish();
                    }else if(Chack_Value.equals(getString(R.string.OneHundred_One)))Snackbar.make(linearLayout2,getString(R.string.Null),Snackbar.LENGTH_SHORT).show();
                    else if(Chack_Value.equals(getString(R.string.OneHundred_Two)))Snackbar.make(linearLayout2,getString(R.string.Null),Snackbar.LENGTH_SHORT).show();
                    else if(Chack_Value.equals(getString(R.string.OneHundred_Three)))Snackbar.make(linearLayout2,getString(R.string.Null),Snackbar.LENGTH_SHORT).show();
                    else if(Chack_Value.equals(getString(R.string.OneHundred_Four)))Snackbar.make(linearLayout2,getString(R.string.Null),Snackbar.LENGTH_SHORT).show();
                    else if(Chack_Value.equals(getString(R.string.OneHundred_Seven)))Snackbar.make(linearLayout2,getString(R.string.Null),Snackbar.LENGTH_SHORT).show();
                    else Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG,"LocalLoginIng Success");
                }catch (Exception e){
                    Log.d(TAG,"LocalLoginIng Error",e);
                    Snackbar.make(linearLayout2,getString(R.string.Pleasewait),Snackbar.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        public String doInBackground(String... params) {
            String serverURL,postParameters; //excute 에 서 받은 값들을 params 변수에 저장
            String id = (String)params[1];
            String pw = (String)params[2];
            String type = (String)params[3];
            String provide = (String)params[4];
            String email = (String)params[5];
            String key = (String)params[6];
            serverURL = (String)params[0];

            postParameters = "&mb_mobile_id="+id+"&mb_mobile_password="+pw+"&type="+type+"&provide="+provide+"&mb_mobile_email="+email+"&mb_mobile_key="+key; // &mb = 키값저장

            Log.d(TAG,"Local Login Parameters : "+postParameters);
            try {
                URL url = new URL(serverURL); //url 선언
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //httpurl에 url 연결 준비

                httpURLConnection.setReadTimeout(10000);  // 5초뒤 종료 (옵션)
                httpURLConnection.setConnectTimeout(10000); // 5초뒤 종료 (옵션)
                httpURLConnection.setRequestMethod("POST");  //통신 방식은  post 방식
                httpURLConnection.connect();  //설정후 연결

                OutputStream outputStream = httpURLConnection.getOutputStream(); //클라이언트에서 서버로 보낼 준비
                outputStream.write(postParameters.getBytes("UTF-8"));  //utf-8 아닐시 글자가 깨질수가 있기때문에 utf8로 인코딩후 서버 전송
                outputStream.flush();  //서버보낸후 초기화
                outputStream.close(); //종료

                int responseStatusCode = httpURLConnection.getResponseCode(); //http연결 돼었는지 확인하기위해 변숫너언
                Log.d(TAG, "Local Login POST response code - " + responseStatusCode);

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
                Log.d(TAG,"LocalLoginIng Success "+sb);
                return sb.toString();  //sb로 반환
            } catch (Exception e) {
                Snackbar.make(linearLayout2,getString(R.string.OneHundred_Seven),Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "LocalLoginIng Error ", e);
                return new String("LocalLoginIng Error  " + e.getMessage());
            }
        }
    }

    /*******************************************로컬 로그인 결과 출력*******************************************/
    public void LocalData(){
        String TAG_JSON="appamado_login";
        String TAG_Key = "key";
        String TAG_Value = "value";  // 벨류값 들고오기 (100,101 같은거)
        String TAG_NICK = "mb_nick";
        String TAG_EMAIL="mb_id";
        String TAG_GENRE="mb_genre";
        String TAG_GENDER="mb_gender";
        String  TAG_AGE="mb_age";
        String TAG_ID="mb_email";
        try {
            JSONObject jsonObject = new JSONObject(LoginResult); //mJsonString 을 jsonobject로 선언
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
            //정보들을 밑에 저장
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                Chack_Value = item.getString(TAG_Value);  //value 정보 저장
                if(Chack_Value.equals("112")){ //sns로그인
                    Sharde_email = item.getString(TAG_EMAIL);
                    Sharde_nick = item.getString(TAG_NICK);
                    Sharde_gender = item.getString(TAG_GENDER);
                    Sharde_age = item.getString(TAG_AGE);
                    Sharde_Key = item.getString(TAG_Key);
                }else if(Chack_Value.equals("105")){
                    Sharde_email = item.getString(TAG_EMAIL);
                    Sharde_nick = item.getString(TAG_NICK);
                    Sharde_gender = item.getString(TAG_GENDER);
                    Sharde_age = item.getString(TAG_AGE);
                    Sharde_Key = item.getString(TAG_Key);
                }else { //로컬로그인
                    Sharde_email = item.getString(TAG_EMAIL);
                    Sharde_nick = item.getString(TAG_NICK);
                    Sharde_gender = item.getString(TAG_GENDER);
                    Sharde_age = item.getString(TAG_AGE);
                    Sharde_Key = item.getString(TAG_Key);
                }
            }
            Log.d(TAG,"LocalData Success");
        } catch (JSONException e) {
            Log.d(TAG, "LocalData Err ", e);
            Snackbar.make(linearLayout2,getString(R.string.OneHundred_Seven),Snackbar.LENGTH_SHORT).show();
        }
    }
}