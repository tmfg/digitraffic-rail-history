package fi.livi.trainhistorybackend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import fi.livi.trainhistorybackend.BaseTest;
import jakarta.transaction.Transactional;

@Sql({"/trains/insert_test_data.sql"})
@Transactional
public class TrainControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    private String PATH = "/api/v1/trains/history";

    @Test
    public void validRequestReturnsTrains() throws Exception {
        mockMvc.perform(get(PATH + "/2023-11-01/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].json.key").value("value1"));
    }

    @Test
    public void noResultsReturnsEmptyList() throws Exception {
        mockMvc.perform(get(PATH + "/2023-10-05/99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void invalidDateReturnsBadRequest() throws Exception {
        mockMvc.perform(get(PATH + "/invalid-date/12345"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void unsupportedAcceptHeaderReturns406() throws Exception {
        mockMvc.perform(get(PATH + "/2023-11-01/12345")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotAcceptable());
    }
}