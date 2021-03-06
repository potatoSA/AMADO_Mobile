package ssongtech.android.amado.Function.SlidingVIew;
/****************************************************************
 클래스명    : Main_Music1Sliding_Adapter.java
 설명       : 서버 이미지 URL 받아와서 메인 뮤직1 슬라이드 구현
 생성일     : 2020.03.02
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

public class Main_MusicSliding1_Adapter extends RecyclerView.Adapter<Main_MusicSliding1_Adapter.ViewHolder> {
    private final String TAG = this.getClass().getName();

    private ArrayList<String> Music_Info_1 = new ArrayList<String>();
    private ArrayList<String> Music_Names_1 = new ArrayList<String>();
    private ArrayList<Integer> Music_ImageUrls_1 = new ArrayList<Integer>();
    private Context Music_Context_1;

    public Main_MusicSliding1_Adapter(Context Music1Context, ArrayList<String> MusicInfo_1, ArrayList<String> MusicNames_1, ArrayList<Integer> MusicImageUrls_1) {
        Music_Info_1 = MusicInfo_1;
        Music_Names_1 = MusicNames_1;
        Music_ImageUrls_1 = MusicImageUrls_1;
        Music_Context_1 = Music1Context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chart_music_sliding1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(Music_Context_1)
                .asBitmap()
                .load(Music_ImageUrls_1.get(position))
                .into(holder.Main_ivMusic1);

        holder.Main_tvMusicInfo_1.setText(Music_Info_1.get(position));
        holder.Main_tvMusicName_1.setText(Music_Names_1.get(position));

        holder.Main_ivMusic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + Music_Names_1.get(position));
                Toast.makeText(Music_Context_1, Music_Names_1.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Music_ImageUrls_1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView Main_ivMusic1;
        TextView Main_tvMusicInfo_1;
        TextView Main_tvMusicName_1;

        public ViewHolder(View itemView) {
            super(itemView);
            Main_ivMusic1 = itemView.findViewById(R.id.Main_ivMusic1);
            Main_tvMusicInfo_1 = itemView.findViewById(R.id.Main_tvMusicInfo_1);
            Main_tvMusicName_1 = itemView.findViewById(R.id.Main_tvMusicName_1);
        }
    }
}