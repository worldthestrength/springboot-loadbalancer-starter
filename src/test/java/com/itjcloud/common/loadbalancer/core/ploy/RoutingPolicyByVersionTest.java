package com.itjcloud.common.loadbalancer.core.ploy;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

/**
 * @author ZhangYinGang
 */
class RoutingPolicyByVersionTest {


    @Test
    void filtered() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("111111");
        List<Object> objects1 = Optional.of(Collections.emptyList())
            .filter(item -> !CollectionUtils.isEmpty(item)).orElse(objects);
        System.out.println(objects1);
        Assertions.assertEquals(objects, objects1);

    }

}