package com.example.richtest;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaticTools {
	
	 //4��<a href="2">����</a><br/>
	//<u><comment id=1>��ǰ</comment></u>\n
	public static float phoneScale = 0.35f;
	public static String andTOios(String and){
		
		String s1 = and.replaceAll("<p dir=\"ltr\">","");
		String s2 = s1.replaceAll("</p>","");
		String s3 = s2.replaceAll("<a href=\"","<u><comment id=");
		String s4 = s3.replaceAll("</a>","</comment></u>");
		String s5 = s4.replaceAll("\">",">");
		String s6 = s5.replaceAll("<br>\n","\n");
		String s7 = s6.replaceAll("<br>","\n");//		String s6 = s5.replaceAll("<br />","\n");
		return s7;
	}
	
	//<u><comment id=1>��ǰ</comment></u>\n
    //4��<a href="2">����</a><br/>
   public static String iosTOand(String ios){

	   	String s1 = ios.replaceAll("<u><comment id=","kengchikuachai");
		String s2 = s1.replaceAll("</comment></u>","kuachaikengchi");
		String s11 = s2.replaceAll("<comment id=","kengchikuachai");
		String s22 = s11.replaceAll("</comment>","kuachaikengchi");
		String s3 = s22.replaceAll("\n","huanhangfu");
		String s4 = s3.replaceAll(">","\">");
		String s5 = s4.replaceAll("kengchikuachai","<a href=\"");
		String s6 = s5.replaceAll("kuachaikengchi","</a>");
		String s7 = s6.replaceAll("huanhangfu","<br>");
		return s7;
	}
//	String s4 = s3.replaceAll("</comment>","</font></a>");
   
   
   @SuppressLint("SimpleDateFormat")
public static String getTimeformat(String fmt){

	   SimpleDateFormat sdf = new SimpleDateFormat(fmt);
	   Date date=new Date();
	   String dateStr = sdf.format(date);

	   return dateStr;
   }
}









        
