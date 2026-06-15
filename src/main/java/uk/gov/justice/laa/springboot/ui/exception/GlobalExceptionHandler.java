package uk.gov.justice.laa.springboot.ui.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Produces RFC 9457 problem details for API requests while leaving browser
 * requests to the MVC error controller.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final URI DEFAULT_PROBLEM_TYPE = URI.create("about:blank");

  /**
   * Handle generic exceptions for JSON or API requests.
   * Browser requests are handled by {@code CustomErrorController} via /error dispatch.
   *
   * @param exception the exception that was raised
   * @param request the current servlet request
   * @return a problem-detail response for API requests
   * @throws Exception rethrows non-API exceptions for normal MVC error dispatch
   */
  @ExceptionHandler(Exception.class)
  public Object handleGenericException(
      Exception exception,
      HttpServletRequest request
  ) throws Exception {
    if (isApiRequest(request)) {
      log.error("Unexpected error in API request", exception);
      ProblemDetail problemDetail =
          ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      problemDetail.setType(DEFAULT_PROBLEM_TYPE);
      problemDetail.setDetail("An unexpected error occurred.");
      problemDetail.setInstance(URI.create(request.getRequestURI()));
      return ResponseEntity.internalServerError().body(problemDetail);
    }
    throw exception;
  }

  private boolean isApiRequest(HttpServletRequest request) {
    String accept = request.getHeader("Accept");
    String contentType = request.getHeader("Content-Type");
    String uri = request.getRequestURI();
    return (accept != null && accept.contains("application/json"))
        || (contentType != null && contentType.contains("application/json"))
        || (uri != null && uri.startsWith("/api/"));
  }

  private ProblemDetail buildProblemDetail(
      HttpStatusCode status,
      String detail,
      WebRequest request
  ) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setType(DEFAULT_PROBLEM_TYPE);
    problemDetail.setDetail(detail);
    if (request instanceof ServletWebRequest servletWebRequest) {
      problemDetail.setInstance(
          URI.create(servletWebRequest.getRequest().getRequestURI())
      );
    }
    return problemDetail;
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request
  ) {
    if (body == null) {
      body = buildProblemDetail(statusCode, ex.getMessage(), request);
    }
    return super.handleExceptionInternal(ex, body, headers, statusCode, request);
  }
}
