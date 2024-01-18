package com.example.daycarat.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "DayCarat API", version = "v1", description = """
        # 데이캐럿 API 문서입니다.
        ---
        
        ### 개발용 토큰 (유저 삭제 하지 말아주세요):

                eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNocm9vbTEzMjRAbmF2ZXIuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MDU0MTE5MTAsImV4cCI6MTcxNDA1MTkxMH0._qYlVOQKnYwQtUXetaVRftc0E4BJZ99-r9iu6kztdv4

        ---
       
       
        ### 업데이트:
        - 01-18

        > 에피소드 등록 수정
                        - episodeContentType을 '배운 점', '아쉬운 점', '자유롭게 작성' 중 하나를 입력할 수 있습니다.

        > 에피소드 상세 조회, 최신순 3개 조회, 에피소드 조회: 활동 많은순: 활동별 조회, 에피소드 조회: 날짜 최신순: 월별 조회 시 episodeState 추가
                        - 'UNFINALIZED'는 다듬지 않은 에피소드를, 'FINALIZED'는 다듬은 에피소드를 의미합니다.

        """),
        servers = {
                @Server(url = "https://www.daycarat.shop/api", description = "Server URL"),
                @Server(url = "http://localhost:8080/api", description = "Local Server URL")
        },
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
