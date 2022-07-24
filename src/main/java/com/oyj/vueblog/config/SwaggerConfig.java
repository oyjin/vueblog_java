package com.oyj.vueblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    // 设置多个:
//     @Bean
//     public Docket appApi() {
//         List<Parameter> pars = new ArrayList<>();
//         ParameterBuilder token = new ParameterBuilder();
//         token.name("token").description("用户令牌").modelRef(new ModelRef("string")).parameterType("header").required(false)
//                 .build();
//         pars.add(token.build());
//         return new Docket(DocumentationType.SWAGGER_2).select().paths(regex("/app/.*")).build()
//                 .globalOperationParameters(pars).apiInfo(pdaApiInfo()).useDefaultResponseMessages(false)
//                 .enable(enableSwagger)
//                 .groupName("appApi");
//     }
//
//     @Bean
//     public Docket adminApi() {
//         List<Parameter> pars = new ArrayList<>();
//         ParameterBuilder token = new ParameterBuilder();
//         token.name("token").description("用户令牌").modelRef(new ModelRef("string")).parameterType("header").required(false)
//                 .build();
//         pars.add(token.build());
//         return new Docket(DocumentationType.SWAGGER_2).select().paths(regex("/admin/.*")).build()
//                 .globalOperationParameters(pars).apiInfo(pdaApiInfo()).useDefaultResponseMessages(false)
//                 .enable(enableSwagger)
//                 .groupName("adminApi");
//     }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否开启 (true 开启  false隐藏。生产环境建议隐藏)
                .enable(true)
                .select()
                // 扫描的路径包,设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.oyj.vueblog.controller"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any())
                .build().globalOperationParameters(setHeaderToken());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题(API名称)
                .title("swagger2接口文档")
                // 文档描述
                .description("swagger接口说明")
                // 服务条款URL
                .termsOfServiceUrl("")
                // 版本号
                .version("1.0")
                .build();
    }

    /**
     * @Description: 设置swagger文档中全局参数
     * 设置了一个setToken方法，代表生成文档的所有接口中，都要包含一个header类型的token参数
     * .required(false)表示该token参数不是必须的，如果想让所有接口的请求头中都必须带上参数可以设置为true
     * @param
     * @Date: 2022/7/24
     * @return: java.util.List<springfox.documentation.service.Parameter>
     */
    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder userId = new ParameterBuilder();
        userId.name("Authorization").description("用户TOKEN").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(userId.build());
        return pars;
    }
}
