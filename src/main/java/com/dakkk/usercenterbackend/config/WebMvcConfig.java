package com.dakkk.usercenterbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebMvcConfig
 * Package: com.dakkk.usercenterbackend.config
 *
 * @Create 2024/4/16 20:06
 * @Author dakkk
 * Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置运行跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                //当 Credentials为true时，Origin不能为*号，需要为具体的ip地址 【如果接口不带Cookies,ip无需设置成具体的ip】
                // .allowedOrigins("http://203.195.193.58")
                .allowedOriginPatterns("http://203.195.193.58")
                // 是否允许证书 不在默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }
}
