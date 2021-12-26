package adrianliz.smartthermostat.shared.infrastructure.spring;

import adrianliz.smartthermostat.shared.domain.DomainError;
import adrianliz.smartthermostat.shared.domain.Utils;
import adrianliz.smartthermostat.shared.domain.bus.command.CommandHandlerExecutionError;
import adrianliz.smartthermostat.shared.domain.bus.query.QueryHandlerExecutionError;
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

public final class ApiExceptionMiddleware implements Filter {

  private RequestMappingHandlerMapping mapping;

  public ApiExceptionMiddleware(RequestMappingHandlerMapping mapping) {
    this.mapping = mapping;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
    HttpServletRequest httpRequest = ((HttpServletRequest) request);
    HttpServletResponse httpResponse = ((HttpServletResponse) response);

    try {
      HandlerExecutionChain handlerChain = mapping.getHandler(httpRequest);
      if (handlerChain == null) {
        chain.doFilter(request, response);
      } else {
        Object possibleController = ((HandlerMethod) handlerChain.getHandler()).getBean();

        try {
          chain.doFilter(request, response);
        } catch (NestedServletException exception) {
          if (possibleController instanceof ApiController) {
            handleCustomError(response, httpResponse, (ApiController) possibleController, exception);
          }
        }
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  private void handleCustomError(
    ServletResponse response,
    HttpServletResponse httpResponse,
    ApiController possibleController,
    NestedServletException exception
  ) throws IOException {
    HashMap<Class<? extends DomainError>, HttpStatus> errorMapping = possibleController.errorMapping();
    Throwable error = (
      exception.getCause() instanceof CommandHandlerExecutionError || exception.getCause() instanceof QueryHandlerExecutionError
    )
      ? exception.getCause().getCause()
      : exception.getCause();

    int statusCode = statusFor(errorMapping, error);
    String errorCode = errorCodeFor(error);
    String errorMessage = error.getMessage();

    exception.printStackTrace();

    httpResponse.reset();
    httpResponse.setHeader("Content-Type", "application/json");
    httpResponse.setStatus(statusCode);
    PrintWriter writer = response.getWriter();
    writer.write(String.format("{\"error_code\": \"%s\", \"message\": \"%s\"}", errorCode, errorMessage));
    writer.close();
  }

  private String errorCodeFor(Throwable error) {
    if (error instanceof DomainError) {
      return ((DomainError) error).errorCode();
    }

    return Utils.toSnake(error.getClass().toString());
  }

  private int statusFor(HashMap<Class<? extends DomainError>, HttpStatus> errorMapping, Throwable error) {
    return errorMapping.getOrDefault(error.getClass(), HttpStatus.INTERNAL_SERVER_ERROR).value();
  }
}
