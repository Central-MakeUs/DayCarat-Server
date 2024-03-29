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
                        
        - 01-21
        
        > 이번달 에피소드 개수 조회
                        - 요청 경로 "/episode/count/month" 로 수정
        
        > 전체 에피소드 개수 조회
                        - 전체 에피소드 개수 조회 API 추가 : "/episode/count/all" 로 요청
                        

        - 01-25
        
        > 소아라 수정
                        - 개별 등록 API 추가 : PATCH "/gem/soara" 로 요청
                        
        > 소아라 조회
                        - 소아라 조회 API 추가 : GET "/gem/soara/{episodeId}" 로 요청

        > 소아라 등록
                        - 소아라 등록 API 요청값 변경 : POST "/gem/register" episodeId만 받음
                        
        - 01-31
        
        > 에피소드 수정
                        - PATCH "/episode/update" 입력 양식 변경
                        
        - 02-01
        
        > 유저 정보 조회
                        - GET "/user/info" 유저 정보 조회 API 에 새 데이터 userClass 추가: 보석 개수에 따라 정해짐 (루키콜렉터 | 초보콜렉터 | 프로콜렉터)
        
        - 02-06
        
        > 에피소드 등록
                        - POST "/episode/register" 시 episodeContentType 에 '보완할 점' 추가 (에피소드 수정 시에도 적용)

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
