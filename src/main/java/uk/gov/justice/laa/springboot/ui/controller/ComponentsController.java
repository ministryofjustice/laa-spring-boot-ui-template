package uk.gov.justice.laa.springboot.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Renders the example component library page.
 */
@Controller
public class ComponentsController {

  @GetMapping("/components")
  public String components() {
    return "components";
  }
}
