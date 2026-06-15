package uk.gov.justice.laa.springboot.ui.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.justice.laa.springboot.ui.form.ExampleForm;

/**
 * Handles the example form pages, demonstrating GOV.UK form patterns with Bean Validation.
 */
@Controller
@RequestMapping("/example-form")
public class ExampleFormController {

  /**
   * Renders the empty contact details form.
   */
  @GetMapping
  public String showForm(Model model) {
    model.addAttribute("exampleForm", new ExampleForm());
    return "example-form/form";
  }

  /**
   * Processes the submitted form. Re-renders with errors on validation failure,
   * or redirects to the success page on success (PRG pattern).
   */
  @PostMapping
  public String submitForm(
      @Valid @ModelAttribute("exampleForm") ExampleForm form,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "example-form/form";
    }
    return "redirect:/example-form/success";
  }

  /**
   * Renders the submission confirmation page.
   */
  @GetMapping("/success")
  public String success() {
    return "example-form/success";
  }
}
