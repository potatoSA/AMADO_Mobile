package ssongtech.android.amado.App_Main.Tab_Setting_AllCahrt;
/****************************************************************
 클래스명   : AcPageViewModel.java
 설명       : 탭 클릭 시 화면 출력 클래스
 생성일     : 2020.02.24
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class AcPageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        // 화면에 출력할 내용
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}