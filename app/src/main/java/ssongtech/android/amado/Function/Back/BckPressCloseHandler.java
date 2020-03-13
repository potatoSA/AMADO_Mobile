package ssongtech.android.amado.Function.Back;
/*************************************************************
 클래스명   : BckPressCloseHandler
 설명       : 뒤로가기 기능
 생성일     : 2020.01.31
 생성자     : 박기태
 1차 수정일 :
 1차 수정자 :
 param      :
 return     :
 **************************************************************/
import android.app.Activity;
import android.widget.Toast;

public class BckPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BckPressCloseHandler(Activity context){  //생성자 생성후 생성자는 activity를 매개변수로 받는다
        this.activity = context;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){ //뒤로가기 버튼 클릭시 2초시간 지난후 토스트 메세지 띄움
            backKeyPressedTime = System.currentTimeMillis();
            showGuide(); //토스트 메시지 띄워주는 함수
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){ //한번더 누를시 종료
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity, "['뒤로']버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }


}

