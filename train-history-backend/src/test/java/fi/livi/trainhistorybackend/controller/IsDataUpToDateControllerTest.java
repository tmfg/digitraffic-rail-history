package fi.livi.trainhistorybackend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import fi.livi.trainhistorybackend.BaseTest;
import jakarta.transaction.Transactional;

@Transactional
public class IsDataUpToDateControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    private String PATH = "/trains/history/is-up-to-date";

    @Sql({"/trains/insert_test_data.sql"})
    @Test
    public void dataIsUpToDateReturns200() throws Exception {
        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Sql({"/trains/insert_test_data_out_of_date.sql"})
    @Test
    public void dataIsNotUpToDateReturns500() throws Exception {
        mockMvc.perform(get(PATH))
                .andExpect(status().isInternalServerError());
    }
}