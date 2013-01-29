package com.example.dicview_ch;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
	ListView lv;
	GridView grid;
	LinearLayout dicList;
	ArrayList<String> arGeneral = new ArrayList<String>();
	ArrayAdapter<String> Adapter;
	TextView[] wordArray,dicTxtArray;
	EditText et;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lv = (ListView) findViewById(R.id.wList);

//		lv.add	
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
		
		lv.setOnItemClickListener(mItemClickListener);		
		AdapterView.OnItemClickListener mItemClickListener2 = new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				selDicBtn.setText(((TextView)(parent.getAdapter().getItem(position))).getText());
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
		float textSize=0;
		textSize=wordArray[0].getTextSize();
		textSize+=10;
		if(textSize>40) textSize=20;
		for(int i=0; i<wordArray.length; i++)
			wordArray[i].setTextSize(textSize);
	}
	
	protected void parse() {
		// DOM�뚯꽌瑜��쎌뼱二쇰뒗 媛앹껜 �좎뼵
		/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
			//dom臾몄꽌瑜��쎄퀬 �댁꽍 �뚯떛�댁＜��媛앹껜 �좎뼵
			DocumentBuilder parse = factory.newDocumentBuilder();
			
			File file = new File("/sdcard/imgtest/DF.xml");
			InputStream in = new FileInputStream(file);
			//xml臾몄꽌 �뚯떛
			Document doc = parse.parse(in);
			// 猷⑦듃�섎━癒쇳듃 異붿텧
			Element element = doc.getDocumentElement();
			
			// �섎━癒쇳듃�먯꽌 entry�쇰뒗 �댁엫�쒓렇瑜�湲곗��쇰줈���몃뱶由ъ뒪��異붿텧
			NodeList nlist = element.getElementsByTagName("word");
			NodeList dList = element.getElementsByTagName("entry");
			
			wordArray=new TextView[nlist.getLength()];
			for(int i=0;i<nlist.getLength();i++){
				Element name = (Element)nlist.item(i);
				
				// �섎━癒쇳듃���붿냼瑜�異붿텧 NodeList���꾩씠�쒖쓽 i踰덉㎏ �ㅻ툕�앺듃瑜�異붿텧
				Text text = (Text)name.getFirstChild();
				//arGeneral.add(text.getData());
				wordArray[i]=new TextView(this);
				wordArray[i].setTextSize(20);
				wordArray[i].setText((CharSequence)text.getData());
				//Log.d(TAG,arGeneral.get(i));			
			}	
			//Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , arGeneral);*/
			//TextViewAdapter Adapter = new TextViewAdapter(this,wordArray);
			
			Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , arGeneral);
			lv.setAdapter(Adapter);		

			dicTxtArray=new TextView[dList.getLength()];
			for(int i=0;i<dList.getLength();i++){
				Element name = (Element)dList.item(i);
				
				Text text = (Text)name.getFirstChild();
				dicTxtArray[i]=new TextView(this);
				dicTxtArray[i].setTextSize(25);
				dicTxtArray[i].setGravity(0x11);
				dicTxtArray[i].setText((CharSequence)text.getData());				
				Log.d(TAG,(String) dicTxtArray[i].getText());
				//System.out.println(text.getData());				
			}	
			TextViewAdapter ba = new TextViewAdapter(this,dicTxtArray);
			grid = (GridView)findViewById(R.id.dGridList);
			grid.setAdapter(ba);
			
//		}catch(Exception e){
//			e.printStackTrace();
//			Log.d(TAG,"error occurred while constructing ListView");
//		
//		}
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
			TextView text;
			text =(TextView)getItem(position);			
			return text;
		}
		
	}
}
	

