package ssongtech.android.amado.DataBase.GenreInfoData;

/****************************************************************
 클래스명   : GenreInfoData.java
 설명       : 장르 데이터 셋팅
              -ArrayList에 있는 데이터를 저장하기 위해 사용된다.
 생성일     : 2020.02.20
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

public class GenreListData {
    private String code_value;
    private String code_name;

    public String getCode_value() {
        return code_value;
    }

    public String getCode_name() { return code_name; }

    public void setCode_value(String code_value) { this.code_value = code_value; }

    public void setCode_name(String code_name) { this.code_name = code_name; }
}
