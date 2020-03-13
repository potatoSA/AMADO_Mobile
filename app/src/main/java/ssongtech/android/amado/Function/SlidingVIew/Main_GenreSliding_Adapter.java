package ssongtech.android.amado.Function.SlidingVIew;
/****************************************************************
 클래스명    : Main_GenreSliding_Adapter.java
 설명       : 서버 이미지 URL 받아와서 메인 장르 슬라이드 구현
 생성일     : 2020.02.27
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

public class Main_GenreSliding_Adapter extends RecyclerView.Adapter<Main_GenreSliding_Adapter.ViewHolder> {
    private final String TAG = this.getClass().getName();

    private ArrayList<String> Genre_Info = new ArrayList<String>();
    private ArrayList<String> Genre_Names = new ArrayList<String>();
    private ArrayList<Integer> Genre_ImageUrls = new ArrayList<Integer>();
    private Context Genre_Context;

    public Main_GenreSliding_Adapter(Context GenreContext, ArrayList<String> GenreInfo, ArrayList<String> GenreNames, ArrayList<Integer> GenreImageUrls) {
        Genre_Info = GenreInfo;
        Genre_Names = GenreNames;
        Genre_ImageUrls = GenreImageUrls;
        Genre_Context = GenreContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chart_genresliding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(Genre_Context)
                .asBitmap()
                .load(Genre_ImageUrls.get(position))
                .into(holder.Main_ivGenre);

        holder.Main_tvGenreInfo.setText(Genre_Info.get(position));
        holder.Main_tvGenreName.setText(Genre_Names.get(position));

        holder.Main_ivGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + Genre_Names.get(position));
                Toast.makeText(Genre_Context, Genre_Names.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Genre_ImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView Main_ivGenre;
        TextView Main_tvGenreInfo;
        TextView Main_tvGenreName;

        public ViewHolder(View itemView) {
            super(itemView);
            Main_ivGenre = itemView.findViewById(R.id.Main_ivGenre);
            Main_tvGenreInfo = itemView.findViewById(R.id.Main_tvGenreInfo);
            Main_tvGenreName = itemView.findViewById(R.id.Main_tvGenreName);
        }
    }
}