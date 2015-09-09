package com.example.improvedbaseadapter.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/********************************* 电话短信邮件功能类 ***************************************/

/********************************* 电话短信邮件功能类 ***************************************/

public class DialMessageUtil {
	public static String PHONE_GS = "0591-87522311";

	/**
	 * 拨打电话
	 * 
	 * @param context
	 * @param name
	 * @param mobile
	 */
	public static void dial(final Context context, final String mobile) {

		
		// Intent.ACTION_DIAL Intent.ACTION_CALL 区别在与 dial 移交给拨号程序，默认显示传递的号码
		// call 直接拨打
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ mobile));
		context.startActivity(intent);

	}


	/**
	 * 发送信息
	 * 
	 * @param context
	 * @param mobile
	 */
	public static void sendMessage(Context context, String mobile,String body) {
		Uri uri = Uri.parse("smsto:" + mobile);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body",body);
		context.startActivity(it);

//		if (false) {
//			// 直接发送信息
//			String smsContent = "102";
//			// note: SMS must be divided before being sent
//			SmsManager sms = SmsManager.getDefault();
//			List<String> texts = sms.divideMessage(smsContent);
//			for (String text : texts) {
//				sms.sendTextMessage(mobile, null, text, null, null);
//			}
//			// note: not checked success or failure yet
//			Toast.makeText(context, "短信已发送", Toast.LENGTH_SHORT).show();
//		}
	}

	/**
	 * 发送邮件
	 * 
	 */
	public static void sendEmail(Context context, String[] emails, String text,
			String subject, String textType) {

		// Create a new Intent to send messages
		StringBuilder sb = new StringBuilder();
		for (int i = 0, count = emails.length; i < count; i++) {
			sb.append(emails[i]);
			if (i != count - 1) {
				sb.append(",");
			}

		}
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		// Write the body of theEmail

		// Add attributes to the intent
		if (textType == null || textType.trim().equals(""))
			sendIntent.setType("text/plain"); // use this line for testing
		else
			sendIntent.setType(textType); // use this line for testing
		// in the emulator
		// sendIntent.setType("message/rfc822"); // use this line for testing
		// on the real phone

		// String aEmailCCList[] = { "user3@fakehost.com", "user4@fakehost.com"
		// };
		// String aEmailBCCList[] = { "user5@fakehost.com" };
		// sendIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
		// sendIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);

		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, emails);

		context.startActivity(sendIntent);
	}

}
