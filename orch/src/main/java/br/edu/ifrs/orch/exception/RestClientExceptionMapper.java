package br.edu.ifrs.orch.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class RestClientExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    @Override
    public RuntimeException toThrowable(Response response) {

        int status = response.getStatus();
        String body = response.readEntity(String.class);

        // Guardamos status + body dentro da mensagem
        return new RuntimeException(status + "::" + body);
    }

}