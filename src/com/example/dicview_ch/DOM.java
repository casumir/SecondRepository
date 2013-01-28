package com.example.dicview_ch;

import java.io.*;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;




public class DOM {
	static Document docu;
	public static void main(String[] args) throws SAXException, IOException{
		// DOM파서를 읽어주는 객체 선언
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
			//dom문서를 읽고 해석 파싱해주는 객체 선언
			DocumentBuilder parse = factory.newDocumentBuilder();
			
			File file = new File("DF.xml");
			InputStream in = new FileInputStream(file);
			//xml문서 파싱
			Document doc = parse.parse(in);
			// 루트엘리먼트 추출
			Element element = doc.getDocumentElement();
			
			// 엘리먼트에서 entry라는 내임태그를 기준으로한 노드리스트 추출
			NodeList nlist = element.getElementsByTagName("entry");
			
			for(int i=0;i<nlist.getLength();i++){
				Element name = (Element)nlist.item(i);
				
				// 엘리먼트의 요소를 추출 NodeList의 아이템의 i번째 오브젝트를 추출
				Text text = (Text)name.getFirstChild();
				System.out.println(text.getData());
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
