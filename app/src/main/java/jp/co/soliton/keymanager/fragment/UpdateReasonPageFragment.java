package jp.co.soliton.keymanager.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.activity.ViewPagerUpdateActivity;
import jp.co.soliton.keymanager.common.SoftKeyboardCtrl;

/**
 * Created by luongdolong on 2/3/2017.
 *
 * Page input reason for apply
 */

public class UpdateReasonPageFragment extends ReapplyBasePageFragment {
    private EditText txtReason;
    private Button btnSkipReason;

    public static Fragment newInstance(Context context) {
        UpdateReasonPageFragment f = new UpdateReasonPageFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reapply_reason, null);
        txtReason = root.findViewById(R.id.txtReason);
        btnSkipReason = root.findViewById(R.id.btnSkipReason);
        initValueControl();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewPagerUpdateActivity) {
            this.pagerReapplyActivity = (ViewPagerUpdateActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Action for edit text
        txtReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setStatusControl();
            }
        });

        txtReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
		            String reason = txtReason.getText().toString().trim();
		            pagerReapplyActivity.getInputApplyInfo().setReason(reason);
		            pagerReapplyActivity.getInputApplyInfo().savePref(pagerReapplyActivity);
		            txtReason.setText(reason);
		            SoftKeyboardCtrl.hideKeyboard(v, getContext());
		            updateStatusSkipButton();
	            } else {
		            btnSkipReason.setVisibility(View.INVISIBLE);
	            }
            }
        });
    }

	private void updateStatusSkipButton() {
		if (txtReason.getText().toString().trim().length() == 0) {
			btnSkipReason.setVisibility(View.VISIBLE);
		} else {
			btnSkipReason.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void clearFocusEditText() {
		super.clearFocusEditText();
		updateStatusSkipButton();
	}

    @Override
    public void onResume() {
        super.onResume();
        //Action skip input reason
        btnSkipReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerReapplyActivity.getInputApplyInfo().setReason(null);
                pagerReapplyActivity.getInputApplyInfo().savePref(pagerReapplyActivity);
                pagerReapplyActivity.gotoConfirmApply();
            }
        });
	    updateStatusSkipButton();
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            initValueControl();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initValueControl();
        }
    }

    /**
     * Move to confirm apply screen
     */
    @Override
    public void nextAction() {
        pagerReapplyActivity.getInputApplyInfo().setReason(txtReason.getText().toString().trim());
        pagerReapplyActivity.getInputApplyInfo().savePref(pagerReapplyActivity);

        pagerReapplyActivity.gotoConfirmApply();
    }

    /**
     * init value control
     */
    private void initValueControl() {
        if (pagerReapplyActivity == null) {
            return;
        }
	    if (!nullOrEmpty(pagerReapplyActivity.getInputApplyInfo().getReason())) {
		    txtReason.setText(pagerReapplyActivity.getInputApplyInfo().getReason());
	    } else {
		    txtReason.setText("");
	    }
	    updateStatusSkipButton();
        setStatusControl();
    }

    /**
     * Set status for next/back button
     */
    private void setStatusControl() {
        if (pagerReapplyActivity.getCurrentPage() != 2) {
            return;
        }
        if (nullOrEmpty(txtReason.getText().toString())) {
            pagerReapplyActivity.setActiveBackNext(true, false);
        } else {
            pagerReapplyActivity.setActiveBackNext(true, true);
        }
    }
}
