package com.ldedusoft.ldstu.viewpagerfragment;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toast;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.adapters.SchoolAdapter;
import com.ldedusoft.ldstu.component.customComp.TopBar;
import com.ldedusoft.ldstu.interfaces.FormToolBarListener;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceResault;

import java.util.ArrayList;

/**
 * @ClassName: TabDFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:56
 *
 */
public class SchoolFragment extends Fragment {
	private ArrayList<String> dataList;
	private ListView listView;
	private SchoolAdapter adapter;
	private int _position=0;

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
		return inflater.inflate(R.layout.f_school, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		_position = intent.getIntExtra("position",0);
		View view = getView();
		initListView(view);
		getData("");
	}

	private void initListView(View view){
		topBar = (TopBar)view.findViewById(R.id.school_top_bar);
		topBar.setTitle("学校列表");
		topBar.showBackBtn();
		topBar.setFormToolBarListener(new FormToolBarListener() {
			@Override
			public void OnSaveClick() {

			}

			@Override
			public void OnBackClick() {
				getActivity().finish();;
			}
		});
		listView = (ListView)view.findViewById(R.id.school_listView);
		dataList = new ArrayList<String>();
		adapter = new SchoolAdapter(getActivity(),R.layout.f_school_item,dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String school = dataList.get(position);
				//返回数据到上一个活动
				Intent intent = new Intent();
				intent.putExtra("item", school);
				intent.putExtra("position",_position);
				getActivity().setResult(getActivity().RESULT_OK, intent);
				getActivity().finish();
			}
		});
	}


	private void getData(final String time){
		String serverPath = InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().getSchoolQuery();
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.schoolQueryResult);
						if (TextUtils.isEmpty(result)) {
							//失败方法
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
						} else {
							//成功方法
							dataList = InterfaceResault.getSchoolQuery(dataList, result);
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
