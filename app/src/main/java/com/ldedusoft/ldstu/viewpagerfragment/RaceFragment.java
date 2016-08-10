package com.ldedusoft.ldstu.viewpagerfragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.adapters.RaceQueryAdapter;
import com.ldedusoft.ldstu.component.customComp.TopBar;
import com.ldedusoft.ldstu.interfaces.FormToolBarListener;
import com.ldedusoft.ldstu.model.RaceQuery;
import com.ldedusoft.ldstu.model.UserProperty;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceResault;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @ClassName: TabDFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:56
 *
 */
public class RaceFragment extends Fragment {
//	private EditText nameEdit;
	private TextView timeText;
	private TextView submitBtn;
	private ArrayList<RaceQuery> dataList;
	private ListView listView;
	private RaceQueryAdapter adapter;
	private TopBar topBar;
	private String needBack = "false";
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
		return inflater.inflate(R.layout.f_race, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		needBack = intent.getStringExtra("needBack");
		View view = getView();
		initView(view);
		initListView(view);
//		getData("");
	}

	private void initListView(View view){
		listView = (ListView)view.findViewById(R.id.race_listView);
		dataList = new ArrayList<RaceQuery>();
		adapter = new RaceQueryAdapter(getActivity(),R.layout.item_race_query,dataList);
		listView.setAdapter(adapter);
		if("true".equals(needBack)) {
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					RaceQuery rq = dataList.get(position);
					//返回数据到上一个活动
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("item", rq);
					intent.putExtras(bundle);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();
				}
			});
			getData(""); //查询时需要初始化数据
		}
	}

	private void initView(View view){
//		nameEdit = (EditText)view.findViewById(R.id.race_mingcheng);
		timeText = (TextView)view.findViewById(R.id.race_shijian);
		submitBtn = (TextView)view.findViewById(R.id.race_submit);
		topBar = (TopBar)view.findViewById(R.id.race_top_bar);
		topBar.setTitle("我的比赛");
		if("true".equals(needBack)){
			topBar.showBackBtn();
		}
		topBar.setFormToolBarListener(new FormToolBarListener() {
			@Override
			public void OnSaveClick() {

			}

			@Override
			public void OnBackClick() {
				getActivity().finish();
			}
		});
		//初始化当前时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		timeText.setText(date);

		timeText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				nameEdit.clearFocus();
				hiddenKB(v);
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker dp, int year, int mounth, int day) {
						timeText.setText(year + "-" + (mounth + 1) + "-" + day);
					}
				},
				c.get(Calendar.YEAR),
				c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					hiddenKB(v);
					String paramStr = "";
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("name", nameEdit.getText());
					jsonObject.put("time", timeText.getText());
					paramStr = jsonObject.toString();
//					getData(timeText.getText().toString());
					getData("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void getData(final String time){
		if(dataList.size()>0){
			return;
		}
		String serverPath = InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().getRaceQuery(time, UserProperty.getInstance().getUserName());
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.raceQueryResult);
						if (TextUtils.isEmpty(result)) {
							//失败方法
//                            Toast.makeText(AnswerActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
						} else {
							//成功方法
							dataList = InterfaceResault.getRaceQuery(dataList, result);
							adapter.notifyDataSetChanged();
						}

					}
				});
			}
			@Override
			public void onWarning(String warning) {
			}

			@Override
			public void onError(Exception e) {
			}
		});
	}
	private void hiddenKB(View v){
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
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
