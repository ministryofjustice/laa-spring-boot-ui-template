package uk.gov.justice.laa.springboot.ui.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomErrorController.class)
class CustomErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleError_404_returnsError404View() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
            .andExpect(status().isOk())
            .andExpect(view().name("errors/error-404"));
    }

    @Test
    void handleError_403_returnsError403View() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403))
            .andExpect(status().isOk())
            .andExpect(view().name("errors/error-403"));
    }

    @Test
    void handleError_500_returnsError500View() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500))
            .andExpect(status().isOk())
            .andExpect(view().name("errors/error-500"));
    }

    @Test
    void handleError_unknownStatus_returnsErrorGenericView() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 418))
            .andExpect(status().isOk())
            .andExpect(view().name("errors/error-generic"));
    }

    @Test
    void handleError_noStatus_returnsErrorGenericView() throws Exception {
        mockMvc.perform(get("/error"))
            .andExpect(status().isOk())
            .andExpect(view().name("errors/error-generic"));
    }
}
