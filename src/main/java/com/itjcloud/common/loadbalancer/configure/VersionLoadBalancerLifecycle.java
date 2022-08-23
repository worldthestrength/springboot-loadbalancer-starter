package com.itjcloud.common.loadbalancer.configure;

import com.itjcloud.common.loadbalancer.utils.LoadThreadLocalUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.CompletionContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


/**
 * 负载均衡之前把获取的参数放到请求头中
 *
 * @author ygzh
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"gatewayConfigurationService"})
public class VersionLoadBalancerLifecycle implements LoadBalancerLifecycle<RequestDataContext, Object, Object> {
    @Override
    public void onStart(Request request) {
        Object context = request.getContext();
        if (context instanceof RequestDataContext) {
            RequestDataContext dataContext = (RequestDataContext) context;
            Map<String, String> load = LoadThreadLocalUtils.get();
            if (load == null) {
                return;
            }
            HttpHeaders headers = dataContext.getClientRequest().getHeaders();
            log.info("打印传递的请求头{}", headers);
            dataContext.getClientRequest().getHeaders().setAll(load);

        }
    }

    /**
     * 负载云横
     *
     * @param request    the {@link Request} that has been used by the LoadBalancer to select
     *                   a service instance
     * @param lbResponse the {@link Response} returned by the LoadBalancer
     */
    @Override
    public void onStartRequest(Request request, Response lbResponse) {

    }

    @Override
    public void onComplete(CompletionContext completionContext) {

    }
}