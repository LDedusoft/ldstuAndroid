package com.ldedusoft.ldstu.viewpagerfragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @ClassName: TabCFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:47
 *
 */
public class ExaminationFragment extends Fragment {
	private EditText nameEdit;
	private TextView timeText;
	private RadioGroup radioGroup;
	private RadioButton lilunRadio;
	private RadioButton caozuoRadio;
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



	private void getData(final String param){
		String serverPath = InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().getExaQuery(UserProperty.getInstance().getUserName());
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.exaQueryResult);
						if (TextUtils.isEmpty(result)) {
							//失败方法
//                            Toast.makeText(AnswerActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
							//测试模式
							Toast.makeText(getActivity(), "测试模式模拟数据", Toast.LENGTH_SHORT).show();
							dataList = InterfaceResault.getExaQuery(dataList, param);
							adapter.notifyDataSetChanged();
						} else {
							//成功方法
							dataList = InterfaceResault.getExaQuery(dataList, param);
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



	private  void initListView(View view){
		listView = (ListView)view.findViewById(R.id.kaoshi_listView);
		dataList = new ArrayList<ExaQuery>();
		adapter = new ExaQueryAdapter(getActivity(),R.layout.item_exa_query,dataList);
		listView.setAdapter(adapter);
	}

	private void initView(View view){
		  nameEdit = (EditText)view.findViewById(R.id.kaoshi_mingcheng);
		  timeText= (TextView)view.findViewById(R.id.kaoshi_shijian);
		  radioGroup= (RadioGroup)view.findViewById(R.id.kaoshi_radiogroup);
		  lilunRadio= (RadioButton)view.findViewById(R.id.kaoshi_radio_lilun);
		  caozuoRadio= (RadioButton)view.findViewById(R.id.kaoshi_radio_caozuo);
		  submitBtn= (TextView)view.findViewById(R.id.kaoshi_submit);
		topBar = (TopBar)view.findViewById(R.id.exa_top_bar);
		topBar.setTitle("考试查询");

		//初始化当前时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		timeText.setText(date);

		lilunRadio.setChecked(true);
		lilunRadio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nameEdit.clearFocus();
				hiddenKB(v);
			}
		});
		caozuoRadio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nameEdit.clearFocus();
				hiddenKB(v);
			}
		});
		timeText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nameEdit.clearFocus();
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
					jsonObject.put("name", nameEdit.getText());
					jsonObject.put("time",timeText.getText());
					if(lilunRadio.isChecked()){
						jsonObject.put("type","liLun");
					}else if(caozuoRadio.isChecked()){
						jsonObject.put("type","caoZuo");
					}
					paramStr = jsonObject.toString();
					getData(paramStr);
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
