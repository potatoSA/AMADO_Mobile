package ssongtech.android.amado.App_Main.Tab_Setting_Commu;
/****************************************************************
 클래스명    : CmViewPagerAdapter.java
 설명       : 메인 커뮤니티 Tab ViewPagerAdapter
 생성일     : 2020.03.03
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CmViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listTitle = new ArrayList<>();

    public CmViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
}

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listTitle.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }

    public void AddFragment (Fragment fragment, String title) {
        listFragment.add(fragment);
        listTitle.add(title);
    }
}