package com.transformuk.hee.tis.auth.api;


import com.transformuk.hee.tis.auth.model.TraineeId;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TraineeIdController.class, secure = false)
public class TraineeIdControllerTest {

    private static final String GMC_NUMBER = "gmc123";
    private static final Long TIS_ID = 1L;
    private static final String REQUEST = "[ { \"gmcNumber\": \"gmc123\" } ]";
    
    @MockBean
    private TraineeIdService traineeIdService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnTraineeIds() throws Exception {
        //Given
        TraineeId traineeId = new TraineeId(TIS_ID, GMC_NUMBER);
        given(traineeIdService.findOrCreate(newArrayList(GMC_NUMBER))).willReturn(newArrayList(traineeId));

        // When & Then
        this.mvc.perform(post("/api/trainee-id/register")
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
        given(traineeIdService.findOrCreate(newArrayList(GMC_NUMBER))).willThrow(RuntimeException.class);

        // When & Then
        this.mvc.perform(post("/api/trainee-id/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnExistingTraineeIds() throws Exception {
        //Given
        Pageable pageable = new PageRequest(0, 10);
        TraineeId existingTraineeId = new TraineeId(1L, GMC_NUMBER);
        Page<TraineeId> page = new PageImpl<>(newArrayList(existingTraineeId));
        
        given(traineeIdService.findAll(pageable)).willReturn(page);

        // When & Then
        this.mvc.perform(get("/api/trainee-id/mappings?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].tisId").value(TIS_ID.intValue()))
                .andExpect(jsonPath("$.content[0].gmcNumber").value(GMC_NUMBER));
    }

}
