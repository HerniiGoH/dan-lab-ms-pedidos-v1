package utn.frsf.isi.dan.grupotp.tplab.danmspedidos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient webClient1(){
        return WebClient.create("http://localhost:4040/api/obra/");
    }
    @Bean
    public WebClient webClient2(){
        return WebClient.create("http://localhost:4042/api/producto/");
    }
    @Bean
    public WebClient webClient3(){
        return WebClient.create("http://localhost:4042/api/producto/");
    }
    @Bean
    public WebClient webClient4(){
        return WebClient.create("http://localhost:4042/api/producto/mvms-stock");
    }
    @Bean
    public WebClient webClient5(){
        return WebClient.create("http://localhost:4040/api/obra");
    }
}
