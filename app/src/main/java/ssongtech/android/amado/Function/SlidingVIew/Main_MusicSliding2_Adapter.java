package ssongtech.android.amado.Function.SlidingVIew;
/****************************************************************
 클래스명    : Main_MusicSliding2_Adapter.java
 설명       : 서버 이미지 URL 받아와서 메인 뮤직2 슬라이드 구현
 생성일     : 2020.03.03
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ssongtech.android.amado.R;


public class Main_MusicSliding2_Adapter extends RecyclerView.Adapter<Main_MusicSliding2_Adapter.ViewHolder> {
    private final String TAG = this.getClass().getName();

    private ArrayList<String> Music_Info_2 = new ArrayList<String>();
    private ArrayList<String> Music_Names_2 = new ArrayList<String>();
    private ArrayList<Integer> Music_ImageUrls_2 = new ArrayList<Integer>();
    private Context Music_Context_2;

    public Main_MusicSliding2_Adapter(Context MusicContext_2, ArrayList<String> MusicInfo_2, ArrayList<String> MusicNames_2, ArrayList<Integer> MusicImageUrls_2) {
        Music_Info_2 = MusicInfo_2;
        Music_Names_2 = MusicNames_2;
        Music_ImageUrls_2 = MusicImageUrls_2;
        Music_Context_2 = MusicContext_2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chart_music_sliding2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(Music_Context_2)
                .asBitmap()
                .load(Music_ImageUrls_2.get(position))
                .into(holder.Main_ivMusic2);

        holder.Main_tvMusicInfo_2.setText(Music_Info_2.get(position));
        holder.Main_tvMusicName_2.setText(Music_Names_2.get(position));

        holder.Main_ivMusic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + Music_Names_2.get(position));
                Toast.makeText(Music_Context_2, Music_Names_2.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Music_ImageUrls_2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView Main_ivMusic2;
        TextView Main_tvMusicInfo_2;
        TextView Main_tvMusicName_2;

        public ViewHolder(View itemView) {
            super(itemView);
            Main_ivMusic2 = itemView.findViewById(R.id.Main_ivMusic2);
            Main_tvMusicInfo_2 = itemView.findViewById(R.id.Main_tvMusicInfo_2);
            Main_tvMusicName_2 = itemView.findViewById(R.id.Main_tvMusicName_2);
        }
    }
}