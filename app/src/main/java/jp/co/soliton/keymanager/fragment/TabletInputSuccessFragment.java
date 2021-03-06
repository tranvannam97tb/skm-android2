package jp.co.soliton.keymanager.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import jp.co.soliton.keymanager.InformCtrl;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.activity.MenuAcivity;
import jp.co.soliton.keymanager.dbalias.ElementApply;

/**
 * Created by nguyenducdat on 4/25/2017.
 */

public class TabletInputSuccessFragment extends TabletInputFragment {
	TextView titleSuccess;
	TextView contentSuccess;
	private boolean isBackToTopAuto;
	private InformCtrl m_InformCtrl;
	private ElementApply element;

	public static Fragment newInstance(InformCtrl m_InformCtrl, ElementApply element) {
		TabletInputSuccessFragment f = new TabletInputSuccessFragment();
		f.isBackToTopAuto = true;
		f.m_InformCtrl = m_InformCtrl;
		f.element = element;
		return f;
	}

	public static Fragment newInstance() {
		TabletInputSuccessFragment f = new TabletInputSuccessFragment();
		return f;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		viewFragment = inflater.inflate(R.layout.fragment_apply_success_tablet, container, false);
		titleSuccess = viewFragment.findViewById(R.id.titleSuccess);
		contentSuccess = viewFragment.findViewById(R.id.contentSuccess);
		if (isBackToTopAuto) {
			contentSuccess.setText(getString(R.string.back_to_top_auto));
		}else {
			contentSuccess.setText(getString(R.string.back_to_top));
		}
		return viewFragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		contentSuccess.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isBackToTopAuto) {
					((MenuAcivity)getActivity()).startUsingProceduresFragment(m_InformCtrl, element);
				} else {
					((MenuAcivity)getActivity()).gotoMenuTablet();
				}
			}
		});
	}

	@Override
	public void nextAction() {

	}
}