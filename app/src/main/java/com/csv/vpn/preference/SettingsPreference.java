package com.csv.vpn.preference;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import androidx.preference.*;
import androidx.preference.ListPreference;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.SharedPreferences;

import android.content.Intent;
import com.csv.vpn.SocksHttpApp;
import com.csv.vpn.R;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import com.slipkprojects.ultrasshservice.logger.SkStatus;
import com.slipkprojects.ultrasshservice.config.SettingsConstants;
import com.slipkprojects.ultrasshservice.config.Settings;
import com.slipkprojects.ultrasshservice.logger.ConnectionStatus;
import android.os.Handler;
import android.app.Activity;
import com.csv.vpn.LauncherActivity;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import com.csv.vpn.SocksHttpMainActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SettingsPreference extends PreferenceFragmentCompat
	implements Preference.OnPreferenceChangeListener, SettingsConstants,
		SkStatus.StateListener
{
	private Handler mHandler;
	private SharedPreferences mPref;

	public static final String
		SSHSERVER_PREFERENCE_KEY = "screenSSHSettings",
		ADVANCED_SCREEN_PREFERENCE_KEY = "screenAdvancedSettings";

	private String[] settings_disabled_keys = {
		DNSFORWARD_KEY,
		DNSRESOLVER_KEY,
		UDPFORWARD_KEY,
		UDPRESOLVER_KEY,
		PINGER_KEY,
		AUTO_CLEAR_LOGS_KEY,
		HIDE_LOG_KEY,
		MODO_NOTURNO_KEY,
		IDIOMA_KEY
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		SkStatus.addStateListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		
		SkStatus.removeStateListener(this);
	}
	
	
	@Override
    public void onCreatePreferences(Bundle bundle, String root_key)
	{
        // Load the Preferences from the XML file
        setPreferencesFromResource(R.xml.app_preferences, root_key);

		mPref = getPreferenceManager().getDefaultSharedPreferences(getContext());
		
		Preference udpForwardPreference = (CheckBoxPreference) findPreference(UDPFORWARD_KEY);
		udpForwardPreference.setOnPreferenceChangeListener(this);
		
		Preference dnsForwardPreference = (CheckBoxPreference) findPreference(DNSFORWARD_KEY);
		dnsForwardPreference.setOnPreferenceChangeListener(this);

		ListPreference modoNoturno = (ListPreference)
			findPreference(MODO_NOTURNO_KEY);
		modoNoturno.setOnPreferenceChangeListener(this);
		SettingsAdvancedPreference.setListPreferenceSummary(modoNoturno, modoNoturno.getValue());
		
		ListPreference idioma = (ListPreference)
			findPreference(IDIOMA_KEY);
		idioma.setOnPreferenceChangeListener(this);
		SettingsAdvancedPreference.setListPreferenceSummary(idioma, idioma.getValue());
		
		// update view
		setRunningTunnel(SkStatus.isTunnelActive());
	}
	
	private void onChangeUseVpn(boolean use_vpn){
		Preference udpResolverPreference = (EditTextPreference)
			findPreference(UDPRESOLVER_KEY);
		Preference dnsResolverPreference = (EditTextPreference)
			findPreference(DNSRESOLVER_KEY);
		
		for (String key : settings_disabled_keys){
			getPreferenceManager().findPreference(key)
				.setEnabled(use_vpn);
		}

		use_vpn = true;
		if (use_vpn) {
			boolean isUdpForward = mPref.getBoolean(UDPFORWARD_KEY, false);
			boolean isDnsForward = mPref.getBoolean(DNSFORWARD_KEY, false);
			
			udpResolverPreference.setEnabled(isUdpForward);
			dnsResolverPreference.setEnabled(isDnsForward);
		}
		else {
			String[] list = {
				UDPFORWARD_KEY,
				UDPRESOLVER_KEY,
				DNSFORWARD_KEY,
				DNSRESOLVER_KEY
			};
			for (String key : list) {
				getPreferenceManager().findPreference(key)
					.setEnabled(false);
			}
		}
	}
	
	private void setRunningTunnel(boolean isRunning) {
		if (isRunning) {
			for (String key : settings_disabled_keys){
				getPreferenceManager().findPreference(key)
					.setEnabled(false);
			}
		}
		else {
			onChangeUseVpn(true);
		}
	}

	
	/**
	* Preference.OnPreferenceChangeListener
	* Implementação
	*/
	
	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue)
	{
		switch (pref.getKey()) {
			case UDPFORWARD_KEY:
				boolean isUdpForward = (boolean) newValue;

				Preference udpResolverPreference = (EditTextPreference)
					findPreference(UDPRESOLVER_KEY);
				udpResolverPreference.setEnabled(isUdpForward);
			break;
			
			case DNSFORWARD_KEY:
				boolean isDnsForward = (boolean) newValue;

				Preference dnsResolverPreference = (EditTextPreference)
					findPreference(DNSRESOLVER_KEY);
				dnsResolverPreference.setEnabled(isDnsForward);
			break;
			
			case MODO_NOTURNO_KEY:
				final String enableModoNoturno = (String)newValue;
				
				if (enableModoNoturno.equals(mPref.getString(MODO_NOTURNO_KEY, "off"))) {
					return false;
				}

				/*SettingsAdvancedPreference.setListPreferenceSummary(pref_list, (String) newValue);
				
				new AlertDialog.Builder(getContext())
					. setTitle(R.string.attention)
					. setMessage(R.string.restarting_app_theme)
					. setCancelable(false)
					. show();*/
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Context context = SocksHttpApp.getApp();
						
						new Settings(context)
							.setModoNoturno(enableModoNoturno);

						// reinicia app
						PackageManager packageManager = context.getPackageManager();
						Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
						ComponentName componentName = intent.getComponent();
						Intent mainIntent = Intent.makeRestartActivityTask(componentName);
						context.startActivity(mainIntent);
						Runtime.getRuntime().exit(0);

						// encerra tudo
						/*if (Build.VERSION.SDK_INT >= 16) {
							Activity act = getActivity();
							if (act != null)
								act.finishAffinity();
						}*/
						
						System.exit(0);
					}
				}, 300);
				
				getActivity().finish();
			return false;
			
			case IDIOMA_KEY:
				final String lang = (String) newValue;
				
				if (((String)newValue).equals(new Settings(getContext())
						.getIdioma())) {
					return false;
				}
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Context context = SocksHttpApp.getApp();
						
						LocaleHelper.setNewLocale(context, lang);

						// reinicia app
						PackageManager packageManager = context.getPackageManager();
						Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
						ComponentName componentName = intent.getComponent();
						Intent mainIntent = Intent.makeRestartActivityTask(componentName);
						context.startActivity(mainIntent);
						Runtime.getRuntime().exit(0);
						// encerra tudo


					}
				}, 300);
				
                getActivity().finish();
            return false;
		}
		return true;
	}

	@Override
	public void updateState(String state, String logMessage, int localizedResId, ConnectionStatus level, Intent intent)
	{
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				setRunningTunnel(SkStatus.isTunnelActive());
			}
		});
	}
	
	
}
