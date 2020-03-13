package ssongtech.android.amado.SubMenu.Sb_MyPage;
/****************************************************************
 클래스명   : MyPage_Main.java
 설명       : 마이페이지 메인 클래스, 프로필과 My앨범을 보여준다.
 생성일     : 2020.01.06
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    : id(ID), name(유저이름), country(국가), imgurl(메인이미지경로),
 imgurl_bg(배경이미지경로), albumurl(앨범이미지경로), msnm(노래제목)
 return	    : User_id(로그인유저ID), AlM_urls(앨범경로), Ms_name(노래제목),
 encodedImage(인코딩이미지)
 ****************************************************************/

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import ssongtech.android.amado.DataBase.UserInfoData.PersonalData;
import ssongtech.android.amado.Function.Check.Login_Check;
import ssongtech.android.amado.HeadMenu.Hd_Login.Login.Login_Main;
import ssongtech.android.amado.R;
import ssongtech.android.amado.Function.SlidingVIew.AlbumSlidingImage_Adapter;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyPage_Main extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    /* 공용 셋팅 string에서 해당 변수들의 값을 수정하면 된다. */
    private String IP_ADRESS, FD_SERVER, FD_MYIMAGE, FD_UPLOAD, ANDROID_INFO_PATH,
                     SSONG_MYPAGEIMG_PATH, SSONG_MUSICPHOTO_PATH, SSONG_DEFAULTIMG_PATH;

    /**************** Login_Main 에서 로그인한 id와 key를 받아오자 ****************/
    private static String Lg_login_id = "", Lg_login_pw = "", Lg_login_key;
    /**************** Login_Main 에서 로그인한 id와 key를 받아오자 ****************/

    // Data Setting
    private String My_JsonString;
    private ArrayList<PersonalData> My_ArrayList;

    // SwipeView 레이아웃 셋팅 하자
    private static ViewPager My_Pager;
    private static int My_currentPage = 0;
    private static int My_NUM_PAGES = 0;
    ArrayList<String> AlM_urls;
    ArrayList<String> AlM_abno;
    Integer[] colors = null;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    // 이미지 불러오기
    ImageView My_ivAlbum;
    Bitmap My_ivbitmap;
    View My_sliding_layout;

    // 프로필 수정을 위해
    ImageView My_ivCarmera_pro, My_ivGallery_pro, My_ivUpload_pro, My_ivView_pro,
              My_ivCarmera_bg, My_ivGallery_bg, My_ivUpload_bg, My_ivView_bg,
              My_Profile_up, My_Profile_cancel;

    /**************** PhotoPermission Class 를 호출해서 카메라, 갤러리를 실행하자 ****************/
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_CAMERA_BG = 2;
    private static final int PICK_FROM_ALBUM = 3;
    private static final int PICK_FROM_ALBUM_BG = 4;

    private String My_selectedPhoto, My_selectedPhoto_bg;

    private File My_tempFile;

    private Boolean isPermission = true;
    /*********************************************************************************************/

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.submenu_mypage);

        /* 기본값 셋팅*/
        IP_ADRESS = getString(R.string.IP_ADRESS);
        FD_SERVER = getString(R.string.FD_SERVER);
        FD_MYIMAGE = getString(R.string.FD_MYIMAGE);
        FD_UPLOAD = getString(R.string.FD_UPLOAD);
        ANDROID_INFO_PATH = getString(R.string.ANDROID_INFO_PATH);
        SSONG_MYPAGEIMG_PATH = getString(R.string.SSONG_MYPAGEIMG_PATH);
        SSONG_MUSICPHOTO_PATH = getString(R.string.SSONG_MUSICPHOTO_PATH);
        SSONG_DEFAULTIMG_PATH = getString(R.string.SSONG_DEFAULTIMG_PATH);

        Key_Chack();

        isExternalStorageWritable();
        isExternalStorageReadable();

        tedPermission();

        /* 기본 셋팅 & 클릭 이벤트 */
        this.InitializeView();
        this.SetListener();

        My_ArrayList = new ArrayList<PersonalData>();
        AlM_urls = new ArrayList<String>();
        AlM_abno = new ArrayList<String>();

        /* Data Asynctask 실행 */
        UserInfoGetData task = new UserInfoGetData();
        //task.execute( IP_ADRESS + FD_SERVER + "get_UserInfo.php", "mb_id", Lg_login_id);
        task.execute(IP_ADRESS + ANDROID_INFO_PATH + "get_UserInfo.php", "mb_id", Lg_login_id);
    }

    /******************************* 로그인 세션 체크 Start *******************************/
    protected void Key_Chack(){
            String SnsChack = "1";
            String provide ="";
            String email ="";
            Context Fi_Context = MyPage_Main.this;

            Lg_login_id = Login_Main.Lg_login_id;  //ID값 불러오기
            Lg_login_key = Login_Main.Chack_Key; //로그인 했던 키값 불러오기
            Login_Check loginChack = new Login_Check();
            loginChack.Login(Lg_login_id, Lg_login_pw, Lg_login_key,SnsChack,provide,email,Fi_Context);
        }
        /******************************** 로그인 세션 체크 End ********************************/

    /********************************* 위젯 불러오기 Start *********************************/
    public void InitializeView() {
        // 카메라, 갤러리, 업로드 버튼
        My_ivCarmera_pro = (ImageView) findViewById(R.id.My_ivCarmera_pro);
        My_ivCarmera_bg = (ImageView) findViewById(R.id.My_ivCarmera_bg);
        My_ivGallery_pro = (ImageView) findViewById(R.id.My_ivGallery_pro);
        My_ivGallery_bg = (ImageView) findViewById(R.id.My_ivGallery_bg);
        My_ivUpload_pro = (ImageView) findViewById(R.id.My_ivUpload_pro);
        My_ivUpload_bg = (ImageView) findViewById(R.id.My_ivUpload_bg);
        My_ivView_pro = (ImageView) findViewById(R.id.My_ivView_pro);
        My_ivView_bg = (ImageView) findViewById(R.id.My_ivView_bg);
        // 프로필 수정, 취소 버튼
        My_Profile_up = (ImageView) findViewById(R.id.My_Profile_up);
        My_Profile_cancel = (ImageView) findViewById(R.id.My_Profile_cancel);

        // My_sliding_layout의 My_ivAlbum 이미지뷰를 사용하자
        My_sliding_layout = getLayoutInflater().inflate(R.layout.mypage_slidingimg, null, false);
        My_ivAlbum = (ImageView) My_sliding_layout.findViewById(R.id.My_ivAlbum);

        // 수정 버튼을 누르기 전엔 카메라, 갤러리 버튼 보이지 않게 하기
        My_ivCarmera_pro.setVisibility(View.INVISIBLE);
        My_ivCarmera_bg.setVisibility(View.INVISIBLE);
        My_ivGallery_pro.setVisibility(View.INVISIBLE);
        My_ivGallery_bg.setVisibility(View.INVISIBLE);
        My_ivUpload_pro.setVisibility(View.INVISIBLE);
        My_ivUpload_bg.setVisibility(View.INVISIBLE);
        My_Profile_cancel.setVisibility(View.GONE);
    }
    /********************************* 위젯 불러오기 End *********************************/

    /********************************* UserInfoGetData Start *********************************/
    public class UserInfoGetData extends AsyncTask<String, Void, String>{
        String errorString = null;

        // 에러가 있는 경우에는 메세지를 보여주고, 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출한다.
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);

            My_JsonString = result;
            UsershowResult();
        }

        // 서버에 있는 PHP파일을 실행시키고 응답을 저장하고, 스트링으로 변환해서 리턴한다.
        @Override
        public String doInBackground(String... params) {
            // 보낼 데이터
            String serverURL = params[0];
            String key1 = (String) params[1];
            String value1 = (String) params[2];

            // URL 뒤에 붙여서 보낼 파라미터
            String postParameters = key1 + "=" + value1 ;  // 전달할 값 넣자

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

    /* 사용자정보 데이터를 받아오자 */
    public void UsershowResult(){

        String TAG_JSON ="appamado_myimage";
        String TAG_MB_ID = "mb_id";
        String TAG_CATEGORY = "category";
        String TAG_FILE = "fi_file";
        String TAG_FIABNO ="fi_abno";
        String TAG_IDX ="idx";

        try {
            JSONObject jsonObject = new JSONObject(My_JsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String mb_id = item.getString(TAG_MB_ID);
                String fi_category = item.getString(TAG_CATEGORY);
                String fi_file = item.getString(TAG_FILE);
                String fi_abno = item.getString(TAG_FIABNO);
                String idx = item.getString(TAG_IDX);

                PersonalData personalData = new PersonalData();

                personalData.setFile_category(fi_category);
                personalData.setFile_file(fi_file);
                personalData.setFile_abno(fi_abno);
                personalData.setIdx(idx);

                My_ArrayList.add(personalData);   // 데이터를 받아와서 사용하기 위해 추가
            }
            AlbumSlidingView(); // 앨범 슬라이드 뷰 실행
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    /********************************* UserInfoGetData End *********************************/

    /********************************* URL_SlideView Start *********************************/
    private void AlbumSlidingView() {
        for (int i = 0; i < My_ArrayList.size(); i++) {
            String fileurl = My_ArrayList.get(i).getFile_file(); //+ My_ArrayList.get(i).getFile_type();
            String category = My_ArrayList.get(i).getFile_category();
            String ab_no = My_ArrayList.get(i).getFile_abno();
            String idx = My_ArrayList.get(i).getIdx();

            switch (category) {
                case "profile":
                    // 프로필 이미지 셋팅하기, GetImageFromURL AsyncTask 실행
                    new GetImageFromURL(My_ivView_pro).execute( IP_ADRESS + SSONG_MYPAGEIMG_PATH + fileurl);
                    break;
                case "background":
                    // 배경 이미지 셋팅하기, GetImageFromURL AsyncTask 실행
                    new GetImageFromURL(My_ivView_bg).execute( IP_ADRESS + SSONG_MYPAGEIMG_PATH + fileurl);
                    break;
                case "music_photo":
                    // 앨범 커버 셋팅하기
                    AlM_urls.add( IP_ADRESS + SSONG_MUSICPHOTO_PATH + fileurl);
                    AlM_abno.add(My_ArrayList.get(i).getFile_abno());
                default:
                    // 음원정보를 등록은 했는데 앨범커버를 Default 이지미로 했을 경우
                    if (ab_no.equals("null") && idx != "") {
                        AlM_urls.add(IP_ADRESS + SSONG_DEFAULTIMG_PATH);
                        AlM_abno.add(My_ArrayList.get(i).getIdx());
                    }
                    break;
            }
        }

        My_Pager = (ViewPager) findViewById(R.id.My_Swipe);
        My_Pager.setAdapter(new AlbumSlidingImage_Adapter(MyPage_Main.this, AlM_urls, AlM_abno));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(My_Pager); //뷰페이저 셋팅

        // getResources().getDisplayMetrics()로 구할 수 있는 density는 단말기의 해상도
        final float density = getResources().getDisplayMetrics().density;

        //circle 표시 반경을 설정할 수 있다.
        indicator.setRadius(5 * density);

        // 뷰 페이지 간격 조정
        My_Pager.setClipToPadding(false);
        My_Pager.setPadding(200, 0, 200, 0);

        // 컬러 Get
        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color5),
                getResources().getColor(R.color.color6),
                getResources().getColor(R.color.color7),
                getResources().getColor(R.color.color8),
                getResources().getColor(R.color.color9),
                getResources().getColor(R.color.color10),
        };
        colors = colors_temp;

        // 페이지 또는 슬라이드 수를 설정한다.
        My_NUM_PAGES = AlM_urls.size();

        /****** 자동 슬라이더 설정 Starat ******/
        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (My_currentPage == My_NUM_PAGES) {
                    My_currentPage = 0;
                }
                My_Pager.setCurrentItem(My_currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        /****** 자동 슬라이더 설정 End ******/


        /** 페이지가 변경되거나 스크롤이 이동될 때 불려지는 listener를 지정한다.
         * 원래는 하나의 listener를 사용할 수 있었지만 변경되면서 여러개 사용할 수 있 다.
         * setOnPageChangeListener -> addOnPageChangeListener로 변경
         **/
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /** onPageScrolled 스크롤 효과가 나는 동안 계속해서 호출,
             * position 현재 표시된 위치
             * offset   float:[0,1]의 값은 페이지에서 위치의 오프셋을 나타낸다.
             * positionOffsetPixels     int:위치로부터의 오프셋을 나타내는 픽셀 단위의 값
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (colors.length - 1)) {
                    My_Pager.setBackgroundColor(       // ViewPager 배경 컬러 셋팅
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    My_Pager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            // 탭 선택효과를 내는 부분
            @Override
            public void onPageSelected(int position) {
                My_currentPage = position;
            }

            // 스크롤 상태
            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }
    /********************************* URL_SlideView End **********************************/

    /********************************* 클릭 이벤트 Start *********************************/
    public void SetListener() {
        /* 프로필 수정버튼 클릭 이벤트 */
        My_Profile_up.setOnClickListener(v -> {

            My_ivCarmera_pro.setVisibility(View.VISIBLE);
            My_ivCarmera_bg.setVisibility(View.VISIBLE);
            My_ivGallery_pro.setVisibility(View.VISIBLE);
            My_ivGallery_bg.setVisibility(View.VISIBLE);
            My_ivUpload_pro.setVisibility(View.VISIBLE);
            My_ivUpload_bg.setVisibility(View.VISIBLE);

            My_Profile_up.setVisibility(View.GONE);
            My_Profile_cancel.setVisibility(View.VISIBLE);
        });

        /* 프로필 수정취소버튼 클릭 이벤트 */
        My_Profile_cancel.setOnClickListener(v -> {

            // 취소를 눌렀으니 원상태로 되돌리자
            My_ArrayList.clear();
            AlM_urls.clear();
            UsershowResult();

            My_ivCarmera_pro.setVisibility(View.INVISIBLE);
            My_ivCarmera_bg.setVisibility(View.INVISIBLE);
            My_ivGallery_pro.setVisibility(View.INVISIBLE);
            My_ivGallery_bg.setVisibility(View.INVISIBLE);
            My_ivUpload_pro.setVisibility(View.INVISIBLE);
            My_ivUpload_bg.setVisibility(View.INVISIBLE);

            My_Profile_cancel.setVisibility(View.GONE);
            My_Profile_up.setVisibility(View.VISIBLE);
        });

        /* 프로필 카메라, 갤러리 클릭 이벤트 */
        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    // 카메라 클릭 이벤트
                    case R.id.My_ivCarmera_pro:
                        // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                        if(isPermission) takePhoto();   // 카메라 실행을 위한 PhotoPermission Class의 함수
                        else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.My_ivCarmera_bg:
                        // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                        if(isPermission) takePhoto_bg();
                        else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                        break;
                    // 갤러리 클릭 이벤트
                    case R.id.My_ivGallery_pro:
                        // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                        if(isPermission) goToAlbum();      // 갤러리 실행을 위한 PhotoPermission Class의 함수
                        else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.My_ivGallery_bg:
                        // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                        if(isPermission) goToAlbum_bg();
                        else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                        break;
                };
            }
        };

        My_ivCarmera_pro.setOnClickListener(mClickListener);
        My_ivCarmera_bg.setOnClickListener(mClickListener);
        My_ivGallery_pro.setOnClickListener(mClickListener);
        My_ivGallery_bg.setOnClickListener(mClickListener);

        /* 프로필 업로드버튼 클릭 이벤트 */
        My_ivUpload_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (My_selectedPhoto == null || My_selectedPhoto.equals("")) {        // 이미지가 없으면 선택한 이미지가 없다고 Toast를 띄워준다.
                    Toast.makeText(getApplicationContext(), "No Image Selected.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Bitmap bitmap = ImageLoader.init().from(My_selectedPhoto).requestSize(1024, 1024).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);     // 비트맵을 문자열로 인코딩
                    Log.d(TAG, encodedImage);

                    HashMap postData_my = new HashMap();   // 데이터를 주고 받는다
                    postData_my.put("image", encodedImage);    // PHP로 POST 키 값 동일하게 셋팅, 첫번째 이미지, 두번째 인코딩된 문자열 이미지
                    postData_my.put("userid", Lg_login_id);

                    PostResponseAsyncTask taskupload_al = new PostResponseAsyncTask(MyPage_Main.this, postData_my, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {   // 프로세스가 끝난 후 결과 얻기
                            Log.d(TAG, s);
                            if (s.contains("uploaded_success")) {
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully",
                                        Toast.LENGTH_SHORT).show();

                                My_ivCarmera_pro.setVisibility(View.INVISIBLE);
                                My_ivGallery_pro.setVisibility(View.INVISIBLE);
                                My_ivUpload_pro.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error while uploading",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    taskupload_al.execute( IP_ADRESS + FD_UPLOAD + "upload.php");      // AsyncTask 실행, 휴대전화에서 연결하려면 여기에 있는 IP가 가장 빠른 IP 이여야 한다?
                    taskupload_al.setEachExceptionsHandler(new EachExceptionsHandler() {     // 예외 처리
                        @Override
                        public void handleIOException(IOException e) {
                            Toast.makeText(getApplicationContext(), "Cannot Connect to Server.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {
                            Toast.makeText(getApplicationContext(), "URL Error",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleProtocolException(ProtocolException e) {
                            Toast.makeText(getApplicationContext(), "Protocol Error",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                            Toast.makeText(getApplicationContext(), "Encoding Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while encoding photos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* 배경 업로드버튼 클릭 이벤트 */
        My_ivUpload_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (My_selectedPhoto == null || My_selectedPhoto.equals("")) {        // 이미지가 없으면 선택한 이미지가 없다고 Toast를 띄워준다.
                    Toast.makeText(getApplicationContext(), "No Image Selected.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Bitmap bitmap = ImageLoader.init().from(My_selectedPhoto).requestSize(1024, 1024).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);     // 비트맵을 문자열로 인코딩
                    Log.d(TAG, encodedImage);

                    HashMap postData_my = new HashMap();   // 데이터를 주고 받는다
                    postData_my.put("image", encodedImage);    // PHP로 POST 키 값 동일하게 셋팅, 첫번째 이미지, 두번째 인코딩된 문자열 이미지
                    postData_my.put("userid", Lg_login_id);

                    PostResponseAsyncTask taskupload_al = new PostResponseAsyncTask(MyPage_Main.this, postData_my, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {   // 프로세스가 끝난 후 결과 얻기
                            Log.d(TAG, s);
                            if (s.contains("uploaded_success")) {
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully",
                                        Toast.LENGTH_SHORT).show();

                                My_ivCarmera_bg.setVisibility(View.INVISIBLE);
                                My_ivGallery_bg.setVisibility(View.INVISIBLE);
                                My_ivUpload_bg.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error while uploading",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    taskupload_al.execute( IP_ADRESS + FD_UPLOAD + "upload.php");      // AsyncTask 실행, 휴대전화에서 연결하려면 여기에 있는 IP가 가장 빠른 IP 이여야 한다?
                    taskupload_al.setEachExceptionsHandler(new EachExceptionsHandler() {     // 예외 처리
                        @Override
                        public void handleIOException(IOException e) {
                            Toast.makeText(getApplicationContext(), "Cannot Connect to Server.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {
                            Toast.makeText(getApplicationContext(), "URL Error",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleProtocolException(ProtocolException e) {
                            Toast.makeText(getApplicationContext(), "Protocol Error",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                            Toast.makeText(getApplicationContext(), "Encoding Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while encoding photos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /********************************* 클릭 이벤트 End *********************************/

    /********************* 이미지 불러와서 셋팅 하기 Start *********************/
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgV;

        public GetImageFromURL(ImageView imgV) {
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            My_ivbitmap = null;
            try {
                InputStream srt = new URL(urldisplay).openStream();    // 전달 받은 url로 Stream
                My_ivbitmap = BitmapFactory.decodeStream(srt);     // inputStream으로 부터 Bitmap을 만들어 낸다.
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return My_ivbitmap;
        }

        // Bitmap 이미지를 받아서 뷰에 셋팅한다.
        @Override
        protected  void onPostExecute(Bitmap My_ivbitmap) {
            super.onPostExecute(My_ivbitmap);
            imgV.setImageBitmap(My_ivbitmap);
        }
    }
    /********************* 이미지 불러와서 셋팅 하기 End *********************/

    /************ 카메라, 갤러리 실행 후 결과 받아와서 셋팅하기 Start ************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (My_tempFile != null) {
                if (My_tempFile.exists()) {
                    if (My_tempFile.delete()) {
                        Log.e(TAG, My_tempFile.getAbsolutePath() + " 삭제 성공");
                        My_tempFile = null;
                    }
                }
            }
            return;
        }

        if (requestCode == PICK_FROM_ALBUM || requestCode == PICK_FROM_ALBUM_BG) {       // 갤러리 실행 후
            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {
                // Uri 스키마를 content:/// 에서 file:/// 로  변경한다.
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                My_tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(My_tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            if (requestCode == PICK_FROM_ALBUM) {
                setImage();
            } else {
                setImage_bg();
            }
        } else if (requestCode == PICK_FROM_CAMERA || requestCode == PICK_FROM_CAMERA_BG) {       // 카메라 실행 후
            if (requestCode == PICK_FROM_CAMERA) {
                setImage();
            } else {
                setImage_bg();
            }
        }
    }

    /* 앨범에서 프로필 이미지 가져오기 */
    public void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /* 앨범에서 배경 이미지 가져오기 */
    public void goToAlbum_bg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM_BG);
    }

    /* 카메라를 프로필 이미지 가져오기 */
    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            My_tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (My_tempFile != null) {

            /*
             *  안드로이드 OS 누가 버전 이후부터는 file:// URI 의 노출을 금지로 FileUriExposedException 발생
             *  Uri 를 FileProvider 도 감싸 주어야 한다.
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "ssongtech.android.amado.fileprovider", My_tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(My_tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    /* 카메라를 배경 이미지 가져오기 */
    public void takePhoto_bg() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            My_tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (My_tempFile != null) {
            /*
             *  안드로이드 OS 누가 버전 이후부터는 file:// URI 의 노출을 금지로 FileUriExposedException 발생
             *  Uri 를 FileProvider 도 감싸 주어야 한다.
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "ssongtech.android.amado.fileprovider", My_tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA_BG);
            } else {
                Uri photoUri = Uri.fromFile(My_tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA_BG);
            }
        }
    }

    /* 폴더 및 파일 만들기 */
    public File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "SSONG_OU_" + timeStamp + "_";

        // 이미지가 저장될 파일 주소 ( blackJin )
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        My_selectedPhoto = image.getAbsolutePath();

        return image;
    }

    /* tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다. */
    public void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(My_tempFile.getAbsolutePath(), options);
        My_selectedPhoto = My_tempFile.getAbsolutePath();
        Log.d(TAG, "setImage : " + My_tempFile.getAbsolutePath());

        My_ivView_pro =(ImageView) findViewById(R.id.My_ivView_pro);
        My_ivView_pro.setImageBitmap(getRotatedBitmap(originalBm, 90)); // 이미지 뷰에 비트맵 set

        /**
         *  tempFile 사용 후 null 처리를 해줘야 한다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄진다.
         */
        My_tempFile = null;

    }

    /* tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다. */
    public void setImage_bg() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(My_tempFile.getAbsolutePath(), options);
        My_selectedPhoto_bg = My_tempFile.getAbsolutePath();
        Log.d(TAG, "setImage : " + My_tempFile.getAbsolutePath());

        My_ivView_bg =(ImageView) findViewById(R.id.My_ivView_bg);
        My_ivView_bg.setImageBitmap(getRotatedBitmap(originalBm, 90)); // 이미지 뷰에 비트맵 set

        /**
         *  tempFile 사용 후 null 처리를 해줘야 한다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄진다.
         */
        My_tempFile = null;

    }

    // 카메라 촬영 이미지 회전
    public Bitmap getRotatedBitmap(Bitmap source, int angle){      // 회전시키려면 새로운 각도와 행렬이여야 한다.
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);   // 회전할 각도
        Bitmap bitmap1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);   // 전환 값 설정 (source(원본 이미지), x, y, width, height, Matrix(m), filter)
        return bitmap1;
    }
    /************ 카메라, 갤러리 실행 후 결과 받아와서 셋팅하기 End ************/

    /******************* 카메라 권한 체크 Start ******************/
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;
            }

        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }
    /******************* 카메라 권한 체크 End ******************/

    /* 외부 저장소 쓰기 권한이 허용 되었는지 체크 */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* 외부 저장소 읽기 권한이 허용 되었는지 체크 */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
