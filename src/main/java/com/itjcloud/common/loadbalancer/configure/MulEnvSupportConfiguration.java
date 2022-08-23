package com.itjcloud.common.loadbalancer.configure;


import com.itjcloud.common.loadbalancer.core.RoutingPolicyFiltration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 用来实例化对象到spring 容器中的配置类
 *
 * @author ygzh
 * @see MulEnvServiceInstanceListSupplier
 */
@LoadBalancerClients(defaultConfiguration = MulEnvSupportConfiguration.class)
public class MulEnvSupportConfiguration {

    /**
     * 把服务策略注入到spring中
     *
     * @param context 上下文
     * @return 服务
     */
    @Bean
    public ServiceInstanceListSupplier mulEnvServiceInstanceListSupplier(ConfigurableApplicationContext context,
                                                                         RoutingPolicyFiltration routingPolicy) {
        ServiceInstanceListSupplier base = ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().build(context);

        //配置动态实例列表
        MulEnvServiceInstanceListSupplier mulEnv = new MulEnvServiceInstanceListSupplier(base, routingPolicy);
        //.withBase(mulEnv)
        return ServiceInstanceListSupplier.builder().withBase(mulEnv).build(context);
    }


}