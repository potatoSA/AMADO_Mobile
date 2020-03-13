package ssongtech.android.amado.SubMenu.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ssongtech.android.amado.R;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    private List<String> list;
    private List<String> list_Name;

    public ListViewAdapter(Context context, List<String> list, List<String> list_name){
        // MainActivity 에서 데이터 리스트를 넘겨 받는다.
        this.list = list;
        this.list_Name = list_name;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.community_board_row,null);

            viewHolder = new ViewHolder();

            viewHolder.label = (TextView) convertView.findViewById(R.id.label);
            viewHolder.label_Name = (TextView)convertView.findViewById(R.id.label_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 각 셀에 넘겨받은 텍스트 데이터를 넣는다.
        viewHolder.label.setText(list.get(position) );
        viewHolder.label_Name.setText(list_Name.get(position));
        return convertView;
    }

    class ViewHolder{
        public TextView label;
        public TextView label_Name;

    }
}
