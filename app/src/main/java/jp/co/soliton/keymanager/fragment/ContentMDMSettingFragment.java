package jp.co.soliton.keymanager.fragment;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import jp.co.soliton.keymanager.EpsapAdminReceiver;
import jp.co.soliton.keymanager.LogCtrl;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.adapter.AdapterMDM;
import jp.co.soliton.keymanager.customview.DialogApplyConfirm;
import jp.co.soliton.keymanager.mdm.MDMControl;
import jp.co.soliton.keymanager.mdm.MDMFlgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyenducdat on 4/25/2017.
 */

public class ContentMDMSettingFragment extends TabletBaseSettingFragment {

	private Button btnDeleteMDM;
	private ListView listView;
	private AdapterMDM adapterMDM;
	private List<AdapterMDM.ItemMDM> listItemMDM;
	private RelativeLayout viewProgressBar;
	private MDMFlgs mdm;

	public static Fragment newInstance(MDMFlgs mdm) {
		ContentMDMSettingFragment f = new ContentMDMSettingFragment();
		f.mdm = mdm;
		return f;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		viewFragment = inflater.inflate(R.layout.fragment_mdm, container, false);
		textViewBack = viewFragment.findViewById(R.id.textViewBack);
		tvTitleHeader = viewFragment.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setText(getString(R.string.profile));
		btnDeleteMDM = viewFragment.findViewById(R.id.btnDeleteMDM);
		listView = viewFragment.findViewById(R.id.listMdmItem);
		viewProgressBar = viewFragment.findViewById(R.id.pb);
		listItemMDM = new ArrayList<>();
		adapterMDM = new AdapterMDM(getActivity(), listItemMDM);
		listView.setAdapter(adapterMDM);
		return viewFragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		prepareData();
		setupControl();
	}

	public void setupControl() {
		textViewBack.setText(getString(R.string.label_setting));
		tvTitleHeader.setText(getString(R.string.profile));
		btnDeleteMDM.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final DialogApplyConfirm dialog = new DialogApplyConfirm(getActivity());
				dialog.setTextDisplay(null, getString(R.string.content_delete_mdm_profile_dialog)
						, getString(R.string.label_dialog_cancel), getString(R.string.label_dialog_delete_cert));
				dialog.setOnClickOK(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						deleteMDM();
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	private void deleteMDM() {
		//Stop service
		MDMControl mdmctrl = new MDMControl(getActivity(), mdm.GetUDID());
		//Delete AdminDevice
		DevicePolicyManager m_DPM = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName m_DeviceAdmin = new ComponentName(getActivity(), EpsapAdminReceiver.class);
		if (m_DPM.isAdminActive(m_DeviceAdmin)) {
			m_DPM.removeActiveAdmin(m_DeviceAdmin);
		}
		//CheckOutMDM
		viewProgressBar.setVisibility(View.VISIBLE);
		MDMControl.CheckOutMdmTask checkOutMdmTask = new MDMControl.CheckOutMdmTask(getActivity(), new MDMControl.CheckOutListener() {
			@Override
			public void checkOutComplete() {
				LogCtrl.getInstance().info("Setting: MDM profile has deleted");
				viewProgressBar.setVisibility(View.GONE);
				getActivity().onBackPressed();
			}
		});
		checkOutMdmTask.execute();
	}

	private void prepareData() {
		boolean isEnableLock = ((mdm.GetAccessRight() & mdm.getM_n_devicelock()) == mdm.getM_n_devicelock());
		boolean isEnableWipe = ((mdm.GetAccessRight() & mdm.getM_n_devaiceerace()) == mdm.getM_n_devaiceerace());
		boolean isEnableDeviceInfo = ((mdm.GetAccessRight() & mdm.getM_n_deviceinf()) == mdm.getM_n_deviceinf());
		boolean isEnableNetworkInfo = ((mdm.GetAccessRight() & mdm.getM_n_network()) == mdm.getM_n_network());
		boolean isEnableAppInfo = ((mdm.GetAccessRight() & mdm.getM_n_inst_appinf()) == mdm.getM_n_inst_appinf());
		AdapterMDM.ItemMDM itemLock = new AdapterMDM.ItemMDM(getString(R.string.device_lock), isEnableLock);
		AdapterMDM.ItemMDM itemWipe = new AdapterMDM.ItemMDM(getString(R.string.remote_wipe), isEnableWipe);
		AdapterMDM.ItemMDM itemDeviceInfo = new AdapterMDM.ItemMDM(getString(R.string.device_info), isEnableDeviceInfo);
		AdapterMDM.ItemMDM itemNetworkInfo = new AdapterMDM.ItemMDM(getString(R.string.network_info), isEnableNetworkInfo);
		AdapterMDM.ItemMDM itemAppInfo = new AdapterMDM.ItemMDM(getString(R.string.app_info), isEnableAppInfo);
		listItemMDM.clear();
		listItemMDM.add(itemLock);
		listItemMDM.add(itemWipe);
		listItemMDM.add(itemDeviceInfo);
		listItemMDM.add(itemNetworkInfo);
		listItemMDM.add(itemAppInfo);
		adapterMDM.setListItemMDM(listItemMDM);
		adapterMDM.notifyDataSetChanged();
	}
}
