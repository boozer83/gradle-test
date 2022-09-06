package io.kakaoi.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kakaoi.config.Constants;
import io.kakaoi.web.rest.vm.SampleVM;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("리포지토리 CRUD 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SampleControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();
    }

    private Snippet authHeader() {
        return requestHeaders(headerWithName(Constants.Headers.X_AUTH_TOKEN).description("KiC Cloud IAM Token"));
    }

    @Test
    void Sample_생성() throws Exception {
        SampleVM.Apply sampleVM = new SampleVM.Apply();
        sampleVM.setName("test");
        sampleVM.setDescription("test-description");

        ResultActions result = this.mockMvc.perform(
            post("/api/sample")
                .header(Constants.Headers.X_AUTH_TOKEN, "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(sampleVM)));

        result
            .andExpect(status().isOk())
            .andDo(document("sample-create",
                    authHeader(),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 HTTP 상태 코드"),
                        fieldWithPath("data.id").type(JsonFieldType.STRING).description("Simple ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("Simple 이름"),
                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("Simple 설명"),
                        fieldWithPath("data.createdDt").type(JsonFieldType.STRING).description("생성일시"),
                        fieldWithPath("data.createdBy").type(JsonFieldType.STRING).description("생성자"),
                        fieldWithPath("data.updatedDt").type(JsonFieldType.STRING).description("수정일시"),
                        fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).description("수정자")
                    )
                )
            );
    }

}
