package jp.co.soliton.keymanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import jp.co.soliton.keymanager.*;
import jp.co.soliton.keymanager.alarm.AlarmReceiver;
import jp.co.soliton.keymanager.asynctask.StartUsingProceduresControl;
import jp.co.soliton.keymanager.customview.DialogConfirmTablet;
import jp.co.soliton.keymanager.dbalias.ElementApply;
import jp.co.soliton.keymanager.dbalias.ElementApplyManager;
import jp.co.soliton.keymanager.fragment.*;

import java.util.ArrayList;
import java.util.List;

import static jp.co.soliton.keymanager.common.ControlPagesInput.REQUEST_CODE_INSTALL_CERTIFICATION;

/**
 * Created by luongdolong on 2/3/2017.
 *
 * Activity for menu apply screen
 */

public class MenuAcivity extends FragmentActivity {
	public static final int SCROLL_TO_RIGHT = 0;
	public static final int SCROLL_TO_LEFT = 1;
	public static final int NOT_SCROLL = 2;

	public static final int RESET_STATUS = 0;
	public static final int APID_STATUS = 1;
	public static final int START_APPLY_STATUS = 2;
	public static final int COMPLETE_STATUS = 3;
	public static final int LIST_CONFIRM_APPLY_STATUS = 4;
	public static final int DETAIL_CONFIRM_APPLY_STATUS = 5;
	public static final int APPLY_SUCCESS_STATUS = 6;
	public static final int CONFIRM_APPLY_STATUS = 7;
	public static final int WITHDRAW_APPLY_STATUS = 8;
	public static final int REAPPLY_STATUS = 9;

    private int PERMISSIONS_REQUEST_READ_PHONE_STATE = 10;
	private boolean isTablet;
	private boolean isFocusMenuTablet;
	ElementApplyManager elementMgr;
	String idDetail;

	public int currentStatus;
	FragmentManager fragmentManager;
	Fragment fragmentLeft, fragmentContent;
	private List<ElementApply> listElementApply = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    isTablet = getResources().getBoolean(R.bool.isTablet);
	    elementMgr = new ElementApplyManager(this);
	    setContentView(R.layout.activity_menu);
	    setOrientation();
	    fragmentManager = getSupportFragmentManager();
	    if (savedInstanceState == null) {
		    createView();
	    } else if (isTablet) {
		    fragmentContent = getSupportFragmentManager().getFragment(savedInstanceState, "fragmentContent");
		    fragmentLeft = getSupportFragmentManager().getFragment(savedInstanceState, "fragmentLeft");
		    currentStatus = savedInstanceState.getInt("currentStatus");
	    }
	    checkGoToConfirmIfNeed();
    }

	public ElementApplyManager getElementMgr() {
		return elementMgr;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (isTablet) {
			savedInstanceState.putInt("currentStatus", currentStatus);
			getSupportFragmentManager().putFragment(savedInstanceState, "fragmentContent", fragmentContent);
			getSupportFragmentManager().putFragment(savedInstanceState, "fragmentLeft", fragmentLeft);
		}
	}

	private void createView() {
		if (isTablet) {
			isFocusMenuTablet = true;
			gotoMenuTablet();
		}
	}

	private void setOrientation() {
		if (!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
	}

	@Override
	public void onBackPressed() {
		if (!isFocusMenuTablet && isTablet) {
			if (currentStatus == COMPLETE_STATUS) {
				InputApplyInfo.deletePref(this);
				gotoMenuTablet();
			}else if (currentStatus == START_APPLY_STATUS) {
				((TabletBaseInputFragment)fragmentContent).clickBackButton();
			}else if (currentStatus == REAPPLY_STATUS) {
				((TabletBaseInputFragment)fragmentContent).clickBackButton();
			}else if (currentStatus == DETAIL_CONFIRM_APPLY_STATUS) {
				onBackPressedFromDetailCetificate();
			}else if (currentStatus == CONFIRM_APPLY_STATUS || currentStatus == WITHDRAW_APPLY_STATUS) {
				startDetailConfirmApplyFragment(SCROLL_TO_RIGHT);
			} else {
				gotoMenuTablet();
			}
		} else {
			super.onBackPressed();
		}
	}

	public void onBackPressedFromDetailCetificate() {
		updateListElementApply();
		if (listElementApply.size() > 1) {
			startListConfirmApplyFragment(SCROLL_TO_RIGHT);
		} else {
			gotoMenuTablet();
		}
	}

	public void setFocusMenuTablet(boolean focusMenuTablet) {
		isFocusMenuTablet = focusMenuTablet;
	}

	@Override
    protected void onResume() {
        super.onResume();
		checkGoToConfirmIfNeed();
		updateListElementApply();
		if(android.os.Build.VERSION.SDK_INT >= 23) {
            NewPermissionSet();
        }
    }

    private void updateListElementApply() {
	    listElementApply = elementMgr.getAllElementApply();
    }

	public List<ElementApply> getListElementApply() {
		updateListElementApply();
		return listElementApply;
	}

	private void checkGoToConfirmIfNeed() {
		if (StringList.GO_TO_LIST_APPLY.equals("1")) {
		    StringList.GO_TO_LIST_APPLY = "0";
		    gotoConfirmActivity();
		}
	}

	public void gotoConfirmActivity() {
		updateListElementApply();
	    if (listElementApply.size() == 1) {
		    if (!isTablet) {
			    Intent intent = new Intent(MenuAcivity.this, DetailConfirmActivity.class);
			    intent.putExtra("ELEMENT_APPLY_ID", String.valueOf(listElementApply.get(0).getId()));
			    startActivity(intent);
		    } else {
			    startDetailConfirmApplyFragment(SCROLL_TO_LEFT);
		    }
	    } else {
		    if (!isTablet) {
			    Intent intent = new Intent(MenuAcivity.this, ListConfirmActivity.class);
			    startActivity(intent);
		    } else {
			    startListConfirmApplyFragment(SCROLL_TO_LEFT);
		    }
	    }
    }

    private void NewPermissionSet() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

	public void gotoMenuTablet() {
		isFocusMenuTablet = true;
		currentStatus = RESET_STATUS;
		fragmentContent = ContentMenuTabletFragment.newInstance();
		changeFragmentContent(SCROLL_TO_RIGHT);

		fragmentLeft = new LeftSideMenuTabletFragment();
		changeFragmentLeftTablet();
	}

	public void startActivityAPID() {
		isFocusMenuTablet = false;
		currentStatus = APID_STATUS;
		fragmentLeft = new LeftSideAPIDTabletFragment();
		changeFragmentLeftTablet();

		fragmentContent= new ContentAPIDTabletFragment();
		changeFragmentContent(SCROLL_TO_LEFT);
	}

	public void startApplyActivityFragment() {
		InputApplyInfo.deletePref(this);
		isFocusMenuTablet = false;
		currentStatus = START_APPLY_STATUS;
		fragmentLeft = new LeftSideInputTabletFragment();
		changeFragmentLeftTablet();
		fragmentContent= TabletBaseInputFragment.newInstance(null, true);
		changeFragmentContent(SCROLL_TO_LEFT);
	}

	public void updateLeftSideInput(int possition) {
		if (fragmentLeft == null) {
			return;
		}
		((LeftSideInputTabletFragment)fragmentLeft).highlightItem(possition);
	}

	public void goApplyCompleted(){
		fragmentContent = TabletInputSuccessFragment.newInstance();
		gotoApplyCompleteFragment();
	}

	public void goApplyCompleted(InformCtrl m_InformCtrl, ElementApply element){
		fragmentContent = TabletInputSuccessFragment.newInstance(m_InformCtrl, element);
		gotoApplyCompleteFragment();
	}

	private void gotoApplyCompleteFragment(){
		currentStatus = APPLY_SUCCESS_STATUS;
		hideFragmentLeft();
		changeFragmentContent(SCROLL_TO_LEFT);
	}

	//====================================================================================================================

	public void startListConfirmApplyFragment(int typeScroll) {
		isFocusMenuTablet = false;
		currentStatus = LIST_CONFIRM_APPLY_STATUS;
		resetIdDetail();
		fragmentLeft = new LeftSideListConfirmTabletFragment();
		changeFragmentLeftTablet();

		fragmentContent= ContentListConfirmTabletFragment.newInstance(listElementApply);
		changeFragmentContent(typeScroll);
	}

	public String getIdDetail() {
		return idDetail;
	}

	public void resetIdDetail() {
		setIdDetail("");
	}

	public void setIdDetail(String idDetail) {
		this.idDetail = idDetail;
	}

	public void startDetailConfirmApplyFragment(int typeScroll) {
		isFocusMenuTablet = false;
		currentStatus = DETAIL_CONFIRM_APPLY_STATUS;
		fragmentLeft = new LeftSideDetailConfirmTabletFragment();
		changeFragmentLeftTablet();

		fragmentContent = ContentDetailConfirmFragment.newInstance();
		changeFragmentContent(typeScroll);

	}

	public void updateDesLeftSideDetailConfirm(String newDes) {
		if (fragmentLeft == null || currentStatus != DETAIL_CONFIRM_APPLY_STATUS) {
			return;
		}
		((LeftSideDetailConfirmTabletFragment)fragmentLeft).setTextDes(newDes);
	}

	public void clickConfirmApply() {
		currentStatus = WITHDRAW_APPLY_STATUS;
		fragmentLeft = new LeftSideInputPasswordTabletFragment();
		changeFragmentLeftTablet();
		fragmentContent= ContentInputPasswordTabletFragment.newInstance(false);
		changeFragmentContent(SCROLL_TO_LEFT);
	}

	public void clickWithdrawApply() {
		currentStatus = WITHDRAW_APPLY_STATUS;
		fragmentLeft = new LeftSideInputPasswordTabletFragment();
		changeFragmentLeftTablet();

		fragmentContent= ContentInputPasswordTabletFragment.newInstance(true);
		changeFragmentContent(SCROLL_TO_LEFT);
	}

	public void clickReApply() {
		InputApplyInfo.deletePref(this);
		isFocusMenuTablet = false;
		currentStatus = REAPPLY_STATUS;
		fragmentLeft = new LeftSideInputTabletFragment();
		changeFragmentLeftTablet();
		fragmentContent= TabletBaseInputFragment.newInstance(idDetail, false);
		changeFragmentContent(SCROLL_TO_LEFT);
	}

	public void clickDeleteApplyTablet() {
	    final DialogConfirmTablet dialog = new DialogConfirmTablet(this);
	    dialog.setTextDisplay(getString(R.string.dialog_delete_title), getString(R.string.dialog_delete_msg)
			    , getString(R.string.label_dialog_Cancle), getString(R.string.label_dialog_delete_cert));
	    dialog.setOnClickOK(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    dialog.dismiss();
			    elementMgr.deleteElementApply(idDetail);
			    gotoMenuTablet();
		    }
	    });
	    dialog.show();
	}

	public void gotoCompleteConfirmApplyFragment(int status, ElementApply element, InformCtrl m_InformCtrl) {
		hideFragmentLeft();
		fragmentContent= ContentCompleteConfirmApplyFragment.newInstance(status, element, m_InformCtrl, idDetail);
		if (status == ElementApply.STATUS_APPLY_PENDING || status == ElementApply.STATUS_APPLY_REJECT  || status
				== ElementApply.STATUS_APPLY_CANCEL ) {
			changeFragmentContent(NOT_SCROLL);
		} else if (status == ElementApply.STATUS_APPLY_APPROVED){
			changeFragmentContent(SCROLL_TO_LEFT);
		} else {
			changeFragmentContent(SCROLL_TO_RIGHT);
		}
	}

	public void startUsingProceduresFragment(InformCtrl m_InformCtrl, ElementApply element) {
		hideFragmentLeft();
		fragmentContent = ContentStartUsingProceduresFragment.newInstance();
		changeFragmentContent(SCROLL_TO_LEFT);
		StartUsingProceduresControl startUsingProceduresControl = StartUsingProceduresControl.newInstance(this,
				m_InformCtrl, element);
		startUsingProceduresControl.startDeviceCertTask();
	}

	private void hideFragmentLeft() {
		if (fragmentLeft == null) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.hide(fragmentLeft);
		fragmentTransaction.commit();
	}

	private void changeFragmentLeftTablet(){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_leftside_tablet, fragmentLeft);
		fragmentTransaction.commit();
	}

	private void changeFragmentContent(int typeScroll) {
		switch (typeScroll) {
			case SCROLL_TO_RIGHT:
				changeFragmentContentTabletGoToRight();
				break;
			case SCROLL_TO_LEFT:
				changeFragmentContentTabletGoToLeft();
				break;
			case NOT_SCROLL:
				changeFragmentContentTabletNotScroll();
				break;
			default:
				changeFragmentContentTabletNotScroll();
				break;
		}
	}

	private void changeFragmentContentTabletGoToLeft(){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
		fragmentTransaction.replace(R.id.fragment_content_tablet, fragmentContent);
		fragmentTransaction.commit();
	}
	private void changeFragmentContentTabletNotScroll(){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_content_tablet, fragmentContent);
		fragmentTransaction.commit();
	}

	private void changeFragmentContentTabletGoToRight(){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit, R.anim.enter, R.anim.exit);
		fragmentTransaction.replace(R.id.fragment_content_tablet, fragmentContent);
		fragmentTransaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogCtrl.getInstance(this).loggerInfo("MenuAcivity:onActivityResult start REC CODE = " + Integer
				.toString(requestCode));
		if (requestCode == REQUEST_CODE_INSTALL_CERTIFICATION) {
			((TabletBaseInputFragment)fragmentContent).finishInstallCertificate(resultCode);
		}

		if (requestCode == StartUsingProceduresControl.m_nEnrollRtnCode) {
			// After CertificateEnrollTask
			Log.i("CertLoginActivity","REC CODE = " + Integer.toString(resultCode));
			if (resultCode != 0) {
				ElementApplyManager mgr = new ElementApplyManager(getApplicationContext());
				mgr.updateElementCertificate(StartUsingProceduresControl.getInstance(this).getElement());
				AlarmReceiver alarm = new AlarmReceiver();
				alarm.setupNotification(getApplicationContext());
				//alarm.setOnetimeTimer(getApplicationContext(), String.valueOf(element.getId()));
				Intent intent = new Intent(getApplicationContext(), CompleteUsingProceduresActivity.class);
				intent.putExtra("ELEMENT_APPLY", StartUsingProceduresControl.getInstance(this).getElement());
				finish();
				startActivity(intent);
				overridePendingTransition(0, 0);
			} else {
				StringList.GO_TO_LIST_APPLY = "1";
				Intent intent = new Intent(getApplicationContext(), MenuAcivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		} else if (requestCode == StartUsingProceduresControl.m_nMDM_RequestCode) {
			if (resultCode == RESULT_OK) {
				StartUsingProceduresControl.getInstance(this).resultWithRequestCodeMDM();
			} else {
				finish();
			}
		} else if (requestCode == ViewPagerInputActivity.REQUEST_CODE_INSTALL_CERTIFICATION) {
			if (resultCode == Activity.RESULT_OK) {
				StartUsingProceduresControl.getInstance(this).startCertificateEnrollTask();
			}
		}
	}
}
