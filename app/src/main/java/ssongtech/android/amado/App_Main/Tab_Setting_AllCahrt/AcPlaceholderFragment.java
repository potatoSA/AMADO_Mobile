package ssongtech.android.amado.App_Main.Tab_Setting_AllCahrt;
/****************************************************************
 클래스명   : AcPlaceholderFragment.java
 설명       : AcSectionsPagerAdapter 에서 호출한다.
 생성일     : 2020.02.24
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ssongtech.android.amado.R;

public class AcPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private AcPageViewModel acPageViewModel;

    // 사용자가 탭을 스와이프하면 호줄되는 메소드, 탭 페이지를 가공해서 보여준다.
    public static AcPlaceholderFragment newInstance(int index) {
        AcPlaceholderFragment fragment = new AcPlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acPageViewModel = ViewModelProviders.of(this).get(AcPageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        acPageViewModel.setIndex(index);
    }

    // 레이아웃을 가져온다.
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chart_fragment_all, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        acPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}