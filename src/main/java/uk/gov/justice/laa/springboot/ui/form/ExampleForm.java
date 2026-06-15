package uk.gov.justice.laa.springboot.ui.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Form backing object for the example contact details form.
 */
@Data
public class ExampleForm {

  @NotBlank(message = "{form.example.fullName.required}")
  @Size(max = 100, message = "{form.example.fullName.size}")
  private String fullName;

  @NotBlank(message = "{form.example.emailAddress.required}")
  @Email(message = "{form.example.emailAddress.invalid}")
  private String emailAddress;

  @NotNull(message = "{form.example.contactPreference.required}")
  private String contactPreference;
}
