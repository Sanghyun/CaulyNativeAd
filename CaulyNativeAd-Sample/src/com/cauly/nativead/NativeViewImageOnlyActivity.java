package com.cauly.nativead;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfo.Orientation;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.fsn.cauly.CaulyNativeAdView;
import com.fsn.cauly.CaulyNativeAdViewListener;

public class NativeViewImageOnlyActivity extends Activity implements CaulyNativeAdViewListener, CaulyAdViewListener {
	private static final String LOG_TAG = "NativeViewActivity";

	String APP_CODE = "vZxEr8bK";// your app code which you are assigned.
	ViewGroup native_container;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		native_container = (ViewGroup) findViewById(R.id.native_container);
		showNative();
		showBanner();
	}

	// Request Native AD
	// 네이티브 애드에 보여질 디자인을 정의하고 세팅하는 작업을 수행한다. (icon, image, title, subtitle, description ...)
	// CaulyNativeAdViewListener 를 등록하여 onReceiveNativeAd or onFailedToReceiveNativeAd 로 네이티브광고의 상태를 전달받는다.
	public void showNative() {
		CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE).layoutID(R.layout.activity_native_image_only) // 네이티브애드에 보여질 디자인을 작성하여 등록한다.
				.mainImageID(R.id.image)						// 메인 이미지 등록
				.mainImageOrientation(Orientation.LANDSCAPE).build();
		CaulyNativeAdView nativeAd = new CaulyNativeAdView(this);
		nativeAd.setAdInfo(adInfo);
		nativeAd.setAdViewListener(this);
		nativeAd.request();

	}

	private void showBanner() {
		CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).effect("RightSlide").disableDefaultBannerAd(). // 추가 시 카울리 광고 수신 실패 시에 노출되는  디폴트 배너 노출을 막을 수 있습니다.
				build();
		CaulyAdView javaAdView = new CaulyAdView(this);
		javaAdView.setAdInfo(adInfo);
		javaAdView.setAdViewListener(this);

		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.java_root_view);

		// 화면 하단에 배너 부착
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rootView.addView(javaAdView, params);
	}

	// 네이티브애드가 없거나, 네트웍상의 이유로 정상적인 수신이 불가능 할 경우 호출이 된다. 
	public void onFailedToReceiveNativeAd(CaulyNativeAdView adView, int errorCode, String errorMsg) {

	}

	// 네이티브애드가 정상적으로 수신되었을 떄, 호출된다.
	public void onReceiveNativeAd(CaulyNativeAdView adView, boolean isChargeableAd) {
		adView.attachToView(native_container);  //지정된 위치에 adView를 붙인다.
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

}
