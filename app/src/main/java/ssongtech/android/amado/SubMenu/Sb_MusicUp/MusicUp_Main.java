package ssongtech.android.amado.SubMenu.Sb_MusicUp;
/****************************************************************
 클래스명   : MusicUp_Main.java
 설명       : 노래 음원파일, 정보를 업로드 하는 클래스
 생성일     : 2020.02.06
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.rtp.AudioCodec;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import ssongtech.android.amado.DataBase.AlbumInfoData.MusicAlData;
import ssongtech.android.amado.Function.FileMediaStore.File_Path;
import ssongtech.android.amado.R;

public class MusicUp_Main extends AppCompatActivity {
    private static final String TAG = MusicUp_Main.class.getSimpleName();

    /* 공용 셋팅 string에서 해당 변수들의 값을 수정하면 된다. */
    private String IP_ADRESS, FD_SERVER, FD_MYIMAGE, FD_MSFILE, FD_UPLOAD;

    /**************** Login_Main 에서 로그인한 id와 key를 받아오자 ****************/
    private static String Lg_login_id = "test@naver.com", Lg_login_pw = "", Lg_login_key;
    /**************** Login_Main 에서 로그인한 id와 key를 받아오자 ****************/

    // 파일/이미지 관리를 위한 변수
    private static final int PICK_FILE_REQUEST = 1;
    final int GALLERY_REQUEST_MS = 2;
    private String Ms_selectedFilePath = null, Ms_selectedImagePath = null;
    String[] parts;
    String fileName = "";

    // 다이얼로그
    ProgressDialog dialog;

    // Data Setting
    private String MS_JsonString, ab_no;
    private ArrayList<MusicAlData> MS_ArrayList;

    // 위젯 변수
    ImageView MS_ivCover, MS_ivGallery;
    Button MS_btnFile, MS_btnUpload;
    TextView MS_tvFilename, MS_tvGenreSet, MS_tvPrice;
    EditText MS_tvAtnm, MS_tvMsnm, MS_tvMsinfo, MS_tvMslyric;

    Spinner MS_spGenre1 = null, MS_spGenre2 = null;

    ScrollView MS_ScrollView;

    String Atnm_put, Msnm_put, Msinfo_put, Mslyric_put, Genre1_put, Genre2_put, Msprice_put;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicup_main);

        // 앨범 정보 데이터를 받을 변수
        MS_ArrayList = new ArrayList<MusicAlData>();

        /* 기본값 셋팅*/
        IP_ADRESS = getString(R.string.IP_ADRESS);
        FD_SERVER = getString(R.string.FD_SERVER);
        FD_MYIMAGE = getString(R.string.FD_MYIMAGE);
        FD_MSFILE = getString(R.string.FD_MSFILE);
        FD_UPLOAD = getString(R.string.FD_UPLOAD);

        InitializeView();
        SetListener();
    }

    /********************************* 위젯 불러오기 Start *********************************/
    public void InitializeView() {

        MS_btnFile = (Button) findViewById(R.id.MS_btnFile);
        MS_tvFilename = (TextView) findViewById(R.id.MS_tvFilename);

        MS_ivCover = (ImageView) findViewById(R.id.MS_ivCover);
        MS_ivGallery = (ImageView) findViewById(R.id.MS_ivGallery);
        MS_tvAtnm = (EditText) findViewById(R.id.MS_tvAtnm);
        MS_tvMsnm = (EditText) findViewById(R.id.MS_tvMsnm);
        MS_tvMsinfo = (EditText) findViewById(R.id.MS_tvMsinfo);
        MS_tvMslyric = (EditText) findViewById(R.id.MS_tvMslyric);

        MS_btnUpload = (Button) findViewById(R.id.MS_btnUpload);

        MS_spGenre1 = (Spinner) findViewById(R.id.MS_spGenre1);
        MS_spGenre2 = (Spinner) findViewById(R.id.MS_spGenre2);
        MS_tvGenreSet = (TextView) findViewById(R.id.MS_tvGenreSet);
        MS_tvPrice = (TextView) findViewById(R.id.MS_tvPrice);

        MS_ScrollView = (ScrollView) findViewById(R.id.MS_ScrollView);


    }
    /********************************* 위젯 불러오기 End *********************************/

    /**************************** 클릭 이벤트 Start ****************************/
    public void SetListener() {

        /* 곡 정보 텍스트뷰 클릭 이벤트 */
        MS_tvMsinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 스크롤뷰가 텍스트뷰 MS_tvMsinfo 의 터치이벤트를 가져가지 못하게 처리
                MS_ScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        /* 가사 텍스트뷰 클릭 이벤트 */
        MS_tvMslyric.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 스크롤뷰가 텍스트뷰 MS_tvMslyric 의 터치이벤트를 가져가지 못하게 처리
                MS_ScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        View.OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v == MS_btnFile) {  /* 파일 첨부 버튼 클릭 */
                    showFileChooser();
                } else if (v == MS_ivGallery) { /* 앨범커버 갤러리 클릭 */
                    showImageChooser();
                }

                if (v == MS_btnUpload) {    /* 업로드 버튼 클릭 */
                    // 음원 파일/정보가 있을 경우
                    if (Ms_selectedFilePath != null) {
                        dialog = ProgressDialog.show(MusicUp_Main.this, "", "Uploading File...", true);

                        // 파일 경로를 split로 잘라서 변수에 담는다.
                        parts = Ms_selectedFilePath.split("/");
                        // 파일 이름
                        fileName = parts[parts.length - 1];

                        /* 음원 파일 업로드 */
                        MusicInsert musicInsert = new MusicInsert();
                        musicInsert.execute( IP_ADRESS + FD_UPLOAD + "Music_insert.php");
                    }

                    /* 앨범 테이블에 데이터 업로드 */
                    Atnm_put = MS_tvAtnm.getText().toString();
                    Msnm_put = MS_tvMsnm.getText().toString();
                    Msinfo_put = MS_tvMsinfo.getText().toString();
                    Mslyric_put = MS_tvMslyric.getText().toString();
                    Genre1_put = MS_spGenre1.getSelectedItem().toString();
                    Genre2_put = MS_spGenre2.getSelectedItem().toString();
                    Msprice_put = MS_tvPrice.getText().toString();


                    HashMap postData_ms = new HashMap();

                    postData_ms.put("mb_id", Lg_login_id);
                    postData_ms.put("mb_name", Atnm_put);
                    postData_ms.put("ab_name", Msnm_put);
                    postData_ms.put("ab_info", Msinfo_put);
                    postData_ms.put("ab_lyric", Mslyric_put);
                    postData_ms.put("ab_fileurl", fileName);
                    postData_ms.put("ab_genre1", Genre1_put);
                    postData_ms.put("ab_genre2", Genre2_put);
                    postData_ms.put("ab_price", Msprice_put);

                    PostResponseAsyncTask AlbumDataInsert = new PostResponseAsyncTask(MusicUp_Main.this, postData_ms, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(TAG, s);
                            if (s.contains("uploaded_success")) {
                                Toast.makeText(getApplicationContext(), "Uploaded Succeddfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });

                    if (postData_ms != null && !postData_ms.equals("")) {
                        AlbumDataInsert.execute( IP_ADRESS + FD_UPLOAD + "Album_insert.php");
                        AlbumDataInsert.setEachExceptionsHandler(new EachExceptionsHandler() {  // 예외 처리
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
                    }

                    // 앨범커버 이미지가 있을 경우
                    if (Ms_selectedImagePath != null) {
                        dialog = ProgressDialog.show(MusicUp_Main.this, "", "Uploading Image...", true);

                        try {
                            Bitmap bitmap = ImageLoader.init().from(Ms_selectedImagePath).requestSize(1024,1024).getBitmap();
                            String encodedImage = ImageBase64.encode(bitmap);
                            Log.d(TAG, encodedImage);

                            for (int i = 0; i < MS_ArrayList.size(); i++) {
                                ab_no = MS_ArrayList.get(i).getMusic_no();
                            }

                            HashMap postData_alcover = new HashMap();

                            postData_alcover.put("image", encodedImage);
                            postData_alcover.put("ab_name", Msnm_put);
                            postData_alcover.put("mb_id", Lg_login_id);

                            // 앨범 커버 이미지 Insert AsyncTask
                            PostResponseAsyncTask taskinsert_ci = new PostResponseAsyncTask(MusicUp_Main.this, postData_alcover, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(TAG, s);
                                    if (s.contains("uploaded_succedduploaded_succedd")) {
                                        Toast.makeText(getApplicationContext(), "Uploaded Succeddfully",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            });

                            taskinsert_ci.execute( IP_ADRESS + FD_UPLOAD + "Abcover_insert.php");
                            taskinsert_ci.setEachExceptionsHandler(new EachExceptionsHandler() {  // 예외 처리
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
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };

        MS_btnFile.setOnClickListener(mClickListener);
        MS_ivGallery.setOnClickListener(mClickListener);
        MS_btnUpload.setOnClickListener(mClickListener);

        ///////////////////////// 장르 스피너 선택 이벤트 처리 /////////////////////////
        MS_spGenre1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 선택 되었을 경우
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MS_tvGenreSet.setText(MS_spGenre2.getSelectedItem().toString());
                MS_tvGenreSet.setText(MS_spGenre1.getSelectedItem().toString() + " , " + MS_tvGenreSet.getText().toString());
                MS_spGenre2.setEnabled(true);
            }

            // 선택 되지 않았을 경우
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "1차 장르 선택은 필수 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // MS_spGenre2는 필수 장르가 선택되어야 선택 장르로 넘어갈 수 있다.
        MS_spGenre2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (MS_spGenre1.getSelectedItem().toString() != null && !MS_spGenre1.getSelectedItem().toString().equals("")) {
                    MS_spGenre2.setEnabled(true);
                    MS_tvGenreSet.setText(MS_spGenre1.getSelectedItem().toString());
                    MS_tvGenreSet.setText(MS_tvGenreSet.getText().toString() + " , " + MS_spGenre2.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///////////////////////// 장르 스피너 선택 이벤트 처리 /////////////////////////
    }
    /**************************** 클릭 이벤트 End ****************************/

    /********************** 선택 파일 경로 얻기 Start ***********************/
    // 파일 선택하자
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        // 파일 선택 및 데이터 반환을 위한 결과를 가지고 시작
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }

    // 앨범커버 갤러리 실행해서 이미지 선택하자
    private void showImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        // 파일 선택 및 데이터 반환을 위한 결과를 가지고 시작
        startActivityForResult(Intent.createChooser(intent, "Choose Image to Upload.."), GALLERY_REQUEST_MS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    // 파일 선택 후 결과값을 받았을 때 실행
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) { // 파일 실행 후 결과
                if (data == null) {
                    //no data present
                    return;
                }

                // 선택한 파일의 Uri를 변수에 담자
                Uri selectedFileUri = data.getData();
                // 경로 변수에 담아준다.
                Ms_selectedFilePath = File_Path.getPath(this, selectedFileUri);
                Log.i(TAG, "Selected File Path:" + Ms_selectedFilePath);

                if (Ms_selectedFilePath != null && !Ms_selectedFilePath.equals("")) {
                    MS_tvFilename.setText(Ms_selectedFilePath);
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == GALLERY_REQUEST_MS) { // 갤러리 실행 후 결과
                // 선택한 이미지의 Uri를 변수에 담자
                Uri selectedImageUri = data.getData();
                // 경로 변수에 담아준다.
                Ms_selectedImagePath = File_Path.getPath(this, selectedImageUri);
                Log.i(TAG, "Selected Image Path:" + Ms_selectedImagePath);
                Bitmap bitmap = null;

                try {
                    bitmap = ImageLoader.init().from(Ms_selectedImagePath).requestSize(512,512).getBitmap();
                    MS_ivCover.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Cannot upload image to server", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    /*********************** 선택 파일 경로 얻기 End ***********************/

    /*********************** MusicUpload 실행 Start ***********************/
    public class MusicInsert extends AsyncTask<String , Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 확인을 위해 서버 응답 코드를 띄우자!
            Toast.makeText(MusicUp_Main.this, "serverResponseCode : " + result, Toast.LENGTH_SHORT).show();
            if (result == "200") {
                Toast.makeText(MusicUp_Main.this, "음원 등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = params[0];

            int serverResponseCode = 0;

            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File selectedFile = new File(Ms_selectedFilePath);

            if (!selectedFile.isFile()) {
                dialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MS_tvFilename.setText("Source File Doesn't Exist: " + Ms_selectedFilePath);
                    }
                });
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
                    httpURLConnection.setRequestProperty("uploaded_file", Ms_selectedFilePath);

                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                    // 데이터 출력 스트림으로 바이트 형태 파일 전송
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + Ms_selectedFilePath + "\"" + lineEnd);
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

                    // 200 성공시 알려준다.
                    if (serverResponseCode == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MS_tvFilename.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + IP_ADRESS + FD_MSFILE + fileName);
                            }
                        });
                    }

                    // PHP 결과 체크할 때 사용, 값을 받아온다
                    InputStreamReader tmp = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null) {
                        builder.append(str);
                    }

                    String res = builder.toString();

                    // 입,출력 스트림 닫기
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MusicUp_Main.this, "File Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(MusicUp_Main.this, "URL error!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MusicUp_Main.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                return String.valueOf(serverResponseCode);
            }
        }
    }
    /*********************** MusicUpload 실행 End ***********************/
}
