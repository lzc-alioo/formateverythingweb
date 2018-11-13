//package com.alioo.format.web.config;
//
//import com.google.common.base.Predicate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.util.UriComponentsBuilder;
//import springfox.documentation.PathProvider;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.paths.AbstractPathProvider;
//import springfox.documentation.spring.web.paths.Paths;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.google.common.base.Predicates.or;
//import static springfox.documentation.builders.PathSelectors.regex;
//
///**
// * Swagger2 配置<br/>
// *
// * @author miaohongbin
// * @version Date: 2017/8/11 下午2:49 <br/>
// */
//@Configuration
//@EnableSwagger2
//public class Swagger2Configuration {
//
//    @Value("${swagger.authentication.token}")
//    private String swaggerToken;
//
//    @Value("${swagger.service.basePath}")
//    private String basePath;
//
//    @Value("${spring.profiles}")
//    private String profiles;
//
////    @Bean
////    public Docket apis() {
////        Predicate<String> paths = null;
////        if ("prod".equals(profiles)) {
////            paths = or(
////                    regex("/api/boards.*"),
////                    regex("/api/issues.*")
////            );
////        } else {
////            paths = or(
////                    regex("/api/.*")
////            );
////        }
////        return createDocket("api-based", paths)
////                .globalOperationParameters(getAuthenticationParam());
////    }
//
//    @Bean
//    public Docket as() {
//        if ("prod".equals(profiles)) {
//            return null;
//        }
//        Predicate<String> paths = or(regex("/a/.*"));
//        return createDocket("a-based", paths);
//    }
//
//    @Bean
//    public Docket indexs() {
//        if ("prod".equals(profiles)) {
//            return null;
//        }
//        Predicate<String> paths = or(regex("/index/.*"));
//        return createDocket("index-test", paths);
//
//    }
//
//
//    private List<Parameter> getAuthenticationParam() {
//        List<Parameter> parameters = new ArrayList<>(2);
//        Parameter parameter = new ParameterBuilder()
//                .name("OneoffSecret")
//                .defaultValue(swaggerToken)
//                .parameterType("header")
//                .description("authorization token")
//                .required(true)
//                .modelRef(new ModelRef("string"))
//                .build();
//        Parameter appIdParameter = new ParameterBuilder()
//                .name("AppId")
//                .defaultValue("")
//                .parameterType("header")
//                .description("authorization AppId")
//                .required(true)
//                .modelRef(new ModelRef("string"))
//                .build();
//        Parameter usernameParameter = new ParameterBuilder()
//                .name("username")
//                .defaultValue("")
//                .parameterType("header")
//                .description("当前用户域帐号")
//                .required(true)
//                .modelRef(new ModelRef("string"))
//                .build();
//        parameters.add(usernameParameter);
//        parameters.add(appIdParameter);
//        parameters.add(parameter);
//        return parameters;
//    }
//
//    private Docket createDocket(String groupName, Predicate<String> selector) {
//        PathProvider myPathProvider = new BasePathAwareRelativePathProvider(basePath);
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName(groupName)
//                .apiInfo(apiInfo())
//                .pathProvider(myPathProvider)
//                .select()
//                .paths(selector)
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("formateverythingweb RESTful API")
//                .description("API文档-区域管理系统")
//                .version("1.0.0")
//                .termsOfServiceUrl("http://3-dev.intra.alioo.com/luodi")
//                .contact(contact())
//                .build();
//    }
//
//    //设置联系方式
//    private Contact contact() {
//        return new Contact("俞佳元（yujiayuan@alioo.com）、柳志崇（liuzhichong@alioo.com）、刘迪（liualioo@alioo.com）、苗红宾（miaohongbin@alioo.com）",
//                "http://wiki.intra.alioo.com/pages/viewpage.action?pageId=147046415",
//                "yujiayuan@alioo.com;liuzhichong@alioo.com;liualioo@alioo.com;miaohongbin@alioo.com");
//    }
//
//
//    private class BasePathAwareRelativePathProvider extends AbstractPathProvider {
//        private String basePath;
//
//        BasePathAwareRelativePathProvider(String basePath) {
//            this.basePath = basePath;
//        }
//
//        @Override
//        protected String applicationPath() {
//            return basePath;
//        }
//
//        @Override
//        protected String getDocumentationPath() {
//            return "/";
//        }
//
//        @Override
//        public String getOperationPath(String operationPath) {
//            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
//            return Paths.removeAdjacentForwardSlashes(
//                    uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
//        }
//    }
//
//}
