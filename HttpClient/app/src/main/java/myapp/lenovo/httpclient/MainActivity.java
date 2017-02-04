package myapp.lenovo.httpclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity{
    private android.app.ActionBar actionBar;

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton course;
    private RadioButton score;
    private RadioButton plan;
    private RadioButton me;
    private Drawable[] drawables=new Drawable[8];
    public List<Fragment> fragments;

    public static String loginResult;
    public static String cookie;
    public static String accountStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar=getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        Intent intent=MainActivity.this.getIntent();
        loginResult=intent.getStringExtra("loginResult");
        cookie=intent.getStringExtra("cookie");
        accountStr=intent.getStringExtra("accountStr");

        initView();
        setRadioGroup();
        setViewPager();
    }

    public void initView(){
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        radioGroup= (RadioGroup) findViewById(R.id.radio_group);
        course= (RadioButton) findViewById(R.id.course_rb);
        score= (RadioButton) findViewById(R.id.score_rb);
        plan= (RadioButton) findViewById(R.id.plan_rb);
        me= (RadioButton) findViewById(R.id.me_rb);
    }

    public void setRadioGroup(){

        drawables[0]=MainActivity.this.getResources().getDrawable(R.drawable.course_off);
        drawables[1]=MainActivity.this.getResources().getDrawable(R.drawable.score_off);
        drawables[2]=MainActivity.this.getResources().getDrawable(R.drawable.plan_off);
        drawables[3]=MainActivity.this.getResources().getDrawable(R.drawable.me_off);
        drawables[4]=MainActivity.this.getResources().getDrawable(R.drawable.course_on);
        drawables[5]=MainActivity.this.getResources().getDrawable(R.drawable.score_on);
        drawables[6]=MainActivity.this.getResources().getDrawable(R.drawable.plan_on);
        drawables[7]=MainActivity.this.getResources().getDrawable(R.drawable.me_on);
        for(int i=0;i<8;i++)
            drawables[i].setBounds(0,0,100,100);
        course.setCompoundDrawables(null, drawables[4], null, null);
        score.setCompoundDrawables(null,drawables[1],null,null);
        plan.setCompoundDrawables(null,drawables[2],null,null);
        me.setCompoundDrawables(null,drawables[3],null,null);
        radioGroup.check(R.id.course_rb);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                resetRadioGroupDrawableColor();
                switch (i){
                    case R.id.course_rb:
                        course.setCompoundDrawables(null,drawables[4],null,null);
                        viewPager.setCurrentItem(0,false);
                        actionBar.setDisplayShowTitleEnabled(true);
                        break;
                    case R.id.score_rb:
                        score.setCompoundDrawables(null,drawables[5],null,null);
                        viewPager.setCurrentItem(1,false);
                        actionBar.setDisplayShowTitleEnabled(true);
                        break;
                    case R.id.plan_rb:
                        plan.setCompoundDrawables(null,drawables[6],null,null);
                        viewPager.setCurrentItem(2,false);
                        actionBar.setDisplayShowTitleEnabled(true);
                        break;
                    case R.id.me_rb:
                        me.setCompoundDrawables(null,drawables[7],null,null);
                        viewPager.setCurrentItem(3,false);
                        actionBar.setDisplayShowTitleEnabled(false);
                        break;
                }
            }

        });
    }

    public void setViewPager(){
        CourseFragment courseFragment=new CourseFragment();
        ScoreFragment scoreFragment=new ScoreFragment();
        PlanFragment planFragment=new PlanFragment();
        MeFragment meFragment=new MeFragment();

        fragments=new ArrayList<>();
        fragments.add(courseFragment);
        fragments.add(scoreFragment);
        fragments.add(planFragment);
        fragments.add(meFragment);

        MyFragmentPagerAdapter fpg=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fpg);
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(2);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetRadioGroupDrawableColor();
                switch (position) {
                    case 0:
                        course.setCompoundDrawables(null, drawables[4], null, null);
                        viewPager.setCurrentItem(0, false);
                        radioGroup.check(R.id.course_rb);
                        actionBar.setDisplayShowTitleEnabled(true);
                        break;
                    case 1:
                        score.setCompoundDrawables(null, drawables[5], null, null);
                        viewPager.setCurrentItem(1, false);
                        radioGroup.check(R.id.score_rb);
                        actionBar.setDisplayShowTitleEnabled(true);
                        break;
                    case 2:
                        plan.setCompoundDrawables(null, drawables[6], null, null);
                        viewPager.setCurrentItem(2, false);
                        radioGroup.check(R.id.plan_rb);
                        actionBar.setDisplayShowTitleEnabled(true);
                        break;
                    case 3:
                        me.setCompoundDrawables(null, drawables[7], null, null);
                        viewPager.setCurrentItem(3, false);
                        radioGroup.check(R.id.me_rb);
                        actionBar.setDisplayShowTitleEnabled(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetRadioGroupDrawableColor(){
        course.setCompoundDrawables(null,drawables[0],null,null);
        score.setCompoundDrawables(null,drawables[1],null,null);
        plan.setCompoundDrawables(null,drawables[2],null,null);
        me.setCompoundDrawables(null,drawables[3],null,null);
    }
}
