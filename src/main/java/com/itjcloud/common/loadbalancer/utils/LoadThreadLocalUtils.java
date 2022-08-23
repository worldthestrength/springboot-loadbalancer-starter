package com.itjcloud.common.loadbalancer.utils;


import java.util.Map;

/**
 * 线程变量工具类
 *
 * @author ygzh
 */
public class LoadThreadLocalUtils {

  private static final ThreadLocal<Map<String, String>> LOAD_VERSION_THREAD_LOCAL = new ThreadLocal<>();


  /*
    如果为空,设置默认值
   */

  /**
   * 清除线程变量
   */
  public static void remove() {

    LOAD_VERSION_THREAD_LOCAL.remove();

  }

  /**
   * 设置线程变量
   *
   * @param map 变量
   */
  public static void set(Map<String, String> map) {
    LOAD_VERSION_THREAD_LOCAL.set(map);
  }


/*
  public static void add(String key, String value) {
    Map<String, String> stringStringMultiValueMap = LOAD_VERSION_THREAD_LOCAL.get();
    if (stringStringMultiValueMap == null) {
      stringStringMultiValueMap = new HashMap<>(16);
    }
    stringStringMultiValueMap.put(key, value);
    set(stringStringMultiValueMap);
  }
*/


  /**
   * 获取线程变量
   *
   * @return 对象
   */
  public static Map<String, String> get() {
    return LOAD_VERSION_THREAD_LOCAL.get();
  }

}
