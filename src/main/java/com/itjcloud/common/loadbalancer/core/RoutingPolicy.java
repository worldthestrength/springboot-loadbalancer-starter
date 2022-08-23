package com.itjcloud.common.loadbalancer.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 微服务拦截转发的
 *
 * @author ygzh
 */
public interface RoutingPolicy extends Ordered {

  /**
   * 实现筛选实例的逻辑
   *
   * @param instances 路由列表
   * @param request   请求头
   * @return 根据自己条件筛选的数据
   */
  List<ServiceInstance> filtered(List<ServiceInstance> instances, Request<?> request);


  /**
   * 从请求头李设置数据到headers
   *
   * @param httpServletRequest            设置数据
   * @param httpServletRequest 接受的数据
   * @return list
   */
  Map<String, String> setHeaders(HttpServletRequest httpServletRequest);










}
