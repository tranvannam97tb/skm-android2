package jp.co.soliton.keymanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import jp.co.soliton.keymanager.StringList;
import jp.co.soliton.keymanager.activity.MenuAcivity;
import jp.co.soliton.keymanager.adapter.AdapterListConfirmTabletApply;
import jp.co.soliton.keymanager.dbalias.ElementApply;
import jp.co.soliton.keymanager.dbalias.ElementApplyManager;

import java.util.ArrayList;
import java.util.List;

import static jp.co.soliton.keymanager.common.TypeScrollFragment.SCROLL_TO_LEFT;

/**
 * Created by nguyenducdat on 4/25/2017.
 */

public class ContentListConfirmTabletFragment extends ListFragment {

	Activity activity;
	ElementApplyManager elementMgr;
	private List<ElementApply> listElementApply = new ArrayList<>();
	AdapterListConfirmTabletApply adapterListConfirmTabletApply;

	public static Fragment newInstance(List<ElementApply> listElementApply) {
		ContentListConfirmTabletFragment f = new ContentListConfirmTabletFragment();
		f.listElementApply = listElementApply;
		return f;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.activity = (Activity) context;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		elementMgr = ElementApplyManager.getInstance(activity);
		setListShown(true);
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
		listElementApply = ((MenuAcivity)getActivity()).getListElementApply();
		adapterListConfirmTabletApply = new AdapterListConfirmTabletApply(getActivity(), listElementApply);
		getListView().setAdapter(adapterListConfirmTabletApply);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			StringList.ID_DETAIL_CURRENT = String.valueOf(listElementApply.get(position).getId());
			((MenuAcivity)getActivity()).startDetailConfirmApplyFragment(SCROLL_TO_LEFT);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		listElementApply = ((MenuAcivity)getActivity()).getListElementApply();
		adapterListConfirmTabletApply.setListElementApply(listElementApply);
		adapterListConfirmTabletApply.notifyDataSetChanged();
	}
}
