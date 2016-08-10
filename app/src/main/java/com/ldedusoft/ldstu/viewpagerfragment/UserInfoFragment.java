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
import com.ldedusoft.ldstu.interfaces.FormToolBarListener;
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
		topBar.showSaveBtn();
		topBar.setTitle("用户信息");
		topBar.setFormToolBarListener(new FormToolBarListener() {
			@Override
			public void OnSaveClick() {
				try {
					JSONObject jsonObject = new JSONObject();
					//{"UName":"用户名","PassWord":"密码","RealName":"姓名","Sex":"性别","School":"学校"}
					jsonObject.put("UName", UserProperty.getInstance().getUserName());
					String psd = "";
					String newPsd = "";
					for (int i = 0; i < dataList.size(); i++) {
						if ("密码".equals(dataList.get(i).title)) {
							psd = dataList.get(i).value;
						}
						if ("确认密码".equals(dataList.get(i).title)) {
							newPsd = dataList.get(i).value;
						}
					}
					if (!psd.equals(newPsd)) {
						Toast.makeText(getActivity(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
						return;
					}
					for (int i = 0; i < dataList.size(); i++) {
						InputItem item = dataList.get(i);
						if ("考生姓名".equals(item.title)) {
							jsonObject.put("RealName", item.value);
						} else if ("性别".equals(item.title)) {
							jsonObject.put("Sex", item.value);
						} else if ("学校".equals(item.title)) {
							jsonObject.put("School", item.value);
						} else if ("密码".equals(item.title)) {
							jsonObject.put("PassWord", item.value);
						}
					}
					saveUserInfo(jsonObject.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void OnBackClick() {

			}
		});
		initListView(activity, fragment0View);
		initData();
	}

	/**
	 * 保存信息到服务器
	 * @param userInfo
	 */
	private void saveUserInfo(String userInfo){
		String serverPath = InterfaceParam.SERVER_PATH;
		String paramXml = InterfaceParam.getInstance().saveUserInfo(userInfo);//!!
		HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String result = ParseXML.getItemValueWidthName(response, InterfaceResault.ModifyUserInfoResult);
						if ("true".equals(result)) {
							Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}

			@Override
			public void onWarning(String warning) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onError(Exception e) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void initListView(FragmentMainActivity activity,View fragment0View){
		ListView userInfoListView = (ListView) fragment0View.findViewById(R.id.userinfo_listView);
		dataList = new ArrayList<InputItem>();
		userInfoAdapter = new UserInfoAdapter(activity,R.layout.f_userinfo_item,dataList);
		userInfoListView.setAdapter(userInfoAdapter);
		userInfoListView.setDividerHeight(0);
	}

	public void initData(){
		if(dataList.size()>0){
			return;
		}
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
							Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
						} else {
							//成功方法
							userInfoObj = InterfaceResault.getUserInfo(result);
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
			String realName = userInfoObj.getString("StudentName");
			UserProperty.getInstance().setRealName(realName); //真实姓名保存到全局

			for(String dicKey:dicKeyMap.keySet()){
				Iterator iterator = userInfoObj.keys();
				while(iterator.hasNext()){
					String jsonKey = (String) iterator.next();
					if(dicKey.equals("用户信息"+jsonKey)) {
						InputItem item = new InputItem();
						item.title = dicKeyMap.get(dicKey);//取中文名
						item.value = userInfoObj.getString(jsonKey);
						dataList.add(item);
						if(dicKey.equals("用户信息KSPWD")&&jsonKey.equals("KSPWD")) {
							InputItem item2 = new InputItem();
							item2.title = "确认密码";
							item2.value = userInfoObj.getString("KSPWD");
							dataList.add(item2);
						}
					}

				}
			}
			userInfoAdapter.notifyDataSetChanged();
		}catch (Exception e){e.printStackTrace();}

	}

	private void getUserInfoData(){

	}

	public void setSchool(String schoolName,int position){

		InputItem item = dataList.get(position);
		item.value = schoolName;
		dataList.set(position, item);
		userInfoAdapter.notifyDataSetChanged();
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
