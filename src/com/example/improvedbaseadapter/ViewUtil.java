package com.example.improvedbaseadapter;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * a helper class
 * 
 * use java reflect to inject view to the obj whose member has the same name to
 * the subViewId(R.id.name)
 * 利用java反射功能 执行控件注射
 * 
 * @author davidleen29
 * 
 */

public class ViewUtil {

	private static final String TAG = "ViewUtil";

	/**
	 * inflate view  by class annotation.  and do the field inject work.
	 *
	 *
	 *	根据配置的对象  自动生成View工具。
	 * 
	 * @param obj
	 *            所传递的对象 必须实现UnMixable接口 以避免代码混搅过程中 影响java 反射功能使用。
	 * @param context
	 */
	public static final View getViewByAnnotate(UnMixable obj, Context context) {


		Class<?> aClass = obj.getClass();
		ResId layoutResId=aClass.getAnnotation(ResId.class);
		if(layoutResId==null||layoutResId.value()<0)
		{
			//throw new exception
			Log.e(TAG,
			"class:"
					+ aClass


					+ "  no found the relative layout id for the viewId    ");
			return null;
		}

		View v= LayoutInflater.from(context).inflate(layoutResId.value(),null);

		injectByFieldAnnotate(obj,aClass,v);





		return v;
	}


	/**
	 * do inject work by field annotation.
	 * 执行View 与 控件索引对象 关联
	 *
	 * 执行注入的对象
	 * @param object
	 * @param v
	 */
	public  static void injectByFieldAnnotate(Object object,View v)
	{

			injectByFieldAnnotate(object,object.getClass(),v);

	}


	public  static void injectByFieldAnnotate(Object obj,Class<?> aClass,View v)
	{
		while (aClass != null) {// 循环往上查询成员变量

			Field[] fields = aClass.getDeclaredFields();

			for (Field field : fields) {
				if (BuildConfig.DEBUG)
					Log.i(TAG, "field:" + field.getName());

				field.setAccessible(true);

				ResId resId=field.getAnnotation(ResId.class);
				if(resId==null||resId.value()<0)
				{


//					if (BuildConfig.DEBUG)
//						Log.e(TAG,
//								"class:"
//										+ aClass
//										+ ",field: "
//										+ field.getName()
//										+ "  no found the relative id for the field name  please ensure the field name ,it must equals to a id name ");
				} else {
					int id=resId.value();
					View fieldView = v.findViewById(id);
					if (fieldView == null) {
						if (BuildConfig.DEBUG)
							Log.e(TAG,
									"view :"
											+ v
											+ " ,does not  have the childView which id equals:["
											+ id+
										  "]" );
					} else
						try {
							field.set(obj, fieldView);

							//





						} catch (IllegalArgumentException e) {

							e.printStackTrace();
						} catch (IllegalAccessException e) {

							e.printStackTrace();
						}
				}
				field.setAccessible(false);

			}
			aClass = aClass.getSuperclass();
		}
	}

}
