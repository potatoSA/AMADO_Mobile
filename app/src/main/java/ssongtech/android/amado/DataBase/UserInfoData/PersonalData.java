package ssongtech.android.amado.DataBase.UserInfoData;
/****************************************************************
 클래스명   : PersonalData.java
 설명       : UserInfo Data Setting
              MainActivity에서 사용하는 클래스
              ainActivity -ArrayList에 있는 데이터를 저장하기 위해 사용된다.
 생성일     : 2020.01.10
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

public class PersonalData {
    public String member_id;
    private String file_category;
    private String file_file;
    private String file_abno;
    private String idx;

    public String getMember_id() {
        return member_id;
    }

    public String getFile_category() { return file_category; }

    public String getFile_file() { return file_file; }

    public String getFile_abno() { return file_abno; }

    public String getIdx() { return idx; }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setFile_category(String file_category) {
        this.file_category = file_category;
    }

    public void setFile_file(String file_file) { this.file_file = file_file; }

    public void setFile_abno(String file_abno) { this.file_abno = file_abno; }

    public void setIdx(String idx) { this.idx = idx; }
}
