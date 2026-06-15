package uk.gov.justice.laa.springboot.ui.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

  private final TestableGlobalExceptionHandler handler =
      new TestableGlobalExceptionHandler();

  @Test
  void handleGenericException_returnsProblemDetailForAcceptHeader() throws Exception {
    RuntimeException exception = new RuntimeException("boom");
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Accept", "application/json");
    request.setRequestURI("/api/error");

    Object response = handler.handleGenericException(exception, request);

    assertThat(response).isInstanceOf(ResponseEntity.class);
    ResponseEntity<?> entity = (ResponseEntity<?>) response;
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(entity.getBody()).isInstanceOf(ProblemDetail.class);
    ProblemDetail problemDetail = (ProblemDetail) entity.getBody();
    assertThat(problemDetail.getDetail()).isEqualTo("An unexpected error occurred.");
    assertThat(problemDetail.getInstance()).isEqualTo(URI.create("/api/error"));
  }

  @Test
  void handleGenericException_returnsProblemDetailForApiUri() throws Exception {
    RuntimeException exception = new RuntimeException("boom");
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/example");

    Object response = handler.handleGenericException(exception, request);

    assertThat(response).isInstanceOf(ResponseEntity.class);
    assertThat(((ResponseEntity<?>) response).getStatusCode())
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void handleGenericException_rethrowsNonApiRequests() {
    RuntimeException exception = new RuntimeException("boom");
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/components");

    assertThatThrownBy(() -> handler.handleGenericException(exception, request))
        .isSameAs(exception);
  }

  @Test
  void handleExceptionInternal_buildsProblemDetailWhenBodyMissing() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/components");
    WebRequest webRequest = new ServletWebRequest(request);

    ResponseEntity<Object> response = handler.invokeHandleExceptionInternal(
        new RuntimeException("bad request"),
        null,
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        webRequest
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
    ProblemDetail body = (ProblemDetail) response.getBody();
    assertThat(body.getDetail()).isEqualTo("bad request");
    assertThat(body.getInstance()).isEqualTo(URI.create("/components"));
  }

  @Test
  void handleExceptionInternal_preservesProvidedBody() {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    ResponseEntity<Object> response = handler.invokeHandleExceptionInternal(
        new RuntimeException("ignored"),
        problemDetail,
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        new ServletWebRequest(new MockHttpServletRequest())
    );

    assertThat(response.getBody()).isSameAs(problemDetail);
  }

  private static final class TestableGlobalExceptionHandler
      extends GlobalExceptionHandler {

    private ResponseEntity<Object> invokeHandleExceptionInternal(
        Exception ex,
        Object body,
        HttpHeaders headers,
        HttpStatus statusCode,
        WebRequest request
    ) {
      return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
  }
}
