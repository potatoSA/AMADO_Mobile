package ssongtech.android.amado.DataBase.Asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Sb_Menu_Profile extends AsyncTask<String,Integer, Bitmap>{

    public static Bitmap bmImg;

    @Override
    protected Bitmap doInBackground(String... urls){
        try {
            URL myFileUrl = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmImg = BitmapFactory.decodeStream(is);
        }catch (EOFException | MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmImg;
    }
    protected void onPostExecute(Bitmap img){
//        imageView.setImageBitmap(bmImg);

    }
}
