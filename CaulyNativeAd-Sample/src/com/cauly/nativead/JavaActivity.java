package com.cauly.nativead;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.fsn.cauly.Logger;
import com.fsn.cauly.Logger.LogLevel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class JavaActivity extends Activity implements CaulyAdViewListener {

	// XML Activity 전환 버튼
	public void switchToXMLActivity(View button) {
		Intent xmlActivityIntent = new Intent(this, XMLActivity.class);
		xmlActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(xmlActivityIntent);
	}

	// 광고 요청을 위한 App Code

	private CaulyAdView javaAdView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_java);

		// Cauly 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
		//	LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
		Logger.setLogLevel(LogLevel.Info);

		//////////////////////
		//
		// Java 기반 배너 광고
		//
		//////////////////////

		// CaulyAdInfo 생성 : CaulyAdInfoBuilder 사용. APP_CODE 비롯한 전체 설정.
		// 설정 메소드
		//    	appCode(String appCode)				: APP_CODE 지정. 생성자 "CaulyAdInfoBuilder(APP_CODE)"를 사용해도 됨.
		//    	effect(CaulyAdInfo.Effect effect)	: 광고 교체 애니메이션. CaulyAdInfo.Effect.[None|LeftSlide(기본값)|RightSlide|TopSlide|BottomSlide|FadeIn|Circle]
		//    	gps(String gps)						: GPS 정보 사용 여부. [auto|off]
		//    	allowcall(boolean allowcall)		: call 광고 노출 여부. [true(기본값)|false]
		//    	dynamicReloadInterval(boolean dynamicReloadInterval)	: 광고별 갱신 주기 사용. true이면 아래의 reloadInterval 무시. [true(기본값)|false]
		//    	reloadInterval(int reloadInterval)	: 광고 갱신 주기(초단위). 기본값 20초. 최소값 15초.
		//    	threadPriority(int priority)		: 광고 요청 스레드의 우선 순위. 기본값은 부모 스레드와 동일.
		//    	bannerHeight(BannerHeight height)	: 배너 광고의 높이. CaulyAdInfo.BannerHeight.[Proportional(기본값, 디바이스 긴방향 해상도의 10%)|Fixed(48dp)]
		//    	disableDefaultBannerA()	            : 광고 수신 실패 시 디폴트 배너 노출 막기. 
		CaulyAdInfo adInfo = new CaulyAdInfoBuilder(AppCode.getAppCode(this)).effect("RightSlide").build();

		// CaulyAdInfo를 이용, CaulyAdView 생성.
		javaAdView = new CaulyAdView(this);
		javaAdView.setAdInfo(adInfo);
		javaAdView.setAdViewListener(this);

		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.java_root_view);

		// 화면 하단에 배너 부착
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rootView.addView(javaAdView, params);

	}

	//////////////////////////////
	//
	// Java 기반 배너 광고 Listener
	//
	//////////////////////////////

	// CaulyAdViewListener
	//	광고 동작에 대해 별도 처리가 필요 없는 경우,
	//	Activity의 "implements CaulyAdViewListener" 부분 제거하고 생략 가능.

	@Override
	public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
		// 광고 수신 성공 & 노출된 경우 호출됨.
		// 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
		if (isChargeableAd == false) {
			Log.d("CaulyExample", "free banner AD received.");
		} else {
			Log.d("CaulyExample", "normal banner AD received.");
		}
	}

	@Override
	public void onFailedToReceiveAd(CaulyAdView adView, int errorCode, String errorMsg) {
		// 배너 광고 수신 실패할 경우 호출됨.
		Log.d("CaulyExample", "failed to receive banner AD." + errorCode + " " + errorMsg);
	}

	@Override
	public void onShowLandingScreen(CaulyAdView adView) {
		// 광고 배너를 클릭하여 랜딩 페이지가 열린 경우 호출됨.
		Log.d("CaulyExample", "banner AD landing screen opened.");
	}

	@Override
	public void onCloseLandingScreen(CaulyAdView adView) {
		// 광고 배너를 클릭하여 열린 랜딩 페이지가 닫힌 경우 호출됨.
		Log.d("CaulyExample", "banner AD landing screen closed.");
	}

	// Activity 버튼 처리
	// - Java 배너 광고 갱신 버튼
	public void onReloadJavaAdView(View button) {
		javaAdView.reload();
	}

}
