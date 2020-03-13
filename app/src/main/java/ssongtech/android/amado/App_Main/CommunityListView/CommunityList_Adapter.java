package ssongtech.android.amado.App_Main.CommunityListView;
/****************************************************************
 클래스명    : CommunityList_Adapter.java
 설명       : 메인 커뮤니티 탭 리스트 Adapter
 생성일     : 2020.03.03
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ssongtech.android.amado.DataBase.CommunityListData.CommunityListData;
import ssongtech.android.amado.R;

public class CommunityList_Adapter extends RecyclerView.Adapter<CommunityList_Adapter.MyViewHolder> {

    Context mContext;
    private List<CommunityListData> mData;

    public CommunityList_Adapter(Context Context, List<CommunityListData> Data) {
        this.mContext = Context;
        this.mData = Data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.main_commu_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Main_tvCmName.setText(mData.get(position).getName());
        holder.Main_tvCmInfo.setText(mData.get(position).getInfo());
        holder.Main_ivCmImage.setImageResource(mData.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView Main_tvCmName;
        private TextView Main_tvCmInfo;
        private CircleImageView Main_ivCmImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Main_tvCmName = (TextView)itemView.findViewById(R.id.Main_tvCmName);
            Main_tvCmInfo = (TextView)itemView.findViewById(R.id.Main_tvCmInfo);
            Main_ivCmImage = (CircleImageView)itemView.findViewById(R.id.Main_ivCmImage);
        }
    }
}
