package ssongtech.android.amado.Function.SlidingVIew;
/****************************************************************
 클래스명   : SlidingImage_Adapter.java
 설명       : 서버 이미지 URL 받아와서 앨범 슬라이드 구현
 생성일     : 2020.01.15
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    : My_context(MyPage_Main), AlM_urls(앨범경로),Ms_name(노래제목)
 return	    : My_al_imageLayout(슬라이드뷰),
 ****************************************************************/

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ssongtech.android.amado.R;
import ssongtech.android.amado.SubMenu.Sb_MyPage.My_Album.Album_Manager;

import java.util.ArrayList;

public class AlbumSlidingImage_Adapter extends PagerAdapter {

    private Context My_context;
    private ArrayList<String> Alm_urls;
    private ArrayList<String> Alm_abno;
    private LayoutInflater inflater;

    public AlbumSlidingImage_Adapter(Context context, ArrayList<String> alm_urls, ArrayList<String> alm_abno){
        this.My_context = context;
        this.Alm_urls = alm_urls;
        this.Alm_abno = alm_abno;
        inflater = LayoutInflater.from(context);    // Context를 통해 LayoutInflater로 뷰를 생성한다.
    }

    // container로 인스턴스의 생명 주기를 컨트롤, position 위치의 페이지 제거
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);    // 객체 제거
    }

    // 사용 가능한 뷰의 갯수를 리턴
    @Override
    public int getCount() {
        return Alm_urls.size();
    }

    // position에 해당하는 페이지 생성
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        // 전체 슬라이드뷰를 찾는다.
        View My_al_imageLayout = inflater.inflate(R.layout.mypage_slidingimg, view, false);

        // assert는 null이 있으면 고의로 앱을 강제 종료시킴으로써 버그를 잡아낸다.
        assert My_al_imageLayout != null;
        final ImageView imageView = (ImageView) My_al_imageLayout.findViewById(R.id.My_ivAlbum);

        // Glide 라이브러리를 이용해 url을 이미지로 불러와 준다.
        Glide.with(My_context)
                .load(Alm_urls.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);

        // 앨범 이미지 클릭 시 클릭 이벤트
        imageView.setOnClickListener(v ->  {
            Intent intent = new Intent(My_context, Album_Manager.class);
            // 변환된 비트맴 이미지를 Intent로 Album_Manager에 전달해준다.
            intent.putExtra("fi_aburl", Alm_urls.get(position));
            intent.putExtra("fi_abno", Alm_abno.get(position));
            My_context.startActivity(intent);
        });

        view.addView(My_al_imageLayout, 0);   // 뷰를 추가 한다.
        return My_al_imageLayout;
    }

    // 페이지뷰가 특정 키 객체와 연관되는지 여부 체크
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    // 설정 정보에 저장된 데이터를 가져와 보여준다.
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    // 상태 정보 저장
    @Override
    public Parcelable saveState() {
        return null;
    }
}