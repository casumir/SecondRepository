package com.example.dicview_ch;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	String TAG="DixView.MainActivity";
	Button selDicBtn, btn1, btn2;
	ImageButton foldBtn;
	ListView mListView;
	GridView grid;
	LinearLayout dicList;
	ArrayList<String> arGeneral = new ArrayList<String>();
	MArrayAdapter listAdapter ;
	String[] dictString = {"영한","영영","한영","국어","불한","불영","중한","한중","독한","독영","일한","한일","서영","옥편"};
	EditText et;
	Provider mProvider;

	private float mTextSize=20f;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (ListView) findViewById(R.id.wList);

//		mListView.add	
		parse();
		et = (EditText)findViewById(R.id.editText1);
		
		dicList = (LinearLayout)findViewById(R.id.dicLayout);
		dicList.setVisibility(dicList.INVISIBLE);
		
		
		selDicBtn = (Button)findViewById(R.id.selDicBtn);
		selDicBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(dicList.getVisibility()==dicList.INVISIBLE){
					dicList.setVisibility(dicList.VISIBLE);					
				}else{
					dicList.setVisibility(dicList.INVISIBLE);
				}
			}			
		});
		
		foldBtn = (ImageButton)findViewById(R.id.fold);
		foldBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dicList.setVisibility(dicList.INVISIBLE);
			}
		});
		
		btn2 = (Button)findViewById(R.id.btn2);
		
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				textSizeToggle();
			}	
		});
		
		AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				/*Configuration config = getResources().getConfiguration();
				if (config.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO)	{*/
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getApplicationWindowToken(),0 );
					//toggleSoftInputFromWindow(et.getApplicationWindowToken(),  InputMethodManager.SHOW_IMPLICIT, 0);
//				}
				
			}
			
		};		
		mListView.setFastScrollEnabled(true);
		mListView.setOnItemClickListener(mItemClickListener);		
		AdapterView.OnItemClickListener mItemClickListener2 = new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				selDicBtn.setText(dictString[position]);
				dicList.setVisibility(dicList.INVISIBLE);
			}
		};
		
		grid.setOnItemClickListener(mItemClickListener2);
		
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getApplicationWindowToken(),0 );	
				}
								
			}
		});
		 
	} 
	
	public void textSizeToggle(){

		mTextSize += 5f;
		if(mTextSize>30) mTextSize =15;
		
		listAdapter.notifyDataSetChanged();
	}
	
	protected void parse() {
			mProvider = Provider.getInstance();
			for(String str : mProvider.getM_headWord()){
				arGeneral.add(str);
			}
			Log.d(TAG,"asGenera size :"+arGeneral.size());
			listAdapter = new MArrayAdapter(this, R.layout.listitemnormal , arGeneral);
			mListView.setAdapter(listAdapter);		
			
			ArrayList<String> dicList=new ArrayList<String>();
			for(int i =0; i<dictString.length; i++){
				dicList.add(dictString[i]);
			}
			MArrayAdapter adapter = new MArrayAdapter(this, R.layout.listitemcenter ,dicList);
			grid = (GridView)findViewById(R.id.dGridList);
			
			grid.setAdapter(adapter);
			
	}
	
	class MArrayAdapter extends ArrayAdapter<String>{

		public MArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text = (TextView)super.getView(position, convertView, parent);
			text.setTextSize(mTextSize);
			return text;
		}
	}
	
	class TextViewAdapter extends BaseAdapter{
		private Context mContext;
		TextView[] dicTxtArray;
		int arrayLength=0;
		
		public TextViewAdapter(Context c,TextView[] Txt){
			mContext = c;
			dicTxtArray=Txt;			
			arrayLength=dicTxtArray.length;
			Log.d(TAG,"");
		}
		
		@Override
		public int getCount() {
			return arrayLength;
		}

		@Override
		public Object getItem(int arg0) {
			return dicTxtArray[arg0%arrayLength];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
						
			return (View)getItem(position);
		}		
	}
}