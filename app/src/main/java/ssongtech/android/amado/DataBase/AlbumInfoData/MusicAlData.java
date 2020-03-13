package ssongtech.android.amado.DataBase.AlbumInfoData;

/****************************************************************
 클래스명   : MusicAlData.java
 설명       : AlbumInfo Data Setting
              MyPage_Main에서 사용하는 클래스
              MyPage_Main -ArrayList에 있는 데이터를 저장하기 위해 사용된다.
 생성일     : 2020.01.21
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	    :
 return	    :
 ****************************************************************/

public class MusicAlData {
    private String ab_no;
    private String wr_id;
    private String ms_name;
    private String genre1;
    private String genre2;
    private String ms_info;
    private String ms_lyrics;
    private String mb_nick;

    public String getMusic_no() {
        return ab_no;
    }

    public String getMember_id() {
        return wr_id;
    }

    public String getMusic_name() {
        return ms_name;
    }

    public String getGenre_1() { return genre1; }

    public String getGenre_2() { return genre2; }

    public String getMusic_info() { return ms_info; }

    public String getMusic_lyrics() { return ms_lyrics; }

    public String getMember_nick() { return mb_nick; }

    public void setMusic_no(String ab_no) { this.ab_no = ab_no; }

    public void setMember_id(String wr_id) { this.wr_id = wr_id; }

    public void setMusic_name(String ms_name) {
        this.ms_name = ms_name;
    }

    public void setGenre_1(String genre1) {
        this.genre1 = genre1;
    }

    public void setGenre_2(String genre2) { this.genre2 = genre2; }

    public void setMusic_info(String ms_info) { this.ms_info = ms_info; }

    public void setMusic_lyric(String ms_lyrics) { this.ms_lyrics = ms_lyrics; }

    public void setMember_nick(String mb_nick) { this.mb_nick = mb_nick; }
}
