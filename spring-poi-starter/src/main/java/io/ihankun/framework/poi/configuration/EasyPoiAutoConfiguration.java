package io.ihankun.framework.poi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.BeanNameViewResolver;

/**
 * @author hankun
 **/
@Configuration
@EnableConfigurationProperties(EasyPoiProperties.class)
@ConditionalOnProperty(prefix = "easy.poi.base", name = "enable", matchIfMissing = true)
public class EasyPoiAutoConfiguration {


    @Autowired
    private EasyPoiProperties easyPoiProperties;


    /**
     * 通过 order 属性来定义视图解析器的优先级, order 值越小优先级越高
     * @return resolver 视图跳转
     */
    @Bean
    @ConditionalOnMissingBean
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        resolver.setOrder(10);
        return resolver;
    }


}
