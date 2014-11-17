package com.cauly.nativead;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;

public class EntryActivity extends Activity implements CaulyCloseAdListener {

	ListView listview;
	static final String[] type = { "네이티브 리스트뷰 타입", "네이티브 뷰타입", "네이티브 카드뷰 타입" };

	private static final String APP_CODE = "vZxEr8bK";// your app code which you are assigned.
	private static final String TAG = "EntryActivity";
	private CaulyCloseAd mCloseAd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		listview = (ListView) findViewById(R.id.native_area);
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, type);
		listview.setAdapter(myAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0)
					startActivity(new Intent(EntryActivity.this, NativeListActivity.class));
				else if (position == 1)
					startActivity(new Intent(EntryActivity.this, NativeViewActivity.class));
				else
					startActivity(new Intent(EntryActivity.this, NativeCardActivity.class));
			}
		});

		//CloseAd 초기화
		CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
		mCloseAd = new CaulyCloseAd();
		/* Optional
		 * //원하는 버튼의 문구를 설젇 할 수 있다.
		 * mCloseAd.setButtonText("취소", "종료");
		 * //원하는 텍스트의 문구를 설젇 할 수 있다.
		 * mCloseAd.setDescriptionText("종료하시겠습니까?"); */
		mCloseAd.setAdInfo(closeAdInfo);
		mCloseAd.setCloseAdListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCloseAd != null) {
			mCloseAd.resume(this); // 필수 호출
		}
	}

	@Override
	public void onBackPressed() {// 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
		boolean isModuleLoaded = mCloseAd != null && mCloseAd.isModuleLoaded();
		Log.d(TAG, "isModuleLoaded??? " + isModuleLoaded);
		if (isModuleLoaded) {
			mCloseAd.show(this);
		} else {
			// 광고에 필요한 리소스를 한번만 다운받는데 실패했을 때 앱의 종료팝업 구현
			showDefaultClosePopup();
		}
	}

	private void showDefaultClosePopup() {
		new AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EntryActivity.super.onBackPressed();
			}
		}).setNegativeButton("아니요", null).show();
	}

	@Override
	public void onFailedToReceiveCloseAd(CaulyCloseAd arg0, int arg1, String arg2) {
		Log.d(TAG, "onFailedToReceiveCloseAd");
	}

	@Override
	public void onLeaveCloseAd(CaulyCloseAd arg0) {
		Log.d(TAG, "onLeaveCloseAd");
	}

	@Override
	public void onLeftClicked(CaulyCloseAd arg0) {
		Log.d(TAG, "onLeftClicked");
	}

	@Override
	public void onReceiveCloseAd(CaulyCloseAd arg0, boolean arg1) {
		Log.d(TAG, "onReceiveCloseAd");
	}

	@Override
	public void onRightClicked(CaulyCloseAd arg0) {
		Log.d(TAG, "onRightClicked");
		finish();
	}

	@Override
	public void onShowedCloseAd(CaulyCloseAd arg0, boolean arg1) {
		Log.d(TAG, "onShowedCloseAd");
	}

}
