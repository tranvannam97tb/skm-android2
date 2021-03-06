package jp.co.soliton.keymanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.activity.SettingTabletActivity;

/**
 * Created by nguyenducdat on 4/25/2017.
 */

public class LeftSideMenuTabletFragment extends Fragment {

	private Button btnSetting;
	private TextView textViewGuide3;
	private Activity activity;
	private View viewFragment;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.activity = (Activity) context;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		viewFragment = inflater.inflate(R.layout.fragment_left_side_menu_tablet, container, false);
		textViewGuide3 = viewFragment.findViewById(R.id.tv_guide_3);
		btnSetting = viewFragment.findViewById(R.id.btnSetting);
		return viewFragment;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, SettingTabletActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
		String str1 = activity.getString(R.string.guide_31);
		String str2 = activity.getString(R.string.guide_32);
		textViewGuide3.measure(0,0);
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(str1 + "   " + str2);
		Bitmap bitmapSetting = BitmapFactory.decodeResource( getResources(), R.drawable.gear_guide);
		bitmapSetting = scaleBitmap(bitmapSetting, textViewGuide3.getMeasuredHeight(), true);
		Drawable drawable = new BitmapDrawable(getResources(), bitmapSetting);
		drawable.setBounds(0, 0, bitmapSetting.getWidth(), bitmapSetting.getHeight());
		ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE){
			@Override
			public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
				Paint.FontMetricsInt fm = paint.getFontMetricsInt();
				Drawable drawable = getDrawable();
				int transY = (y + fm.descent + y + fm.ascent) / 2
						- drawable.getBounds().bottom / 2;
				canvas.save();
				canvas.translate(x, transY);
				drawable.draw(canvas);
				canvas.restore();
			}
		};
		ssb.setSpan(imageSpan, str1.length() + 1  , str1.length() + 2 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textViewGuide3.setText( ssb, TextView.BufferType.SPANNABLE );
	}

	public static Bitmap scaleBitmap(Bitmap realImage, float newSize, boolean filter) {
		newSize *= 0.67;
		float ratio = Math.min(newSize / realImage.getWidth(), newSize / realImage.getHeight());
		int width = Math.round(ratio * realImage.getWidth());
		int height = Math.round(ratio * realImage.getHeight());
		Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
				height, filter);
		return newBitmap;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		viewFragment = null;
	}
}
