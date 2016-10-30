package myapp.lenovo.ocr2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFragment extends Fragment {
    private ListView listView;
    private String[] names={"我的文档", "未归档", "文件", "备忘录", "名片", "证件"};
    private String[] number={"(0)","(0)","(0)","(0)","(0)","(0)"};

    public static final String SELECTION_NUMBER="selection_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        List<Map<String,String>> list=new ArrayList<>();
        for(int i=0;i<names.length;i++){
            Map<String,String> listItem=new HashMap<>();
            listItem.put("name",names[i]);
            listItem.put("number",number[i]);
            list.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),list,R.layout.tab_item,new String[]{"name","number"},new int[]{R.id.name_tv,R.id.number_tv});
        View rootView=inflater.inflate(R.layout.fragment_my,container,false);
        listView=(ListView)rootView.findViewById(R.id.tab_lv);
        listView.setAdapter(simpleAdapter);
        Bundle args=getArguments();
        args.getInt(SELECTION_NUMBER);
        return rootView;
    }
}
