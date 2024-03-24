package com.hzvtc.myproject.config;

import com.hzvtc.myproject.interceptor.LoginInterceptor;
import com.hzvtc.myproject.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {
    private final SecurityInterceptor securityInterceptor;

    private final LoginInterceptor loginInterceptor;

    public MyConfig(SecurityInterceptor securityInterceptor, LoginInterceptor loginInterceptor) {
        this.securityInterceptor = securityInterceptor;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截器
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/imgs/**", "/js/**", "/css/**", "/login/**"
                        , "/login.html", "/error", "/upload_img/**").order(0);

        //权限拦截器
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/imgs/**", "/js/**", "/css/**", "/login/**"
                        , "/login.html", "/error", "/upload_img/**","/permission/**").order(1);
    }


    /**
     * 跨域
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(false);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

    /**
     * 外部资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload_img/**")
                .addResourceLocations("file:" + Constant.UPLOAD_PATH);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
