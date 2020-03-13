package ssongtech.android.amado.Function.FileMediaStore;
/****************************************************************
 클래스명   : File_Path.java
 설명       : 갤러리 이미지/ 문서/ 비디오의 파일 경로를 반환해주는
              클래스로 MysicUp_Main에서 사용중
 생성일     : 2020.02.06
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    : File_context(자신을 호출한 클래스), File_uri(uri)
 return	    : path(갤러리, 문서, 비디오에서 선택한 경로)
 ****************************************************************/
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

public class File_Path {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getPath(final Context File_context, final Uri File_uri) {

        // 새 버전을 확인한다.
        final boolean isVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        // DocumentProvider
        if (isVersion && DocumentsContract.isDocumentUri(File_context, File_uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(File_uri)) {
                final String docId = DocumentsContract.getDocumentId(File_uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(File_uri)) {

                final String id = DocumentsContract.getDocumentId(File_uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(File_context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(File_uri)) {
                final String docId = DocumentsContract.getDocumentId(File_uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(File_context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(File_uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(File_uri))
                return File_uri.getLastPathSegment();

            return getDataColumn(File_context, File_uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(File_uri.getScheme())) {
            return File_uri.getPath();
        }

        return null;
    }

    /**
     * MediaStore Uris 및 기타 파일 기반 ContentProviders.
     *
     * @param File_context       자신을 호출한 클래스
     * @param File_uri            uri
     * @param selection     (옵션) 조회에 사용될 필터
     * @param selectionArgs (옵션) 조회에 사용될 선택 인수
     * @return data
     */
    public static String getDataColumn(Context File_context, Uri File_uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = File_context.getContentResolver().query(File_uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param File_uri uri 체크
     * @return uri의 권한이 외부저장소 인지 여부
     */
    public static boolean isExternalStorageDocument(Uri File_uri) {
        return "com.android.externalstorage.documents".equals(File_uri
                .getAuthority());
    }

    /**
     * @param File_uri uri 체크
     * @return uri의 권한이 다운로드 인지 여부
     */
    public static boolean isDownloadsDocument(Uri File_uri) {
        return "com.android.providers.downloads.documents".equals(File_uri
                .getAuthority());
    }

    /**
     * @param File_uri uri 체크
     * @return uri의 권한이 media 인지 여부
     */
    public static boolean isMediaDocument(Uri File_uri) {
        return "com.android.providers.media.documents".equals(File_uri
                .getAuthority());
    }

    /**
     * @param File_uri uri 체크
     * @return uri의 권한이 googlephoto 인지 여부
     */
    public static boolean isGooglePhotosUri(Uri File_uri) {
        return "com.google.android.apps.photos.content".equals(File_uri
                .getAuthority());
    }
}
