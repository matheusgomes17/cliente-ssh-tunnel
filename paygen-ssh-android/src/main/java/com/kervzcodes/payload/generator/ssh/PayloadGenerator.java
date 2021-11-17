package com.kervzcodes.payload.generator.ssh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class PayloadGenerator {

    private static ArrayAdapter<String> rAdapter, iAdapter, sAdapter;
    private static CheckBox cbBack;
    private static CheckBox cbDual;
    private static CheckBox cbForward;
    private static CheckBox cbFront;
    private static CheckBox cbKeep;
    private static CheckBox cbOnline;
    private static CheckBox cbRaw;
    private static CheckBox cbReferer;
    private static CheckBox cbReverse;
    private static CheckBox cbRotate;
    private static CheckBox cbUser;
    private static EditText payload;
    private static LayoutInflater inflater;
    private static RadioButton rNomal;
    private static RadioButton rSplit;
    private static RadioButton rDirect;
    private static Spinner injectSpin;
    private static Spinner requestSpin;
    private static Spinner splitSpin;
    private static String[] inject_items = new String[]{"NORMAL", "FRONT INJECT", "BACK INJECT"};
    private static String[] request_items = new String[]{ "CONNECT", "GET", "POST", "PUT", "HEAD", "TRACE", "OPTIONS", "PATCH", "PROPATCH", "DELETE"};
    private static String[] split_items = new String[]{"NORMAL","INSTANT SPLIT", "DELAY SPLIT"};
    private static Context context;
    private static View v;
    private static SharedPreferences prefs_vmodz;
    private static SharedPreferences.Editor editor_vmodz;


    private OnGenerateListener generateListener;
    private OnCancelListener cancelListener;
    private OnNeutralListener neutralButtonListener;

    private CharSequence generateButtonName;
    private CharSequence cancelButtonName;
    private CharSequence neutralButtonName;

    private String title;

    public PayloadGenerator(Context context) {
        this.context = context;
    }

    /** interface for positive button **/
    public interface OnGenerateListener{
        void onGenerate(String payloadGenerated);
    }

    /** interface for negative button **/
    public interface OnCancelListener {
        void onCancelListener();
    }

    /** interface for neutral button **/
    public interface OnNeutralListener {
        void onNeutralListener();
    }

    /**
     * This should not be null
     * This serves as your positive button
     **/
    public void setGenerateListener(CharSequence generateButtonName, OnGenerateListener generateListener) {
        this.generateButtonName = generateButtonName;
        this.generateListener = generateListener;
    }

    /**
     * This should not be null
     * This serves as your negative button
     **/
    public void setCancelListener(CharSequence cancelButtonName, OnCancelListener cancelListener) {
        this.cancelButtonName = cancelButtonName;
        this.cancelListener = cancelListener;
    }

    /**
     * This should not be null
     * This serves as your neutral button
     **/
    public void setNeutralButtonListener(CharSequence neutralButtonName, OnNeutralListener neutralButtonListener) {
        this.neutralButtonName = neutralButtonName;
        this.neutralButtonListener = neutralButtonListener;
    }


    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void show() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setView(generatorView());
        if (title != null) {
            adb.setMessage(title);
        }
        adb.setView(generatorView());
        if (generateListener != null) {
            adb.setPositiveButton(generateButtonName, generate);
        }
        if (cancelListener != null) {
            adb.setNegativeButton(cancelButtonName, cancel);
        }
        if (neutralButtonListener != null) {
            adb.setNeutralButton(neutralButtonName, neutral);
        }
        adb.create().show();
    }


    private DialogInterface.OnClickListener generate = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int in) {
            switch (in) {
                case BUTTON_POSITIVE:
                    String sPayload = payload.getText().toString();
                    StringBuilder sb = new StringBuilder();
                    String crlf = "[crlf]";
                    String space = " ";
                    String connect = "CONNECT ";
                    String host = "Host: ";
                    String host_port = "[host_port]";
                    String protocol = " [protocol]";
                    String outro = crlf + crlf;
                    String onePone = "HTTP/1.1";
                    String http = "http://";
                    String raw = "[raw]";
                    int r = requestSpin.getSelectedItemPosition();
                    int i = injectSpin.getSelectedItemPosition();
                    if(!rDirect.isChecked()){
                        switch(i){
                            case 0:
                                sb.append(connect);
                                if(cbFront.isChecked()){
                                    sb.append(sPayload).append("@");
                                    if(r == 0){
                                        sb.append(host_port);
                                        sb.append(protocol).append(crlf);
                                    }else{
                                        sb.append(host_port).append(space);
                                        sb.append(onePone);
                                        sb.append(outro);
                                        sb.append((String) requestSpin.getSelectedItem()).append(space);
                                        sb.append(http.concat(sPayload).concat("/"));
                                        sb.append(protocol).append(crlf);
                                    }
                                }else if(cbBack.isChecked()){
                                    sb.append(host_port).append("@").append(sPayload);
                                    if(r == 0){
                                        sb.append(protocol).append(crlf);
                                    }else{
                                        sb.append(space);
                                        sb.append(onePone);
                                        sb.append(outro);
                                        sb.append((String) requestSpin.getSelectedItem()).append(space);
                                        sb.append(http.concat(sPayload).concat("/"));
                                        sb.append(protocol).append(crlf);
                                    }
                                }else{
                                    if(r == 0){
                                        sb.append(host_port);
                                        sb.append(protocol).append(crlf);
                                        if(rSplit.isChecked()){
                                            int s = splitSpin.getSelectedItemPosition();
                                            switch(s){
                                                case 0:
                                                    sb.append("[split]");
                                                    sb.append(connect).append(http).append(sPayload).append("/").append(space).append(onePone).append(crlf);
                                                    break;
                                                case 1:
                                                    sb.append("[instant_split]");
                                                    sb.append(connect).append(http).append(sPayload).append("/").append(space).append(onePone).append(crlf);
                                                    break;
                                                case 2:
                                                    sb.append("[delay_split]");
                                                    sb.append(connect).append(http).append(sPayload).append("/").append(space).append(onePone).append(crlf);
                                                    break;
                                                default: break;
                                            }
                                        }
                                    }
                                }
                                break;
                            case 1:
                                sb.append((String) requestSpin.getSelectedItem()).append(space);
                                sb.append(http.concat(sPayload).concat("/")).append(space);
                                sb.append(onePone).append(crlf);
                                break;
                            case 2:
                                sb.append(connect);
                                if(cbFront.isChecked()){
                                    sb.append(sPayload).append("@");
                                    sb.append(host_port);
                                }else if(cbBack.isChecked()){
                                    sb.append(host_port);
                                    sb.append("@").append(sPayload);
                                }else{
                                    sb.append(host_port);
                                }
                                if(rSplit.isChecked()){
                                    sb.append(protocol);
                                    sb.append(crlf);
                                }else{
                                    sb.append(space);
                                    sb.append(onePone);
                                    sb.append(crlf);
                                }
                                if(rSplit.isChecked()){
                                    int s = splitSpin.getSelectedItemPosition();
                                    switch(s){
                                        case 0:
                                            sb.append("[split]");
                                            break;
                                        case 1:
                                            sb.append("[instant_split]");
                                            break;
                                        case 2:
                                            sb.append("[delay_split]");
                                            break;
                                        default: break;
                                    }
                                }else{
                                    sb.append(crlf);
                                }
                                sb.append((String) requestSpin.getSelectedItem()).append(space);
                                sb.append(http.concat(sPayload).concat("/"));
                                if(rSplit.isChecked()){
                                    sb.append(space).append(onePone).append(crlf);
                                }else{
                                    sb.append(protocol).append(crlf);
                                }
                                break;
                        }
                    }else{
                        String d = (String) requestSpin.getSelectedItem();
                        sb.append(d);
                        if(cbFront.isChecked())
                            sb.append(space).append(sPayload).append("@").append(host_port).append(protocol).append(crlf);
                        else if(cbBack.isChecked())
                            sb.append(" [host_port]").append("@").append(sPayload).append(protocol).append(crlf);
                        else
                            sb.append(space).append(host_port).append(protocol).append(crlf);
                    }
                    if(sPayload.isEmpty() || sPayload.equals("")){

                    }else{
                        sb.append(host).append(sPayload);

                        if(cbOnline.isChecked()){
                            sb.append(crlf).append("X-Online-Host: ").append(sPayload);
                        }
                        if(cbForward.isChecked()){
                            sb.append(crlf).append("X-Forward-Host: ").append(sPayload);
                        }
                        if(cbReverse.isChecked()){
                            sb.append(crlf).append("X-Forwarded-For: ").append(sPayload);
                        }
                    }
                    if(cbKeep.isChecked()){
                        sb.append(crlf).append("Connection: Keep-Alive");
                    }
                    if(cbUser.isChecked()){
                        sb.append(crlf).append("User-Agent: [ua]");
                    }
                    if(cbReferer.isChecked()){
                        sb.append(crlf).append("Referer: ").append(sPayload);
                    }
                    if(cbDual.isChecked()){
                        sb.append(crlf).append(connect).append(host_port).append(protocol);
                    }
                    if(i ==1){
                        sb.append(outro);
                        if(rSplit.isChecked()){
                            int s = splitSpin.getSelectedItemPosition();
                            switch(s){
                                case 0:
                                    sb.append("[split]");
                                    break;
                                case 1:
                                    sb.append("[instant_split]");
                                    break;
                                case 2:
                                    sb.append("[delay_split]");
                                    break;
                                default: break;
                            }
                        }
                        sb.append(connect);
                        if(cbFront.isChecked()){
                            sb.append(sPayload).append("@").append(host_port).append(protocol).append(outro);
                        }else if(cbBack.isChecked()){
                            sb.append(host_port).append("@").append(sPayload).append(protocol).append(outro);
                        }else{
                            sb.append(host_port).append(protocol).append(outro);
                        }
                    }else{
                        sb.append(outro);
                    }
                    String f = sb.toString();
                    if(cbRaw.isChecked()){
                        if(f.contains("CONNECT [host_port] [protocol]")){
                            String rw = f.replace("CONNECT [host_port] [protocol]",raw);
                            if(cbRotate.isChecked()){
                                if(!rw.contains(";")){
                                    Toast.makeText(context, "Invalid URL/Host",Toast.LENGTH_LONG).show();
                                }else{
                                    generateListener.onGenerate(rw);
                                }
                            }else{
                                generateListener.onGenerate(rw);
                            }
                        }
                    }else{
                        if(cbRotate.isChecked()){
                            if(!sb.toString().contains(";")){
                                Toast.makeText(context, "Invalid URL/Host", Toast.LENGTH_LONG).show();
                            }else{
                                generateListener.onGenerate(sb.toString());
                            }
                        }else{
                            generateListener.onGenerate(sb.toString());
                        }

                    }
                    save();
            }
        }
    };

    private DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface p1, int p2)
        {
            // TODO: Implement this method
            if(cancelListener != null){
                cancelListener.onCancelListener();
            }

        }
    };

    private DialogInterface.OnClickListener neutral = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface p1, int p2)
        {
            // TODO: Implement this method
            if(neutralButtonListener != null){
                neutralButtonListener.onNeutralListener();
            }

        }
    };

    private static View generatorView(){
        inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.payloadgen_dialog, null);
        payload = (EditText) v.findViewById(R.id.paygenEditText);
        rNomal = (RadioButton) v.findViewById(R.id.rNormal);
        rSplit = (RadioButton) v.findViewById(R.id.rSplit);
        rDirect = (RadioButton) v.findViewById(R.id.rDirect);
        cbRotate = (CheckBox) v.findViewById(R.id.cbRotate);
        cbOnline = (CheckBox) v.findViewById(R.id.cbOnline);
        cbForward = (CheckBox) v.findViewById(R.id.cbForward);
        cbReverse = (CheckBox) v.findViewById(R.id.cbReverse);
        cbKeep = (CheckBox) v.findViewById(R.id.cbKeep);
        cbUser = (CheckBox) v.findViewById(R.id.cbUser);
        cbReferer = (CheckBox) v.findViewById(R.id.cbReferer);
        cbFront = (CheckBox) v.findViewById(R.id.cbFront);
        cbBack = (CheckBox) v.findViewById(R.id.cbBack);
        cbRaw = (CheckBox) v.findViewById(R.id.cbRaw);
        cbDual = (CheckBox) v.findViewById(R.id.cbDual);
        requestSpin = (Spinner) v.findViewById(R.id.request_spin);
        injectSpin = (Spinner) v.findViewById(R.id.inject_spin);
        splitSpin = (Spinner) v.findViewById(R.id.split_spin);
        rNomal.setOnCheckedChangeListener(cb);
        rSplit.setOnCheckedChangeListener(cb);
        rDirect.setOnCheckedChangeListener(cb);
        cbFront.setOnCheckedChangeListener(cb);
        cbBack.setOnCheckedChangeListener(cb);
        cbRaw.setOnCheckedChangeListener(cb);
        cbDual.setOnCheckedChangeListener(cb);
        rAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, request_items);
        iAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, inject_items);
        sAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, split_items);
        requestSpin.setAdapter(rAdapter);
        injectSpin.setAdapter(iAdapter);
        splitSpin.setAdapter(sAdapter);
        rAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestSpin.setPrompt("Request Method");
        injectSpin.setPrompt("Injection Method");
        splitSpin.setPrompt("Split Method");
        requestSpin.setOnItemSelectedListener(isl);
        injectSpin.setOnItemSelectedListener(isl);
        load();
        splitSpin.setSelection(0);
        requestSpin.setSelection(0);
        injectSpin.setSelection(0);
        rNomal.setChecked(true);
        return v;
    }

    private static CompoundButton.OnCheckedChangeListener cb = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton p1, boolean p2)
        {
            int id = p1.getId();
            if(id == R.id.rNormal){
                if(p2){
                    splitSpin.setEnabled(false);
                    injectSpin.setEnabled(true);
                    requestSpin.setSelection(0);
                    injectSpin.setSelection(0);
                }
            }else if(id == R.id.rSplit){
                if(p2){
                    splitSpin.setEnabled(true);
                    requestSpin.setSelection(1);
                    injectSpin.setSelection(2);
                    splitSpin.setSelection(2);
                }
            }else if(id == R.id.rDirect){
                if(p2){
                    splitSpin.setEnabled(false);
                    injectSpin.setEnabled(false);
                }
            }else if(id == R.id.cbRaw){
                if(p2){
                    cbDual.setEnabled(false);
                }else{
                    cbDual.setEnabled(true);
                }
            }else if(id == R.id.cbDual){
                if(p2){
                    cbRaw.setEnabled(false);
                }else{
                    cbRaw.setEnabled(true);
                }
            }else if(id == R.id.cbFront){
                if(p2){
                    if(cbBack.isChecked()){
                        cbBack.setChecked(false);
                    }
                }
            }else if(id == R.id.cbBack){
                if(p2){
                    if(cbFront.isChecked()){
                        cbFront.setChecked(false);
                    }
                }
            }else if(id == R.id.cbRaw){
                if(p2){
                    payload.setHint("URL/HOST");
                }else{
                    payload.setHint("URL/HOST (eg: 1.com;2.com;3.com)");
                }
            }
        }

    };

    private static AdapterView.OnItemSelectedListener isl = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
        {
            int id = p1.getId();
            if(id == R.id.request_spin){
                if(p3!=0){
                    if(injectSpin.getSelectedItemPosition() == 1){
                        return;
                    }else{
                        injectSpin.setSelection(2);
                    }
                }
                editor_vmodz.putInt("reqSpin_vmodz", p3).commit();
            }else if(id == R.id.inject_spin){
                if(p3 !=0 ){
                    requestSpin.setSelection(1);
                }else if(p3 == 0){
                    if(rSplit.isChecked()){
                        rNomal.setChecked(true);
                    }
                    requestSpin.setSelection(0);
                }
                editor_vmodz.putInt("injSpin_vmodz", p3).commit();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> p1)
        {
        }
    };

    private static void load(){
        if (prefs_vmodz == null && editor_vmodz == null) {
            prefs_vmodz = context.getSharedPreferences("status404error_prefs_vmodz", context.MODE_PRIVATE);
            editor_vmodz = prefs_vmodz.edit();
        }
        payload.setText(prefs_vmodz.getString("my_inputted_payload",""));
        rNomal.setChecked(prefs_vmodz.getBoolean("rNormal_vmodz", true));
        rSplit.setChecked(prefs_vmodz.getBoolean("rSplit_vmodz", false));
        rDirect.setChecked(prefs_vmodz.getBoolean("rDirect_vmodz", false));
        cbBack.setChecked(prefs_vmodz.getBoolean("cbBack_vmodz", false));
        cbOnline.setChecked(prefs_vmodz.getBoolean("cbOnline_vmodz", false));
        cbForward.setChecked(prefs_vmodz.getBoolean("cbForward_vmodz", false));
        cbReverse.setChecked(prefs_vmodz.getBoolean("cbReverse_vmodz", false));
        cbKeep.setChecked(prefs_vmodz.getBoolean("cbKeep_vmodz", false));
        cbUser.setChecked(prefs_vmodz.getBoolean("cbUser_vmodz", false));
        cbReferer.setChecked(prefs_vmodz.getBoolean("cbReferer_vmodz", false));
        cbRaw.setChecked(prefs_vmodz.getBoolean("cbRaw_vmodz", false));
        cbDual.setChecked(prefs_vmodz.getBoolean("cbDual_vmodz", false));
        requestSpin.setSelection(prefs_vmodz.getInt("reqSpin_vmodz",0));
        injectSpin.setSelection(prefs_vmodz.getInt("injSpin_vmodz",0));
    }

    private static void save(){
        if (prefs_vmodz == null && editor_vmodz == null) {
            prefs_vmodz = context.getSharedPreferences("status404error_prefs_vmodz", context.MODE_PRIVATE);
            editor_vmodz = prefs_vmodz.edit();
        }
        editor_vmodz.putString("my_inputted_payload",payload.getText().toString()).commit();
        editor_vmodz.putBoolean("rNormal_vmodz", rNomal.isChecked()).commit();
        editor_vmodz.putBoolean("rSplit_vmodz", rSplit.isChecked()).commit();
        editor_vmodz.putBoolean("rDirect_vmodz", rDirect.isChecked()).commit();
        editor_vmodz.putBoolean("cbFront_vmodz", cbFront.isChecked()).commit();
        editor_vmodz.putBoolean("cbBack_vmodz", cbBack.isChecked()).commit();
        editor_vmodz.putBoolean("cbOnline_vmodz", cbOnline.isChecked()).commit();
        editor_vmodz.putBoolean("cbForward_vmodz", cbForward.isChecked()).commit();
        editor_vmodz.putBoolean("cbReverse_vmodz", cbReverse.isChecked()).commit();
        editor_vmodz.putBoolean("cbKeep_vmodz", cbKeep.isChecked()).commit();
        editor_vmodz.putBoolean("cbUser_vmodz", cbUser.isChecked()).commit();
        editor_vmodz.putBoolean("cbReferer_vmodz", cbReferer.isChecked()).commit();
        editor_vmodz.putBoolean("cbRaw_vmodz", cbRaw.isChecked()).commit();
        editor_vmodz.putBoolean("cbDual_vmodz", cbDual.isChecked()).commit();
    }

}
