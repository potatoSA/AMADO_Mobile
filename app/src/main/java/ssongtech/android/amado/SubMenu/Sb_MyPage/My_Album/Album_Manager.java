package ssongtech.android.amado.SubMenu.Sb_MyPage.My_Album;
/****************************************************************
 클래스명   : Album_Manager.java
 설명       : 음원 관리 메인 페이지, 사용자의 등록 앨범 정보를
 관리 및 수정하고 그 앨범에 달린 댓글도 관리
 생성일     : 2020.01.21
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    : idalbuminfo(key), id(ID), atnm(아티스트명), msnm(노래제목),
 msinfo(노래설명), mslyric(가사)
 return	    : Lg_login_id(로그인유저ID), Ms_name(노래제목),
 ****************************************************************/

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import ssongtech.android.amado.DataBase.AlbumInfoData.MusicAlData;
import ssongtech.android.amado.DataBase.GenreInfoData.GenreListData;
import ssongtech.android.amado.Function.Check.Login_Check;
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.Sb_MusicUp.MusicUp_Main;
import ssongtech.android.amado.SubMenu.Sb_MyPage.MyPage_Main;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Album_Manager extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    /* 공용 셋팅 string에서 해당 변수들의 값을 수정하면 된다. */
    private String IP_ADRESS, FD_MYIMAGE, FD_UPLOAD, FD_SERVER,
                    ANDROID_INFO_PATH, ANDROID_UP_PATH, SSONG_DEFAULTIMG_PATH;

    /**************** Login_Main 에서 로그인한 id와 key를 받아오자 ****************/
    private static String Lg_login_id = "", Lg_login_pw = "", Lg_login_key;
    /**************** Login_Main 에서 로그인한 id와 key를 받아오자 ****************/

    // Intent로 받아올 앨범 url
    String AlM_url;
    String AlM_abno;

    // Data Setting
    private String AlM_JsonString;
    private ArrayList<MusicAlData> AlM_ArrayList;

    // 앨범 커버, 정보 셋팅을 위한 변수
    ImageView AlM_ivCover, AlM_ivChange, AlM_ivUpload, AlM_ivCancel;
    EditText AlM_tvMsnm, AlM_tvMslyric, AlM_tvMsinfo;
    TextView AlM_tvAtnm, AlM_tvAbno, AlM_tvAtid, AlM_tvSelegenre;

    // 장르를 셋팅하기 위한 변수
    private ArrayList<GenreListData> GenerInfoList;
    private ArrayList<String> GenerInfoList1;
    private ArrayList<String> GenerInfoList2;
    Spinner AlM_spGenre1, AlM_spGenre2;

    // 앨범커버 사진 변경을 위한 갤러리 사용
    final int GALLERY_REQUEST_AL = 57751;
    GalleryPhoto galleryPhoto_al;
    String selectedPhoto_al;
    ImageView AlM_ivGallery;

    // 스크롤뷰
    ScrollView AlM_ScrollView;

    // 업로드시 수정 정보 데이터를 담을 변수
    String AlM_abno_up, AlM_id_up, AlM_genre_1, AlM_genre_2, AlM_open_yn = "y",
           Ms_name_up, Ms_info_up, Ms_lyric_up;

    // 다이얼로그
    ProgressDialog dialog;

    // 앨범 커버 업로드 구분, 파일 카테고리 구분
    String insert_type = "C", upload_type = "U";
    String file_category = "music_photo";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_manager);

        /* 기본값 셋팅*/
        IP_ADRESS = getString(R.string.IP_ADRESS);
        FD_SERVER = getString(R.string.FD_SERVER);
        FD_UPLOAD = getString(R.string.FD_UPLOAD);
        ANDROID_INFO_PATH = getString(R.string.ANDROID_INFO_PATH);
        ANDROID_UP_PATH = getString(R.string.ANDROID_UP_PATH);
        SSONG_DEFAULTIMG_PATH = getString(R.string.SSONG_DEFAULTIMG_PATH);

//        Key_Chack();

        // 앨범 정보 데이터를 받을 변수
        AlM_ArrayList = new ArrayList<MusicAlData>();
        GenerInfoList = new ArrayList<GenreListData>();
        GenerInfoList1 = new ArrayList<String>();
        GenerInfoList2 = new ArrayList<String>();

        /* 기본 셋팅 & 클릭 이벤트 */
        this.InitializeView();
        this.SetListener();

        // 자신을 호출한 Intent 얻어오기
        Intent intent = getIntent();

        // 호출할 때 보낸 값 받아오기
        AlM_url = intent.getStringExtra("fi_aburl");
        AlM_abno = intent.getStringExtra("fi_abno");

        /* AlbumData Asynctask 실행 */
        AlbumInfoGetData albuminfo = new AlbumInfoGetData();
        albuminfo.execute( IP_ADRESS + ANDROID_INFO_PATH + "get_Albuminfo.php", "mb_id", Lg_login_id, "ab_no", AlM_abno);

        /* GenreData Asynctask 실행 */
        GetGenre genre = new GetGenre();
        genre.execute( IP_ADRESS + ANDROID_INFO_PATH + "get_GenreInfo.php");
    }

    /******************************* 로그인 세션 체크 Start *******************************/
    /******************************* 로그인 세션 체크 Start *******************************/
//    protected void Key_Chack(){
//        String SnsChack = "1";
//        String provide ="";
//        String email ="";
//        Context Fi_Context = Album_Manager.this;
//
//        Lg_login_id = Login_Main.Lg_login_id;  //ID값 불러오기
//        Lg_login_key = Login_Main.Chack_Key; //로그인 했던 키값 불러오기
//        Login_Check loginChack = new Login_Check();
//        loginChack.Login(Lg_login_id, Lg_login_pw, Lg_login_key,SnsChack,provide,email,Fi_Context);
//    }
    /******************************** 로그인 세션 체크 End ********************************/

    /***************** 위젯 불러오기 *****************/
    private void InitializeView() {
        AlM_ivCover = (ImageView)findViewById(R.id.AlM_ivCover);
        AlM_ivChange = (ImageView)findViewById(R.id.AlM_ivChange);
        AlM_ivUpload = (ImageView)findViewById(R.id.AlM_ivUpload);
        AlM_ivCancel = (ImageView)findViewById(R.id.AlM_ivCancel);

        // 갤러리 사용
        galleryPhoto_al = new GalleryPhoto(getApplicationContext());
        AlM_ivGallery = (ImageView)findViewById(R.id.AlM_ivGallery);

        // Albuminfo에서 데이터 받아올 변수
        AlM_tvAbno = (TextView)findViewById(R.id.AlM_tvAbno);
        AlM_tvAtid = (TextView)findViewById(R.id.AlM_tvAtid);
        AlM_tvAtnm = (TextView)findViewById(R.id.AlM_tvAtnm);
        AlM_tvSelegenre = (TextView)findViewById(R.id.AlM_tvSelegenre);
        AlM_tvMsnm = (EditText)findViewById(R.id.AlM_tvMsnm);
        AlM_tvMsinfo = (EditText) findViewById(R.id.AlM_tvMsinfo);
        AlM_tvMslyric = (EditText)findViewById(R.id.AlM_tvMslyric);

        // 수정 불가능 하도록 셋팅
        AlM_ivGallery.setVisibility(View.INVISIBLE);
        AlM_ivCancel.setVisibility(View.GONE);
        AlM_tvMsnm.setEnabled(false);
        AlM_tvMsinfo.setCursorVisible(false);
        AlM_tvMsinfo.setClickable(false);
        AlM_tvMsinfo.setFocusableInTouchMode(false);
        AlM_tvMslyric.setCursorVisible(false);
        AlM_tvMslyric.setClickable(false);
        AlM_tvMslyric.setFocusableInTouchMode(false);

        // 계속 보일필요 엾음(공간도 차지하지 않도록)
        AlM_tvAbno.setVisibility(View.GONE);
        //AlM_tvAtid.setVisibility(View.GONE);

        // 스크롤뷰 제어
        AlM_ScrollView = (ScrollView)findViewById(R.id.AlM_ScrollView);
    }

    /********************************* AlbumInfoGetData Start *********************************/
    public class AlbumInfoGetData extends AsyncTask<String, Void, String> {
        String errorString = null;

        // 에러가 있는 경우에는 메세지를 보여주고, 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출한다.
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);

            AlM_JsonString = result;
            AlbumshowResult();
        }

        // 서버에 있는 PHP파일을 실행시키고 응답을 저장하고, 스트링으로 변환해서 리턴한다.
        @Override
        public String doInBackground(String... params) {
            String serverURL = params[0];
            String key1 = (String) params[1];
            String value1 = (String) params[2];;
            String key2 = (String) params[3];
            String value2 = (String) params[4];
            String postParameters = key1 + "=" + value1 + "&" + key2 + "=" + value2;  // 전달할 값 넣자

            // Connect
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 바이트 스트림에서 문자 스트림으로
                // UTF-8 문자 집합의 인코딩을 사용해 inputStream을 문자스트림으로 변환 객체를 생성 한다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                // BufferedReader 대용량 출력에 효율적이다.
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();

                String line;

                // readLine 스트림으로 부터 한 줄을 읽어 문자열로 리턴
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                // BufferedReader의 경우 버퍼를 잡아 놓았기 때문에 반드시 flush(), close()를 호출해서 뒤처리를 해줘야 한하.
                bufferedReader.close(); // 스트림을 닫는다.

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    // 앨범 정보 데이터를 받아오자
    public void AlbumshowResult(){

        String TAG_JSON ="appamado_musicinfo";
        String TAG_AB_NO = "idx";
        String TAG_WR_ID = "wr_id";
        String TAG_MS_NAME = "music_title";
        String TAG_GENRE1 = "first_genre";
        String TAG_GENRE2 = "second_genre";
        String TAG_MS_INFO ="music_info";
        String TAG_MS_LYRICS ="music_lyrics";
        String TAG_MB_NICK ="mb_nick";

        try {
            JSONObject jsonObject = new JSONObject(AlM_JsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String ab_no = item.getString(TAG_AB_NO);
                String wr_id = item.getString(TAG_WR_ID);
                String ms_name = item.getString(TAG_MS_NAME);
                String genre1 = item.getString(TAG_GENRE1);
                String genre2 = item.getString(TAG_GENRE2);
                String ms_info = item.getString(TAG_MS_INFO);
                String ms_lyrics = item.getString(TAG_MS_LYRICS);
                String mb_nick = item.getString(TAG_MB_NICK);

                MusicAlData musicalData = new MusicAlData();

                musicalData.setMusic_no(ab_no);
                musicalData.setMember_id(wr_id);
                musicalData.setMusic_name(ms_name);
                musicalData.setGenre_1(genre1);
                musicalData.setGenre_2(genre2);
                musicalData.setMusic_info(ms_info);
                musicalData.setMusic_lyric(ms_lyrics);
                musicalData.setMember_nick(mb_nick);

                AlM_ArrayList.add(musicalData);   // 데이터를 받아와서 사용하기 위해 추가
            }
            AlbumInfoSetting();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    /********************************* AlbumInfoSetting Start *********************************/
    public void AlbumInfoSetting() {

        /***** 앨범 슬라이딩뷰에서 보낸 이미지 가져오기 Start *****/
        Glide.with(this)
                .asBitmap()
                .load(AlM_url)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap Albitmap, @Nullable Transition<? super Bitmap> transition) {
                        // Intent로 받아온 클릭 된 이미지를 앨범커버View에 넣기
                        AlM_ivCover.setImageBitmap(Albitmap);
                    }
                });
        /***** 앨범 슬라이딩뷰에서 보낸 이미지 가져오기 End *****/

        for (int i = 0; i < AlM_ArrayList.size(); i++) {
            AlM_tvAbno.setText(AlM_ArrayList.get(i).getMusic_no());
            AlM_tvAtid.setText(AlM_ArrayList.get(i).getMember_id());
            AlM_tvAtnm.setText(AlM_ArrayList.get(i).getMember_nick());
            AlM_tvMsnm.setText(AlM_ArrayList.get(i).getMusic_name());
            AlM_tvMsinfo.setText(AlM_ArrayList.get(i).getMusic_info());
            AlM_tvMslyric.setText(AlM_ArrayList.get(i).getMusic_lyrics());
            AlM_tvSelegenre.setText(AlM_ArrayList.get(i).getGenre_1() + " , " + AlM_ArrayList.get(i).getGenre_2());
        }
    }
    /********************************* AlbumInfoSetting End *********************************/

    /********************************* GenreGetData End *********************************/
    public class GetGenre extends AsyncTask<String, Void, String> {
        String errorString = null;

        // 에러가 있는 경우에는 메세지를 보여주고, 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출한다.
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);

            AlM_JsonString = result;
            GenresResult();
        }

        // 서버에 있는 PHP파일을 실행시키고 응답을 저장하고, 스트링으로 변환해서 리턴한다.
        @Override
        public String doInBackground(String... params) {
            String serverURL = params[0];

            // Connect
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 바이트 스트림에서 문자 스트림으로
                // UTF-8 문자 집합의 인코딩을 사용해 inputStream을 문자스트림으로 변환 객체를 생성 한다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                // BufferedReader 대용량 출력에 효율적이다.
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();

                String line;

                // readLine 스트림으로 부터 한 줄을 읽어 문자열로 리턴
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                // BufferedReader의 경우 버퍼를 잡아 놓았기 때문에 반드시 flush(), close()를 호출해서 뒤처리를 해줘야 한하.
                bufferedReader.close(); // 스트림을 닫는다.

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    // 장르 정보 데이터를 받아오자
    public void GenresResult(){

        String TAG_JSON ="appamado_genreinfo";
        String TAG_CODE_VALUE = "code_value";
        String TAG_CODE_NAME = "code_name";

        try {
            JSONObject jsonObject = new JSONObject(AlM_JsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String code_value = item.getString(TAG_CODE_VALUE);
                String code_name = item.getString(TAG_CODE_NAME);

                GenreListData generListData = new GenreListData();
                generListData.setCode_value(code_value);
                generListData.setCode_name(code_name);
                GenerInfoList.add(generListData);   // 데이터를 받아와서 사용하기 위해 추가
            }
            GenreSpinnerSetting();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    /********************************* GenreGetData End *********************************/

    /********************************* GenreSpinnerSetting Start *********************************/
    public void GenreSpinnerSetting() {
        for (int i = 1; i < GenerInfoList.size(); i++) {
            GenerInfoList1.add(GenerInfoList.get(i).getCode_value());
            GenerInfoList2.add(GenerInfoList.get(i).getCode_name());
        }

        ArrayAdapter<String> GenrearrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, GenerInfoList2);

        // 장르 스피너 셋팅
        AlM_spGenre1 = (Spinner)findViewById(R.id.AlM_spGenre1);
        AlM_spGenre2 = (Spinner)findViewById(R.id.AlM_spGenre2);

        AlM_spGenre1.setEnabled(false);
        AlM_spGenre2.setEnabled(false);

        AlM_spGenre1.setAdapter(GenrearrayAdapter);
        AlM_spGenre1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), GenerInfoList2.get(i).toString() , Toast.LENGTH_SHORT).show();
                AlM_genre_1 = GenerInfoList1.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlM_spGenre2.setAdapter(GenrearrayAdapter);
        AlM_spGenre2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), GenerInfoList2.get(i).toString(), Toast.LENGTH_SHORT).show();
                AlM_genre_2 = GenerInfoList1.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /********************************* GenreSpinnerSetting End *********************************/

    /********************************* 클릭 이벤트 Start*********************************/
    public void SetListener() {

        /* 가사 텍스트뷰 클릭 이벤트 */
        AlM_tvMslyric.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 스크롤뷰가 텍스트뷰 AlM_tvMslyric 의 터치이벤트를 가져가지 못하게 처리
                AlM_ScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        /* 곡 정보 텍스트뷰 클릭 이벤트 */
        AlM_tvMsinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 스크롤뷰가 텍스트뷰 AlM_tvMsinfo 의 터치이벤트를 가져가지 못하게 처리
                AlM_ScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        /* 앨범 수정버튼 클릭 이벤트 */
        AlM_ivChange.setOnClickListener(v -> {
            // EditText 수정 가능하게
            AlM_tvMsnm.setEnabled(true);

            // 노래설명 ReadmoreTextView 수정 가능하게
            AlM_tvMsinfo.setCursorVisible(true);
            AlM_tvMsinfo.setClickable(true);
            AlM_tvMsinfo.setFocusableInTouchMode(true);

            // 가사 ReadmoreTextView 수정 가능하게
            AlM_tvMslyric.setCursorVisible(true);
            AlM_tvMslyric.setClickable(true);
            AlM_tvMslyric.setFocusableInTouchMode(true);

            // 앨범 커버 수정 가능하게 갤러리를 보여주자
            AlM_ivGallery.setVisibility(View.VISIBLE);

            // 수정 버튼을 숨기고, 혹시 취소할 수 있으니 취소버튼으로 변경
            AlM_ivChange.setVisibility(View.GONE);
            AlM_ivCancel.setVisibility(View.VISIBLE);

            AlM_spGenre1.setEnabled(true);
            AlM_spGenre2.setEnabled(true);
        });

        /* 취소 버튼 클릭 이벤트 */
        AlM_ivCancel.setOnClickListener(v -> {
            // 취소를 했으니 원래 상태로 되돌아 간다.
            AlbumInfoSetting();

            AlM_ivGallery.setVisibility(View.INVISIBLE);
            AlM_tvMsnm.setEnabled(false);

            // 노래설명 ReadmoreTextView View 모드
            AlM_tvMsinfo.setCursorVisible(false);
            AlM_tvMsinfo.setClickable(false);
            AlM_tvMsinfo.setFocusableInTouchMode(false);

            // 가사 ReadmoreTextView View 모드
            AlM_tvMslyric.setCursorVisible(false);
            AlM_tvMslyric.setClickable(false);
            AlM_tvMslyric.setFocusableInTouchMode(false);

            // 취소 버튼을 숨기고, 수정버튼으로 변경
            AlM_ivCancel.setVisibility(View.GONE);
            AlM_ivChange.setVisibility(View.VISIBLE);

            AlM_spGenre1.setEnabled(false);
            AlM_spGenre2.setEnabled(false);
        });

        /* 앨범커버 사진 갤러리버튼 클릭 이벤트*/
        AlM_ivGallery.setOnClickListener(v -> {
            startActivityForResult(galleryPhoto_al.openGalleryIntent(), GALLERY_REQUEST_AL);
        });

        /* 앨범 업로드버튼 클릭 이벤트 */
        AlM_ivUpload.setOnClickListener(v -> {

            // 더보기 기능이 있는 텍스트는 show... 문자열을 제외하고 받아오도록
            Ms_info_up = AlM_tvMsinfo.getText().toString().replaceAll("show less", "").replaceAll("...show more", "");
            Ms_lyric_up = AlM_tvMslyric.getText().toString().replaceAll("show less", "").replaceAll("...show more", "");

            // 사용자가 적은 음원 정보 받아오자
            AlM_abno_up = AlM_tvAbno.getText().toString();
            Ms_name_up = AlM_tvMsnm.getText().toString();
            Ms_info_up = AlM_tvMsinfo.getText().toString();
            Ms_lyric_up = AlM_tvMslyric.getText().toString();

            Album_Manager.AlbumCoverUpload albumCoverUpload = new Album_Manager.AlbumCoverUpload();
            albumCoverUpload.execute(IP_ADRESS + ANDROID_UP_PATH + "Album_upload.php");

            // 업로드 완료됬으니 다시 View 모드
            AlM_ivGallery.setVisibility(View.INVISIBLE);
            AlM_tvAtnm.setEnabled(false);
            AlM_tvMsnm.setEnabled(false);

            // 노래설명 ReadmoreTextView View 모드
            AlM_tvMsinfo.setCursorVisible(false);
            AlM_tvMsinfo.setClickable(false);

            // 가사 ReadmoreTextView View 모드
            AlM_tvMslyric.setCursorVisible(false);
            AlM_tvMslyric.setClickable(false);

            // 갤러리 버튼 비활성화
            AlM_ivGallery.setVisibility(View.INVISIBLE);

            // 취소 버튼을 숨기고, 수정버튼으로 변경
            AlM_ivCancel.setVisibility(View.GONE);
            AlM_ivChange.setVisibility(View.VISIBLE);

            AlM_spGenre1.setEnabled(false);
            AlM_spGenre2.setEnabled(false);
        });
    }
    /********************************* 클릭 이벤트 End*********************************/

    /********************** 갤러리 실행 후 결과 받아오기 Start ***********************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 갤러리 실행 후 받은 결과
            if (requestCode == GALLERY_REQUEST_AL) {  // 프로필에서 실행된 갤러리
                Uri uri = data.getData();

                galleryPhoto_al.setPhotoUri(uri);
                String photoPath = galleryPhoto_al.getPath();  // 안드로이드에서 선택된 갤러리 이미지 경로를 받아온다.
                selectedPhoto_al = photoPath;   // selectedPhoto 배열에 선택된 이미지 경로들을 담는다.
                Bitmap bitmap;

                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    AlM_ivCover.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosingphotos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /********************** 카메라, 갤러리 실행 후 결과 받아오기 End ***********************/

    /***************************** AlbumCoverUpload 실행 Start *****************************/
    public class AlbumCoverUpload extends AsyncTask<String , Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(Album_Manager.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            int serverResponseCode = 0;

            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File selectedFile = new File(selectedPhoto_al);
            String result = null;

            if (!selectedFile.isFile()) {
                dialog.dismiss();

                return String.valueOf(0);
            } else {
                try {
                    // FileInputStream는 디스크상에 존재하는 파일로부터 바이트 단위의 입력을 받는 클래스
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);

                    URL url = new URL(serverURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection = (HttpURLConnection) url.openConnection();   // URL 연결
                    httpURLConnection.setDoInput(true);     // 읽기 모드
                    httpURLConnection.setDoOutput(true);    // 쓰기 모드
                    httpURLConnection.setUseCaches(false);  // 캐시를 사용하지 않게
                    httpURLConnection.setRequestMethod("POST");
                    // 헤더 설정
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    httpURLConnection.setRequestProperty("uploaded_file", selectedPhoto_al);

                    // 데이터 출력 스트림으로 바이트 형태 파일 전송
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                    // 업로드 타입에 따라 전달 값이 다르다. 'C' insert, 'U' upload
                    if (AlM_url.equals(IP_ADRESS + SSONG_DEFAULTIMG_PATH)) {
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"update_type\";" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd + insert_type + lineEnd);
                    } else {
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"update_type\";" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd + upload_type + lineEnd);
                    }

                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"idx\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + AlM_abno_up + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"mb_id\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + Lg_login_id + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"category\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + file_category + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"music_title\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + Ms_name_up + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"first_genre\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + AlM_genre_1 + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"second_genre\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + AlM_genre_2 + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"music_info\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + Ms_info_up + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"music_lyrics\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + Ms_lyric_up + lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"open_yn\";" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd + AlM_open_yn + lineEnd);

                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + selectedPhoto_al + "\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);

                    // 파일에 있는 바이트 수 반환
                    bytesAvailable = fileInputStream.available();
                    // 사용 가능한 최소 바이트 또는 1MB의 버퍼 크기로 선택
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    // 버퍼를 버퍼 크기의 바이트 배열로 설정
                    buffer = new byte[bufferSize];

                    // fileInputStream 에서 바이트 읽기(버퍼의 0번째 인덱스에서 버퍼 크기 조정)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    // 바이트가 표시될 때까지 루프 반복읽기 = -1, 즉 읽을 바이트가 남아있지 않음
                    while (bytesRead > 0) {
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = httpURLConnection.getResponseCode();
                    String serverResponseMessage = httpURLConnection.getResponseMessage();

                    Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    // PHP 결과 체크할 때 사용, 값을 받아온다
                    InputStreamReader tmp = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                    }

                    result = builder.toString();

                    // 입,출력 스트림 닫기
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Album_Manager.this, "File Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(Album_Manager.this, "URL error!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Album_Manager.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
                }
                //dialog.dismiss();
                return result;
            }
        }
    }

    /***************************** AlbumCoverUpload 실행 End *****************************/
}

