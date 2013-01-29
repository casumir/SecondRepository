package com.example.dicview_ch;

import java.io.File;
import java.util.ArrayList;

//import com.unichal.tutorial.designpattern.Singleton;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Provider {
	
	private static final String TAG = "Provider";
	private SQLiteDatabase dictionaryDB;		//사전 디비
	//private SQLiteDatabase etcDB;				//암기장디비
	private int entrySize;							//헤드워크 갯수
	public ArrayList<String> getM_headWord() {
		return m_headWord;
	}

	private String textSize;							//텍스트 크기가 스트링 (small normal large)
	private String viewMode;						//추정 사전을 보여주는 모드 헤드워드와 컨텐트 보여주는 모드
	//private String studyMode;					//암기장 보여주는 모드
	private ArrayList<String> m_headWord;
	//private int playCount;							//옵션에서의 재생 회수
	//private int lastBook;							//이전에 봤던 사전의 종류	
	//private int studyTime;							//단어장에서의 옵션 정보
	//private String studyRepeat;					//상동
	//private String lastSound;						//이전에 재생됐던 놈.
	
	private String dbPath;							//db의 패쓰
	private static Provider INSTANCE;					//singleton 패턴 적용을 위한 인스턴스
	
	//생성자
	private Provider()
	{
		dbPath = "/sdcard/imgtest/dictionary.db";
		
		try {
			// open dictioanry.db
			File dictionaryFile = null;
			if(dbPath != null) {				
				dictionaryFile = new File(dbPath);
				if(!dictionaryFile.exists()) {
					dictionaryFile = null;
					Log.d("Provider", "Dictionary files not exist");
				}else
					Log.d(TAG,"file exist");
			}
			/*if(dictionaryFile == null) {
				dictionaryFile = getContext().getDatabasePath("dictionary.db");
			}*/
			try {
				dictionaryDB = SQLiteDatabase.openDatabase(dictionaryFile.getPath(), null, 
						SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			} catch (Exception e) {
				
			}
//			if(dictionaryDB == null) {
//				
//			}
//
//			Cursor cursor = dictionaryDB.query("meta_data", null, null, null, null, null, null);
//			cursor.moveToFirst();
//			entrySize = cursor.getInt(cursor.getColumnIndex("entry_size"));
//			String sounds = cursor.getString(cursor.getColumnIndex("sounds"));
//		 
//			cursor.close();
		}catch(Exception e){	
		
		}
		String [] headword = {"headword"};
		m_headWord = new ArrayList<String>();
		
		int start=0, end=1000;
		
		while(true){
			
			String select="entry_id >="+String.valueOf(start)+" AND entry_id <"+String.valueOf(end);
			Cursor cursor = dictionaryDB.query("content", headword, select, null, null, null, null);
			if(!cursor.moveToFirst()) break; 
			do{	
				m_headWord.add(cursor.getString(0));
			}while(cursor.moveToNext());			
			
			if(cursor.getCount()<1000) {
				cursor.close();
				break;
			}
			cursor.close();
			start=end;
			end +=1000;						
		}
	}
	
	static public Provider getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Provider();
		}else {
			Log.e("Provider","Provider Instance already obtained by one of other instances. It should be obtained only one in an application");
			System.exit(0);
		}
		return INSTANCE;
	}
}
