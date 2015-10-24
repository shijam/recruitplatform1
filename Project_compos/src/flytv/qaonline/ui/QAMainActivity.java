package flytv.qaonline.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.view.NavigationBar;
import flytv.qaonline.view.TitleView;
import flytv.qaonline.view.TitleView.TitleClickListener;
import flytv.run.bean.TVCodeBean;

public class QAMainActivity extends FragmentActivity {
	public static boolean mustRefresh = false;
	private TitleView mTitleView;
	private NavigationBar mNavigationBar;
	private ViewPager mViewPager;
	public TVCodeBean mUserBean;
	private MyQAFragment mMyQAFragment;
	private ShareQAFragment mShareQAFragment;
	private VideoQAFragment mVideoQAFragment;
	private FragmentPgeAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_qa_main);
		initMainView();
		loadViewData();
	}
	
	private void initMainView(){
		mTitleView = (TitleView)findViewById(R.id.qa_main_title_view);
		mNavigationBar = (NavigationBar)findViewById(R.id.qa_main_navi_view);
		mViewPager = (ViewPager)findViewById(R.id.qa_main_viewpager);
		mTitleView.setTitleValue(R.string.str_return,R.string.str_qa_title, R.string.str_qa_newqa);
		
		mUserBean = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(this);
		if (!"2".equals(mUserBean.getUserType())) {
			mTitleView.mRightTv.setVisibility(View.GONE);
		}
		mTitleView.setTitleClickListener(new TitleClickListener() {
			
			@Override
			public void onRightClick(View view) {
				QAMainActivity.this.startActivityForResult(new Intent(QAMainActivity.this, NewQAActivity.class), 1);
			}
			
			@Override
			public void onLeftClick(View view) {
				QAMainActivity.this.finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mustRefresh && mViewPager != null && mViewPager.getCurrentItem() == 0){
			mustRefresh = false;
			mMyQAFragment.refreshLisView();
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg1) {
			case RESULT_OK:
				if(mViewPager != null && mViewPager.getCurrentItem() == 0){
					mMyQAFragment.refreshLisView();
				}
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
		}
	}
	
	private void loadViewData(){
		String[] navData= new String[3];
		if (!"2".equals(mUserBean.getUserType())) {
			navData = getResources().getStringArray(R.array.qaonline_nav_teacher);
		}else {
			navData = getResources().getStringArray(R.array.qaonline_nav);
		}
		List<TextView> tabViews = new ArrayList<TextView>();
		for(int i = 0;i < navData.length;i++){
			final int index = i;
			TextView textView = new TextView(this);
			textView.setText(navData[i]);
			textView.setGravity(Gravity.CENTER);
			textView.setPadding(0, 20, 0, 20);
			textView.setTextSize(16);
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mNavigationBar.setNaviIndex(index);
					mViewPager.setCurrentItem(index);
				}
			});
			tabViews.add(textView);
		}
		mNavigationBar.setNaviView(tabViews);
		
        final ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        
        mMyQAFragment = new MyQAFragment();
        mShareQAFragment = new ShareQAFragment();
        mVideoQAFragment = new VideoQAFragment();
        fragmentList.add(mMyQAFragment);
        fragmentList.add(mShareQAFragment);
        fragmentList.add(mVideoQAFragment);
        mAdapter = new FragmentPgeAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				mNavigationBar.setNaviIndex(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
        mViewPager.setCurrentItem(0);
	}
	
	
	public class FragmentPgeAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentsList;

        public FragmentPgeAdapter(FragmentManager fm) {
            super(fm);
        }

        public FragmentPgeAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
	
	
}
