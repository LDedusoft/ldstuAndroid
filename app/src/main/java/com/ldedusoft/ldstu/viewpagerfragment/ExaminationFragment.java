package com.ldedusoft.ldstu.viewpagerfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.adapters.ExaQueryAdapter;
import com.ldedusoft.ldstu.component.customComp.TopBar;
import com.ldedusoft.ldstu.model.ExaQuery;
import com.ldedusoft.ldstu.model.UserProperty;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceResault;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @ClassName: TabCFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:47
 *
 */
public class ExaminationFragment extends Fragment {
	private TextView nameEdit;
	private TextView userName,realName;
	private TextView submitBtn;
	private ListView listView;
	private ExaQueryAdapter adapter;
	private ArrayList<ExaQuery> dataList;
	private TopBar topBar;
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
		return inflater.inflate(R.layout.f_examination, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View view = this.getView();
		initView(view);
		initListView(view);
	}

	public void setUserRealName(){
		realName.setText(UserProperty.getInstance().getRealName());
	}

	private void getData(final String raceName,final String userName){
		String serverPath = InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().getExaQuery(raceName,userName);
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.exaQueryResult);
						if (TextUtils.isEmpty(result)) {
							//失败方法
							Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
						} else {
							//成功方法
							dataList = InterfaceResault.getExaQuery(dataList, result);
							adapter.notifyDataSetChanged();
						}

					}
				});
			}

			@Override
			public void onWarning(String warning) {
				Log.i("网络请求异常", warning);
			}

			@Override
			public void onError(Exception e) {
				Log.i("网络请求错误", e.getMessage());
			}
		});


	}



	private  void initListView(View view){
		listView = (ListView)view.findViewById(R.id.kaoshi_listView);
		dataList = new ArrayList<ExaQuery>();
		adapter = new ExaQueryAdapter(getActivity(),R.layout.item_exa_query,dataList);
		listView.setAdapter(adapter);
	}

	private void initView(View view){
		  nameEdit = (TextView)view.findViewById(R.id.kaoshi_mingcheng);
		  userName= (TextView)view.findViewById(R.id.kaoshi_username);
		realName =(TextView)view.findViewById(R.id.kaoshi_realName);
		  submitBtn= (TextView)view.findViewById(R.id.kaoshi_submit);
		topBar = (TopBar) view.findViewById(R.id.exa_top_bar);
		topBar.setTitle("考试查询");
		userName.setText(UserProperty.getInstance().getUserName());
//		realName.setText(UserProperty.getInstance().getRealName()); 初始化时没有数据，fragment切换时设置数据
		nameEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("activity.RaceSelectActivity");
				intent.putExtra("needBack","true");
				getActivity().startActivityForResult(intent,1);
			}
		});

//		//初始化当前时间
//		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String date = sDateFormat.format(new java.util.Date());
//		timeText.setText(date);
//
//		lilunRadio.setChecked(true);
//		lilunRadio.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				nameEdit.clearFocus();
//				hiddenKB(v);
//			}
//		});
//		caozuoRadio.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				nameEdit.clearFocus();
//				hiddenKB(v);
//			}
//		});
//		timeText.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				nameEdit.clearFocus();
//				hiddenKB(v);
//				Calendar c = Calendar.getInstance();
//				new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//					@Override
//					public void onDateSet(DatePicker dp, int year, int mounth, int day) {
//						timeText.setText(year + "-" + (mounth + 1) + "-" + day);
//					}
//				},
//						c.get(Calendar.YEAR),
//						c.get(Calendar.MONTH),
//						c.get(Calendar.DAY_OF_MONTH)).show();
//			}
//		});
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					hiddenKB(v);
					JSONObject jsonObject = new JSONObject();
					if(TextUtils.isEmpty(nameEdit.getText())){
						Toast.makeText(getActivity(),"请选择比赛名称",Toast.LENGTH_SHORT).show();
						return;
					}
					jsonObject.put("raceName", nameEdit.getText());
					jsonObject.put("userName", userName.getText());
//					jsonObject.put("time",timeText.getText());
//					if(lilunRadio.isChecked()){
//						jsonObject.put("type","liLun");
//					}else if(caozuoRadio.isChecked()){
//						jsonObject.put("type","caoZuo");
//					}
					getData(jsonObject.getString("raceName"),jsonObject.getString("userName"));
				}catch (Exception e){e.printStackTrace();}
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
