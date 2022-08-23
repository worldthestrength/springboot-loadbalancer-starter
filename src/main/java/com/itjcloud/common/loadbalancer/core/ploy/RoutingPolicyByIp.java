package com.itjcloud.common.loadbalancer.core.ploy;


import com.itjcloud.common.loadbalancer.configure.MulEnvServiceInstanceListSupplier;
import com.itjcloud.common.loadbalancer.core.RoutingPolicy;
import com.itjcloud.common.loadbalancer.core.pojo.LoadConstant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 根据id过滤器实现类
 *
 * @author ygzh
 */
@Slf4j
public class RoutingPolicyByIp implements RoutingPolicy {


    private static String getIp(Object requestContext) {
        if (requestContext == null) {
            return null;
        }
        String ip = null;
        if (requestContext instanceof RequestDataContext) {
            ip = getHintFromHeader((RequestDataContext) requestContext);
        }
        return ip;
    }

    private static String getHintFromHeader(RequestDataContext context) {
        if (context.getClientRequest() != null) {
            HttpHeaders headers = context.getClientRequest().getHeaders();
            if (headers != null) {
                //获取 key为version的版本号
                return headers.getFirst(LoadConstant.IP.toString());
            }
        }
        return null;
    }

    @Override
    public List<ServiceInstance> filtered(List<ServiceInstance> instances, Request<?> request) {
        String ip = getIp(request.getContext());
        //如果为空,不做处理直接返回
        if (!StringUtils.hasText(ip)) {
            return instances;
        }

        //优先匹配版本号, 如果版本号不存在优先匹配测试环境携带稳定标识的类
        List<ServiceInstance> filteredInstances =
            instances.stream().filter(serviceInstance -> serviceInstance.getHost().equals(ip)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filteredInstances)) {
            return filteredInstances;
        }
        //如果版本号不存在,只打印异常不阻塞微服务调用链路
        log.error("类{}:136 中找不到符合IP的服务,当前ip为{}", MulEnvServiceInstanceListSupplier.class, ip);
        return instances;
    }

    @Override
    public Map<String, String> setHeaders(HttpServletRequest httpServletRequest) {
        String version = httpServletRequest.getHeader(LoadConstant.IP.toString());
        Map<String, String> headers = new HashMap<>(16);
        if (version != null) {
            headers.put(LoadConstant.IP.toString(), version);
        }
        return headers;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}