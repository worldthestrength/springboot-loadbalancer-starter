package com.itjcloud.common.loadbalancer.core.ploy;

import com.itjcloud.common.loadbalancer.core.RoutingPolicy;
import com.itjcloud.common.loadbalancer.core.pojo.LoadConstant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
 * 根据版本进行匹配
 * <br>
 * 服务治理版本管理策略中,会优先走指定版本
 * <p>
 * 例如:
 * <li>v1的流量优先调用v1版本的微服务，如果找不到v1版本的微服务时，要调用基准版本的微服务</li>
 * <li>v2的流量优先调用v2版本的微服务，如果找不到v2版本的微服务时，要调用基准版本的微服务。</li>
 * </P>
 *
 * @author ygzh
 */
@Slf4j
public class RoutingPolicyByVersion implements RoutingPolicy {
    private List<ServiceInstance> getServiceInstances(List<ServiceInstance> instances, String version) {
        //筛选符合条件的服务
        List<ServiceInstance> filteredInstances =
            instances.stream().filter(serviceInstance -> serviceInstance
                    .getMetadata()
                    .getOrDefault(LoadConstant.VERSION.toString().toLowerCase(), "")
                    .equals(version))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filteredInstances)) {
            return filteredInstances;
        }


        //如果版本号不存在,只打印异常不阻塞微服务调用链路
        log.error("服务{} 中找不到符合版本号的服务,当前版本号为{}",
            instances.stream().findFirst().map(ServiceInstance::getServiceId).orElse("服务列表不存在"), version);
        return Collections.emptyList();
    }


    @Override
    public List<ServiceInstance> filtered(List<ServiceInstance> instances, Request<?> request) {

        String version = this.getVersion(request.getContext());

        //判断是否为空,如果为空优先匹配基准服务
        if (!StringUtils.hasText(version)) {
            return Optional.ofNullable(getServiceInstances(instances, LoadConstant.DEFAULT.toString()))
                .filter(item -> !CollectionUtils.isEmpty(item)).orElse(instances);
        }

        List<ServiceInstance> filteredInstances = getServiceInstances(instances, version);
        if (!CollectionUtils.isEmpty(filteredInstances)) {
            return filteredInstances;
        }

        List<ServiceInstance> serviceInstances = getServiceInstances(instances, LoadConstant.DEFAULT.toString());
        if (!CollectionUtils.isEmpty(serviceInstances)) {
            return serviceInstances;
        }
        return instances;
    }

    @Override
    public Map<String, String> setHeaders(HttpServletRequest httpServletRequest) {

        String version = httpServletRequest.getHeader(LoadConstant.VERSION.toString());
        Map<String, String> headers = new HashMap<>();
        if (version != null) {
            headers.put(LoadConstant.VERSION.toString(), version);
        }
        return headers;
    }

    private String getVersion(Object requestContext) {
        if (requestContext == null) {
            return null;
        }
        String version = null;
        if (requestContext instanceof RequestDataContext) {
            version = getHintFromHeader((RequestDataContext) requestContext);
        }
        return version;
    }

    private String getHintFromHeader(RequestDataContext context) {
        if (context.getClientRequest() != null) {
            HttpHeaders headers = context.getClientRequest().getHeaders();
            if (headers != null) {
                //获取 key为version的版本号
                return headers.getFirst(LoadConstant.VERSION.toString());
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
