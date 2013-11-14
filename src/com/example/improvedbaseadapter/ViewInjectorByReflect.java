package com.example.improvedbaseadapter;

import java.lang.reflect.Field;

import android.util.Log;
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

public class ViewInjectorByReflect {

	private static final String TAG = "ViewInjectorByReflect";

	/**
	 * 执行View 与 控件索引对象 关联
	 * 
	 * 执行注入的对象 成员名称 必须与 View 的子控件的 id 一致
	 * 
	 * @param UnMixable
	 *            所传递的对象 必须实现UnMixable接口 以避免代码混搅过程中 影响java 反射功能使用。
	 * @param v
	 */
	public static final void injectView(UnMixable obj, View v) {

		Class<?> aClass = obj.getClass();

		while (aClass != null) {// 循环往上查询成员变量

			Field[] fields = aClass.getDeclaredFields();

			for (Field field : fields) {
				if (BuildConfig.DEBUG)
					Log.i(TAG, "field:" + field.getName());

				field.setAccessible(true);
				int id = v.getResources().getIdentifier(field.getName(), "id",
						v.getContext().getPackageName());
				if (id <= 0) {
					if (BuildConfig.DEBUG)
						Log.e(TAG,
								"class:"
										+ aClass
										+ ",field: "
										+ field.getName()
										+ "  no found the relative id for the field name  please ensure the field name ,it must equals to a id name ");
				} else {
					View fieldView = v.findViewById(id);
					if (fieldView == null) {
						if (BuildConfig.DEBUG)
							Log.e(TAG,
									"class:"
											+ aClass
											+ ",field: "
											+ field.getName()
											+ ",id"
											+ id
											+ " not a viewItemId from the currentView ");
					} else
						try {
							field.set(obj, fieldView);
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
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "obj:" + obj.toString());
		}

	}

}
