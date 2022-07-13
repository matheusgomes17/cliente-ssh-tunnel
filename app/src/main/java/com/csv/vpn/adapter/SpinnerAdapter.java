package com.csv.vpn.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csv.vpn.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

public class SpinnerAdapter extends ArrayAdapter<JSONObject> {

	private int spinner_id;

	public SpinnerAdapter(Context context, int spinner_id, ArrayList<JSONObject> list) {
		super(context, R.layout.spinner_item, list);
		this.spinner_id = spinner_id;
	}

	@Override
	public JSONObject getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return view(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return view(position, convertView, parent);
	}

	private View view(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
		TextView tv = v.findViewById(R.id.itemName);
		ImageView im = v.findViewById(R.id.itemImage);
		try {
			tv.setText(getItem(position).getString("Name"));
			if (spinner_id == R.id.serverSpinner) {
				getServerIcon(position, im);
			} else if (spinner_id == R.id.payloadSpinner) {
				getPayloadIcon(position, im);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	private void getServerIcon(int position, ImageView im) throws Exception {
		InputStream inputStream = getContext().getAssets().open("flags/" + getItem(position).getString("FLAG") + "");
		im.setImageDrawable(Drawable.createFromStream(inputStream, getItem(position).getString("FLAG") + ""));
		if (inputStream != null) {
			inputStream.close();
		}
	}

	private void getPayloadIcon(int position, ImageView im) throws Exception {
		String name = getItem(position).getString("Name").toLowerCase();
		if (name.contains("vivo")) {
			im.setImageResource(R.drawable.vivo);
		} else if (name.contains("claro")) {
			im.setImageResource(R.drawable.claro);
		} else if (name.contains("tim")) {
			im.setImageResource(R.drawable.tim);
		}else if(name.contains("oi")) {
			im.setImageResource(R.drawable.oi);
		}
	}

}

