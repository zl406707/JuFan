package GengXin;

import java.io.File;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Xml;


	/**
	  *
	  * ��ȡ�������ĸ�������
	 *
	  * ��ǰ�汾
	 *
	  * apk��·��
	 *
	  * ����ϸ��
	 *
	  * @author dagang
	  *
	  */
	 public class UpdateTools {
	  /*
	   * ��pull�������������������ص�xml�ļ� (xml��װ�˰汾��)
	   */
	  public static UpdateEntity getUpdataInfo(InputStream is) throws Exception {
	   XmlPullParser parser = Xml.newPullParser();
	   parser.setInput(is, "utf-8");// ���ý���������Դ
	  int type = parser.getEventType();
	   UpdateEntity info = new UpdateEntity();// ʵ��
	  while (type != XmlPullParser.END_DOCUMENT) {
	    switch (type) {
	     case XmlPullParser.START_TAG:
	      if ("version".equals(parser.getName())) {
	       info.setVersion(parser.nextText()); // ��ȡ�汾��
	     } else if ("url".equals(parser.getName())) {
	       info.setUrl(parser.nextText()); // ��ȡҪ������APK�ļ�
	     } else if ("description".equals(parser.getName())) {
	       info.setDescription(parser.nextText()); // ��ȡ���ļ�����Ϣ
	     }
	      break;
	    }
	    type = parser.next();
	   }
	   return info;
	  }

	 /**
	   *
	   * ��װApk
	   *
	   * @param file
	   * @param context
	   */
	  public void installApk(File file,Context context) {
	   Intent intent = new Intent();
	   //ִ�ж���
	  intent.setAction(Intent.ACTION_VIEW);
	   //ִ�е���������
	  intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
	   context.startActivity(intent);
	  }
	 }


