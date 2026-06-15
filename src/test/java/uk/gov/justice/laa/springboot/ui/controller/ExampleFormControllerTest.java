package uk.gov.justice.laa.springboot.ui.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExampleFormController.class)
class ExampleFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/example-form"))
            .andExpect(status().isOk())
            .andExpect(view().name("example-form/form"))
            .andExpect(model().attributeExists("exampleForm"));
    }

    @Test
    void submitForm_withValidData_redirectsToSuccess() throws Exception {
        mockMvc.perform(post("/example-form")
                .param("fullName", "Jane Smith")
                .param("emailAddress", "jane.smith@example.com")
                .param("contactPreference", "email"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/example-form/success"));
    }

    @Test
    void submitForm_withMissingFields_rendersFormWithErrors() throws Exception {
        mockMvc.perform(post("/example-form")
                .param("fullName", "")
                .param("emailAddress", ""))
            // contactPreference deliberately omitted — radios send no param when unselected
            .andExpect(status().isOk())
            .andExpect(view().name("example-form/form"))
            .andExpect(model().attributeHasFieldErrors("exampleForm", "fullName", "emailAddress", "contactPreference"));
    }

    @Test
    void submitForm_withInvalidEmail_rendersFormWithEmailError() throws Exception {
        mockMvc.perform(post("/example-form")
                .param("fullName", "Jane Smith")
                .param("emailAddress", "not-an-email")
                .param("contactPreference", "phone"))
            .andExpect(status().isOk())
            .andExpect(view().name("example-form/form"))
            .andExpect(model().attributeHasFieldErrors("exampleForm", "emailAddress"));
    }

    @Test
    void success_returnsSuccessView() throws Exception {
        mockMvc.perform(get("/example-form/success"))
            .andExpect(status().isOk())
            .andExpect(view().name("example-form/success"));
    }
}
