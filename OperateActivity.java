package myapp.lenovo.ocr2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class OperateActivity extends Activity implements android.app.ActionBar.OnNavigationListener {

    private android.app.ActionBar actionBar;

    private static final String SELECTION_ITEM = "selection_item";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_operate);

        actionBar = OperateActivity.this.getActionBar();
        actionBar.setTitle("OCR");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(this, R.layout.my_text_view,
                R.id.list_item_tv, new String[]{"我的文档", "未归档", "文件", "备忘录", "名片", "证件"}), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemPosition;
        if (item.isChecked()) {
            item.setChecked(true);
        }
        switch (item.getItemId()) {
            case R.id.queue_item_one:
                Intent intentStart = new Intent(OperateActivity.this, FirstActivity.class);
                startActivity(intentStart);
            case R.id.queue_item_four:
                Toast.makeText(OperateActivity.this,"dfsfsdfs",Toast.LENGTH_SHORT).show();
                actionBar.setTitle("标签");
                itemPosition=3;
                android.app.Fragment fragment=new android.app.Fragment();
                Bundle args=new Bundle();
                args.putInt(MyFragment.SELECTION_NUMBER,itemPosition+1);
                fragment.setArguments(args);
                android.app.FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.activity_main,fragment);
                ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState.containsKey(SELECTION_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(SELECTION_ITEM));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTION_ITEM, getActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        android.app.Fragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt(MyFragment.SELECTION_NUMBER, itemPosition + 1);
        fragment.setArguments(args);
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.activity_main, fragment);
        ft.commit();
        return true;
    }
}
