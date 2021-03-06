package jp.co.soliton.keymanager.fragment;

import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.EditText;
import jp.co.soliton.keymanager.InformCtrl;
import jp.co.soliton.keymanager.activity.ViewPagerUpdateActivity;
import jp.co.soliton.keymanager.customview.DialogApplyMessage;
import jp.co.soliton.keymanager.customview.DialogApplyProgressBar;

/**
 * Created by luongdolong on 2/8/2017.
 *
 * Processing base input page
 */

public class ReapplyBasePageFragment extends Fragment {

    protected ViewPagerUpdateActivity pagerReapplyActivity;
    protected DialogApplyProgressBar progressDialog;
    protected InformCtrl m_InformCtrl;
    protected int m_nErroType;

    /**
     * Check null or empty string value
     *
     * @param value
     * @return
     */
    protected boolean nullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.trim().isEmpty();
    }

    /**
     * Set runnable button back next
     *
     * @param enable
     */
    protected void setButtonRunnable(boolean enable) {
        pagerReapplyActivity.setActiveBackNext(enable, enable);
    }

    /**
     * Show message
     *
     * @param message
     */
    protected void showMessage(String message) {
        DialogApplyMessage dlgMessage = new DialogApplyMessage(getContext(), message);
        dlgMessage.show();
    }

    /**
     * Show message
     *
     * @param message
     */
    protected void showMessage(String message, DialogApplyMessage.OnOkDismissMessageListener listener) {
        DialogApplyMessage dlgMessage = new DialogApplyMessage(getContext(), message);
        dlgMessage.setOnOkDismissMessageListener(listener);
        dlgMessage.show();
    }

    /**
     * Next action when click next button in every page
     */
    public void nextAction() {
    }

	/**
	 * Clear focus EditText
	 */
	public void clearFocusEditText() {
		View v = pagerReapplyActivity.getCurrentFocus();
		if (v != null && v instanceof EditText) {
			v.clearFocus();
		}
	}
}
