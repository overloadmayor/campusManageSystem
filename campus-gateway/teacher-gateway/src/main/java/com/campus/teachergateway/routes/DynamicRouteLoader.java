//com.hmall.gateway.routers.DynamicRouteLoader
package com.campus.teachergateway.routes;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteLoader {
    private final NacosConfigManager nacosConfigManager;

    private final String dataId="teacher-gateway-routes.json";
    private final String group="DEFAULT_GROUP";
    private final RouteDefinitionWriter writer;
    private final Set<String> routeIds=new HashSet<>();

    @PostConstruct//在容器初始化后执行
    public void initRouteConfigListener() throws NacosException {
        //项目启动时，先拉取一次配置，并且添加配置监听器
        String configInfo = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        //监听到配置变更，需要去更新路由表
                        updateConfigInfo(configInfo);
                    }
                });
        //第一次读取到配置，也需要更新到路由表
        updateConfigInfo(configInfo);
    }

    private void updateConfigInfo(String configInfo) {
        log.debug("监听到路由配置信息:{}",configInfo);
        //1.解析配置信息，转为RouteDefinition

        List<RouteDefinition> routeDefinition = JSONUtil.toList(configInfo, RouteDefinition.class);
        //2.删除旧的路由表
        for (String routeId : routeIds) {
            writer.delete(Mono.just(routeId)).subscribe();
        }
        //3.更新路由表
        for (RouteDefinition definition : routeDefinition) {
            //更新路由表
            writer.save(Mono.just(definition)).subscribe();
            //记录路由id，便于下一次更新时删除
            routeIds.add(definition.getId());
        }
    }
}
