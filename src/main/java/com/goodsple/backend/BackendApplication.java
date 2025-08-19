package com.goodsple.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.goodsple")
@MapperScan(basePackages =     {
    "com.goodsple.features.admin.user.mapper",
    "com.goodsple.features.auction.mapper",
    "com.goodsple.features.auth.mapper",
    "com.goodsple.features.category.mapper",
    "com.goodsple.features.notice.mapper",
    "com.goodsple.features.report.mapper",
    "com.goodsple.features.exchange.mapper",
    "com.goodsple.features.community.mapper",

})
@EnableScheduling
@OpenAPIDefinition( // <<-- 이 어노테이션과 그 내용을 추가합니다.
        info = @Info(
                title = "GoodsPle API 명세서", // API 문서의 제목을 팀에서 정해주세요.
                version = "v1.0.0",             // API의 초기 버전을 명시합니다. (예: "v0.1", "v1.0")
                description = "GoodsPle 프로젝트에서 제공하는 전체 API에 대한 상세 명세입니다. " +
                        "이 문서를 통해 백엔드 API 사용법을 확인하고 개발을 진행할 수 있습니다.", // API 문서에 대한 설명을 자유롭게 작성해주세요.
                contact = @Contact( // 선택 사항: API 관련 문의처
                        name = "GoodsPle Team",
                        email = "goodsple2@example.com", // 팀 대표 이메일 또는 담당자 이메일
                        url = "https://project.example.com" // 프로젝트 관련 웹사이트 (선택)
                )
//                license = @License( // 선택 사항: API 라이선스 정보
//                        name = "Apache 2.0",
//                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
//                )
        )
        // , // servers 정보가 필요하면 쉼표(,)를 찍고 아래 주석을 해제하여 사용합니다.
        // servers = { // 선택 사항: API 서버 URL들을 명시합니다.
        //     @Server(url = "http://localhost:8080", description = "로컬 개발 서버"),
        //     @Server(url = "https://dev-api.minute.com", description = "개발 환경 서버"), // 팀의 개발 서버 주소
//             @Server(url = "https://api.minute.com", description = "운영 환경 서버")       // 팀의 운영 서버 주소
        // }
)

public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

//http://localhost:8080/swagger-ui.html 스웨거 확인 주소.
//http://localhost:8080/swagger-ui/index.html

