package com.ldedusoft.ldstu.viewpagerfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.adapters.UserInfoAdapter;
import com.ldedusoft.ldstu.component.customComp.TopBar;
import com.ldedusoft.ldstu.model.InputItem;
import com.ldedusoft.ldstu.model.SysProperty;
import com.ldedusoft.ldstu.model.UserProperty;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceResault;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @ClassName: TabAFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:26
 *
 */
public class UserInfoFragment extends Fragment {

	private ArrayList<InputItem> dataList;
	private UserInfoAdapter userInfoAdapter;
	private TopBar topBar;
	private JSONObject userInfoObj;
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
		return inflater.inflate(R.layout.f_userinfo, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final FragmentMainActivity activity = ((FragmentMainActivity) getActivity());
		View fragment0View = activity.fragments.get(0).getView();
		topBar = (TopBar)fragment0View.findViewById(R.id.userinfo_top_bar);
		topBar.setTitle("用户信息");
		initListView(activity, fragment0View);
		initData();
	}

	private void initListView(FragmentMainActivity activity,View fragment0View){
		ListView userInfoListView = (ListView) fragment0View.findViewById(R.id.userinfo_listView);
		dataList = new ArrayList<InputItem>();
		userInfoAdapter = new UserInfoAdapter(activity,R.layout.f_userinfo_item,dataList);
		userInfoListView.setAdapter(userInfoAdapter);
		userInfoListView.setDividerHeight(0);
	}

	private void initData(){
		String serverPath =InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().getUserInfo(UserProperty.getInstance().getUserName());
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.userInfoResult);
						if (TextUtils.isEmpty(result)) {
							//失败方法
//							Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
							//测试模拟数据
							Toast.makeText(getActivity(),"测试模式模拟数据",Toast.LENGTH_SHORT).show();
							userInfoObj = InterfaceResault.getUserInfo();
							updateListView();
						} else {
							//成功方法
							userInfoObj = InterfaceResault.getUserInfo();
							updateListView();
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

	private void updateListView( ){
		try{
			LinkedHashMap<String,String> dicKeyMap = SysProperty.getInstance().getReportKeyDic();
			for(String dicKey:dicKeyMap.keySet()){
				Iterator iterator = userInfoObj.keys();
				while(iterator.hasNext()){
					String jsonKey = (String) iterator.next();
					if(dicKey.equals("用户信息"+jsonKey)) {
						InputItem item = new InputItem();
						item.title = dicKeyMap.get(dicKey);//取中文名
						item.value = userInfoObj.getString(jsonKey);
						dataList.add(item);
					}
				}
			}
			userInfoAdapter.notifyDataSetChanged();
		}catch (Exception e){e.printStackTrace();}

	}

	private void getUserInfoData(){

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
