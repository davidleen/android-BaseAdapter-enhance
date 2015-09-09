
package com.example.improvedbaseadapter.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.util.Log;

import com.example.improvedbaseadapter.BuildConfig;

/**
 * 算法类
 * 
 * @author david
 */
public class ArrayUtils {
    private static final String TAG = ArrayUtils.class.getName();

    /**
     * 封装 对列表数据进行排序
     * @param <T>
     * 
     * @param <T>
     * @param datas
     * @param comparator
     */

    @SuppressWarnings("unchecked")
    public static   <T>    void  SortList(List<T>  datas, Comparator<T>  comparator) {

        if (datas == null || comparator == null)
            return;

        int size = datas.size();
        T[] array = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = datas.get(i);
        }

        Arrays.sort(array, comparator);

        datas.clear();
        for (int i = 0; i < size; i++) {
            datas.add(array[i]);
        }

        if (BuildConfig.DEBUG)
            for (int i = 0; i < datas.size(); i++) {
                Log.v(TAG, "DATA:" + datas.get(i));
            }

    }

    /**
     * 封装 由数组转换成 列表
     * 
     * @param <T>
     */

    public static <T> List<T> changeArrayToList(T[] array) {

        List<T> list = new ArrayList<T>();

        if (array != null && array.length > 0) {
            for (T data : array) {
                list.add(data);
            }
        }

        return list;

    }
    
    /**
     * 封装 由列表转换成数组
     * 
     * @param <T>

     */

    public static <T> T[]    changeArrayToList(List<T> list) {

        int size=list.size();
        T[] result=(T[])new Object[size];

        
        for (int i = 0; i < size; i++) {
            result[i]=   list.get(i);
        }
        

        return result;

    }
   
}
