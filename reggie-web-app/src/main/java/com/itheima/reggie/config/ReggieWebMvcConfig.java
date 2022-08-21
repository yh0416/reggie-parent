package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import com.itheima.reggie.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ReggieWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    //指定静态资源
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
//    }
//    //mvc框架的消息转换,前后端使用单位不一样,不换算会有精度损失
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//         //创建消息转换器对象
//        MappingJackson2HttpMessageConverter messageConverter =
//                new MappingJackson2HttpMessageConverter();
//        //设置对象转换器,底层使用jackson将对象转为json
//        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        //将消息转换器加入mvc转换器集合中
//        converters.add(0,messageConverter);
//    }
    //LoginCheckInterceptor的对应配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> urls = new ArrayList<>();
        urls.add("/error");
        urls.add("/user/sendMsg");
        urls.add("/user/login");
        urls.add("/front/**");

        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns(urls);

    }

    //设置静态资源映射
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //当访问请求是/front/**时,去classpath:/front/寻找对应资源
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    //扩展mvc框架的消息转换器
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }
}
