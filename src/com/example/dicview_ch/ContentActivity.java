package com.example.dicview_ch;


import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContentActivity extends Activity implements OnClickListener {
	
	private int m_positionOfCurrentContent;
	
	private ImageButton previousButton;
	private ImageButton nextButton;    
	private ImageButton addButton;     
	private ImageButton fontButton;    
	private ImageButton bookButton;  	
	private LinearLayout linearContent;
	private Provider mprovider;
	private Cursor cursor;
	private EditText[] contentTextView;
	private int mTextSize;
	private int syncCode=0;

	private ViewApplication application=new ViewApplication();
	private ArrayList<ContentViewControl> mContents = new ArrayList<ContentViewControl>();

	private String TAG="ContentActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);						
		setContentView(R.layout.activity_content);
		
		previousButton = (ImageButton) findViewById(R.id.previous_button);
		nextButton = (ImageButton) findViewById(R.id.next_button);
		addButton = (ImageButton) findViewById(R.id.add_button);
		fontButton = (ImageButton) findViewById(R.id.font_button);
		bookButton = (ImageButton) findViewById(R.id.book_button);		
		mprovider = Provider.getInstance();
		
		previousButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		fontButton.setOnClickListener(this);
		
		Intent intent = getIntent();
		m_positionOfCurrentContent = intent.getIntExtra("CurrentPosition",0);
		mTextSize=20;
		
//		Log.d("content","문자 출력됨");
		
		linearContent = (LinearLayout)findViewById(R.id.content_frame);
		
		add1Content(m_positionOfCurrentContent);
		addContents();	
		
	} 
	
	/**
	 *  entry_id 에 해당하는 하나의뷰를 생성하여 LinearList에 붙인다. 
	 * @param entry_id
	 */
	private void add1Content(final int entry_id) {
		Thread addThread = new Thread( new Runnable(){
			@Override
			public void run() {
				int sync = syncCode;
				String detail=mprovider.getContentByID(entry_id);
				detail = application.modifyContentWithSound(detail, "detail", true);
				final EditText textView = (EditText)getLayoutInflater().inflate(R.layout.content_view, null);
				ContentViewControl control=new ContentViewControl(textView,m_positionOfCurrentContent);
				control.setText(Html.fromHtml(detail));
				if(sync==syncCode){
					linearContent.post(new Runnable(){
						@Override
						public void run() {
							linearContent.addView(textView);
						}					
					});
					mContents.add(control);
				}else{
					Log.d(TAG,"previous Thread");
				}
			}
		});
		addThread.start();
//	}

	/**
	 *  해당 포지션과 searchword가 같은 entry 들의 content를 담은 뷰를 linearContent에 추가한다.
	 *  같은 엔트리 아이디를 가진 커서들의 entry_id 목록을 가지고 와서 사용한다.
	 */
	private void addContents() {
		
		Thread addListThread = new Thread( new Runnable(){
			@Override
			public void run() {
				int sync = syncCode;
				int[] entry_id;
				entry_id = mprovider.getIdwithSameSW(m_positionOfCurrentContent);
				if(sync!=syncCode)
					return;
				// entry_id가 어레이 내에서 내림차순으로 되어있음
				// 따라서 맨 뒤에서부터 뷰에 add.
				for(int i=entry_id.length-1; i>=0; i--){
					Log.d(TAG,i+"th entry_id = "+entry_id[i]);
//					if((entry_id[i])!=m_positionOfCurrentContent)
//						add1Content((entry_id[i]));
				}				
			}			
		});
		addListThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_content, menu);
		return true;
	}
	
	public void onClick(View view) {
		if(view == previousButton) {			
			moveLeft();
			
		} else if(view == nextButton) {
			moveRight();
			
		} else if(view == addButton) {

			
		} else if(view == fontButton) {
			changeTextSize();
			
		} else if(view == bookButton) {
			
		} else {
			// n.a.
		}
	}

	/**
	 *  mTextSize 변경후 TextView의 설정 변경
	 */
	private void changeTextSize() {
		mTextSize +=5;
		if(mTextSize>25)
			mTextSize =15;
		
	
		for(ContentViewControl view:mContents){
			view.getmEditText().setTextSize(mTextSize);
			Log.d(TAG,"Text size changed to "+ mTextSize);
		}
	}

	/*	public void moveLeft() {
		int newId = (int) control.getId()-1;
		String newWord = null;
		Cursor cursor = getContentResolver().query(Uri.parse("content://"+provider+"/entry?id_normal="+newId), null, null, null, null);
		if(cursor.moveToFirst()) {
			newId = cursor.getInt(cursor.getColumnIndex("_id"));
			newWord = cursor.getString(cursor.getColumnIndex("searchword"));
			
			Intent intent = new Intent(this, ContentActivity.class);
			intent.putExtra(EXTRA_KEY_ROOT_PROVIDER, root);
			intent.putExtra(EXTRA_KEY_PROVIDER, provider);
			intent.putExtra(EXTRA_KEY_ID, newId);
			intent.putExtra(EXTRA_KEY_WORD, newWord);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
		}
		cursor.close();
	}*/
	public void moveLeft(){
		Log.d(TAG,"left button clicked");
		m_positionOfCurrentContent--;
		if(m_positionOfCurrentContent<0)
			m_positionOfCurrentContent=mprovider.getSize()-1;
		reView();
	}
	

	public void moveRight(){
		m_positionOfCurrentContent++;		
		if(m_positionOfCurrentContent==mprovider.getSize())
			m_positionOfCurrentContent=0;
		reView();
	}
	
	private void reView() {
		linearContent.removeAllViews();
		syncCode++;
		mContents.clear();
		add1Content(m_positionOfCurrentContent);
		addContents();
	}
	
}
