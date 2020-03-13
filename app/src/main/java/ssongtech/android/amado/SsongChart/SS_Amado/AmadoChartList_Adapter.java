package ssongtech.android.amado.SsongChart.SS_Amado;
/****************************************************************
 클래스명    : AmadoChartList_Adapter.java
 설명       :
 생성일     : 2020.03.04
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.ObjectKey;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ssongtech.android.amado.DataBase.MusicChartListData.MusicChartListData;
import ssongtech.android.amado.R;

public class AmadoChartList_Adapter extends BaseAdapter {
    private final String TAG = this.getClass().getName();

    private Context SS_AmadoContext = null;
    private LayoutInflater mLayoutInflater = null;
    ArrayList<MusicChartListData> SSAMADO_ArrayList;
    ArrayList<String> SS_MusicCoverURL;

    ImageView SS_ivMusicCover;
    TextView SS_tvMusicTitle;
    TextView SS_tvArtistName;

    public AmadoChartList_Adapter(Context mContext, ArrayList<MusicChartListData> Data, ArrayList<String> FileUrl) {
        SS_AmadoContext = mContext;
        SSAMADO_ArrayList = Data;
        SS_MusicCoverURL = FileUrl;
        mLayoutInflater = LayoutInflater.from(SS_AmadoContext);
    }

    @Override
    public int getCount() {
        return SSAMADO_ArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MusicChartListData getItem(int position) {
        return SSAMADO_ArrayList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View Ssongview = mLayoutInflater.inflate(R.layout.ssong_amadochart_list, null);

        SS_ivMusicCover = (ImageView) Ssongview.findViewById(R.id.SS_ivMusicCover);
        SS_tvMusicTitle = (TextView) Ssongview.findViewById(R.id.SS_tvMusicTitle);
        SS_tvArtistName = (TextView) Ssongview.findViewById(R.id.SS_tvArtistName);

        String url = SS_MusicCoverURL.get(position);

        Glide.with(SS_AmadoContext)
                .asBitmap().load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  Log.d(TAG, "비트맵변환한거0 => " + bitmap);
                                  SS_ivMusicCover.setImageBitmap(bitmap);
                                  return false;
                              }
                          }
                ).submit();


        SS_tvMusicTitle.setText(SSAMADO_ArrayList.get(position).getMusic_title());
        SS_tvArtistName.setText(SSAMADO_ArrayList.get(position).getMember_nick());

        return Ssongview;
    }
}
