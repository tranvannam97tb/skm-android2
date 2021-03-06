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

public class AdapterListConfirmTabletApply extends ArrayAdapter<ElementApply> {
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
        public TextView tvUpdateDate;
	    public TextView titleStorage;
	    public TextView contentStorage;
    }

    /**
     * This method contructor AdapterCertificates
     *
     * @param context
     * @param listElementApply
     */
    public AdapterListConfirmTabletApply(Context context, List<ElementApply> listElementApply) {
        super(context, 0);
	    setListElementApply(listElementApply);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_confirm_apply_tablet, parent, false);
            viewHolder.titleHost = convertView.findViewById(R.id.titleDateInfo);
            viewHolder.tvHostValue = convertView.findViewById(R.id.titleUserId);
            viewHolder.titleID = convertView.findViewById(R.id.titleID);
            viewHolder.tvIdValue = convertView.findViewById(R.id.tvIdValue);
            viewHolder.tvStatus = convertView.findViewById(R.id.tvStatus);
            viewHolder.tvUpdateDate = convertView.findViewById(R.id.tvUpdateDate);
	        viewHolder.titleStorage = convertView.findViewById(R.id.titleStorage);
	        viewHolder.contentStorage = convertView.findViewById(R.id.contentStorage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Lookup view for data ElementApply
	    ElementApply elementApply = listElementApply.get(position);
        if (elementApply.getHost() != null) {
            viewHolder.tvHostValue.setText(elementApply.getHost());
        }
        if (elementApply.getUserId() != null) {
            viewHolder.tvIdValue.setText(elementApply.getUserId());
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
        if (elementApply.getStatus() == ElementApply.STATUS_APPLY_CANCEL) {
            viewHolder.tvStatus.setText(getContext().getText(R.string.stt_cancel));
        } else if (elementApply.getStatus() == ElementApply.STATUS_APPLY_PENDING) {
            String stt = getContext().getText(R.string.stt_waiting_approval).toString();
            viewHolder.tvStatus.setText(stt);
        } else if (elementApply.getStatus() == ElementApply.STATUS_APPLY_REJECT) {
            viewHolder.tvStatus.setText(getContext().getText(R.string.stt_rejected));
        } else if (elementApply.getStatus() == ElementApply.STATUS_APPLY_FAILURE) {
            viewHolder.tvStatus.setText(getContext().getText(R.string.failure));
        }
	    String updateDate = elementApply.getUpdateDate().split(" ")[0];
	    updateDate = getContext().getString(R.string.title_apply_date) + " " + updateDate;
	    viewHolder.tvUpdateDate.setText(updateDate.replace("-", "/"));
        return convertView;
    }
}
