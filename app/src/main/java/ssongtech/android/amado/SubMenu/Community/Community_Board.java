package ssongtech.android.amado.SubMenu.Community;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ssongtech.android.amado.App_Main.BaseActivity.BaseActivity;
import ssongtech.android.amado.R;


public class Community_Board extends BaseActivity implements AbsListView.OnScrollListener {
    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private List<String> list;                      // String 데이터를 담고있는 리스트
    private List<String> list_name;
    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    private int PageNumber= 1;

    int a=0;
    private Context CommunityCtx;
    public ArrayList<BoardDataList> boardDataListArrayList;

    public static String Board_Js;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_board);

        listView =  findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        boardDataListArrayList = new ArrayList<BoardDataList>();
        list = new ArrayList<String>();
        list_name = new ArrayList<String>();
        adapter = new ListViewAdapter(this, list,list_name);

        listView.setAdapter(adapter);
//        listViewHeightSet(adapter,listView);


        progressBar.setVisibility(View.GONE);
        listView.setOnScrollListener(this);
        Board();
//        getItem();


    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);
            // 다음 데이터를 불러온다.
            PageNumber++;
            Board();

//            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem(){
        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;
        // 다음 20개의 데이터를 불러와서 리스트에 저장한다.
            for(int i =0; i < boardDataListArrayList.size(); i++){
                list.add(boardDataListArrayList.get(i).getSubject());
                list_name.add(boardDataListArrayList.get(i).getWr_name());
                a++;
            }

        boardDataListArrayList.clear();

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);
    }


    private void Board(){
        String Page_Nb = String.valueOf(PageNumber);
        String table = "free";
        String id = "pcqkdwjdlfq@naver.com";
        String wr_id = "";
        String sfl = "";
        String stx = "";
        String page = "5";
        BoardTask boardTask = new BoardTask();
        boardTask.execute("http://192.168.0.100/homepage/mobile/board/mobile_board.php",table,id,wr_id,sfl,stx,page,Page_Nb);
    }


    public class BoardTask extends AsyncTask<String, Void, String> {
        String TAG = "Board 불러오기";
        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {

            } else {
                try {
                    Board_Js = result;
                    BoardData();
                    getItem();
                    Log.d(TAG, "Board Success");
                } catch (Exception e) {
                    Log.d(TAG, "Board Error", e);
                }
            }
        }

        @Override
        public String doInBackground(String... params) {
            String serverURL, postParameters; //excute 에 서 받은 값들을 params 변수에 저장
            String table = (String) params[1];
            String id = (String) params[2];
            String wr_id = (String) params[3];
            String sfl = (String) params[4];
            String stx = (String) params[5];
            String page_rows = (String) params[6];
            String page_nb = (String)params[7];
            serverURL = (String) params[0];

            postParameters = "&board_table="+table+"&mb_id="+id+"&wr_id="+wr_id+"&sfl="+sfl+"&stx="+stx+"&page_rows="+page_rows+"&page="+page_nb; // &mb = 키값저장
            Log.d(TAG, "Board Parameters : " + postParameters);

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
                Log.d(TAG, "Local Login POST response code - " + responseStatusCode);

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
                Log.d(TAG, "LocalLoginIng Success " + sb);
                return sb.toString();  //sb로 반환
            } catch (Exception e) {

                Log.d(TAG, "LocalLoginIng Error ", e);
                return new String("LocalLoginIng Error  " + e.getMessage());
            }
        }

        public void BoardData(){
            String list = "list";
            String TAG_JSON="app_board";
            String Wr_id = "wr_id";
            String Wr_subject = "wr_subject";  // 벨류값 들고오기 (100,101 같은거)
            String Wr_Content = "wr_content";
            String Wr_Seo_Title ="wr_seo_title";
            String Mb_Id="mb_id";
            String Wr_Name="wr_name";
            String Wr_Datetime="wr_datetime";
            try {
                Board_Js = Board_Js.replace("list","");
                JSONObject jsonObject = new JSONObject(Board_Js); //mJsonString 을 jsonobject로 선언
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);  //TAG_JSON 에 있는 정보를 배열 로 선언
                //정보들을 밑에 저장
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    String wrid= item.getString(Wr_id);  //value 정보 저장
                    String wr_subject = item.getString(Wr_subject);
                    String wr_content = item.getString(Wr_Content);
                    String wr_seo_title = item.getString(Wr_Seo_Title);
                    String mb_id = item.getString(Mb_Id);
                    String wr_name = item.getString(Wr_Name);
                    String wr_datetime = item.getString(Wr_Datetime);

                    BoardDataList boardDataList = new BoardDataList();

                    boardDataList.setId(wrid);
                    boardDataList.setSubject(wr_subject);
                    boardDataList.setContent(wr_content);
                    boardDataList.setSeo_title(wr_seo_title);
                    boardDataList.setMb_id(mb_id);
                    boardDataList.setWr_name(wr_name);
                    boardDataList.setWr_datetime(wr_datetime);

                    boardDataListArrayList.add(boardDataList);
                }
                Log.d(TAG,"Board Success");
            } catch (JSONException e) {
                Log.d(TAG, "Board Err ", e);
            }
        }
    }

//    private void listViewHeightSet(Adapter listAdapter, ListView listView){
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }

}