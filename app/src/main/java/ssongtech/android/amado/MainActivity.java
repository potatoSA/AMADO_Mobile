package ssongtech.android.amado;
/****************************************************************
 클래스명    : MainActivity.java
 설명       : 메인 화면 Java class
 생성일     : 2020.02.27
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import ssongtech.android.amado.App_Main.BaseActivity.BaseActivity;
import ssongtech.android.amado.App_Main.Tab_Setting_Commu.CmFragmentComment;
import ssongtech.android.amado.App_Main.Tab_Setting_Commu.CmFragmentToday;
import ssongtech.android.amado.App_Main.Tab_Setting_Commu.CmFragmentYester;
import ssongtech.android.amado.App_Main.Tab_Setting_Commu.CmFragmentWeek;
import ssongtech.android.amado.App_Main.Tab_Setting_Commu.CmViewPagerAdapter;
import ssongtech.android.amado.DataBase.MusicChartListData.MusicChartListData;
import ssongtech.android.amado.Function.SlidingVIew.Main_MusicSliding2_Adapter;
import ssongtech.android.amado.Function.SlidingVIew.Main_MusicSliding3_Adapter;
import ssongtech.android.amado.DataBase.MusicChartListData.MusicChartListData;
import ssongtech.android.amado.App_Main.TopChartListView.TopChartList_Adapter;
import ssongtech.android.amado.Function.SlidingVIew.Main_GenreSliding_Adapter;
import ssongtech.android.amado.Function.SlidingVIew.Main_MusicSliding1_Adapter;
import ssongtech.android.amado.SsongChart.SS_Amado.AmadoChartList_Adapter;
import ssongtech.android.amado.SsongChart.SS_Amado.AmadoChart_Main;
import ssongtech.android.amado.App_Main.Tab_Setting_Main.SectionsPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final String TAG = this.getClass().getName();

    /* 공용 셋팅 string에서 해당 변수들의 값을 수정하면 된다. */
    private String IP_ADRESS, SSONG_MUSICCHART_PATH, SSONG_MUSICPHOTO_PATH;

    /******************************** 메인 작업중_200227_김선아 *********************************/
    // 알림 팝업 변수
    private NotificationCompat.Builder builder;

    // 장르 Sliding 관련 변수 (url은 기능 로직 작업때 변경)
    private RecyclerView GenreRecyclerView;

    private ArrayList<String> Genre_Info = new ArrayList<String>();
    private ArrayList<String> Genre_Names = new ArrayList<String>();
    private ArrayList<Integer> Genre_ImageUrls = new ArrayList<Integer>();

    // Top100 ChartList 관련 변수
    private String MainTop_JsonString;
    private ArrayList<MusicChartListData> MainTop_ArrayList;
    private ArrayList<String> MainTop_MusicCoverURL;

    private ListView Main_lvTopChart;
    private ImageView TopChart_ViewMore;

    // 메인 중간 배너 관련 변수 (url은 기능 로직 작업때 변경)
    ViewFlipper Main_viBannerLipper;
    private ArrayList<Integer> BannerImages = new ArrayList<Integer>();

    // 커뮤니티 TOP5 관련 변수
    private TabLayout CommuTabLayout;
    private ViewPager CommuViewPager;
    private CmViewPagerAdapter CommuViewAdapter;

    // 노래1 슬라이드 관련 변수 (url은 기능 로직 작업때 변경)
    private ArrayList<String> Music_Info_1 = new ArrayList<String>();
    private ArrayList<String> Music_Names_1 = new ArrayList<String>();
    private ArrayList<Integer> Music_ImageUrls_1 = new ArrayList<Integer>();

    private RecyclerView MusicRecyclerView_1;

    // 노래2 슬라이드 관련 변수 (url은 기능 로직 작업때 변경)
    private ArrayList<String> Music_Info_2 = new ArrayList<String>();
    private ArrayList<String> Music_Names_2 = new ArrayList<String>();
    private ArrayList<Integer> Music_ImageUrls_2 = new ArrayList<Integer>();

    private RecyclerView MusicRecyclerView_2;

    // 노래3 슬라이드 관련 변수 (url은 기능 로직 작업때 변경)
    private ArrayList<String> Music_Info_3 = new ArrayList<String>();
    private ArrayList<String> Music_Names_3 = new ArrayList<String>();
    private ArrayList<Integer> Music_ImageUrls_3 = new ArrayList<Integer>();

    private RecyclerView MusicRecyclerView_3;

    public static String first2, first3;

    /******************************************* 레이아웃 작업 진행 중 *******************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 전체 권한을 체크하자_김선아 */
        tedPermission();
        isExternalStorageWritable();

        /******************************* 툴바에 알림 팝업 띄우기_202005_김선아 ***************************/
        // 알림을 만들어야 하므로 앱이 시작하자마자 이 코드를 실행해야 한다.
        createNotificationChannel();

        // 다음 스니펫은 사용자가 알림을 탭하면 활동을 여는 기본 인텐트를 만드는 방법을 보여준다.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0); //PendingIntent->앱이 꺼져 있오도 원격으로 킬 수가 있는 거

        // 알림의 콘텐츠와 채널 설정
        builder = new NotificationCompat.Builder(this, "Channel_Id")    // 채널 ID를 제공해야 한다.
                .setSmallIcon(R.drawable.ssong_ic)  // 작은 아이콘
                .setContentTitle("쏭테크")  // 제목
                .setContentText("쏭뮤직 입니다~!!!!!")  // 본문 텍스트
                .setStyle(new NotificationCompat.BigTextStyle().bigText("SSONG"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 알림 우선순위
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)    // 잠금화면에도 알림을 띄우겠다.
                //밑에는 intent 사용해서 작성한거
                .setContentIntent(pendingIntent);
        //.setAutoCancel(true); // 사용자가 탭하면 자동으로 알림을 삭제

        // 알림 진짜 띄우게 하는거 (알림 표시)
        // 알림을 표시하려면 notificationManager.notify를 호출하고 알림의 고유 ID 및 build의 결과값을 전달한다.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(0, builder.build()); // 0 줌

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        /* 다시 수정하기
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        */
        /******************************* 툴바에 알림 팝업 띄우기_202005_김선아 ***************************/


        /******************************** 레이아웃 작업중_200227_김선아 *********************************/
        InitializeView();
        SetListener();

        /* 장르 슬라이드 뷰 */
        GenreRecyclerView();

        /* Top100 차트 셋팅 */
        MainTop_ArrayList = new ArrayList<MusicChartListData>();
        MainTop_MusicCoverURL = new ArrayList<String>();

        /* MainTopChartGetData Asynctask 실행 */
        MainActivity.TopChartGetData topchart_h = new TopChartGetData();
        topchart_h.execute( IP_ADRESS + SSONG_MUSICCHART_PATH + "music_mobile_chart.php");

        /* 중간 배너 셋팅 */
        BannerViewSetting();

        /* 커뮤니티 TOP5 셋팅 */
        CommunitySetting();

        /* Music1 리스트 셋팅 */
        MusicRecyclerView1();

        /* Music2 리스트 셋팅 */
        MusicRecyclerView2();

        /* Music3 리스트 셋팅 */
        MusicRecyclerView3();
        /******************************************* 레이아웃 작업 진행 중 *******************************************/

//        // 로컬 자동로그인
//        SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
//
//        first2 = appData.getString("key", "");
//        first3 = appData.getString("kitae", "");
//
//        while (first2.equals("1")) {
//            startActivity(new Intent(MainActivity.this, Main2Activity.class));
//            finish();
//            break;
//        }
//        while (first3.equals("105")) {
//            startActivity(new Intent(MainActivity.this, Main2Activity.class));
//            finish();
//            break;
//        }
    }

    /********************************* 위젯 불러오기 Start *********************************/
    public void InitializeView() {
        /* 기본값 셋팅*/
        IP_ADRESS = getString(R.string.IP_ADRESS);
        SSONG_MUSICCHART_PATH = getString(R.string.SSONG_MUSICCHART_PATH);
        SSONG_MUSICPHOTO_PATH = getString(R.string.SSONG_MUSICPHOTO_PATH);

        // 장르 슬라이딩
        GenreRecyclerView = (RecyclerView) findViewById(R.id.GenreRecyclerView);

        // Top100 ChartList
        Main_lvTopChart = (ListView) findViewById(R.id.Main_lvTopChart);
        TopChart_ViewMore = (ImageView) findViewById(R.id.TopChart_ViewMore);

        // 중간 배너
        Main_viBannerLipper = (ViewFlipper) findViewById(R.id.Main_viBannerLipper);

        // 커뮤니티 TOP5 Tab
        CommuTabLayout = (TabLayout) findViewById(R.id.Main_CommuTabLayout);
        CommuViewPager = (ViewPager) findViewById(R.id.Main_CommuViewPager);
        CommuViewAdapter = new CmViewPagerAdapter(getSupportFragmentManager());

        // Music1 슬라이딩
        MusicRecyclerView_1 = (RecyclerView) findViewById(R.id.MusicRecyclerView_1);

        // Music2 슬라이딩
        MusicRecyclerView_2 = (RecyclerView) findViewById(R.id.MusicRecyclerView_2);

        // Music3 슬라이딩
        MusicRecyclerView_3 = (RecyclerView) findViewById(R.id.MusicRecyclerView_3);
    }
    /********************************* 위젯 불러오기 End *********************************/

    /********************************* 클릭 이벤트 Start *********************************/
    public void SetListener() {
        TopChart_ViewMore.setOnClickListener((v -> {
            Intent intent = new Intent(MainActivity.this, AmadoChart_Main.class);
            startActivity(intent);
        }));
    }

    /********************************** 클릭 이벤트 End **********************************/

    /******************************** 레이아웃 작업중_200227_김선아 *********************************/
    // 장르 슬라이딩 Setting
    private void GenreRecyclerView() {
        Genre_Info.add("힙합");
        Genre_Info.add("발라드");
        Genre_Info.add("댄스");
        Genre_Info.add("R&B");
        Genre_Info.add("커버곡");
        Genre_Info.add("EDM");

        Genre_Names.add("버벌진트");
        Genre_Names.add("거미");
        Genre_Names.add("레드벨벳");
        Genre_Names.add("노을");
        Genre_Names.add("창현 노래방");
        Genre_Names.add("박명수");

        Genre_ImageUrls.add(R.drawable.genre_img);
        Genre_ImageUrls.add(R.drawable.genre_img);
        Genre_ImageUrls.add(R.drawable.genre_img);
        Genre_ImageUrls.add(R.drawable.genre_img);
        Genre_ImageUrls.add(R.drawable.genre_img);
        Genre_ImageUrls.add(R.drawable.genre_img);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GenreRecyclerView.setLayoutManager(layoutManager);
        Main_GenreSliding_Adapter adapter = new Main_GenreSliding_Adapter(this, Genre_Info, Genre_Names, Genre_ImageUrls);
        GenreRecyclerView.setAdapter(adapter);
    }

    /********************************* TopChartGetData Start *********************************/
    public class TopChartGetData extends AsyncTask<String, Void, String> {
        String errorString = null;

        // 에러가 있는 경우에는 메세지를 보여주고, 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출한다.
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);

            MainTop_JsonString = result;
            TopChartResult();
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
                //outputStream.write(postParameters.getBytes("UTF-8"));
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

    // 실시간 음악 정보 데이터를 받아오자
    public void TopChartResult(){

        String TAG_JSON ="appamado_musicinfo";
        String TAG_AB_NO = "idx";
        String TAG_MS_TITLE = "music_title";
        String TAG_MB_NICK = "mb_nick";
        String TAG_GENRE1 = "first_genre";
        String TAG_GENRE2 = "second_genre";
        String TAG_MS_INFO ="music_info";
        String TAG_MS_LYRICS ="music_lyrics";
        String TAG_OPEN_YN = "open_yn";
        String TAG_PLAY_CNT = "play_cnt";
        String TAG_FI_SOURCE = "fi_source";
        String TAG_FI_FILE = "fi_file";
        //String TAG_MB_NICK ="mb_nick";

        try {
            JSONObject jsonObject = new JSONObject(MainTop_JsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String ab_no = item.getString(TAG_AB_NO);
                String ms_title = item.getString(TAG_MS_TITLE);
                String mb_nick = item.getString(TAG_MB_NICK);
                String genre1 = item.getString(TAG_GENRE1);
                String genre2 = item.getString(TAG_GENRE2);
                String ms_info = item.getString(TAG_MS_INFO);
                String ms_lyrics = item.getString(TAG_MS_LYRICS);
                String ms_openyn = item.getString(TAG_OPEN_YN);
                String ms_playcnt = item.getString(TAG_PLAY_CNT);
                String ms_fisource = item.getString(TAG_FI_SOURCE);
                String ms_imgfile = item.getString(TAG_FI_FILE);
                //String mb_nick = item.getString(TAG_MB_NICK);

                MusicChartListData musicChartListData = new MusicChartListData();

                musicChartListData.setMusic_no(ab_no);
                musicChartListData.setMusic_title(ms_title);
                musicChartListData.setMember_nick(mb_nick);
                musicChartListData.setGenre_1(genre1);
                musicChartListData.setGenre_2(genre2);
                musicChartListData.setMusic_info(ms_info);
                musicChartListData.setMusic_lyric(ms_lyrics);
                musicChartListData.setMusic_openyn(ms_openyn);
                musicChartListData.setMusic_playcnt(ms_playcnt);
                musicChartListData.setMusic_fisource(ms_fisource);
                musicChartListData.setMusic_imgfile(ms_imgfile);
                //musicalData.setMember_nick(mb_nick);

                MainTop_ArrayList.add(musicChartListData);   // 데이터를 받아와서 사용하기 위해 추가
            }
            TopChartSetting();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    /********************************* MusicChartGetData End *********************************/

    // Top100 리스트 Setting
    public void TopChartSetting() {

        // 앨범 커버 URL은 따로 배열에 담아서 전달한다.
        for (int i = 0; i < MainTop_ArrayList.size(); i++) {
            String fileurl = MainTop_ArrayList.get(i).getMusic_imgfile();
            MainTop_MusicCoverURL.add(IP_ADRESS + SSONG_MUSICPHOTO_PATH + fileurl);
        }

        TopChartList_Adapter TopAdapter = new TopChartList_Adapter(this, MainTop_ArrayList, MainTop_MusicCoverURL);

        Main_lvTopChart.setAdapter(TopAdapter);

        Main_lvTopChart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        TopAdapter.getItem(position).getMusic_title(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // 중간 배너 슬라이드 Setting
    public void BannerViewSetting() {
        BannerImages.add(R.drawable.banner_02_banner_01);
        BannerImages.add(R.drawable.banner01_2);

        for (int Baimage : BannerImages) {
            BanneripperImage(Baimage);
        }
    }

    // 중간 배너 자동 슬라이더 구현 메서드
    public void BanneripperImage(int Baimage) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(Baimage);

        Main_viBannerLipper.addView(imageView);     // 이미지 추가
        Main_viBannerLipper.setFlipInterval(4000);  // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        Main_viBannerLipper.setAutoStart(true);     // 자동 시작 유무 설정

        // animation
        Main_viBannerLipper.setInAnimation(this, android.R.anim.slide_in_left);
        Main_viBannerLipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    // 커뮤니티 TOP5 Setting
    public void CommunitySetting() {
        CommuViewAdapter.AddFragment(new CmFragmentToday(), "오늘");
        CommuViewAdapter.AddFragment(new CmFragmentYester(), "어제");
        CommuViewAdapter.AddFragment(new CmFragmentWeek(), "주간");
        CommuViewAdapter.AddFragment(new CmFragmentComment(), "댓글순");

        CommuViewPager.setAdapter(CommuViewAdapter);
        CommuTabLayout.setupWithViewPager(CommuViewPager);
    }

    // Music1 슬라이딩 Setting
    public void MusicRecyclerView1() {
        Music_Info_1.add("요즘 이곡");
        Music_Info_1.add("POP NOW");
        Music_Info_1.add("이노래 뭐지?");
        Music_Info_1.add("육성재가 들려주고 싶어서");
        Music_Info_1.add("지붕뚫고 급상승");
        Music_Info_1.add("미스터트롯 모음집");

        Music_Names_1.add("AMADO");
        Music_Names_1.add("트렌드");
        Music_Names_1.add("AMADO");
        Music_Names_1.add("육성재");
        Music_Names_1.add("AMADO");
        Music_Names_1.add("AMADO 트로트");

        Music_ImageUrls_1.add(R.drawable.slide_01_img);
        Music_ImageUrls_1.add(R.drawable.slide_01_img);
        Music_ImageUrls_1.add(R.drawable.slide_01_img);
        Music_ImageUrls_1.add(R.drawable.slide_01_img);
        Music_ImageUrls_1.add(R.drawable.slide_01_img);
        Music_ImageUrls_1.add(R.drawable.slide_01_img);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MusicRecyclerView_1.setLayoutManager(layoutManager);
        Main_MusicSliding1_Adapter adapter = new Main_MusicSliding1_Adapter(this, Music_Info_1, Music_Names_1, Music_ImageUrls_1);
        MusicRecyclerView_1.setAdapter(adapter);
    }

    // Music2 슬라이딩 Setting
    public void MusicRecyclerView2() {
        Music_Info_2.add("나 오늘 떠나요");
        Music_Info_2.add("공항으로");
        Music_Info_2.add("바빠서");
        Music_Info_2.add("투유 프로젝트");
        Music_Info_2.add("ㅇㅇㅇ");
        Music_Info_2.add("Blue");

        Music_Names_2.add("싱가포르");
        Music_Names_2.add("김해공항");
        Music_Names_2.add("개코");
        Music_Names_2.add("슈가맨");
        Music_Names_2.add("안예은");
        Music_Names_2.add("헤이트");

        Music_ImageUrls_2.add(R.drawable.slide_02_img);
        Music_ImageUrls_2.add(R.drawable.slide_02_img);
        Music_ImageUrls_2.add(R.drawable.slide_02_img);
        Music_ImageUrls_2.add(R.drawable.slide_02_img);
        Music_ImageUrls_2.add(R.drawable.slide_02_img);
        Music_ImageUrls_2.add(R.drawable.slide_02_img);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MusicRecyclerView_2.setLayoutManager(layoutManager);
        Main_MusicSliding2_Adapter adapter = new Main_MusicSliding2_Adapter(this, Music_Info_2, Music_Names_2, Music_ImageUrls_2);
        MusicRecyclerView_2.setAdapter(adapter);
    }

    // Music3 슬라이딩 Setting
    public void MusicRecyclerView3() {
        Music_Info_3.add("이주의 디깅 #77");
        Music_Info_3.add("봄에 듣는 재즈");
        Music_Info_3.add("아이돌-릭");
        Music_Info_3.add("Work/Study Lo-fi");
        Music_Info_3.add("DOPE!");
        Music_Info_3.add("WAVY WEDNESDAY");

        Music_Names_3.add("AMADO");
        Music_Names_3.add("AMAD 재즈");
        Music_Names_3.add("AMADO 트랜트");
        Music_Names_3.add("AMADO");
        Music_Names_3.add("AMADO 트랜트");
        Music_Names_3.add("AMADO 힙합");

        Music_ImageUrls_3.add(R.drawable.slide_03_img);
        Music_ImageUrls_3.add(R.drawable.slide_03_img);
        Music_ImageUrls_3.add(R.drawable.slide_03_img);
        Music_ImageUrls_3.add(R.drawable.slide_03_img);
        Music_ImageUrls_3.add(R.drawable.slide_03_img);
        Music_ImageUrls_3.add(R.drawable.slide_03_img);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MusicRecyclerView_3.setLayoutManager(layoutManager);
        Main_MusicSliding3_Adapter adapter = new Main_MusicSliding3_Adapter(this, Music_Info_3, Music_Names_3, Music_ImageUrls_3);
        MusicRecyclerView_3.setAdapter(adapter);
    }
    /**************************************** 레이아웃 작업중 *****************************************/

    /************************* ToolBar Options Setting_20228_김선아 Start **************************/

    /********************************* ToolBar Options Setting End *********************************/

    /********************************* 채널 만들기_202005_김선아 *****************************/
    private void createNotificationChannel() {
        // NotificationChannel을 생성하지만 API 26 이상 에서만 생성되는 이유는
        // NotificationChannel 클래스가 새롭고 지원 라이브러리에 없음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //SDK_INT 버전에서 조건에 의해 차단
            CharSequence name = getString(R.string.channel_name); //채널이름
            String description = getString(R.string.channel_description); //채널설명

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("Channel_Id", name, importance);
            channel.setDescription(description);    // Android 7.1 이하를 지원하려면 setDescription()을 사용하여 우선순위를 정해야 한다.

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    /********************************* 채널 만들기_202005_김선아 *****************************/

    /******************* 읽기/쓰기를 위한 전체 권한 체크 Start_202005_김선아 ******************/
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                Toast.makeText(getApplicationContext(), "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // 권한 요청 실패
                Toast.makeText(getApplicationContext(), "권한 요청을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

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
    /******************* 읽기/쓰기를 위한 전체 권한 체크 End ******************/
}
