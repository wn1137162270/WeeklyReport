package myapp.lenovo.ocr;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OperateActivity extends FragmentActivity {
    private ViewPager viewPager;
    private TextView guideOne;
    private TextView guideTwo;
    private TextView guideThree;
    private ImageView image;
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    private int index;
    private int bmpW;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        guideOne= (TextView) findViewById(R.id.guide_one_tv);
        guideTwo= (TextView) findViewById(R.id.guide_two_tv);
        guideThree= (TextView) findViewById(R.id.guide_three_tv);
        viewPager= (ViewPager) findViewById(R.id.operate_vp);
        image = (ImageView)findViewById(R.id.cursor);

        guideOne.setOnClickListener(new MyTextViewOnClickListener(0));
        guideTwo.setOnClickListener(new MyTextViewOnClickListener(1));
        guideThree.setOnClickListener(new MyTextViewOnClickListener(2));

        setViewPager();
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW/3 - bmpW)/2;

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        image.setImageMatrix(matrix);
    }

    public void setViewPager() {
        Fragment fragmentOne = MyFragmentOne.newInstance("this is first fragment");
        Fragment fragmentTwo = MyFragmentOne.newInstance("this is second fragment");
        Fragment fragmentThree =MyFragmentOne.newInstance("this is third fragment");
        fragmentList.add(fragmentOne);
        fragmentList.add(fragmentTwo);
        fragmentList.add(fragmentThree);

        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        private int one = offset *2 +bmpW;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(index*one,position*one,0,0);
            index = position;
            animation.setFillAfter(true);
            animation.setDuration(200);
            image.startAnimation(animation);
            int i = index + 1;
            Toast.makeText(OperateActivity.this, "您选择了第"+i+"个页卡", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class MyTextViewOnClickListener implements View.OnClickListener{
        private int index;

        public MyTextViewOnClickListener(int index){
            this.index=index;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
