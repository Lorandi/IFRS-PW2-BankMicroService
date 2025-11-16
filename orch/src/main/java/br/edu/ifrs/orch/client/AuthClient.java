package br.edu.ifrs.orch.client;

import br.edu.ifrs.orch.dto.UserLoginRequestDTO;
import br.edu.ifrs.orch.dto.UserSignupRequestDTO;
import br.edu.ifrs.orch.dto.UserTokenResponseDTO;
import br.edu.ifrs.orch.exception.RestClientExceptionMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "auth-api")
@RegisterProvider(RestClientExceptionMapper.class)
@Path("/api/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthClient {

    @POST
    void signup(UserSignupRequestDTO dto);

    @POST
    @Path("/login")
    UserTokenResponseDTO login(UserLoginRequestDTO dto);
}
