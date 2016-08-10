package com.ldedusoft.ldstu.viewpagerfragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.adapters.ExeTypeAdapter;
import com.ldedusoft.ldstu.component.customComp.TopBar;
import com.ldedusoft.ldstu.model.ExeTypeModel;
import com.ldedusoft.ldstu.model.SysProperty;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceResault;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * @ClassName: TabBFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:35
 *
 */
public class ExercisesFragment extends Fragment {
	private final String EXE_TYPE_NORMAL = "normal";
	private final String EXE_TYPE_WRONG = "wrong";
	private final String EXE_TYPE_FAVORITE = "favorite";

	private final String EXE_TYPE_DANXUAN = "单选题";
	private final String EXE_TYPE_DUOXUAN = "多选题";
	private final String EXE_TYPE_SHIFEI = "是非题";
	private Button lianxiBtn;
	private ListView exeTypeListView;
	private ExeTypeAdapter adapter;
	private ArrayList<ExeTypeModel> dataList;
	private ScrollView exeScrollView;
	private TopBar topBar;
	private LinearLayout danxuanLayout,duoxuanLayout,shifeiLayout,qitaLayout,wrongLayout,favLayout;
	private SharedPreferences pref; //保存文件
	private SharedPreferences.Editor editor;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.f_exercises, container, false);
	}

	private Handler handler = new Handler(){
		public void handleMessage(Message msg){

		}
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		View view = this.getView();
		topBar = (TopBar)view.findViewById(R.id.exercises_top_bar);
		topBar.setTitle("试题练习");
		exeScrollView = (ScrollView)view.findViewById(R.id.exe_scrollView);
		danxuanLayout = (LinearLayout)view.findViewById(R.id.exe_danxuan_layout);
		duoxuanLayout = (LinearLayout)view.findViewById(R.id.exe_duoxuan_layout);
		shifeiLayout = (LinearLayout)view.findViewById(R.id.exe_shifei_layout);
		qitaLayout = (LinearLayout)view.findViewById(R.id.exe_qita_layout);
		wrongLayout = (LinearLayout)view.findViewById(R.id.exe_tool_wrongExe);
		favLayout = (LinearLayout)view.findViewById(R.id.exe_fav_layout);
		danxuanLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.answer.AnswerActivity");
				intent.putExtra("title","单选题练习");
				intent.putExtra("param",EXE_TYPE_NORMAL);//normal
				intent.putExtra("type",EXE_TYPE_DANXUAN);
				startActivity(intent);
			}
		});

		duoxuanLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.answer.AnswerActivity");
				intent.putExtra("title","多选题练习");
				intent.putExtra("param",EXE_TYPE_NORMAL);//normal
				intent.putExtra("type",EXE_TYPE_DUOXUAN);
				startActivity(intent);
			}
		});

		shifeiLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.answer.AnswerActivity");
				intent.putExtra("title","是非题练习");
				intent.putExtra("param",EXE_TYPE_NORMAL);//normal
				intent.putExtra("type",EXE_TYPE_SHIFEI);
				startActivity(intent);
			}
		});

		wrongLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.answer.AnswerActivity");
				intent.putExtra("title","错题练习");
				intent.putExtra("param",EXE_TYPE_WRONG);
				startActivity(intent);
			}
		});

		favLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.answer.AnswerActivity");
				intent.putExtra("title","我的收藏");
				intent.putExtra("param",EXE_TYPE_FAVORITE);
				startActivity(intent);
			}
		});

		lianxiBtn = (Button)view.findViewById(R.id.exe_lianxi_btn);
		lianxiBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.answer.AnswerActivity");
				intent.putExtra("title","随机练习");
				intent.putExtra("param",EXE_TYPE_NORMAL);//normal
				startActivity(intent);
			}
		});

		exeTypeListView = (ListView)view.findViewById(R.id.exercises_type_listView);
		dataList = new ArrayList<ExeTypeModel>();
		adapter = new ExeTypeAdapter(getActivity(),R.layout.f_exercises_type_item,dataList);
		exeTypeListView.setAdapter(adapter);
		initData();
		TimerTask task = new TimerTask(){
			public void run(){
//				Toast.makeText(getActivity(),"aaa",Toast.LENGTH_SHORT).show();
				exeScrollView.scrollTo(0, 0);
				exeScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		};
		exeTypeListView.setFocusable(false);
		exeScrollView.smoothScrollTo(0,1);
	}


	private void initData(){
		if (SysProperty.offLineModel){
			String[] cateArray = pref.getString(SysProperty.CONFIG_EXETYPE,",").split(",");
			initExeTypeData(cateArray);
			return;
		}
		String serverPath = InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().getExeCategory();
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.CourseListResult);
						if (TextUtils.isEmpty(result)) {
							//失败方法
							Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
						} else {
							//成功方法
							String[] cateArray = InterfaceResault.getExeCategory(result);
							initExeTypeData(cateArray);
						}

					}
				});
			}
			@Override
			public void onWarning(String warning) {}
			@Override
			public void onError(Exception e) {}
		});
	}

	private void initExeTypeData(String[] cateArray){
//		String[] cateArray = InterfaceResault.getExeCategory();
		String typeStr = "";
		for(int i=0;i<cateArray.length;i=i+2){
			ExeTypeModel et = new ExeTypeModel();
			et.icon1 = "list_icon_3";
			et.value1 = cateArray[i];
			typeStr +=","+cateArray[i];
			try {
				et.icon2 = "list_icon_2";
				et.value2 = cateArray[i + 1];
				typeStr +=","+cateArray[i + 1];
			}catch (Exception e){}
			dataList.add(et);
		}

		//保存知识类别到本地
		if (!TextUtils.isEmpty(typeStr)&&!SysProperty.offLineModel) {
			editor = pref.edit();
			editor.putString(SysProperty.CONFIG_EXETYPE, typeStr.substring(1, typeStr.length()));
			editor.commit();
		}

		adapter.notifyDataSetChanged();
		ViewGroup.LayoutParams params = exeTypeListView.getLayoutParams();
		params.height = adapter.getCount()*(int)getResources().getDimension(R.dimen.exercises_type_item_height);
		exeTypeListView.setLayoutParams(params);

	}
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
