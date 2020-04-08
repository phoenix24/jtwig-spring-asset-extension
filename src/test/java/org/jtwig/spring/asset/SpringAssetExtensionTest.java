package org.jtwig.spring.asset;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.spring.asset.resolver.AssetResolver;
import org.jtwig.spring.asset.resolver.BaseAssetResolver;
import org.jtwig.spring.boot.config.JtwigViewResolverConfigurer;
import org.jtwig.web.servlet.JtwigRenderer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SpringAssetExtensionTest {
    @Before
    public void setUp() throws Exception {
        ExampleController.start();
    }

    @After
    public void tearDown() throws Exception {
        ExampleController.stop();
    }

    @Test
    public void integrationTest() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d", ExampleController.getPort()));
        CloseableHttpResponse result = httpClient.execute(httpGet);

        try (InputStream inputStream = result.getEntity().getContent()) {
            String resultAsString = IOUtils.toString(inputStream);

            assertThat(resultAsString, is("/public/css/test.css"));
        }
    }

    @Controller
    @Configuration
    @EnableAutoConfiguration
    @EnableWebMvc
    public static class ExampleController implements JtwigViewResolverConfigurer {
        private static ConfigurableApplicationContext applicationContext;

        public static void start() {
            applicationContext = SpringApplication.run(ExampleController.class, "--server.port", "0");
        }

        public static void stop () {
            applicationContext.stop();
        }

        public static int getPort () {
            return ((AnnotationConfigServletWebServerApplicationContext) ExampleController.applicationContext).getWebServer().getPort();
        }

        @RequestMapping("")
        public String index () {
            return "index";
        }

        @Override
        public void configure(JtwigViewResolver viewResolver) {
            viewResolver.setRenderer(new JtwigRenderer(
                    EnvironmentConfigurationBuilder.configuration()
                            .extensions().add(new SpringAssetExtension()).and()
                            .build()
            ));
        }

        @Bean
        public AssetResolver assetResolver () {
            BaseAssetResolver assetResolver = new BaseAssetResolver();
            assetResolver.setPrefix("/public");
            return assetResolver;
        }
    }
}