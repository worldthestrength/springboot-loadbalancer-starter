package com.itjcloud.common.loadbalancer.configure;

import com.itjcloud.common.loadbalancer.core.RoutingPolicy;
import com.itjcloud.common.loadbalancer.core.RoutingPolicyFiltration;
import com.itjcloud.common.loadbalancer.core.ploy.RoutingPolicyByIp;
import com.itjcloud.common.loadbalancer.core.ploy.RoutingPolicyByVersion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 配置默认拦截策略
 * 1. ip过滤册罗
 * 2. 版本过滤策略
 *
 * @author ygzh
 */
@Configuration
@Import({MulEnvSupportConfiguration.class, LoadConfigFilter.class, VersionLoadBalancerLifecycle.class, RoutingPolicyFiltration.class})
@ConditionalOnExpression("'${spring.profiles.active}'.equals('dev') || '${spring.profiles.active}'.equals('test') || '${spring.profiles.active}'.equals('local')")
public class LoadStartConfiguration {

    @Bean
    @ConditionalOnProperty(value = "spring.cloud.nacos.env-isolation", havingValue = "true", matchIfMissing = true)
    public RoutingPolicy filteredByIp() {
        return new RoutingPolicyByIp();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.cloud.nacos.env-isolation", havingValue = "true", matchIfMissing = true)
    public RoutingPolicy filteredByVersion() {
        return new RoutingPolicyByVersion();
    }


}
