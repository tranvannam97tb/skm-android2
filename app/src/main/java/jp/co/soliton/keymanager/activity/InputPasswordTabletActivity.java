package jp.co.soliton.keymanager.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.dbalias.ElementApply;
import jp.co.soliton.keymanager.dbalias.ElementApplyManager;
import jp.co.soliton.keymanager.fragment.ContentInputPasswordTabletFragment;
import jp.co.soliton.keymanager.fragment.LeftSideInputPasswordTabletFragment;

public class InputPasswordTabletActivity extends FragmentActivity {

	private boolean isTablet;
	private ElementApplyManager elementMgr;
	private String id;
	private String cancelApply;
	Fragment fragmentLeft, fragmentContent;
	FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    fragmentManager = getSupportFragmentManager();
	    elementMgr = new ElementApplyManager(this);
	    setContentView(R.layout.activity_detail_confirm);
	    setOrientation();
	    id = getIntent().getStringExtra("ELEMENT_APPLY_ID");
	    cancelApply = getIntent().getStringExtra("CANCEL_APPLY");
    }

	public String getCancelApply() {
		return cancelApply;
	}

	public void setCancelApply(String cancelApply) {
		this.cancelApply = cancelApply;
	}

	public String getId() {
		return id;
	}

	private void setOrientation() {
		isTablet = getResources().getBoolean(R.bool.isTablet);
		if (isTablet) {
			FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
			fragmentLeft = new LeftSideInputPasswordTabletFragment();
			fragmentTransaction1.replace(R.id.fragment_left_side_menu_tablet, fragmentLeft);
			fragmentTransaction1.commit();

			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
			fragmentContent= new ContentInputPasswordTabletFragment();
			fragmentTransaction.replace(R.id.fragment_content_menu_tablet, fragmentContent);
			fragmentTransaction.commit();
		}
	}

	public void setTvValueHost(String str) {
		((LeftSideInputPasswordTabletFragment)fragmentLeft).setTvValueHost(str);
	}
	public void setTvValueUserId(String str) {
		((LeftSideInputPasswordTabletFragment)fragmentLeft).setTvValueUserId(str);
	}
	public void setTvValueApplyDate(String str) {
		((LeftSideInputPasswordTabletFragment)fragmentLeft).setTvValueApplyDate(str);
	}
	public void setTvValueStatus(String str) {
		((LeftSideInputPasswordTabletFragment)fragmentLeft).setTvValueStatus(str);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ElementApply detail = elementMgr.getElementApply(id);
		if (detail.getHost() != null) {
			setTvValueHost(detail.getHost());
		}
		if (detail.getUserId() != null) {
			setTvValueUserId(detail.getUserId());
		}
		if (detail.getUpdateDate() != null) {
			String updateDate = detail.getUpdateDate().split(" ")[0];
			setTvValueApplyDate(updateDate.replace("-", "/"));
		}
		if (detail.getStatus() == ElementApply.STATUS_APPLY_CANCEL) {
			setTvValueStatus(getString(R.string.stt_cancel));
		} else if (detail.getStatus() == ElementApply.STATUS_APPLY_PENDING) {
			setTvValueStatus(getString(R.string.stt_waiting_approval));
		} else if (detail.getStatus() == ElementApply.STATUS_APPLY_REJECT) {
			setTvValueStatus(getString(R.string.stt_rejected));
		}
	}

	@Override
	public void onBackPressed() {
		btnBackClick(null);
	}

	public void btnBackClick(View v) {
		if (isTablet) {
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit, R.anim.enter, R.anim.exit);
			fragmentTransaction.remove(fragmentContent);
			fragmentTransaction.commit();
			final Activity activity = this;
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					activity.finish();
					activity.overridePendingTransition(0, 0);
				}
			}, getResources().getInteger(android.R.integer.config_shortAnimTime));
		} else {
			finish();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		View v = getCurrentFocus();
		if (v != null &&
				(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
				v instanceof EditText &&
				!v.getClass().getName().startsWith("android.webkit.")) {
			int scrcoords[] = new int[2];
			v.getLocationOnScreen(scrcoords);
			float x = ev.getRawX() + v.getLeft() - scrcoords[0];
			float y = ev.getRawY() + v.getTop() - scrcoords[1];

			if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
				v.clearFocus();
				hideKeyboard(this);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private void hideKeyboard(Activity activity) {
		if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
		}
	}
}
