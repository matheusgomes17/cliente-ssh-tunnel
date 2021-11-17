package com.csv.vpn;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.hone.black.bravo.BuildConfig;
import com.kervzcodes.payload.generator.ssh.PayloadGenerator;
import com.hone.black.bravo.R;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import android.view.View;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import com.skyfishjy.library.*;
import com.csv.vpn.adapter.SpinnerAdapter;
import com.csv.vpn.util.AESCrypt;
import com.csv.vpn.util.ConfigUpdate;
import com.csv.vpn.util.ConfigUtil;
import com.csv.vpn.util.Utils;
import android.util.Log;
import android.widget.TextView;
import android.support.v4.view.GravityCompat;
import android.widget.EditText;
import android.support.design.widget.TextInputEditText;
import com.csv.vpn.DrawerLog;
import android.support.v4.widget.DrawerLayout;
import android.net.Uri;
import android.widget.Button;
import com.csv.vpn.SocksHttpApp;
import android.widget.CheckBox;
import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import com.csv.vpn.activities.ConfigGeralActivity;
import android.view.LayoutInflater;
import android.content.pm.PackageManager;
import android.text.Html;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageInfo;
import com.slipkprojects.ultrasshservice.util.SkProtect;
import com.slipkprojects.ultrasshservice.logger.SkStatus;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.widget.LinearLayout;
import com.csv.vpn.fragments.ProxyRemoteDialogFragment;
import android.annotation.TargetApi;
import android.os.Build;
import android.net.VpnService;
import android.content.ActivityNotFoundException;
import android.app.Activity;
import com.slipkprojects.ultrasshservice.logger.ConnectionStatus;
import android.os.Handler;
import com.csv.vpn.fragments.*;
import android.support.v4.content.ContextCompat;
import com.csv.vpn.fragments.ClearConfigDialogFragment;
import com.csv.vpn.activities.ConfigExportFileActivity;
import com.csv.vpn.activities.ConfigImportFileActivity;
import com.slipkprojects.ultrasshservice.config.Settings;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.PersistableBundle;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.RadioGroup;
import com.slipkprojects.ultrasshservice.config.ConfigParser;
import android.support.v4.app.ActivityCompat;
import android.content.DialogInterface;
import com.slipkprojects.ultrasshservice.tunnel.TunnelManagerHelper;
import com.slipkprojects.ultrasshservice.LaunchVpn;
import com.csv.vpn.activities.AboutActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.csv.vpn.model.ViewFragment;
import android.text.InputType;
import android.widget.ImageButton;
import android.widget.AdapterView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import com.csv.vpn.util.GoogleFeedbackUtils;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.csv.vpn.activities.BaseActivity;
import com.slipkprojects.ultrasshservice.tunnel.TunnelUtils;
import android.text.TextUtils;
import com.csv.vpn.preference.LocaleHelper;
import android.support.annotation.Nullable;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import com.csv.vpn.adapter.*;
import android.support.v7.widget.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import java.util.*;
import android.view.*;

//USUARIOS ONLINE

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


//########///


import cn.pedant.SweetAlert.SweetAlertDialog;
import java.security.GeneralSecurityException;
import com.slipkprojects.ultrasshservice.config.SettingsConstants;
import android.net.TrafficStats;


/**
 * Activity Principal
 * @author SlipkHunter
 */

public class SocksHttpMainActivity extends BaseActivity
	implements DrawerLayout.DrawerListener,
			View.OnClickListener, RadioGroup.OnCheckedChangeListener,
				CompoundButton.OnCheckedChangeListener, SkStatus.StateListener
{
	private static final String TAG = SocksHttpMainActivity.class.getSimpleName();
	private static final String UPDATE_VIEWS = "MainUpdate";
	public static final String OPEN_LOGS = "com.csv.vpn:openLogs";
	
	private DrawerLog mDrawer;
	private DrawerPanelMain mDrawerPanel;
	
	private Settings mConfig;
	private Toolbar toolbar_main;
	private Handler mHandler;
	
	private LinearLayout mainLayout;
	private LinearLayout loginLayout;
	private LinearLayout proxyInputLayout;
	private TextView proxyText;
	private RadioGroup metodoConexaoRadio;
	private LinearLayout payloadLayout;
	private TextInputEditText payloadEdit;
	private SwitchCompat customPayloadSwitch;
	private Button starterButton;
	
	private ImageButton inputPwShowPass;
	private TextInputEditText inputPwUser;
	private TextInputEditText inputPwPass;
	
	private LinearLayout configMsgLayout;
	private TextView configMsgText;

	private AdView adsBannerView;
	private LinearLayout archivalSNILayout;
	private TextView sniTextkarl;
	private ConfigUtil config;

	private Spinner serverSpinner;
	private Spinner payloadSpinner;
	private Spinner proxyx;
	private SpinnerAdapter serverAdapter;
	private SpinnerAdapter payloadAdapter;
	private TextView status;
	private ArrayList<JSONObject> serverList;
	private ArrayList<JSONObject> payloadList;
	private Button starterButton1;
	private static final String[] tabTitle = {"INICIO","REGISTRO"};
	private LogsAdapter mLogAdapter;
	private RecyclerView logList;
	private TabLayout tabs;
	private ViewPager vp;
	private FloatingActionButton deleteLogs;
	
	private TextView textstatus;
	//salvar posição spinner e atualizar quando trocar de server
	boolean spinnerTouched = false;
	
	private String[] torrentList = new String[] {"com.xunlei.downloadprovider",
		"torrentvillalite.romreviewer.com",
		"com.termux.app.TermuxService",
		"com.termux",
		"torrentvilla.romreviwer.com",
		"com.epic.app.iTorrent",
		"com.delphicoder.flud",
		"hu.bute.daai.amorg.drtorrent",
		"com.mobilityflow.torrent.prof",
		"com.brute.torrentolite",
		"com.nebula.swift",
		"tv.bitx.media",
		"com.DroiDownloader",
		"bitking.torrent.downloader",
		"org.transdroid.lite",
		"com.mobilityflow.tvp",
		"com.gabordemko.torrnado",
		"com.frostwire.android",
		"com.vuze.android.remote",
		"com.akingi.torrent",
		"com.utorrent.web",
		"com.paolod.torrentsearch2",
		"com.delphicoder.flud.paid",
		"com.teeonsoft.ztorrent",
		"megabyte.tdm",
		"com.bittorrent.client.pro",
		"com.mobilityflow.torrent",
		"com.utorrent.client",
		"com.utorrent.client.pro",
		"com.bittorrent.client",
		"torrent",
		"com.AndroidA.DroiDownloader",
		"com.indris.yifytorrents",
		"com.delphicoder.flud",
		"com.oidapps.bittorrent",
		"dwleee.torrentsearch",
		"com.vuze.torrent.downloader",
		"megabyte.dm",
		"com.fgrouptech.kickasstorrents",
		"com.jrummyapps.rootbrowser.classic",
		"com.bittorrent.client",
		"hu.tagsoft.ttorrent.lite",
		"com.termux.app.TermuxOpenReceiver",
		"co.we.torrent",
		"com.xunlei.downloadprovider",
		"torrentvillalite.romreviewer.com",
		"torrentvilla.romreviwer.com", 
		"com.epic.app.iTorrent", 
		"com.delphicoder.flud", "hu.bute.daai.amorg.drtorrent", 
		"com.mobilityflow.torrent.prof", 
		"com.brute.torrentolite", 
		"com.nebula.swift", 
		"tv.bitx.media", 
		"com.DroiDownloader", 
		"bitking.torrent.downloader", 
		"org.transdroid.lite", 
		"com.mobilityflow.tvp", 
		"com.gabordemko.torrnado", 
		"com.frostwire.android", 
		"com.vuze.android.remote", 
		"com.akingi.torrent", 
		"com.utorrent.web", "com.paolod.torrentsearch2", "com.delphicoder.flud.paid", "com.teeonsoft.ztorrent", "megabyte.tdm", "com.bittorrent.client.pro", "com.mobilityflow.torrent", "com.utorrent.client", "com.utorrent.client.pro", "com.bittorrent.client", "torrent", "com.AndroidA.DroiDownloader", "com.indris.yifytorrents", "com.delphicoder.flud", "com.oidapps.bittorrent", "dwleee.torrentsearch", "com.vuze.torrent.downloader", "megabyte.dm", "com.fgrouptech.kickasstorrents", 
		"com.jrummyapps.rootbrowser.classic",};
	public static final boolean isRunning = false;
	
	
	
	
		
		
	public class TorrentDetection
	{
		private Context context;

		private String[] items;

		public TorrentDetection(Context c, String[] i) {
			context = c;
			items = i;
		}

		private boolean check(String uri)
		{
			PackageManager pm = context.getPackageManager();
			boolean app_installed = false;
			try
			{
				pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
				app_installed = true;
			}
			catch (PackageManager.NameNotFoundException e)
			{
				app_installed = false;
			}
			return app_installed;
		}

		void check() {
			for (int i=0;i < items.length ;i++)
			{
				if(check(items[i])){
					alert(items[i]);
					break;
				}
			}
		}

		public void init() {
			final Handler handler = new Handler();
			Timer timer = new Timer();
			TimerTask doAsynchronousTask = new TimerTask() {
				@Override
				public void run()
				{
					handler.post(new Runnable() {
							public void run()
							{
								check();
							}
						});
				}
			};
			timer.schedule(doAsynchronousTask, 0, 180000);
		}

		void alert(final String app) {
			if (SocksHttpMainActivity.isRunning)
			{
				context.stopService(new Intent(context, SocksHttpMainActivity.class));
			}
			new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("Desinstale")
				.setContentText("Torrent detectado!!\r" +app)
				.setConfirmText("Desinstalar Aplicativo")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {

						try {
							Intent intent = new Intent("android.intent.action.UNINSTALL_PACKAGE", Uri.parse("package:" + app));
							intent.putExtra("android.intent.extra.UNINSTALL_ALL_USERS", true);
							SocksHttpMainActivity.this.startActivity(intent);
							System.exit(0);
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(0);
						}
					}
				})
				.show();
		}
		}

					
		
		
	private final Runnable mRunnable = new Runnable() {
        public void run() {
            TextView textView = (TextView) SocksHttpMainActivity.this.findViewById(R.id.RX);
            TextView textView2 = (TextView) SocksHttpMainActivity.this.findViewById(R.id.TX);
            long totalRxBytes = TrafficStats.getTotalRxBytes();
            long totalRxBytes2 = TrafficStats.getTotalRxBytes() - SocksHttpMainActivity.this.mStartRX;
            String str = " kb";
            textView.setText(Long.toString(totalRxBytes2) + str);
            String gb = " GB";
            String mb = " MB";
            String kb = " KB";
            if (totalRxBytes2 >= 1024) {
                totalRxBytes2 /= 1024;
                textView.setText(Long.toString(totalRxBytes2) + kb);
                if (totalRxBytes2 >= 1024) {
                    totalRxBytes2 /= 1024;
                    textView.setText(Long.toString(totalRxBytes2) + mb);
                    if (totalRxBytes2 >= 1024) {
                        textView.setText(Long.toString(totalRxBytes2 / 1024) + gb);
                    }
                }
            }
            SocksHttpMainActivity.this.mStartRX = totalRxBytes;
            totalRxBytes = TrafficStats.getTotalTxBytes();
            totalRxBytes2 = TrafficStats.getTotalTxBytes() - SocksHttpMainActivity.this.mStartTX;
            textView2.setText(Long.toString(totalRxBytes2) + str);
            if (totalRxBytes2 >= 1024) {
                totalRxBytes2 /= 1024;
                textView2.setText(Long.toString(totalRxBytes2) + kb);
                if (totalRxBytes2 >= 1024) {
                    totalRxBytes2 /= 1024;
                    textView2.setText(Long.toString(totalRxBytes2) + mb);
                    if (totalRxBytes2 >= 1024) {
                        textView2.setText(Long.toString(totalRxBytes2 / 1024) + gb);
                    }
                }
            }
            SocksHttpMainActivity.this.mStartTX = totalRxBytes;
            SocksHttpMainActivity.this.mHandler2.postDelayed(SocksHttpMainActivity.this.mRunnable, 1000);
        }
    };
	private long mStartRX = 0;
    private long mStartTX = 0;
	private Handler mHandler2 = new Handler();

	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		mHandler = new Handler();
		mConfig = new Settings(this);
		mDrawer = new DrawerLog(this);
		mDrawerPanel = new DrawerPanelMain(this);
		
		
		this.mStartRX = TrafficStats.getTotalRxBytes();
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        this.mStartTX = totalTxBytes;
        if (this.mStartRX == -1 || totalTxBytes == -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Uh Oh!");
            builder.setMessage((CharSequence) "Seu dispositivo não suporta monitoramento de estatísticas de tráfego.");
            builder.show();
        } else {
            this.mHandler2.postDelayed(this.mRunnable, 1000);
        }
		{}
		
		
		SharedPreferences prefs = getSharedPreferences(SocksHttpApp.PREFS_GERAL, Context.MODE_PRIVATE);

		boolean showFirstTime = prefs.getBoolean("connect_first_time", true);
		int lastVersion = prefs.getInt("last_version", 0);

		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				
				loadServerData();
				// Atualiza os Online
				loadServer1();
			}
			
		},1000);
		
		
	
		// se primeira vez
		if (showFirstTime)
        {
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean("connect_first_time", false);
            pEdit.apply();

			Settings.setDefaultConfig(this);

			showBoasVindas();
        }

		try {
			int idAtual = ConfigParser.getBuildId(this);

			if (lastVersion < idAtual) {
				SharedPreferences.Editor pEdit = prefs.edit();
				pEdit.putInt("last_version", idAtual);
				pEdit.apply();

				// se estiver atualizando
				if (!showFirstTime) {
					if (lastVersion <= 12) {
						Settings.setDefaultConfig(this);
						Settings.clearSettings(this);

						Toast.makeText(this, "As configurações foram limpas para evitar bugs",
							Toast.LENGTH_LONG).show();
					}
				}

			}
	
		} catch(IOException e) {}
		new TorrentDetection(this, this.torrentList).init();
		
		// set layout
		doLayout();

		// verifica se existe algum problema
		//SkProtect.CharlieProtect();

		// recebe local dados
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_VIEWS);
		filter.addAction(OPEN_LOGS);
		
		LocalBroadcastManager.getInstance(this)
			.registerReceiver(mActivityReceiver, filter);
			
		doUpdateLayout();
		doTabs();
	}


	/**
	 * Layout
	 */
	 
	private void doLayout() {
		setContentView(R.layout.activity_main_drawer);

		toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
		mDrawerPanel.setDrawer(toolbar_main);
		setSupportActionBar(toolbar_main);
		status= (TextView) findViewById(R.id.edson);
		
		mDrawer.setDrawer(this);
		
		
		// set ADS
		adsBannerView = (AdView) findViewById(R.id.adBannerMainView);
		
		if (!BuildConfig.DEBUG) {
			//adsBannerView.setAdUnitId(SocksHttpApp.ADS_UNITID_BANNER_MAIN);
		}
		
		if (TunnelUtils.isNetworkOnline(SocksHttpMainActivity.this)) {
			adsBannerView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (adsBannerView != null) {
						adsBannerView.setVisibility(View.VISIBLE);
					}
				}
			});
			adsBannerView.loadAd(new AdRequest.Builder()
				.build());
		}
		
		archivalSNILayout = (LinearLayout) findViewById(R.id.activity_mainInputSNILayout);
		sniTextkarl = (TextView) findViewById(R.id.activity_mainSNIText);
		
		
		mainLayout = (LinearLayout) findViewById(R.id.activity_mainLinearLayout);
		loginLayout = (LinearLayout) findViewById(R.id.activity_mainInputPasswordLayout);
		starterButton = (Button) findViewById(R.id.activity_starterButtonMain);

		inputPwUser = (TextInputEditText) findViewById(R.id.activity_mainInputPasswordUserEdit);
		inputPwPass = (TextInputEditText) findViewById(R.id.activity_mainInputPasswordPassEdit);

		inputPwShowPass = (ImageButton) findViewById(R.id.show_password);

		((TextView) findViewById(R.id.activity_mainAutorText))
			.setOnClickListener(this);

		proxyInputLayout = (LinearLayout) findViewById(R.id.activity_mainInputProxyLayout);
		proxyText = (TextView) findViewById(R.id.activity_mainProxyText);

		config = new ConfigUtil(this);

		serverSpinner = (Spinner) findViewById(R.id.serverSpinner);
		payloadSpinner = (Spinner) findViewById(R.id.payloadSpinner);

		serverList = new ArrayList<>();
		payloadList = new ArrayList<>();

		serverAdapter = new SpinnerAdapter(this, R.id.serverSpinner, serverList);
		payloadAdapter = new SpinnerAdapter(this, R.id.payloadSpinner, payloadList);

		serverSpinner.setAdapter(serverAdapter);
		payloadSpinner.setAdapter(payloadAdapter);

		loadServer();
		loadNetworks();
		updateConfig(true);
		SharedPreferences sPrefs = mConfig.getPrefsPrivate();
		sPrefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
		sPrefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();
        

		/*Spinner spinnerTunnelType = (Spinner) findViewById(R.id.activity_mainTunnelTypeSpinner);
		String[] items = new String[]{"SSH DIRECT", "SSH + PROXY", "SSH + SSL (beta)"};
		//create an adapter to describe how the items are displayed, adapters are used in several places in android.
		//There are multiple variations of this, but this is the basic variant.
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
		//set the spinners adapter to the previously created one.
		spinnerTunnelType.setAdapter(adapter);*/
		
		metodoConexaoRadio = (RadioGroup) findViewById(R.id.activity_mainMetodoConexaoRadio);
		customPayloadSwitch = (SwitchCompat) findViewById(R.id.activity_mainCustomPayloadSwitch);
		starterButton1 = (Button) findViewById(R.id.activity_starterButtonMain1);
		
		starterButton.setOnClickListener(this);
		proxyInputLayout.setOnClickListener(this);

		payloadLayout = (LinearLayout) findViewById(R.id.activity_mainInputPayloadLinearLayout);
		payloadEdit = (TextInputEditText) findViewById(R.id.activity_mainInputPayloadEditText);

		configMsgLayout = (LinearLayout) findViewById(R.id.activity_mainMensagemConfigLinearLayout);
		configMsgText = (TextView) findViewById(R.id.activity_mainMensagemConfigTextView);

		// fix bugs
		if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				inputPwUser.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
				inputPwPass.setText(mConfig.getPrivString(Settings.SENHA_KEY));
			}
		}
		else {
			payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
		}
		
		
		final SharedPreferences prefs = mConfig.getPrefsPrivate();
        proxyx = (Spinner) findViewById(R.id.proxyxx);
		

		List<String> Listaproxyx = new ArrayList<String>();
		Listaproxyx.add("8080");
		Listaproxyx.add("80");
		//Listaproxyx.add("3128");
		Listaproxyx.add("443");
		//Listaproxyx.add("8081");
		ArrayAdapter<String> Adptadorproxyx = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Listaproxyx);
		Adptadorproxyx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		proxyx.setAdapter(Adptadorproxyx);
		proxyx.setSelection(prefs.getInt("proxyx", 0));
		proxyx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int position, long p4)
				{
					try
					{

						prefs.edit().putInt("proxyx", position).apply();

						prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
						
						if(position == 0) {
                     
							prefs.edit().putString(Settings.PROXY_PORTA_KEY, "8080").apply();

						}else if(position == 1){

							prefs.edit().putString(Settings.PROXY_PORTA_KEY, "80").apply();

					}else if(position == 2){

							prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, "443").apply();
							
						}/*else if(position == 3){

							prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, "444").apply();

						}else if(position == 4){

							prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, "345").apply(); 
							
						}*/

						doUpdateLayout();
					}
					catch (Exception e)
					{}
				}

			});
		
			
        
		metodoConexaoRadio.setOnCheckedChangeListener(this);
		customPayloadSwitch.setOnCheckedChangeListener(this);
		inputPwShowPass.setOnClickListener(this);

		// tela de login ##########################################

		final SharedPreferences prefsTxt = mConfig.getPrefsPrivate();
		inputPwUser.setText(prefsTxt.getString(Settings.USUARIO_KEY, ""));
		inputPwPass.setText(prefsTxt.getString(Settings.SENHA_KEY, ""));
		inputPwUser.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				if(!s.toString().isEmpty()) {
					prefsTxt.edit().putString(Settings.USUARIO_KEY, s.toString()).apply();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		inputPwPass.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				if(!s.toString().isEmpty()) {
					prefsTxt.edit().putString(Settings.SENHA_KEY, s.toString()).apply();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		// ########################################################
	}
	
	
	public void doTabs() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		deleteLogs = (FloatingActionButton)findViewById(R.id.delete_log);
		mLogAdapter = new LogsAdapter(layoutManager,this);
		logList = (RecyclerView) findViewById(R.id.recyclerLog);
		logList.setAdapter(mLogAdapter);
		logList.setLayoutManager(layoutManager);
		mLogAdapter.scrollToLastPosition();
		vp = (ViewPager)findViewById(R.id.viewpager);
		tabs = (TabLayout)findViewById(R.id.tablayout);
		vp.setAdapter(new MyAdapter(Arrays.asList(tabTitle)));
		vp.setOffscreenPageLimit(2);
		tabs.setTabMode(TabLayout.MODE_FIXED);
		tabs.setTabGravity(TabLayout.GRAVITY_FILL);
		tabs.setupWithViewPager(vp);
		deleteLogs.setOnClickListener(new View.OnClickListener() {

		 @Override
		 public void onClick(View p1)
		 {
		 mLogAdapter.clearLog();
		 //SkStatus.logInfo("<font style='color:red;'>Log Cleared!</font>");
		 // TODO: Implement this method
		 }


		 });

		vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
			{
				@Override
				public void onPageSelected(int i)
				{
					if (i == 1) {
						toolbar_main.getMenu().clear();
						getMenuInflater().inflate(R.menu.logs_menu, toolbar_main.getMenu());
					} else {
						toolbar_main.getMenu().clear();
						getMenuInflater().inflate(R.menu.main_menu, toolbar_main.getMenu());
					}
				}
			});

	} 



	public class MyAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			// TODO: Implement this method
			return 2;
		}

		@Override
		public boolean isViewFromObject(View p1, Object p2)
		{
			// TODO: Implement this method
			return p1 == p2;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			int[] ids = new int[]{R.id.tab1, R.id.tab2};
			int id = 0;
			id = ids[position];
			// TODO: Implement this method
			return findViewById(id);
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			// TODO: Implement this method
			return titles.get(position);
		}

		private List<String> titles;
		public MyAdapter(List<String> str)
		{
			titles = str;
		}
	}
	
	
	
	private void doUpdateLayout() {
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		boolean isRunning = SkStatus.isTunnelActive();
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		
		setStarterButton(starterButton, this);
		setPayloadSwitch(tunnelType, !prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true));

		String proxyStr = getText(R.string.no_value).toString();

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			proxyStr = "*******";
			proxyInputLayout.setEnabled(false);
		}
		else {
			String proxy = mConfig.getPrivString(Settings.PROXY_IP_KEY);

			if (proxy != null && !proxy.isEmpty())
				proxyStr = String.format("%s:%s", proxy, mConfig.getPrivString(Settings.PROXY_PORTA_KEY));
			proxyInputLayout.setEnabled(!isRunning);
		} 

		proxyText.setText(proxyStr);
        String sniStr = "Ex. m.facebook.com";

        if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			sniStr = "*******";
			archivalSNILayout.setEnabled(false);
        }
		else {
			
		}

		//end

		switch (tunnelType) {
			case Settings.bTUNNEL_TYPE_SSH_DIRECT:
				((AppCompatRadioButton) findViewById(R.id.activity_mainSSHDirectRadioButton))
					.setChecked(true);
				break;

			case Settings.bTUNNEL_TYPE_SSH_PROXY:
				((AppCompatRadioButton) findViewById(R.id.activity_mainSSHProxyRadioButton))
					.setChecked(true);
				break;
				
	
		}
		int loginVisibility = View.VISIBLE;
		
		boolean enabled_radio = !isRunning;

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			
			if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				loginVisibility = View.VISIBLE;
				
				inputPwUser.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
				inputPwPass.setText(mConfig.getPrivString(Settings.SENHA_KEY));
				
				inputPwUser.setEnabled(!isRunning);
				inputPwPass.setEnabled(!isRunning);
				inputPwShowPass.setEnabled(!isRunning);
				
				//inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			
			
			
			if (mConfig.getPrivString(Settings.PROXY_IP_KEY).isEmpty() ||
					mConfig.getPrivString(Settings.PROXY_PORTA_KEY).isEmpty()) {
				enabled_radio = false;
			}
		}

		loginLayout.setVisibility(loginVisibility);
		
		
		//SALVA A POSIÇÃO DO SPINNER E ATUALIZA OS ONLINE##################################


//dentro de doLayout() 
		textstatus = (TextView) findViewById(R.id.textonline);



		serverSpinner.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						spinnerTouched = true; // User DID touched the spinner!
					}

					return false;
				}
			});


		final SharedPreferences prefsave = mConfig.getPrefsPrivate();

		serverSpinner.setSelection(prefsave.getInt("LastSelectedServer", 0));
		serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					SharedPreferences prefs = mConfig.getPrefsPrivate();
					SharedPreferences.Editor edit = prefs.edit();
					edit.putInt("LastSelectedServer", p3).apply();

					if (spinnerTouched) {
						// Do something
						new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {

									loadServerData();
									loadServer1();
									textstatus.setText("atualizando");


								}
							}, 2000);


					}



				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {

				}
			});
		payloadSpinner.setSelection(prefsave.getInt("LastSelectedPayload", 0));
		payloadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					SharedPreferences prefs = mConfig.getPrefsPrivate();
					SharedPreferences.Editor edit = prefs.edit();
					edit.putInt("LastSelectedPayload", p3).apply();
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
				}
			});


		
	
		
		// desativa/ativa radio group
		for (int i = 0; i < metodoConexaoRadio.getChildCount(); i++) {
			metodoConexaoRadio.getChildAt(i).setEnabled(enabled_radio);
		}
	}
	
	
	
	//UPDATE ONLINE ##########################################################################################

	private synchronized void doSaveData() {
		try {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();

			if (mainLayout != null && !isFinishing())
				mainLayout.requestFocus();

			else {
				if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
					edit.putString(Settings.USUARIO_KEY, inputPwUser.getEditableText().toString());
					edit.putString(Settings.SENHA_KEY, inputPwPass.getEditableText().toString());
				}
			}

			edit.apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadServerData() {
		try {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();

			int pos = payloadSpinner.getSelectedItemPosition();
			int pos1 = serverSpinner.getSelectedItemPosition();


			String ssl_port = config.getServersArray().getJSONObject(pos1).getString("SSLPort");
			String ssh_server = config.getServersArray().getJSONObject(pos1).getString("ServerIP");
			String ssh_port = config.getServersArray().getJSONObject(pos1).getString("ServerPort");

			String remote_proxy = config.getServersArray().getJSONObject(pos1).getString("ProxyIP");
			String proxy_port = config.getServersArray().getJSONObject(pos1).getString("ProxyPort");

			String payload = config.getNetworksArray().getJSONObject(pos).getString("Payload");
			String sni = config.getNetworksArray().getJSONObject(pos).getString("SNI");

			String info = config.getNetworksArray().getJSONObject(pos).getString("Info");

			String ssh_user = config.getServersArray().getJSONObject(pos1).getString("ServerUser");
			String msg = config.getServersArray().getJSONObject(pos1).getString("ServerPass");

			edit.putString(Settings.URL_KEY, ssh_user);
			edit.putString(Settings.CONFIG_MENSAGEM_KEY, msg);


			int msgVisibility = View.VISIBLE;
			String msgText = "";

			if (!msg.isEmpty()) {
				msgText = msg.replace("\n", "<br/>");
				msgVisibility = View.VISIBLE;
			}


			configMsgText.setText(msgText.isEmpty() ? "" : Html.fromHtml(msgText));
			configMsgLayout.setVisibility(msgVisibility);
             // Coloque a info.equals em Payload Info no Gerador.
			 // Exemplo: web, sslp, ssl, direct, proxy
			 
			// MÉTODO DE CONEXÃO 5 = WEBSOCKET
			if (info.equals("web"))
			{
				prefs.edit().putString(Settings.SERVIDOR_KEY,ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT).apply();
				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
			}
			 
			// MÉTODO DE CONEXÃO 4 = SSL+PAYLOAD
			if (info.equals("sslp"))
			{
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAYLOAD_SSL).apply();

				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssl_port).apply();

				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
				prefs.edit().putString(Settings.CUSTOM_SNI, sni).apply();
			}
			 
			// MÉTODO DE CONEXÃO 3 = SSL
			if (info.equals("ssl"))
			{
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true).apply();
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_SSL).apply();

				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssl_port).apply();

				prefs.edit().putString(Settings.PROXY_IP_KEY, remote_proxy).apply();
				prefs.edit().putString(Settings.PROXY_PORTA_KEY, proxy_port).apply();

				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
				prefs.edit().putString(Settings.CUSTOM_SNI, sni).apply();
			}
			 
			// MÉTODO DE CONEXÃO 1 = DIRECT
			if (info.equals("direct"))
			{
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT).apply();

				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();

				prefs.edit().putString(Settings.PROXY_IP_KEY, remote_proxy).apply();
				prefs.edit().putString(Settings.PROXY_PORTA_KEY, proxy_port).apply();
				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
			}
			
			// MÉTODO DE CONEXÃO 2 = SSH PROXY
			if (info.equals("proxy"))
			{
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();

				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();

				prefs.edit().putString(Settings.PROXY_IP_KEY, remote_proxy).apply();
				prefs.edit().putString(Settings.PROXY_PORTA_KEY, proxy_port).apply();

				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
			}

			edit.apply();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadServer() {
		try {
			if (serverList.size() > 0) {
				serverList.clear();
				serverAdapter.notifyDataSetChanged();
			}
			for (int i = 0; i < config.getServersArray().length(); i++) {
				JSONObject obj = config.getServersArray().getJSONObject(i);
				serverList.add(obj);
				serverAdapter.notifyDataSetChanged();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadNetworks() {
		try {
			if (payloadList.size() > 0) {
				payloadList.clear();
				payloadAdapter.notifyDataSetChanged();
			}
			for (int i = 0; i < config.getNetworksArray().length(); i++) {
				JSONObject obj = config.getNetworksArray().getJSONObject(i);
				payloadList.add(obj);
				payloadAdapter.notifyDataSetChanged();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateConfig(final boolean isOnCreate) {
		new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {
			@Override
			public void onUpdateListener(String result) {
				try {
					if (!result.contains("Error on getting data")) {
						String json_data = AESCrypt.decrypt(config.m, result);
						if (isNewVersion(json_data)) {
							newUpdateDialog(result);
						} else {
							if (!isOnCreate) {
								noUpdateDialog();
							}
						}
					} else if(result.contains("Error on getting data") && !isOnCreate){
						errorUpdateDialog(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start(isOnCreate);
	}

	private boolean isNewVersion(String result) {
		try {
			String current = config.getVersion();
			String update = new JSONObject(result).getString("Version");
			return config.versionCompare(update, current);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void newUpdateDialog(final String result) throws JSONException, GeneralSecurityException, GeneralSecurityException{


		String json_data = AESCrypt.decrypt(config.m, result);
		String notes = new JSONObject(json_data).getString("ReleaseNotes");
	//	nops = new
		new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
			.setTitleText("Boas Noticias!")
			.setContentText(notes)
			.setConfirmText("Atualizar agora")
			.setCancelText("Mais Tarde !")
			.showCancelButton(true)
			.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sDialog) {
					sDialog.dismissWithAnimation();
					try {
						File file = new File(getFilesDir(), "Config.json");
						OutputStream out = new FileOutputStream(file);
						out.write(result.getBytes());
						out.flush();
						out.close();
						restart_app();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			})
				
			.show();}
					

	private void noUpdateDialog() {
		new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
			.setTitleText("Esta Tudo Ok •_-")
			.setContentText("Não foi encontrada uma nova atualização.")
			.setConfirmText("OK!")
			.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sDialog) {
					sDialog.dismissWithAnimation();
				}
			})
			.show();
		
	}

	private void errorUpdateDialog(String error) {
		new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
			.setTitleText("Erro na Atualização")
			.setContentText("Use uma conexão ativa para atualizar")
			.show();
		
	}

	private void restart_app() {
		Context context = SocksHttpApp.getApp();
		// reinicia app
		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
		ComponentName componentName = intent.getComponent();
		Intent mainIntent = Intent.makeRestartActivityTask(componentName);
		context.startActivity(mainIntent);
		Runtime.getRuntime().exit(0);
	}
	/**
	 * Tunnel SSH
	 */

	public void startOrStopTunnel(Activity activity) {
		if (SkStatus.isTunnelActive()) {
			TunnelManagerHelper.stopSocksHttp(activity);
		}
		else {
			// oculta teclado se vísivel, tá com bug, tela verde
			//Utils.hideKeyboard(activity);
			
			Settings config = new Settings(activity);
			
			if (config.getPrefsPrivate()
					.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				if (inputPwUser.getText().toString().isEmpty() || 
						inputPwPass.getText().toString().isEmpty()) {
					Toast.makeText(this, R.string.error_userpass_empty, Toast.LENGTH_SHORT)
						.show();
					return;
				}
			}
			
			Intent intent = new Intent(activity, LaunchVpn.class);
			intent.setAction(Intent.ACTION_MAIN);
			
			if (config.getHideLog()) {
				intent.putExtra(LaunchVpn.EXTRA_HIDELOG, true);
			}
			
			activity.startActivity(intent);
		}
	}

	private void setPayloadSwitch(int tunnelType, boolean isCustomPayload) {
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		boolean isRunning = SkStatus.isTunnelActive();

		customPayloadSwitch.setChecked(isCustomPayload);

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			payloadEdit.setEnabled(false);
			
			if (mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY).isEmpty()) {
				customPayloadSwitch.setEnabled(false);
			}
			else {
				customPayloadSwitch.setEnabled(!isRunning);
			}
			
			if (!isCustomPayload && tunnelType == Settings.bTUNNEL_TYPE_SSH_PROXY)
				payloadEdit.setText(Settings.PAYLOAD_DEFAULT);
			else
				payloadEdit.setText("*******");
		}
		else {
			customPayloadSwitch.setEnabled(!isRunning);

			if (isCustomPayload) {
				payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
				payloadEdit.setEnabled(!isRunning);
			}
			else if (tunnelType == Settings.bTUNNEL_TYPE_SSH_PROXY) {
				payloadEdit.setText(Settings.PAYLOAD_DEFAULT);
				payloadEdit.setEnabled(false);
			}
		}

		if (isCustomPayload || tunnelType == Settings.bTUNNEL_TYPE_SSH_PROXY) {
			payloadLayout.setVisibility(View.VISIBLE);
		}
		else {
			payloadLayout.setVisibility(View.GONE);
		}
	}

	public void setStarterButton(Button starterButton, Activity activity) {
		String state = SkStatus.getLastState();
		boolean isRunning = SkStatus.isTunnelActive();

		if (starterButton != null) {
			int resId;
			
			SharedPreferences prefsPrivate = new Settings(activity).getPrefsPrivate();

			if (ConfigParser.isValidadeExpirou(prefsPrivate
					.getLong(Settings.CONFIG_VALIDADE_KEY, 0))) {
				resId = R.string.expired;
				starterButton.setEnabled(false);

				if (isRunning) {
					startOrStopTunnel(activity);
				}
			}
			else if (prefsPrivate.getBoolean(Settings.BLOQUEAR_ROOT_KEY, false) &&
					ConfigParser.isDeviceRooted(activity)) {
			   resId = R.string.blocked;
			   starterButton.setEnabled(false);
			   
			   Toast.makeText(activity, R.string.error_root_detected, Toast.LENGTH_SHORT)
					.show();

			   if (isRunning) {
				   startOrStopTunnel(activity);
			   }
			}
			else if (SkStatus.SSH_INICIANDO.equals(state)) {
				resId = R.string.stop;
				starterButton.setEnabled(false);
			}
			else if (SkStatus.SSH_PARANDO.equals(state)) {
				resId = R.string.state_stopping;
				starterButton.setEnabled(false);
			}
			else {
				resId = isRunning ? R.string.stop : R.string.start;
				starterButton.setEnabled(true);
			}

			starterButton.setText(resId);
		}
	}
	

	
	@Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (mDrawerPanel.getToogle() != null)
			mDrawerPanel.getToogle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerPanel.getToogle() != null)
			mDrawerPanel.getToogle().onConfigurationChanged(newConfig);
    }
	
	private boolean isMostrarSenha = false;
	
	@Override
	public void onClick(View p1)
	{
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		switch (p1.getId()) {
			case R.id.activity_starterButtonMain:
				doSaveData();
				loadServerData();
				startOrStopTunnel(this);
				break;

			case R.id.activity_mainInputProxyLayout:
				if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
					doSaveData();

					DialogFragment fragProxy = new ProxyRemoteDialogFragment();
					fragProxy.show(getSupportFragmentManager(), "proxyDialog");
				}
				break;

			case R.id.activity_mainAutorText:
				String url = "https://seulink.online/U6Ee8";
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getText(R.string.open_with)));
				break;
				
			case R.id.show_password:
				isMostrarSenha = !isMostrarSenha;
				if (isMostrarSenha) {
					inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					inputPwShowPass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_black_24dp));
				}
				else {
					inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					inputPwShowPass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off_black_24dp));
				}
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup p1, int p2)
	{
		SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();

		switch (p1.getCheckedRadioButtonId()) {
			case R.id.activity_mainSSHDirectRadioButton:
				edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
				proxyInputLayout.setVisibility(View.GONE);
                archivalSNILayout.setVisibility(View.GONE);
				break;

            case R.id.activity_mainSSHProxyRadioButton:
                edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY);
                payloadEdit.setHint(R.string.payload);
                archivalSNILayout.setVisibility(View.GONE);
                proxyInputLayout.setVisibility(View.VISIBLE);
                break;
			
		}

		edit.apply();

		doSaveData();
		doUpdateLayout();
	}

	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		SharedPreferences.Editor edit = prefs.edit();

		switch (p1.getId()) {
			case R.id.activity_mainCustomPayloadSwitch:
				edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, !p2);
				setPayloadSwitch(prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT), p2);
				break;
		}

		edit.apply();

		doSaveData();
	}
	
	//###############TEXTO DE ALERTA ###########//
	
	protected void showBoasVindas() {
		new SweetAlertDialog(this)
			.setTitleText("Ola! Bem-vindo a H-one Black")
			.show();
		
	}
	
	@Override
	public void updateState(final String state, String msg, int localizedResId, final ConnectionStatus level, Intent intent)
	{
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				doUpdateLayout();
				if (SkStatus.isTunnelActive()){

					if (level.equals(ConnectionStatus.LEVEL_CONNECTED)){
						status.setText(R.string.connected);
						// Atualiza os Online
						loadServer1();
						
					starterButton1.setBackground(getResources().getDrawable(R.drawable.barra_red));
						
						
						
					}

					if (level.equals(ConnectionStatus.LEVEL_NOTCONNECTED)){
						status.setText(R.string.servicestop);
					}	

					if (level.equals(ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED)){
						status.setText(R.string.authenticating);
						starterButton1.setBackground(getResources().getDrawable(R.drawable.barrauser));
						
					}		

					if (level.equals(ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET)){
						status.setText(R.string.connecting);
						starterButton1.setBackground(getResources().getDrawable(R.drawable.barraerror));
						
						
						
						}			
					if (level.equals(ConnectionStatus.LEVEL_AUTH_FAILED)){
						status.setText(R.string.authfailed);
					}					
					if (level.equals(ConnectionStatus.UNKNOWN_LEVEL)){
						status.setText(R.string.disconnected);
						
						starterButton1.setBackground(getResources().getDrawable(R.drawable.barrazul));
						
					}				
					//if (level.equals(ConnectionStatus.LEVEL_RECONNECTING)){
					//		status.setText(R.string.reconnecting);
				}				
				if (level.equals(ConnectionStatus.LEVEL_NONETWORK)){
					status.setText(R.string.nonetwork);
				}			
			
			}
		});
		
		switch (state) {
			case SkStatus.SSH_CONECTADO:
				// carrega ads banner
				if (adsBannerView != null && TunnelUtils.isNetworkOnline(SocksHttpMainActivity.this)) {
					adsBannerView.setAdListener(new AdListener() {
						@Override
						public void onAdLoaded() {
							if (adsBannerView != null && !isFinishing()) {
								adsBannerView.setVisibility(View.VISIBLE);
							}
						}
					});
					adsBannerView.postDelayed(new Runnable() {
						@Override
						public void run() {
							// carrega ads interestitial
							AdsManager.newInstance(getApplicationContext())
								.loadAdsInterstitial();
							// ads banner
							if (adsBannerView != null && !isFinishing()) {
								adsBannerView.loadAd(new AdRequest.Builder()
									.build());
							}
						}
					}, 5000);
				}
			break;
		}
	}


	/**
	 * Recebe locais Broadcast
	 */

	private BroadcastReceiver mActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;

            if (action.equals(UPDATE_VIEWS) && !isFinishing()) {
				doUpdateLayout();
			}
			else if (action.equals(OPEN_LOGS)) {
				if (mDrawer != null && !isFinishing()) {
					DrawerLayout drawerLayout = mDrawer.getDrawerLayout();
					
					if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
						drawerLayout.openDrawer(GravityCompat.END);
					}
				}
			}
        }
    };


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerPanel.getToogle() != null && mDrawerPanel.getToogle().onOptionsItemSelected(item)) {
            return true;
        }

		// Menu Itens
		switch (item.getItemId()) {

			case R.id.configUpdate:
				updateConfig(false);
				break;
		
			case R.id.miLimparConfig:
				if (!SkStatus.isTunnelActive()) {
					DialogFragment dialog = new ClearConfigDialogFragment();
					dialog.show(getSupportFragmentManager(), "alertClearConf");
				} else {
					Toast.makeText(this, R.string.error_tunnel_service_execution, Toast.LENGTH_SHORT)
						.show();
				}
				break;

			case R.id.miSettings:
				Intent intentSettings = new Intent(this, ConfigGeralActivity.class);
				//intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentSettings);
				break;

			case R.id.miExit:
				if (Build.VERSION.SDK_INT >= 16) {
					finishAffinity();
				}
				
				System.exit(0);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showPayloadGenerator() {
		PayloadGenerator payloadGenerator = new PayloadGenerator(this);
		payloadGenerator.setDialogTitle("Payload Generator");
		payloadGenerator.setGenerateListener("Save", new PayloadGenerator.OnGenerateListener() {
			@Override
			public void onGenerate(String payloadGenerated) {
				payloadEdit.setText(payloadGenerated);
			}
		});
		payloadGenerator.show();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout layout = mDrawer.getDrawerLayout();

		if (mDrawerPanel.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            mDrawerPanel.getDrawerLayout().closeDrawers();
        }
		else if (layout.isDrawerOpen(GravityCompat.END)) {
            // fecha drawer
			layout.closeDrawers();
        }
		else {
			// mostra opção para sair
			showExitDialog();
		}
	}

	@Override
    public void onResume() {
        super.onResume();

		mDrawer.onResume();
		
		//doSaveData();
		//doUpdateLayout();
		
		SkStatus.addStateListener(this);
		
		if (adsBannerView != null) {
			adsBannerView.resume();
		}
    }

	@Override
	protected void onPause()
	{
		super.onPause();
		
		doSaveData();
		
		SkStatus.removeStateListener(this);
		
		if (adsBannerView != null) {
			adsBannerView.pause();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		mDrawer.onDestroy();

		LocalBroadcastManager.getInstance(this)
			.unregisterReceiver(mActivityReceiver);
			
		if (adsBannerView != null) {
			adsBannerView.destroy();
		}
	}


	/**
	 * DrawerLayout Listener
	 */

	@Override
	public void onDrawerOpened(View view) {
		if (view.getId() == R.id.activity_mainLogsDrawerLinear) {
			toolbar_main.getMenu().clear();
			getMenuInflater().inflate(R.menu.logs_menu, toolbar_main.getMenu());
		}
	}

	@Override
	public void onDrawerClosed(View view) {
		if (view.getId() == R.id.activity_mainLogsDrawerLinear) {
			toolbar_main.getMenu().clear();
			getMenuInflater().inflate(R.menu.main_menu, toolbar_main.getMenu());
		}
	}

	@Override
	public void onDrawerStateChanged(int stateId) {}
	@Override
	public void onDrawerSlide(View view, float p2) {}

	
	/**
	 * Utils
	 */

	public static void updateMainViews(Context context) {
		Intent updateView = new Intent(UPDATE_VIEWS);
		LocalBroadcastManager.getInstance(context)
			.sendBroadcast(updateView);
	}
	
	public void showExitDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).
			create();
		dialog.setTitle(getString(R.string.attention));
		dialog.setMessage(getString(R.string.alert_exit));

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.
				string.exit),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Utils.exitAll(SocksHttpMainActivity.this);
				}
			}
		);

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.
				string.minimize),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// minimiza app
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
				}
			}
		);

		dialog.show();
	}
	
	//Final 
	private void loadServer1() {
		new Thread(new Runnable(){
				public void run(){
					final ArrayList<String> urls=new ArrayList<String>();
					try {
						//AQUI VAI A URL DO SEU SERVER COM OS ONLINE

						int pos1 = serverSpinner.getSelectedItemPosition();
						String ip = config.getServersArray().getJSONObject(pos1).getString("ProxyIP");
						//URL url = new URL("http://");
						
						
						URL url = new URL("http://209.14.136.234:8888/server/online");

						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(3000);

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

						String str;
						while ((str = in.readLine()) != null) {
							urls.add(str);
						}
						in.close();
					} catch (Exception e) {
						Log.d("MyTag",e.toString());
					}

					SocksHttpMainActivity.this.runOnUiThread(new Runnable(){
							public void run(){

								try{
									//TENTA OBTER INFOS
									final String nomedoserver = "";
									final String onlineservergreen = "Online " + getColoredSpanned((urls.get(0)) ,"#228b22");
									final String onlineserverorange = "Online " + getColoredSpanned((urls.get(0)),"#ffbc40");
									final String onlineserverred = "Online " + getColoredSpanned((urls.get(0)) ,"#FF0000");
									//SETA INFOS
									int onlines = Integer.parseInt((urls.get(0)));

									if (onlines <=29){
										textstatus.setText(Html.fromHtml(nomedoserver+" "+onlineservergreen));

									}
									if (onlines >=30){
										textstatus.setText(Html.fromHtml(nomedoserver+" "+onlineserverorange));

									}
									if (onlines >=70){
										textstatus.setText(Html.fromHtml(nomedoserver+" "+onlineserverred));
									}


								} catch (Exception e) {
									e.printStackTrace();
									//SETA TEXTVIEW COM ERRO
									final String nomedoserver = "";
									final String erroinfo = getColoredSpanned("","#0256a4");

									textstatus.setText(Html.fromHtml(nomedoserver+" "+erroinfo));


								}


							}
						});
				}
			}).start();
	}

	private String getColoredSpanned(String text, String color) {
		String input = "<font color=" + color + ">" + text + "</font>";
		return input;
	}
	
	
}

