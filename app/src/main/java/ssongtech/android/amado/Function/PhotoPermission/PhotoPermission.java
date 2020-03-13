package ssongtech.android.amado.Function.PhotoPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import ssongtech.android.amado.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoPermission extends AppCompatActivity {
    public final String TAG = this.getClass().getName();

    public static final int PICK_FROM_CAMERA = 1;
    public static final int PICK_FROM_CAMERA_BG = 2;
    public static final int PICK_FROM_ALBUM = 3;
    public static final int PICK_FROM_ALBUM_BG = 4;

    public String My_selectedPhoto, My_selectedPhoto_bg;

    public File My_tempFile;

    public View Submenu_mypage;
    public ImageView My_ivView, My_ivView_bg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Context mContext = getApplicationContext();
    }

    /********************** 카메라, 갤러리 실행 후 결과 받아와서 셋팅하기 Start ***********************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            if (data != null) {
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
            if (requestCode == PICK_FROM_ALBUM){
                setImage();
            } else {
                setImage_bg();
            }
        } else if (requestCode == PICK_FROM_CAMERA || requestCode == PICK_FROM_CAMERA_BG) {       // 카메라 실행 후
            if (requestCode == PICK_FROM_CAMERA){
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
                        "com.example.ou_app.fileprovider", My_tempFile);
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
                        "com.example.ou_app.fileprovider", My_tempFile);
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

        // 이미지 셋팅할 View를 찾는다.
        Submenu_mypage = getLayoutInflater().inflate(R.layout.submenu_mypage, null, false);

        My_ivView =(ImageView) Submenu_mypage.findViewById(R.id.My_ivView_pro);
        My_ivView.setImageBitmap(getRotatedBitmap(originalBm, 90)); // 이미지 뷰에 비트맵 set

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

        // 이미지 셋팅할 View를 찾는다.
        Submenu_mypage = getLayoutInflater().inflate(R.layout.submenu_mypage, null, false);

        My_ivView_bg =(ImageView) Submenu_mypage.findViewById(R.id.My_ivView_bg);
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
    /********************** 카메라, 갤러리 실행 후 결과 받아와서 셋팅하기 End ***********************/

}

