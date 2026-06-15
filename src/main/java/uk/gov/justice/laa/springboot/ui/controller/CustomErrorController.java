package uk.gov.justice.laa.springboot.ui.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Maps servlet error dispatches to service-specific error pages.
 */
@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

  /**
   * Resolves the dispatched servlet error status to the matching Thymeleaf error page.
   *
   * @param request the original servlet request
   * @param model unused view model reserved for future error page data
   * @return the Thymeleaf view name for the resolved error page
   */
  // Spring Boot ErrorController must accept all HTTP methods — errors can be dispatched
  // from any original request method. This handler only renders error views and performs
  // no state changes, so there is no CSRF risk.
  @RequestMapping(value = "/error", method = {
      RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
      RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.HEAD,
      RequestMethod.OPTIONS, RequestMethod.TRACE
  })
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

    log.debug("Error occurred - Status: {}, URI: {}, Exception: {}",
        status, requestUri, exception != null ? exception.getClass().getSimpleName() : "None");

    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());
      HttpStatus httpStatus = HttpStatus.resolve(statusCode);

      if (httpStatus == HttpStatus.NOT_FOUND) {
        return "errors/error-404";
      }
      if (httpStatus == HttpStatus.FORBIDDEN) {
        return "errors/error-403";
      }
      if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
        return "errors/error-500";
      }
      return "errors/error-generic";
    }

    return "errors/error-generic";
  }
}
