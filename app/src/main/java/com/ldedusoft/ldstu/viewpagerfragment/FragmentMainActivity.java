package com.ldedusoft.ldstu.viewpagerfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.component.iconfont.IconfontView;
import com.ldedusoft.ldstu.model.RaceQuery;

import java.util.ArrayList;
import java.util.List;


public class FragmentMainActivity extends FragmentActivity {
	public MainViewPager viewPager;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	public String hello = "hello ";
	private IconfontView mTab1, mTab2, mTab3, mTab4;
	private TextView mTab1_txt, mTab2_txt, mTab3_txt, mTab4_txt;
	private ImageView mTabImg;
	private int currIndex = 0;// 当前页卡编号
	private int zero = 0;// 动画图片偏移量
	private int one;//单个水平动画位移
	private int two;
	private int three;
	private FragmentManager fragmentManager;
	private int displayWidth, displayHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
		displayWidth = currDisplay.getWidth();
		displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; //设置水平动画平移大小
		two = one * 2;
		three = one * 3;

		initViews();//初始化控件



		fragments.add(new UserInfoFragment());
		fragments.add(new ExercisesFragment());
		fragments.add(new ExaminationFragment());
		fragments.add(new RaceFragment());

		fragmentManager = this.getSupportFragmentManager();

		viewPager = (MainViewPager) findViewById(R.id.viewPager);
		//viewPager.setSlipping(false);//设置ViewPager是否可以滑动
		viewPager.setOffscreenPageLimit(4);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(new MyPagerAdapter());

		viewPager.setCurrentItem(1);//直接跳到第二个页面
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data==null){
			return;
		}
		switch (requestCode) {
			case 1: //选择比赛
				RaceQuery race = (RaceQuery)data.getSerializableExtra("item");
				Fragment fragment = fragments.get(2);
				TextView name = (TextView)fragment.getView().findViewById(R.id.kaoshi_mingcheng);
				name.setText(race.TestId);
				break;
			case 2: //选择学校

				String school = data.getStringExtra("item");
				int position = data.getIntExtra("position",0);
				UserInfoFragment fragment0 = (UserInfoFragment)fragments.get(0);
				fragment0.setSchool(school, position);
				break;
			default:
				break;
		}
	}

	/**
	 *@Title: initViews
	 *@Description: TODO初始化控件
	 */
	public void initViews() {
		final LinearLayout tab1Layout = (LinearLayout) findViewById(R.id.tab1Layout);
		mTab1 = (IconfontView) findViewById(R.id.img_weixin);
		mTab2 = (IconfontView) findViewById(R.id.img_address);
		mTab3 = (IconfontView) findViewById(R.id.img_friends);
		mTab4 = (IconfontView) findViewById(R.id.img_settings);
		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));
		mTab1_txt= (TextView) findViewById(R.id.bottombar_txt_userinfo);
		mTab2_txt= (TextView) findViewById(R.id.bottombar_txt_shiti);
		mTab3_txt= (TextView) findViewById(R.id.bottombar_txt_kaoshi);
		mTab4_txt= (TextView) findViewById(R.id.bottombar_txt_bisai);
		mTab1_txt.setOnClickListener(new MyOnClickListener(0));
		mTab2_txt.setOnClickListener(new MyOnClickListener(1));
		mTab3_txt.setOnClickListener(new MyOnClickListener(2));
		mTab4_txt.setOnClickListener(new MyOnClickListener(3));

		//以下是设置移动条的初始位置
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTabImg
				.getLayoutParams();
		int[] location = new int[2];
		mTab1.getLocationInWindow(location);
		int marginLeft = (displayWidth / 4 - 122) / 2;//122是tab下面移动条的宽度
		lp.setMargins(marginLeft, 0, 0, 0);
		mTabImg.setLayoutParams(lp);
		mTabImg.setVisibility(View.VISIBLE);



	}

	private void hiddenKB(View v){
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * @ClassName: MyOnPageChangeListener
	 * @Description: TODO页卡切换监听
	 * @author Panyy
	 * @date 2013 2013年11月6日 下午2:08:10
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
//			hiddenKB();
			Animation animation = null;
			switch (arg0) {
			case 0:
//				UserInfoFragment fragment0 = (UserInfoFragment)fragments.get(0);
//				fragment0.initData();
				mTab1.setTextColor(getResources().getColor(R.color.bootomBarTextSelected));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				}
				break;
			case 1:
				mTab2.setTextColor(getResources().getColor(R.color.bootomBarTextSelected));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				}
				break;
			case 2:
				ExaminationFragment fragment2 = (ExaminationFragment)fragments.get(2);
				fragment2.setUserRealName();
				mTab3.setTextColor(getResources().getColor(R.color.bootomBarTextSelected));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				}
				break;
			case 3:
				RaceFragment fragment3 = (RaceFragment)fragments.get(3);
				fragment3.getData("");
				mTab4.setTextColor(getResources().getColor(R.color.bootomBarTextSelected));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setTextColor(getResources().getColor(R.color.bottom_text_unselected));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * @ClassName: MyOnClickListener
	 * @Description: TODO头标点击监听
	 * @author Panyy
	 * @date 2013 2013年11月6日 下午2:46:08
	 *
	 */
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 1;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};

	/**
	 * @ClassName: MyPagerAdapter
	 * @Description: TODO填充ViewPager的数据适配器
	 * @author Panyy
	 * @date 2013 2013年11月6日 下午2:37:47
	 *
	 */
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(fragments.get(position)
					.getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragments.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中,用异步的方式来执行。
				 * 如果想要立即执行这个等待中的操作,就要调用这个方法(只能在主线程中调用)。
				 * 要注意的是,所有的回调和相关的行为都会在这个调用中被执行完成,因此要仔细确认这个方法的调用位置。
				 */
				fragmentManager.executePendingTransactions();
			}

			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}
			return fragment.getView();
		}
	};
}
