package uk.gov.justice.laa.springboot.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Renders the template home page.
 */
@Controller
public class HomeController {

  @GetMapping("/")
  public String index() {
    return "index";
  }
}
