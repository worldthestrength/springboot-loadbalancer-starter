package com.itjcloud.common.loadbalancer.configure;

import com.itjcloud.common.loadbalancer.core.RoutingPolicy;
import com.itjcloud.common.loadbalancer.utils.LoadThreadLocalUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 拦截器, 把请求头放到本地线程变量, 在feign调用的时候进行传递.
 * gateway默认会转发所有请求参数, 我已gateway不需要这个服务
 *
 * @author ygzh
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"gatewayConfigurationService"})
public class LoadConfigFilter implements Filter {

    /**
     * 注入请求头处理功能, 根据RoutingPolicy setHeaders方法设置请求头到项目中
     */
    @Autowired
    private List<RoutingPolicy> routingPolicy;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //获取当前策略需要传递的请求头
        Map<String, String> headers =
            routingPolicy.stream().map(item -> item.setHeaders(httpServletRequest)).flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //设置到 本地线程变量中
        LoadThreadLocalUtils.set(headers);
        filterChain.doFilter(servletRequest, servletResponse);
        //清除本地线程变量中数据
        LoadThreadLocalUtils.remove();
    }


}