package uk.gov.justice.laa.springboot.ui.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

class ApplicationConfigTest {

  @Test
  void addResourceHandlers_registersWebjarHandler() {
    GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
    applicationContext.refresh();
    ResourceHandlerRegistry registry = new ResourceHandlerRegistry(
        applicationContext,
        new MockServletContext()
    );

    new ApplicationConfig().addResourceHandlers(registry);

    assertThat(registry.hasMappingForPattern("/webjars/**")).isTrue();
    applicationContext.close();
  }
}
