package uk.gov.justice.laa.springboot.ui.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ComponentsController.class)
class ComponentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void components_returnsComponentsView() throws Exception {
        mockMvc.perform(get("/components"))
            .andExpect(status().isOk())
            .andExpect(view().name("components"));
    }
}
