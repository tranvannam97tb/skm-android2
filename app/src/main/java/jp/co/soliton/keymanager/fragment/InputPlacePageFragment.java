package jp.co.soliton.keymanager.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.activity.ViewPagerInputActivity;

import static jp.co.soliton.keymanager.manager.APIDManager.TARGET_VPN;
import static jp.co.soliton.keymanager.manager.APIDManager.TARGET_WiFi;

/**
 * Created by luongdolong on 2/3/2017.
 *
 * Page choose certificate type VPN or Wifi
 */

public class InputPlacePageFragment extends InputBasePageFragment {

    private Button btnTargetVPN;
    private Button btnTargetWiFi;
    private LinearLayout zoneInputPlace;

    public static Fragment newInstance(Context context) {
        InputPlacePageFragment f = new InputPlacePageFragment();
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewPagerInputActivity) {
            this.pagerInputActivity = (ViewPagerInputActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_input_place, null);
        zoneInputPlace = root.findViewById(R.id.zoneInputPlace);
        btnTargetVPN = root.findViewById(R.id.btnTargetVPN);
        btnTargetWiFi = root.findViewById(R.id.btnTargetWifi);
        if (pagerInputActivity.sdk_int_version < Build.VERSION_CODES.JELLY_BEAN_MR2){
            pagerInputActivity.getInputApplyInfo().setPlace(TARGET_VPN);
            pagerInputActivity.getInputApplyInfo().savePref(pagerInputActivity);
            zoneInputPlace.setVisibility(View.GONE);
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Execute action
        btnTargetVPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerInputActivity.getInputApplyInfo().setPlace(TARGET_VPN);
                pagerInputActivity.gotoPage(3);
            }
        });
        btnTargetWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerInputActivity.getInputApplyInfo().setPlace(TARGET_WiFi);
                pagerInputActivity.gotoPage(3);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
