package com.czvv.repairsystemmobile.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.czvv.repairsystemmobile.R;

public class MyToast extends Toast {
	 
	public MyToast(Context context) {
		super(context);
	}
	public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);
 
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.my_toast, null);
        TextView tv = (TextView)v.findViewById(R.id.my_toast);
        tv.setText(text);
        
        result.setView(v);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);
        return result;
    }
	
}
