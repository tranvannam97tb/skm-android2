package jp.co.soliton.keymanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.StringList;
import jp.co.soliton.keymanager.alarm.AlarmReceiver;
import jp.co.soliton.keymanager.customview.DialogApplyConfirm;
import jp.co.soliton.keymanager.dbalias.ElementApply;
import jp.co.soliton.keymanager.dbalias.ElementApplyManager;

/**
 * Created by luongdolong on 4/3/2017.
 */

public class SettingDetailCertificateActivity extends Activity {
    private ElementApplyManager elementMgr;
    private TextView tvTitleHeader;
    private TextView tvDetailSubjectCountry;
    private TextView tvDetailSubjectSpName;
    private TextView tvDetailSubjectLocal;
    private TextView tvDetailSubjectOrgName;
    private TextView tvDetailSubjectCommonName;
    private TextView tvDetailSubjectEmail;

    private TextView tvDetailIssuerCountry;
    private TextView tvDetailIssuerSpName;
    private TextView tvDetailIssuerLocal;
    private TextView tvDetailIssuerOrgName;
    private TextView tvDetailIssuerOrgUnitName;
    private TextView tvDetailIssuerCommonName;
    private TextView tvDetailIssuerEmail;
    private TextView tvDetailIssuerVersion;
    private TextView tvDetailIssuerSerialNum;

    private TextView tvDetailSignAlgorithm;
    private TextView tvDetailNotValidBefore;
    private TextView tvDetailNotValidAfter;

    private TextView tvDetailPublicAlgorithm;
    private TextView tvDetailPublicData;
    private TextView tvDetailSignature;

    private TextView tvDetailCertificateAuthority;
    private TextView tvDetailUsage;
    private TextView tvDetailKeyIdentifier;
    private TextView tvDetailAuthorKeyIdentifier;
    private TextView tvDetailCLRUri;
    private TextView tvDetailInformationAccessUri;
    private TextView tvDetailPurpose;
    private TextView textViewBack;
    private LinearLayout moreOption;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail_certificate);
        id = getIntent().getStringExtra(StringList.ELEMENT_APPLY_ID);
        elementMgr = new ElementApplyManager(this);
        tvTitleHeader = (TextView) findViewById(R.id.tvTitleHeader);
	    moreOption = (LinearLayout) findViewById(R.id.more_option);
	    textViewBack = (TextView) findViewById(R.id.textViewBack);
        tvDetailSubjectCountry = (TextView) findViewById(R.id.tvDetailSubjectCountry);
        tvDetailSubjectSpName = (TextView) findViewById(R.id.tvDetailSubjectSpName);
        tvDetailSubjectLocal = (TextView) findViewById(R.id.tvDetailSubjectLocal);
        tvDetailSubjectOrgName = (TextView) findViewById(R.id.tvDetailSubjectOrgName);
        tvDetailSubjectCommonName = (TextView) findViewById(R.id.tvDetailSubjectCommonName);
        tvDetailSubjectEmail = (TextView) findViewById(R.id.tvDetailSubjectEmail);

        tvDetailIssuerCountry = (TextView) findViewById(R.id.tvDetailIssuerCountry);
        tvDetailIssuerSpName = (TextView) findViewById(R.id.tvDetailIssuerSpName);
        tvDetailIssuerLocal = (TextView) findViewById(R.id.tvDetailIssuerLocal);
        tvDetailIssuerOrgName = (TextView) findViewById(R.id.tvDetailIssuerOrgName);
        tvDetailIssuerOrgUnitName = (TextView) findViewById(R.id.tvDetailIssuerOrgUnitName);
        tvDetailIssuerCommonName = (TextView) findViewById(R.id.tvDetailIssuerCommonName);
        tvDetailIssuerEmail = (TextView) findViewById(R.id.tvDetailIssuerEmail);
        tvDetailIssuerVersion = (TextView) findViewById(R.id.tvDetailIssuerVersion);
        tvDetailIssuerSerialNum = (TextView) findViewById(R.id.tvDetailIssuerSerialNum);

        tvDetailSignAlgorithm = (TextView) findViewById(R.id.tvDetailSignAlgorithm);
        tvDetailNotValidBefore = (TextView) findViewById(R.id.tvDetailNotValidBefore);
        tvDetailNotValidAfter = (TextView) findViewById(R.id.tvDetailNotValidAfter);

        tvDetailPublicAlgorithm = (TextView) findViewById(R.id.tvDetailPublicAlgorithm);
        tvDetailPublicData = (TextView) findViewById(R.id.tvDetailPublicData);
        tvDetailSignature = (TextView) findViewById(R.id.tvDetailSignature);

        tvDetailCertificateAuthority = (TextView) findViewById(R.id.tvDetailCertificateAuthority);
        tvDetailUsage = (TextView) findViewById(R.id.tvDetailUsage);
        tvDetailKeyIdentifier = (TextView) findViewById(R.id.tvDetailKeyIdentifier);
        tvDetailAuthorKeyIdentifier = (TextView) findViewById(R.id.tvDetailAuthorKeyIdentifier);
        tvDetailCLRUri = (TextView) findViewById(R.id.tvDetailCLRUri);
        tvDetailInformationAccessUri = (TextView) findViewById(R.id.tvDetailInformationAccessUri);
        tvDetailPurpose = (TextView) findViewById(R.id.tvDetailPurpose);
    }

    public void btnBackClick(View v) {
        finish();
    }

    public void onMenuSettingClick(View v) {
        final CharSequence[] items = {getResources().getString(R.string.label_dialog_delete_cert),
                                    getResources().getString(R.string.notif_setting),
                                    getResources().getString(R.string.label_dialog_Cancle)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.select_apid));

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    dialog.dismiss();
                    confirmDeleteCert();
                } else if (item == 1) {
                    dialog.dismiss();
                    Intent intent = new Intent(SettingDetailCertificateActivity.this, NotificationSettingActivity.class);
                    intent.putExtra(NotificationSettingActivity.KEY_NOTIF_MODE, NotificationSettingActivity.NotifModeEnum.ONE);
                    intent.putExtra(StringList.ELEMENT_APPLY_ID, id);
                    startActivity(intent);
                } else if (item == 3) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        /*
        final DialogMenuCertDetail dialog = new DialogMenuCertDetail(this);
        dialog.setOnDeleteCert(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmDeleteCert();
            }
        });
        dialog.setOnNotificationSetting(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(SettingDetailCertificateActivity.this, NotificationSettingActivity.class);
                intent.putExtra(NotificationSettingActivity.KEY_NOTIF_MODE, NotificationSettingActivity.NotifModeEnum.ONE);
                intent.putExtra(StringList.ELEMENT_APPLY_ID, id);
                startActivity(intent);
            }
        });
        dialog.show();*/
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        showDataDetail();
    }

    private void showDataDetail() {
        ElementApply elementApply = elementMgr.getElementApply(id);
	    setTextForControl(tvTitleHeader, elementApply.getcNValue());
	    updateTitle();
	    setTextForControl(tvDetailSubjectCountry, elementApply.getSubjectCountryName());
        setTextForControl(tvDetailSubjectSpName, elementApply.getSubjectStateOrProvinceName());
        setTextForControl(tvDetailSubjectLocal, elementApply.getSubjectLocalityName());
        setTextForControl(tvDetailSubjectOrgName, elementApply.getSubjectOrganizationName());
        setTextForControl(tvDetailSubjectCommonName, elementApply.getSubjectCommonName());
        setTextForControl(tvDetailSubjectEmail, elementApply.getSubjectEmailAddress());

        setTextForControl(tvDetailIssuerCountry, elementApply.getIssuerCountryName());
        setTextForControl(tvDetailIssuerSpName, elementApply.getIssuerStateOrProvinceName());
        setTextForControl(tvDetailIssuerLocal, elementApply.getIssuerLocalityName());
        setTextForControl(tvDetailIssuerOrgName, elementApply.getIssuerOrganizationName());
        setTextForControl(tvDetailIssuerOrgUnitName, elementApply.getIssuerOrganizationUnitName());
        setTextForControl(tvDetailIssuerCommonName, elementApply.getIssuerCommonName());
        setTextForControl(tvDetailIssuerEmail, elementApply.getIssuerEmailAdress());
        setTextForControl(tvDetailIssuerVersion, elementApply.getVersion());
        setTextForControl(tvDetailIssuerSerialNum, elementApply.getSerialNumber());

        setTextForControl(tvDetailSignAlgorithm, elementApply.getSignatureAlogrithm());
        setTextForControl(tvDetailNotValidBefore, elementApply.getNotValidBefore());
        setTextForControl(tvDetailNotValidAfter, elementApply.getNotValidAfter());

        setTextForControl(tvDetailPublicAlgorithm, elementApply.getPublicKeyAlogrithm());
	    String detailPublicData = elementApply.getPublicKeyData().toUpperCase().replace(":", " ");
        setTextForControl(tvDetailPublicData, detailPublicData);
	    String detailSignature = elementApply.getPublicSignature().toUpperCase().replace(":", " ");
	    setTextForControl(tvDetailSignature, detailSignature);
        if (Integer.parseInt(elementApply.getCertificateAuthority()) >= 0) {
            tvDetailCertificateAuthority.setText("TRUE");
        } else {
            tvDetailCertificateAuthority.setText("FALSE");
        }
        setTextForControl(tvDetailUsage, elementApply.getUsage());
	    String detailKeyIdentifier = elementApply.getSubjectKeyIdentifier().toUpperCase().replace(":", " ");
        setTextForControl(tvDetailKeyIdentifier, detailKeyIdentifier);
	    String detailAuthorKeyIdentifier = elementApply.getAuthorityKeyIdentifier().toUpperCase().replace(":", " ");
	    setTextForControl(tvDetailAuthorKeyIdentifier, detailAuthorKeyIdentifier);
        setTextForControl(tvDetailCLRUri, elementApply.getClrDistributionPointUri());
        setTextForControl(tvDetailInformationAccessUri, elementApply.getCertificateAuthorityUri());
        setTextForControl(tvDetailPurpose, elementApply.getPurpose());
    }

	private void updateTitle() {
		tvTitleHeader.measure(0, 0);
		textViewBack.measure(0, 0);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;

		if (tvTitleHeader.getMeasuredWidth() > width - (textViewBack.getMeasuredWidth() * 2)) {
			textViewBack.setText("");
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			params.addRule(RelativeLayout.RIGHT_OF, textViewBack.getId());
			params.addRule(RelativeLayout.LEFT_OF, moreOption.getId());
			tvTitleHeader.setLayoutParams(params);
		}
	}

	private void setTextForControl(TextView tv, String text) {
        if (nullOrEmpty(text)) {
            tv.setText("");
        } else {
            tv.setText(text);
        }
    }

    private void confirmDeleteCert() {
        final DialogApplyConfirm dialog = new DialogApplyConfirm(this);
        dialog.setTextDisplay(getString(R.string.dialog_delete_title), getString(R.string.dialog_delete_msg)
                , getString(R.string.label_dialog_Cancle), getString(R.string.label_dialog_delete_cert));
        dialog.setOnClickOK(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                elementMgr.deleteElementApply(id);
                AlarmReceiver alarm = new AlarmReceiver();
                alarm.setupNotification(getApplicationContext());
                finish();
            }
        });
        dialog.show();
    }

    private boolean nullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.trim().isEmpty();
    }
}
