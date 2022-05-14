package adrianliz.shared.infrastructure.spring;

import adrianliz.shared.domain.DomainError;
import adrianliz.shared.domain.Utils;
import adrianliz.shared.domain.bus.command.CommandHandlerExecutionError;
import adrianliz.shared.domain.bus.query.QueryHandlerExecutionError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.ServletRequestPathUtils;

public final class ApiExceptionMiddleware implements Filter {

  private final RequestMappingHandlerMapping mapping;

  public ApiExceptionMiddleware(final RequestMappingHandlerMapping mapping) {
    this.mapping = mapping;
  }

  @Override
  public void doFilter(
      final ServletRequest request, final ServletResponse response, final FilterChain chain)
      throws ServletException {
    final HttpServletRequest httpRequest = ((HttpServletRequest) request);
    final HttpServletResponse httpResponse = ((HttpServletResponse) response);

    try {
      if (!ServletRequestPathUtils.hasParsedRequestPath(httpRequest)) {
        ServletRequestPathUtils.parseAndCache(httpRequest);
      }

      final HandlerExecutionChain handlerChain = mapping.getHandler(httpRequest);
      if (handlerChain == null) {
        chain.doFilter(request, response);
      } else {
        final Object possibleController = ((HandlerMethod) handlerChain.getHandler()).getBean();

        try {
          chain.doFilter(request, response);
        } catch (final NestedServletException exception) {
          if (possibleController instanceof ApiController) {
            handleCustomError(
                response, httpResponse, (ApiController) possibleController, exception);
          }
        }
      }
    } catch (final Exception e) {
      throw new ServletException(e);
    }
  }

  private void handleCustomError(
      final ServletResponse response,
      final HttpServletResponse httpResponse,
      final ApiController possibleController,
      final NestedServletException exception)
      throws IOException {
    final HashMap<Class<? extends DomainError>, HttpStatus> errorMapping =
        possibleController.errorMapping();
    final Throwable error =
        (exception.getCause() instanceof CommandHandlerExecutionError
                || exception.getCause() instanceof QueryHandlerExecutionError)
            ? exception.getCause().getCause()
            : exception.getCause();

    final int statusCode = statusFor(errorMapping, error);
    final String errorCode = errorCodeFor(error);
    final String errorMessage = error.getMessage();

    httpResponse.reset();
    httpResponse.setHeader("Content-Type", "application/json");
    httpResponse.setStatus(statusCode);
    final PrintWriter writer = response.getWriter();
    writer.write(
        String.format("{\"error_code\": \"%s\", \"message\": \"%s\"}", errorCode, errorMessage));
    writer.close();
  }

  private String errorCodeFor(final Throwable error) {
    if (error instanceof DomainError) {
      return ((DomainError) error).errorCode();
    }

    return Utils.toSnake(error.getClass().toString());
  }

  private int statusFor(
      final HashMap<Class<? extends DomainError>, HttpStatus> errorMapping, final Throwable error) {
    return errorMapping.getOrDefault(error.getClass(), HttpStatus.INTERNAL_SERVER_ERROR).value();
  }
}
