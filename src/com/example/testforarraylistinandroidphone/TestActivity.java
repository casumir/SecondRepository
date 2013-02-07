package com.example.testforarraylistinandroidphone;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TestActivity extends Activity {
	Customadapter adtlist;	
	ArrayList<strcls> alstr = new ArrayList<strcls>() ;
	ArrayList<strcls> alstrcpy = new ArrayList<strcls>() ;
	ListView listview  ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		for(int i = 0 ; i < 100; i ++ )
		{
			alstr.add(new strcls());
		}
		
		for(int i = 0 ; i < 10; i ++ )
		{
			alstrcpy.add(0, new strcls());
		}
		
		listview = (ListView)findViewById(R.id.listvw);
		adtlist = new Customadapter(this,R.layout.textview, alstrcpy);
		listview.setAdapter(adtlist);
		findViewById(R.id.btn).setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				for(int i = 0 ; i < 1000; i ++ )
				{
					alstrcpy.add(1, new strcls());
				}
				adtlist.notifyDataSetChanged();
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test, menu);
		return true;
	}
	public static class strcls
	{
		static String instr = "new";
		static int i = 0;
		public strcls()
		{
			str = instr + i;
			i++;
			
		}
		String str;
	}
	public class Customadapter extends BaseAdapter
	{
		private Context context;
		private ArrayList<strcls> data;
		private int layout;		
		private LayoutInflater inflater;
		public Customadapter(Context context, int layout, ArrayList<strcls> data)
		{
			this.context = context;
			this.data = data;
			this.layout = layout;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position).str;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(layout, parent, false);
			}
			TextView textview = (TextView) convertView.findViewById(R.id.btn);
			textview.setText((CharSequence) getItem(position));
			return convertView;
		}
		
	}
	
	

}
