package jp.co.soliton.keymanager.fragment;

import androidx.fragment.app.Fragment;
import android.view.View;

/**
 * Created by nguyenducdat on 5/4/2017.
 */

public abstract class TabletInputFragment extends Fragment {

	public static final String TAG_TABLET_BASE_INPUT_FRAGMENT = "tabletAbtractInputFragment";

	protected View viewFragment;
	/**
	 * Check null or empty string value
	 * @param value
	 * @return
	 */
	protected boolean nullOrEmpty(String value) {
		if (value == null) {
			return true;
		}
		return value.trim().isEmpty();
	}

	public void onPageSelected(){}

	/**
	 * Next action when click next button in every page
	 */
	public abstract void nextAction();
	protected void clickSkipButton(){}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		viewFragment = null;
	}
}
