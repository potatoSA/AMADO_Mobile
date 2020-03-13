package ssongtech.android.amado.SsongChart.SS_Amado;
/****************************************************************
 클래스명    : AmadoChart_Main.java
 설명       : SSONG > AMADO 차트 메인 기능 구현 클래스
 생성일     : 2020.03.04
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import ssongtech.android.amado.App_Main.BaseActivity.BaseActivity;
import ssongtech.android.amado.DataBase.MusicChartListData.MusicChartListData;
import ssongtech.android.amado.R;

public class AmadoChart_Main extends BaseActivity {
    private final String TAG = this.getClass().getName();

    /* 공용 셋팅 string에서 해당 변수들의 값을 수정하면 된다. */
    private String IP_ADRESS, SSONG_MUSICCHART_PATH, SSONG_MUSICPHOTO_PATH;

    // Data Setting
    public String SSAMADO_JsonString;
    public ArrayList<MusicChartListData> SSAMADO_ArrayList;
    public ArrayList<String> SSAMADO_MusicCoverURL;

    // AmadoChartList 변수
    private ListView SS_lvAmadoChart;

    // HDWM 스피너 변수
    Spinner SS_spHDWM;

    public static Context AmadoContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssong_amadochart);
        AmadoContext = this;

        InitializeView();
        SetListener();

        SSAMADO_ArrayList = new ArrayList<MusicChartListData>();
        SSAMADO_MusicCoverURL = new ArrayList<String>();

        /* AlbumData Asynctask 실행 */
        AmadoChart_Main.HAmadoChartGetData amadochart_h = new AmadoChart_Main.HAmadoChartGetData();
        amadochart_h.execute( IP_ADRESS + SSONG_MUSICCHART_PATH + "music_mobile_chart.php");
    }

    /********************************* 위젯 불러오기 Start *********************************/
    public void InitializeView() {
        /* 기본값 셋팅*/
        IP_ADRESS = getString(R.string.IP_ADRESS);
        SSONG_MUSICCHART_PATH = getString(R.string.SSONG_MUSICCHART_PATH);
        SSONG_MUSICPHOTO_PATH = getString(R.string.SSONG_MUSICPHOTO_PATH);

        //Amado ChartList
        SS_lvAmadoChart = (ListView)findViewById(R.id.SS_lvAmadoChart);

        //HDWM Spinner
        SS_spHDWM = (Spinner)findViewById(R.id.SS_spHDWM);
    }
    /********************************* 위젯 불러오기 End *********************************/

    /********************************* 클릭 이벤트 Start *********************************/
    public void SetListener() {

        ArrayAdapter<String> HDWMadapter = new ArrayAdapter<String>(this,   //하나의 스피너 속성 설정
                R.layout.spinner_item,
                getResources().getStringArray(R.array.HDWM));
        HDWMadapter.setDropDownViewResource(R.layout.spinner_boxitem);  //여러개의 스피너 속성 설정
        SS_spHDWM.setAdapter(HDWMadapter);

        SS_spHDWM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /********************************** 클릭 이벤트 End **********************************/

    /********************************* MusicChartGetData Start *********************************/
    public class HAmadoChartGetData extends AsyncTask<String, Void, String> {
        String errorString = null;

        // 에러가 있는 경우에는 메세지를 보여주고, 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출한다.
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);

            SSAMADO_JsonString = result;
            HAmadoChartResult();
        }

        // 서버에 있는 PHP파일을 실행시키고 응답을 저장하고, 스트링으로 변환해서 리턴한다.
        @Override
        public String doInBackground(String... params) {
            String serverURL = params[0];
            //String postParameters = params[1];

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
    public void HAmadoChartResult(){

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
            JSONObject jsonObject = new JSONObject(SSAMADO_JsonString);
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

                SSAMADO_ArrayList.add(musicChartListData);   // 데이터를 받아와서 사용하기 위해 추가
            }
            HAmadoChartData();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    /***************************** AMADO 차트 리스트 Setting *****************************/
    public void HAmadoChartData() {
        // 앨범 커버 URL은 따로 배열에 담아서 전달한다.
        for (int i = 0; i < SSAMADO_ArrayList.size(); i++) {
            String fileurl = SSAMADO_ArrayList.get(i).getMusic_imgfile();
            SSAMADO_MusicCoverURL.add(IP_ADRESS + SSONG_MUSICPHOTO_PATH + fileurl);
        }

        AmadoChartList_Adapter myAdapter = new AmadoChartList_Adapter(this, SSAMADO_ArrayList, SSAMADO_MusicCoverURL);

        SS_lvAmadoChart.setAdapter(myAdapter);

        listViewHeightSet(myAdapter, SS_lvAmadoChart);

        SS_lvAmadoChart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getMusic_title(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // 리스트뷰 데이터가 있는 만큼 목록을 만큼 길이를 설정 한다.
    private void listViewHeightSet(Adapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /***************************** AMADO 차트 리스트 SetEnd *****************************/


}
