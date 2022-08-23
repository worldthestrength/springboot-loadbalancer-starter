package com.itjcloud.common.loadbalancer;


import com.itjcloud.common.loadbalancer.configure.LoadStartConfiguration;
import org.springframework.context.annotation.Import;

/**
 * start组件类
 * <p>
 * 当环境为:
 * <ul>
 *  <li>dev</li>
 *  <li>test</li>
 *  <li>local</li>
 * </ul>
 * 生效
 *
 * @author classgzh
 */

@Import(LoadStartConfiguration.class)
public class LoadbalancerConfiguration {


}
