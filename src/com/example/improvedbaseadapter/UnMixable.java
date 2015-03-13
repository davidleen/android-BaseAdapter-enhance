package com.example.improvedbaseadapter;


/** empty interface   for not to do code mix . 
 * 空接口 仅仅表明
 * 任何实现该接口的类 其成员变量不参与 代码混肴  
 * -keep interface com.nd.hairdressing.UnMixable
 * -keepclassmembernames class * implements com.nd.hairdressing.UnMixable
 * {
 * *;
 * }
 * 
 * 
 * 实现该接口的任何类， 其成员名称（变量名称 TextView tv_name） 必须和android 界面元素标识(R.id.tv_name)一致
 * 方可使用
 * 
 * @see ViewUtil 配合使用
 * 
 * @author davidleen29
 * @创建时间 2013年11月1日
 */
public interface UnMixable {

}
