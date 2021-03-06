package jp.co.soliton.keymanager.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import jp.co.soliton.keymanager.HttpConnectionCtrl;
import jp.co.soliton.keymanager.InformCtrl;
import jp.co.soliton.keymanager.LogCtrl;
import jp.co.soliton.keymanager.R;

import static jp.co.soliton.keymanager.common.ErrorNetwork.*;

/**
 * Created by nguyenducdat on 4/28/2017.
 */

public class ConnectApplyTask extends AsyncTask<Void, Void, Boolean> {

	public interface EndConnection{
		void endConnect(Boolean result, InformCtrl m_InformCtrl, int m_nErroType);
	}

	private Activity activity;
	private InformCtrl m_InformCtrl;
	private int m_nErroType;
	private EndConnection endConnection;
	public ConnectApplyTask(Activity activity, InformCtrl m_InformCtrl, int m_nErroType, EndConnection endConnection) {
		this.activity = activity;
		this.m_InformCtrl = m_InformCtrl;
		this.m_nErroType = m_nErroType;
		this.endConnection = endConnection;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		LogCtrl.getInstance().info("Apply Connection: Start");

		HttpConnectionCtrl conn = new HttpConnectionCtrl(activity);
		//execute send request to server
		boolean ret = conn.RunHttpProbeHostCerConnection(m_InformCtrl);
		//check result return from server
		if (ret == false) {
			LogCtrl.getInstance().error("Apply Connection: Connection error");
			m_nErroType = ERR_NETWORK;
			return false;
		}

		String retStr = m_InformCtrl.GetRtn();

		// ログイン結果
		if (retStr.startsWith(activity.getText(R.string.Forbidden).toString())) {
			LogCtrl.getInstance().error("Apply Connection: Receive " + retStr);
			m_nErroType = ERR_FORBIDDEN;
			return false;
		} else if (retStr.startsWith(activity.getText(R.string.Unauthorized).toString())) {
			LogCtrl.getInstance().error("Apply Connection: Receive " + retStr);
			m_nErroType = ERR_UNAUTHORIZED;
			return false;
		} else if (retStr.startsWith(activity.getText(R.string.ERR).toString())) {
			LogCtrl.getInstance().error("Apply Connection: Receive " + retStr);
			m_nErroType = ERR_COLON;
			return false;
		}
		if (retStr.startsWith(activity.getText(R.string.not_installed_ca).toString())) {
			LogCtrl.getInstance().error("Apply Connection: CA Certificate is not installed");
			m_nErroType = NOT_INSTALL_CA;
		} else {
			LogCtrl.getInstance().info("Apply Connection: Successful");
			m_nErroType = SUCCESSFUL;
		}
		return ret;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		this.endConnection.endConnect(result, m_InformCtrl, m_nErroType);
	}
}