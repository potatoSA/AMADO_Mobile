package ssongtech.android.amado.App_Main.Tab_Setting_Commu;
/****************************************************************
 클래스명    : CmFragmentToday.java
 설명       : 메인 커뮤니티 오늘Tab Fragment 로직
 생성일     : 2020.03.03
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ssongtech.android.amado.DataBase.CommunityListData.CommunityListData;
import ssongtech.android.amado.App_Main.CommunityListView.CommunityList_Adapter;
import ssongtech.android.amado.R;

public class CmFragmentToday extends Fragment {

    View cmView;
    private RecyclerView cmrecyclerview;
    private List<CommunityListData> listData;

    private ArrayList<CommunityListData> CmDataList= new ArrayList<CommunityListData>();

    public CmFragmentToday() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cmView = inflater.inflate(R.layout.main_commu_today_fragment, container, false);
        cmrecyclerview = (RecyclerView)cmView.findViewById(R.id.CmRecyclerView);
        CommunityList_Adapter communityListAdapter = new CommunityList_Adapter(getContext(), listData);
        cmrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        cmrecyclerview.setAdapter(communityListAdapter);
        return cmView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listData = new ArrayList<>();
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));
        listData.add(new CommunityListData("tjsdk11", "디자인은 동사와 명사로 함께 쓰일 수 ...", R.drawable.comunity_img));

    }

}
