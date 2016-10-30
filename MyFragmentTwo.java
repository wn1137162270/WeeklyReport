package myapp.lenovo.ocr2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragmentTwo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView=new TextView(getActivity());
        textView.setGravity(Gravity.START);
        Bundle args=getArguments();
        textView.setText(args.getInt("int")+"");
        textView.setTextSize(30);
        return textView;
    }

}
