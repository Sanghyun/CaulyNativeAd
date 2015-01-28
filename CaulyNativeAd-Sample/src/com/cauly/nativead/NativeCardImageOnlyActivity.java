package com.cauly.nativead;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfo.Direction;
import com.fsn.cauly.CaulyAdInfo.Orientation;
import com.fsn.cauly.CaulyNativeAdHelper;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.fsn.cauly.CaulyNativeAdView;
import com.fsn.cauly.CaulyNativeAdViewListener;

public class NativeCardImageOnlyActivity extends Activity implements CaulyNativeAdViewListener {
	private static final String LOG_TAG = "NativeCardActivity";

	public class Item {
		public int img;

		Item(int img) {
			this.img = img;
		}
	}


	Context context;
	ListView listview;
	ListAdapter mAdapter;
	ArrayList<Item> mList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ListAdapter();
		context = this;
		mList = new ArrayList<Item>();
		Bitmap[] icons = new Bitmap[5];
		for (int i = 0; i < 80; i++) {
			mList.add(new Item(R.drawable.d1 + i % icons.length));
		}

		setContentView(R.layout.activity_card);
		listview = (ListView) findViewById(R.id.native_area);
		listview.setAdapter(mAdapter);
		showNative();

	}

	// Request Native AD
	// 네이티브 애드에 보여질 디자인을 정의하고 세팅하는 작업을 수행한다. (icon, image, title, subtitle, description ...)
	// CaulyNativeAdViewListener 를 등록하여 onReceiveNativeAd or onFailedToReceiveNativeAd 로 네이티브광고의 상태를 전달받는다.
	public void showNative() {
		CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(AppCode.getAppCode(this)).layoutID(R.layout.activity_native_cardlistview_imge_only) // 네이티브애드에 보여질 디자인을 작성하여 등록한다.
				.mainImageID(R.id.image)						// 메인 이미지 등록
				.mainImageOrientation(Orientation.LANDSCAPE).build();
		CaulyNativeAdView nativeView = new CaulyNativeAdView(context);
		nativeView.setAdInfo(adInfo);
		nativeView.setAdViewListener(this);
		nativeView.request();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CaulyNativeAdHelper.getInstance().destroy();
	}

	class ListAdapter extends BaseAdapter {
		private static final int YOUR_ITEM_TYPE = 0;
		private static final int YOUR_ITEM_COUNT = 1;

		public ListAdapter() {
		}

		public int getCount() {
			return mList.size();
		}

		public Item getItem(int position) {
			return mList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		// 새로운 네이티브애드 타입이 존재하기 때문에 하나를 등록해준다. 
		@Override
		public int getItemViewType(int position) {
			if (CaulyNativeAdHelper.getInstance().isAdPosition(listview, position))
				return YOUR_ITEM_TYPE + 1;
			else
				return YOUR_ITEM_TYPE;
		}

		// 기존의 레이아웃타입 + 1 의 총개수 등록
		@Override
		public int getViewTypeCount() {
			return YOUR_ITEM_COUNT + 1;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			// CaulyNativeAdHelper를 이용하여, 현재 리스트뷰와 등록한 포지션을 이용하여 , 현재 뷰가 NativeAd인지 아닌지를 반환한다.
			if (CaulyNativeAdHelper.getInstance().isAdPosition(listview, position)) {
				return CaulyNativeAdHelper.getInstance().getView(listview, position, convertView);
			} else {
				//기존의 getView 코드 구현
				if (convertView == null) {
					View view = View.inflate(context, R.layout.activity_native_cardlistview_imge_only, null);
					view.setFocusable(false);
					ImageView image = (ImageView) view.findViewById(R.id.image);

					image.setImageResource(getItem(position).img);
					return view;
				} else {
					ImageView image = (ImageView) convertView.findViewById(R.id.image);
					image.setImageResource(getItem(position).img);
				}
			}
			return convertView;
		}
	}

	// 네이티브애드가 없거나, 네트웍상의 이유로 정상적인 수신이 불가능 할 경우 호출이 된다. 
	public void onFailedToReceiveNativeAd(CaulyNativeAdView adView, int errorCode, String errorMsg) {

	}

	int r = 8;

	// 네이티브애드가 정상적으로 수신되었을 떄, 호출된다.
	public void onReceiveNativeAd(CaulyNativeAdView adView, boolean isChargeableAd) {

		mList.add(r, null);		//우선 너의 앱의 리스트에 등록을 하고, 똑같은 위치의 포지션에 수신한 네이티브애드를 등록한다. 
		CaulyNativeAdHelper.getInstance().add(this, listview, r, adView);

		r = r + 4;
		mAdapter.notifyDataSetChanged();
	}
}
