package com.cyh.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiPortConfig {
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        // 添加额外连接器（端口8081）
        factory.addAdditionalTomcatConnectors(createAdditionalConnector());
        return factory;
    }

    private Connector createAdditionalConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(9091);  // 设置第二个端口
        // 可选：配置其他参数，如协议、超时时间等
        // connector.setScheme("http");
        // connector.setRedirectPort(8443);
        return connector;
    }
}
