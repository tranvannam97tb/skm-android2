package jp.co.soliton.keymanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.activity.*;
import jp.co.soliton.keymanager.dbalias.ElementApply;
import jp.co.soliton.keymanager.manager.APIDManager;

import java.util.List;

/**
 * Created by nguyenducdat on 4/25/2017.
 */

public class ContentMenuTabletFragment extends Fragment {

	RelativeLayout rlMenuStart;
	RelativeLayout rlMenuAPID;
	RelativeLayout rlMenuConfirmApply;
	TextView contentVPN;
	TextView contentWifi;
	TextView titleWifi;
	TextView titleVPN;
	private APIDManager apidManager;
	Activity activity;

	public static Fragment newInstance() {
		ContentMenuTabletFragment f = new ContentMenuTabletFragment();
		return f;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.activity = (Activity) context;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content_menu_tablet, container, false);
		rlMenuStart = (RelativeLayout) view.findViewById(R.id.rl_menu_start);
		rlMenuAPID = (RelativeLayout) view.findViewById(R.id.rl_menu_apid);
		rlMenuConfirmApply = (RelativeLayout) view.findViewById(R.id.rl_menu_confirm_apply);
		contentVPN = (TextView) view.findViewById(R.id.content_vpn);
		contentWifi = (TextView) view.findViewById(R.id.content_wifi);
		titleWifi = (TextView) view.findViewById(R.id.title_wifi);
		titleVPN = (TextView) view.findViewById(R.id.title_vpn);
		apidManager = new APIDManager(activity);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateViewTitle();
		updateMenuConfirm();
		setupControl();
		String strVpnID = apidManager.getStrVpnID();
		String strUDID = apidManager.getStrUDID();
		contentVPN.setText(strVpnID);
		contentWifi.setText(strUDID);
	}

	private void updateViewTitle() {
		titleWifi.measure(0, 0);
		titleVPN.measure(0, 0);
		int widthWifi = titleWifi.getMeasuredWidth();
		int widthVpn = titleVPN.getMeasuredWidth();
		if (widthWifi < widthVpn) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleWifi.getLayoutParams();
			params.width = widthVpn;
			titleWifi.setLayoutParams(params);
		} else {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleVPN.getLayoutParams();
			params.width = widthWifi;
			titleVPN.setLayoutParams(params);
		}
	}

	private void setupControl() {
			rlMenuStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((MenuAcivity)activity).setFocusMenuTablet(false);
					((MenuAcivity)activity).startApplyActivityFragment();
				}
			});

			rlMenuAPID.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((MenuAcivity)activity).setFocusMenuTablet(false);
					((MenuAcivity)getActivity()).startActivityAPID();
				}
			});
	}

	private void updateMenuConfirm() {
		final List<ElementApply> listElementApply = ((MenuAcivity)activity).getElementMgr().getAllElementApply();
        if (listElementApply.isEmpty()) {
            rlMenuConfirmApply.setVisibility(View.GONE);
        } else {
	        rlMenuConfirmApply.setVisibility(View.VISIBLE);
	        rlMenuConfirmApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
	                if (listElementApply.size() == 1) {
		                ((MenuAcivity)activity).setIdDetail(String.valueOf(listElementApply.get(0).getId()));
		                ((MenuAcivity)activity).startDetailConfirmApplyFragment(MenuAcivity.SCROLL_TO_LEFT);
	                } else {
		                ((MenuAcivity)activity).startListConfirmApplyFragment(MenuAcivity.SCROLL_TO_LEFT);
	                }
                }
            });
        }
	}
}
