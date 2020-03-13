package ssongtech.android.amado.DataBase.MusicChartListData;
/****************************************************************
 클래스명    : MusicChartListData.java
 설명       :
 생성일     : 2020.02.27
 생성자     : 김선아
 1차 수정일 :
 1차 수정자 :
 param	   :
 return	   :
 ****************************************************************/

public class MusicChartListData {
    private String ab_no;
    private String ms_title;
    private String mb_nick;
    private String genre1;
    private String genre2;
    private String ms_info;
    private String ms_lyrics;
    private String ms_openyn;
    private String ms_playcnt;
    private String ms_fisource;
    private String ms_imgfile;

    public String getMusic_no() {
        return ab_no;
    }

    public String getMusic_title() {
        return ms_title;
    }

    public String getGenre_1() { return genre1; }

    public String getGenre_2() { return genre2; }

    public String getMember_nick() { return mb_nick; }

    public String getMusic_info() { return ms_info; }

    public String getMusic_lyrics() { return ms_lyrics; }

    public String getMusic_openyn() { return ms_openyn; }

    public String getMusic_playcnt() { return ms_playcnt; }

    public String getMusic_fisource() { return ms_fisource; }

    public String getMusic_imgfile() { return ms_imgfile; }

    //public String getMember_nick() { return mb_nick; }

    public void setMusic_no(String ab_no) { this.ab_no = ab_no; }

    public void setMusic_title(String ms_title) {
        this.ms_title = ms_title;
    }

    public void setGenre_1(String genre1) {
        this.genre1 = genre1;
    }

    public void setGenre_2(String genre2) { this.genre2 = genre2; }

    public void setMember_nick(String mb_nick) { this.mb_nick = mb_nick; }

    public void setMusic_info(String ms_info) { this.ms_info = ms_info; }

    public void setMusic_lyric(String ms_lyrics) { this.ms_lyrics = ms_lyrics; }

    public void setMusic_openyn(String ms_openyn) { this.ms_openyn = ms_openyn; }

    public void setMusic_playcnt(String ms_playcnt) { this.ms_playcnt = ms_playcnt; }

    public void setMusic_fisource(String ms_fisource) { this.ms_fisource = ms_fisource; }

    public void setMusic_imgfile(String ms_imgfile) { this.ms_imgfile = ms_imgfile; }

    //public void setMember_nick(String mb_nick) { this.mb_nick = mb_nick; }
}
