package jp.co.soliton.keymanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.mdm.MDMFlgs;

/**
 * Created by luongdolong on 3/31/2017.
 */

public class SettingActivity  extends BaseSettingPhoneActivity {
    private RelativeLayout menuSettingCertList;
    private RelativeLayout menuSettingNotif;
    private RelativeLayout menuSettingProduct;
    private RelativeLayout menuSettingLibrary;
    private RelativeLayout menuSettingMDM;
    private LinearLayout layoutMDM;
	private MDMFlgs mdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        menuSettingCertList = findViewById(R.id.menuSettingCertList);
        menuSettingNotif = findViewById(R.id.menuSettingNotif);
        menuSettingProduct = findViewById(R.id.menuSettingProduct);
        menuSettingLibrary = findViewById(R.id.menuSettingLibrary);
	    menuSettingMDM = findViewById(R.id.menuSettingMDM);
	    layoutMDM = findViewById(R.id.ll_mdm);
	    mdm = new MDMFlgs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupControl();
	    boolean bRet = mdm.ReadAndSetScepMdmInfo(this);
	    if (!bRet) {
		    layoutMDM.setVisibility(View.GONE);
	    } else {
		    layoutMDM.setVisibility(View.VISIBLE);
	    }
    }

	@Override
	protected void setTextBtnBack() {
		textViewBack.setText(getString(R.string.back));
	}

	@Override
	protected void setTextTitle() {
		tvTitleHeader.setText(getString(R.string.label_setting));
	}

	public void setupControl() {
        menuSettingCertList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SettingListCertificateActivity.class);
                startActivity(intent);
            }
        });
        menuSettingNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, NotificationSettingActivity.class);
                intent.putExtra(NotificationSettingActivity.KEY_NOTIF_MODE, NotificationSettingActivity.NotifModeEnum.ALL);
                startActivity(intent);
            }
        });
        menuSettingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ProductInfoActivity.class);
                startActivity(intent);
            }
        });
        menuSettingLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LibraryInfoActivity.class);
                startActivity(intent);
            }
        });
	    menuSettingMDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MDMActivity.class);
	            intent.putExtra("mdm" , mdm);
                startActivity(intent);
            }
        });
    }
}
