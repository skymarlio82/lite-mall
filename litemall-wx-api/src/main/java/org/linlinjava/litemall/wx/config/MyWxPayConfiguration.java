
package org.linlinjava.litemall.wx.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MyWxPayProperties.class)
public class MyWxPayConfiguration {

    private final MyWxPayProperties properties;

    public MyWxPayConfiguration(MyWxPayProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MyWxPayService myWxPayService() {
    	return new MyWxPayService(properties);
    }
}