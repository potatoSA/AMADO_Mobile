package ssongtech.android.amado.Function.CommunityListView;
/****************************************************************
 클래스명    : CommunityList_Adapter.java
 설명       : 메인 커뮤니티 탭 리스트 Data get/set
 생성일     : 2020.03.03
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

public class CommunityListData {

    private String Main_tvCmName;
    private String Main_tvCmInfo;
    private int Main_ivCmImage;

    public CommunityListData(String name, String info, int photo) {
        Main_tvCmName = name;
        Main_tvCmInfo = info;
        Main_ivCmImage = photo;
    }

    public String getName() {
        return Main_tvCmName;
    }

    public String getInfo() {
        return Main_tvCmInfo;
    }

    public int getImage() {
        return Main_ivCmImage;
    }

    public void setName(String name) {
        Main_tvCmName = name;
    }

    public void setInfo(String info) {
        Main_tvCmInfo = info;
    }

    public void setImage(int Image) {
        Main_ivCmImage = Image;
    }
}
