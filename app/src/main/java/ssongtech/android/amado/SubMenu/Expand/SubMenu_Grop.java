package ssongtech.android.amado.SubMenu.Expand;

import java.util.ArrayList;

public class SubMenu_Grop {
    public ArrayList<String> child;
    public String groupName;
    SubMenu_Grop(String name){
        groupName = name;
        child = new ArrayList<String>();
    }
}
