package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.infrastructure.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HireNullificationController.class)
@Import(SecurityConfiguration.class)
class HireNullificationControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorizedWhenBearerTokenIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/hire-nullifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnForbiddenWhenTokenDoesNotContainRequiredScope() throws Exception {
        mockMvc.perform(post("/api/v1/hire-nullifications")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAcceptRequestWhenTokenContainsRequiredScope() throws Exception {
        mockMvc.perform(post("/api/v1/hire-nullifications")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(() -> "SCOPE_hire.nullification.write"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isAccepted());
    }

    private String validRequest() {
        return "{\"hireId\":\"hire-123\",\"reason\":\"Candidate declined the offer\",\"requestedBy\":\"hr-specialist\"}";
    }
}
