package jp.co.soliton.keymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import jp.co.soliton.keymanager.R;
import jp.co.soliton.keymanager.dbalias.ElementApply;

import java.util.List;

import static jp.co.soliton.keymanager.manager.APIDManager.PREFIX_APID_WIFI;

/**
 * Created by lexuanvinh on 02/27/2017.
 */

public class AdapterListConfirmApply extends ArrayAdapter<ElementApply> {
    // Param in AdapterListConfirmApply
    private List<ElementApply> listElementApply;

    /**
     * This Item View
     */
    public class ViewHolder {
        public TextView titleHost;
        public TextView tvHostValue;
        public TextView titleID;
        public TextView tvIdValue;
        public TextView tvStatus;
        public TextView titleStorage;
        public TextView contentStorage;
    }

    /**
     * This method contructor AdapterCertificates
     *
     * @param context
     * @param listElementApply
     */
    public AdapterListConfirmApply(Context context, List<ElementApply> listElementApply) {
        super(context, 0);
        this.listElementApply = listElementApply;
    }

	public void setListElementApply(List<ElementApply> listElementApply) {
		this.listElementApply = listElementApply;
	}

	/**
     * This method get size listElementApply
     *
     * @return
     */
    @Override
    public int getCount() {
        if (listElementApply != null) {
            return listElementApply.size();
        } else {
            return 0;
        }
    }

    public ElementApply getItem(int position) {
        return listElementApply.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * This method View item at position
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_confirm_apply, parent, false);
            viewHolder.titleHost = convertView.findViewById(R.id.titleDateInfo);
            viewHolder.tvHostValue = convertView.findViewById(R.id.titleUserId);
            viewHolder.titleID = convertView.findViewById(R.id.titleID);
            viewHolder.tvIdValue = convertView.findViewById(R.id.tvIdValue);
            viewHolder.tvStatus = convertView.findViewById(R.id.tvStatus);
            viewHolder.titleStorage = convertView.findViewById(R.id.titleStorage);
            viewHolder.contentStorage = convertView.findViewById(R.id.contentStorage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Lookup view for data ElementApply
        if (listElementApply.get(position).getHost() != null) {
            viewHolder.tvHostValue.setText(listElementApply.get(position).getHost());
        }
	    if (listElementApply.get(position).getTarget() != null) {
		    viewHolder.titleStorage.setVisibility(View.VISIBLE);
		    viewHolder.contentStorage.setVisibility(View.VISIBLE);
		    if (listElementApply.get(position).getTarget().startsWith(PREFIX_APID_WIFI)) {
			    viewHolder.contentStorage.setText(getContext().getString(R.string.main_apid_wifi));
		    } else {
			    viewHolder.contentStorage.setText(getContext().getString(R.string.main_apid_vpn));
		    }
	    } else {
		    viewHolder.titleStorage.setVisibility(View.GONE);
		    viewHolder.contentStorage.setVisibility(View.GONE);
	    }
        if (listElementApply.get(position).getUserId() != null) {
            viewHolder.tvIdValue.setText(listElementApply.get(position).getUserId());
        }
        if (listElementApply.get(position).getStatus() == ElementApply.STATUS_APPLY_CANCEL) {
            viewHolder.tvStatus.setText(getContext().getText(R.string.stt_cancel));
        } else if (listElementApply.get(position).getStatus() == ElementApply.STATUS_APPLY_PENDING) {
            String stt = getContext().getText(R.string.stt_waiting_approval).toString();
            viewHolder.tvStatus.setText(stt.replace(" ", "\n"));
        } else if (listElementApply.get(position).getStatus() == ElementApply.STATUS_APPLY_REJECT) {
	        viewHolder.tvStatus.setText(getContext().getText(R.string.stt_rejected));
        } else if (listElementApply.get(position).getStatus() == ElementApply.STATUS_APPLY_FAILURE) {
	        viewHolder.tvStatus.setText(getContext().getText(R.string.failure));
        }
        return convertView;

    }
}
