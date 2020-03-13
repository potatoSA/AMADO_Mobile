package ssongtech.android.amado.App_Main.Tab_Setting_AllCahrt;
/****************************************************************
 클래스명   : AcSectionsPagerAdapter.java
 설명       : 각 탭의 전반을 제어하는 프로그램, 탭 레이아웃 지정,
              페이지 수 조정 등
 생성일     : 2020.02.24
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ssongtech.android.amado.R;

/**
 * FragmentPagerAdapter 에 해당하는 파편을 반환하는
 * 섹션/탭/페이지 중 하나.
 */
public class AcSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2
                                                            , R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;

    public AcSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // 프래그먼트를 생성
    @Override
    public Fragment getItem(int position) {
        // getItem은 주어진 페이지에 대한 조각을 인스턴스화하기 위해 호출된다.
        // AcPlaceholderFragment(아래의 정적 내부 클래스로 정의됨) 반환
        return AcPlaceholderFragment.newInstance(position + 1);
    }

    // 옵션 값, 제목 정의
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    // ViewPager에 노출 할 전체 아이템 수 설정
    @Override
    public int getCount() {
        return 5;
    }
}