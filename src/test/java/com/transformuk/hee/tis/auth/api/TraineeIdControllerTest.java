package com.transformuk.hee.tis.auth.api;


import com.transformuk.hee.tis.auth.model.RegistrationRequest;
import com.transformuk.hee.tis.auth.model.TraineeProfile;
import com.transformuk.hee.tis.auth.service.TraineeIdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TraineeIdController.class, secure = false)
public class TraineeIdControllerTest {

    private static final String GMC_NUMBER = "gmc123";
    public static final String DESIGNATED_BODY_CODE = "1-DGBODY";
    private static final Long TIS_ID = 1L;
    private static final String REQUEST = "[ { \"gmcNumber\": \"gmc123\" } ]";
    
    @MockBean
    private TraineeIdService traineeIdService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnTraineeIds() throws Exception {
        //Given
        TraineeProfile traineeProfile = new TraineeProfile(TIS_ID, GMC_NUMBER);
        given(traineeIdService.findOrCreate(eq(DESIGNATED_BODY_CODE), anyListOf(RegistrationRequest.class))).willReturn
                (newArrayList(traineeProfile));

        // When & Then
        this.mvc.perform(post("/api/trainee-id/1-DGBODY/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.traineeIds").isArray())
                .andExpect(jsonPath("$.traineeIds[0].tisId").value(TIS_ID.intValue()))
                .andExpect(jsonPath("$.traineeIds[0].gmcNumber").value(GMC_NUMBER));
    }

    @Test
    public void shouldReturn500ForInternException() throws Exception {
        //Given
        given(traineeIdService.findOrCreate(eq(DESIGNATED_BODY_CODE), anyListOf(RegistrationRequest.class))).willThrow(RuntimeException.class);

        // When & Then
        this.mvc.perform(post("/api/trainee-id/1-DGBODY/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnExistingTraineeIds() throws Exception {
        //Given
        Pageable pageable = new PageRequest(0, 10);
        TraineeProfile existingTraineeProfile = new TraineeProfile(1L, GMC_NUMBER);
        Page<TraineeProfile> page = new PageImpl<>(newArrayList(existingTraineeProfile));
        
        given(traineeIdService.findAll(DESIGNATED_BODY_CODE, pageable)).willReturn(page);

        // When & Then
        this.mvc.perform(get("/api/trainee-id/1-DGBODY/mappings?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].tisId").value(TIS_ID.intValue()))
                .andExpect(jsonPath("$.content[0].gmcNumber").value(GMC_NUMBER));
    }

}
