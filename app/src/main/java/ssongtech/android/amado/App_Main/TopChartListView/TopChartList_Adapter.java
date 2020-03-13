package ssongtech.android.amado.App_Main.TopChartListView;
/****************************************************************
 클래스명    : TopChartList_Adapter.java
 설명       :
 생성일     : 2020.02.27
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.content.Context;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ssongtech.android.amado.DataBase.MusicChartListData.MusicChartListData;
import ssongtech.android.amado.R;

public class TopChartList_Adapter extends BaseAdapter {
    private final String TAG = this.getClass().getName();

    Context TopChart_Context = null;
    private LayoutInflater mLayoutInflater = null;
    ArrayList<MusicChartListData> MainTop_ArrayList;
    ArrayList<String> MainTop_MusicCoverURL;

    ImageView Top_ivMusicCover;
    TextView Top_tvMusicTitle;
    TextView Top_tvArtistName;

    public TopChartList_Adapter(Context mContext, ArrayList<MusicChartListData> Data, ArrayList<String> FileUrl) {
        TopChart_Context = mContext;
        MainTop_ArrayList = Data;
        MainTop_MusicCoverURL = FileUrl;
        mLayoutInflater = LayoutInflater.from(TopChart_Context);
    }

    @Override
    public int getCount() {
        return MainTop_ArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MusicChartListData getItem(int position) {
        return MainTop_ArrayList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View Topview = mLayoutInflater.inflate(R.layout.main_chart_top_list, null);

        Top_ivMusicCover = (CircleImageView) Topview.findViewById(R.id.Top_ivMusicCover);
        Top_tvMusicTitle = (TextView) Topview.findViewById(R.id.Top_tvMusicTitle);
        Top_tvArtistName = (TextView) Topview.findViewById(R.id.Top_tvArtistName);

        String url = MainTop_MusicCoverURL.get(position);

        Glide.with(TopChart_Context)
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
                                  Top_ivMusicCover.setImageBitmap(bitmap);
                                  return false;
                              }
                          }
                ).submit();

        Top_tvMusicTitle.setText(MainTop_ArrayList.get(position).getMusic_title());
        Top_tvArtistName.setText(MainTop_ArrayList.get(position).getMember_nick());

        return Topview;
    }
}
