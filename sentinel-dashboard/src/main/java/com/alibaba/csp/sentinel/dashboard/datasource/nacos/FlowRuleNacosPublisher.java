package com.alibaba.csp.sentinel.dashboard.datasource.nacos;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.nacos.api.config.ConfigService;


@Service
public class FlowRuleNacosPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {
    
    private static Logger logger = LoggerFactory.getLogger(FlowRuleNacosPublisher.class);
    
    @Autowired
    private NacosPropertiesConfiguration nacosConfigProperties;
    
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private Converter<List<FlowRuleEntity>, String> converter;

    @Override
    public void publish(String appName, List<FlowRuleEntity> rules) throws Exception {
        
        if(StringUtils.isBlank(appName)) {
            logger.error("传入的AppName为Null");
            return ;
        }
        
        if(null == rules) {
            logger.error("传入的流控规则数据为null");
            return ;
        }
        
        String dataId = new StringBuilder(appName).append(NacosConstants.DATA_ID_POSTFIX).toString();
        
        configService.publishConfig(dataId, nacosConfigProperties.getGroupId(), converter.convert(rules));
        
    }
}