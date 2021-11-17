package com.csv.vpn.fragments;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.DialogInterface;
import com.sshpluspro.vpn.R;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.csv.vpn.SocksHttpApp;
import com.slipkprojects.ultrasshservice.logger.SkStatus;
import com.csv.vpn.SocksHttpMainActivity;
import com.slipkprojects.ultrasshservice.config.Settings;
import com.csv.vpn.preference.SettingsSSHPreference;

public class ClearConfigDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog dialog = new AlertDialog.Builder(getActivity()).
			create();
		dialog.setTitle(getActivity().getString(R.string.attention));
		dialog.setMessage(getActivity().getString(R.string.alert_clear_settings));

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getActivity().getString(R.
			string.yes),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Settings.clearSettings(getContext());
					Context context = SocksHttpApp.getApp();
					// limpa logs
					SkStatus.clearLog();
					
					SocksHttpMainActivity.updateMainViews(getContext());

					//reinicia o app
					PackageManager packageManager = context.getPackageManager();
					Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
					ComponentName componentName = intent.getComponent();
					Intent mainIntent = Intent.makeRestartActivityTask(componentName);
					context.startActivity(mainIntent);
					Runtime.getRuntime().exit(0);
				}
			}
		);

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getActivity().getString(R.
			string.no),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			}
		);
		
		return dialog;
	}

}
