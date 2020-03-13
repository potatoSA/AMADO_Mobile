package ssongtech.android.amado.App_Main.Tab_Setting_Commu;
/****************************************************************
 클래스명    : CmFragmentYester.java
 설명       : 메인 커뮤니티 어제Tab Fragment 로직
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

import ssongtech.android.amado.R;

public class CmFragmentYester extends Fragment {

    View cmView;

    public CmFragmentYester() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cmView = inflater.inflate(R.layout.main_commu_yester_fragment, container, false);
        return cmView;
    }
}
