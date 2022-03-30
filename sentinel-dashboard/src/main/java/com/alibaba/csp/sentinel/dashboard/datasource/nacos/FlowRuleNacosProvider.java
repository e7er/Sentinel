package com.alibaba.csp.sentinel.dashboard.datasource.nacos;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.nacos.api.config.ConfigService;

@Service
public class FlowRuleNacosProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {
    
    private static Logger logger = LoggerFactory.getLogger(FlowRuleNacosProvider.class);
    
    @Autowired
    private NacosPropertiesConfiguration nacosConfigProperties;
    
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private Converter<String, List<FlowRuleEntity>> converter;
    
    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        
        //定义dataId 应用名+固定后缀
        String dataId = new StringBuilder(appName).append(NacosConstants.DATA_ID_POSTFIX).toString();
        
        String rules = configService.getConfig(dataId, nacosConfigProperties.getGroupId(), 3000);
        
        logger.info("Pull FlowRule from Nacos Config : {}", rules);
        
        if(StringUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}