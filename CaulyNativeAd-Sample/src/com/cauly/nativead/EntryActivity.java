package com.cauly.nativead;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;

public class EntryActivity extends Activity implements CaulyCloseAdListener, CaulyInterstitialAdListener {

	ListView listview;
	static final String[] type = { "네이티브 리스트뷰 타입", "네이티브 뷰타입", "네이티브 카드뷰 타입" };

	private static final String APP_CODE = "vZxEr8bK";// your app code which you are assigned.
	private static final String LOG_TAG = "EntryActivity";
	private CaulyCloseAd mCloseAd;
	private CaulyInterstitialAd interstitialAd;
	CaulyAdInfo mCaulyAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();

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
		mCloseAd = new CaulyCloseAd();
		/* Optional
		 * //원하는 버튼의 문구를 설젇 할 수 있다.
		 * mCloseAd.setButtonText("취소", "종료");
		 * //원하는 텍스트의 문구를 설젇 할 수 있다.
		 * mCloseAd.setDescriptionText("종료하시겠습니까?"); */
		mCloseAd.setAdInfo(mCaulyAdInfo);
		mCloseAd.setCloseAdListener(this);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				onRequestInterstitial(null);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

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
		Log.d(LOG_TAG, "isModuleLoaded??? " + isModuleLoaded);
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
		Log.d(LOG_TAG, "onFailedToReceiveCloseAd");
	}

	@Override
	public void onLeaveCloseAd(CaulyCloseAd arg0) {
		Log.d(LOG_TAG, "onLeaveCloseAd");
	}

	@Override
	public void onLeftClicked(CaulyCloseAd arg0) {
		Log.d(LOG_TAG, "onLeftClicked");
	}

	@Override
	public void onReceiveCloseAd(CaulyCloseAd arg0, boolean arg1) {
		Log.d(LOG_TAG, "onReceiveCloseAd");
	}

	@Override
	public void onRightClicked(CaulyCloseAd arg0) {
		Log.d(LOG_TAG, "onRightClicked");
		finish();
	}

	@Override
	public void onShowedCloseAd(CaulyCloseAd arg0, boolean arg1) {
		Log.d(LOG_TAG, "onShowedCloseAd");
	}

	@Override
	public void onClosedInterstitialAd(CaulyInterstitialAd ad) {// 전면 광고가 닫힌 경우 호출됨.
		Log.d(LOG_TAG, "interstitial AD closed.");

	}

	@Override
	public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad, int errorCode, String errorMsg) {
		// 전면 광고 수신 실패할 경우 호출됨.
		Log.d(LOG_TAG, "failed to receive interstitial AD.");
	}

	@Override
	public void onLeaveInterstitialAd(CaulyInterstitialAd ad) {
		Log.d(LOG_TAG, "onLeaveInterstitialAd");
		if (interstitialAd != null) {
			interstitialAd.cancel();
		}
	}

	@Override
	public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean isChargeableAd) {
		Log.d(LOG_TAG, "onReceiveInterstitialAd");
		// 광고 수신 성공한 경우 호출됨.
		// 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
		if (isChargeableAd == false) {
			Log.d(LOG_TAG, "free interstitial AD received.");
		} else {
			Log.d(LOG_TAG, "normal interstitial AD received.");
		}
		// 노출 활성화 상태이면, 광고 노출
		if (showInterstitial) {
			ad.show();
		} else {
			ad.cancel();
		}
	}

	// 아래와 같이 전면 광고 표시 여부 플래그를 사용하면, 전면 광고 수신 후, 노출 여부를 선택할 수 있다.
	private boolean showInterstitial = false;

	// Activity 버튼 처리
	// - 전면 광고 요청 버튼
	public void onRequestInterstitial(View button) {
		Log.d(LOG_TAG, "onRequestInterstitial");
		// 전면 광고 생성
		interstitialAd = new CaulyInterstitialAd();
		interstitialAd.setAdInfo(mCaulyAdInfo);
		interstitialAd.setInterstialAdListener(this);

		// 전면광고 노출 후 back 버튼을 막기를 원할 경우 disableBackKey();을 추가한다
		// 단, requestInterstitialAd 위에서 추가되어야 합니다.
		// interstitialAd.disableBackKey(); 

		// 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
		interstitialAd.requestInterstitialAd(this);
		showInterstitial = true;
	}

}
