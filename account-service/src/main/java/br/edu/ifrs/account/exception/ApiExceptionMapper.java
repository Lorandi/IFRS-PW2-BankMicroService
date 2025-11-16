package br.edu.ifrs.account.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.UUID;


@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(ApiExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {
        String errorId = UUID.randomUUID().toString();
        int status = switch (e) {
            case IllegalArgumentException ignored -> Response.Status.BAD_REQUEST.getStatusCode();
            case IllegalStateException ignored -> Response.Status.CONFLICT.getStatusCode();
            default -> Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        };

        LOG.errorf("ErrorID=%s | Type=%s | Message=%s", errorId, e.getClass().getSimpleName(), e.getMessage());

        return Response.status(status)
                .entity(Map.of(
                        "errorId", errorId,
                        "status", status,
                        "error", e.getMessage()
                ))
                .build();
    }
}
