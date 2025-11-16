package br.edu.ifrs.orch.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

public class ErrorHandler {

    public static Response toResponse(Throwable ex) {

        String raw = ex.getMessage();

        if (ex instanceof jakarta.ws.rs.ProcessingException) {
            return Response.status(503)
                    .entity(Map.of("status", 503, "message", "Serviço de usuários indisponível"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

            if(ex instanceof RuntimeException re && !ex.getMessage().contains("Constraint Violation")) {

            int separator = raw.indexOf("::");

            int status = Integer.parseInt(raw.substring(0, separator));
            String body = raw.substring(separator + 2);

            return Response.status(status)
                    .entity(body)
                    .type("application/json")
                    .build();
        }


        // --- 1) Erro vindo do USERS "<status>::<json>" ---
        if (raw != null && raw.contains("::")) {
            int sep = raw.indexOf("::");
            int status = Integer.parseInt(raw.substring(0, sep));
            String body = raw.substring(sep + 2);

            String message = extractMessageFromJson(body);

            return Response.status(status)
                    .entity(Map.of("status", status, "message", message))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }


        if (raw != null && raw.startsWith("{") && raw.endsWith("}") && raw.contains("=")) {
            String message = extractFromMapString(raw);
            return Response.status(400)
                    .entity(Map.of("status", 400, "message", message))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response.status(400)
                .entity(Map.of("status", 400, "message", raw))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private static String extractFromMapString(String s) {
        try {
            s = s.replace("{", "").replace("}", "");
            for (String part : s.split(",")) {
                String[] kv = part.split("=");
                if (kv.length == 2 && kv[0].trim().equals("error")) {
                    return kv[1].trim();
                }
            }
        } catch (Exception ignored) {}
        return s;
    }

    private static String extractMessageFromJson(String json) {
        try {
            var obj = jakarta.json.Json.createReader(
                    new java.io.StringReader(json)
            ).readObject();

            // 1. message direto
            if (obj.containsKey("message"))
                return obj.getString("message");

            // 2. violations
            if (obj.containsKey("violations")) {
                var violations = obj.getJsonArray("violations");
                if (!violations.isEmpty()) {
                    var v = violations.getJsonObject(0);

                    String field = v.getString("field", "");
                    if (field.contains(".")) {
                        field = field.substring(field.lastIndexOf(".") + 1);
                    }

                    String msg = v.getString("message", "Erro de validação");

                    return field + ": " + msg;
                }
            }

        } catch (Exception ignore) {}

        return json;
    }

    public static WebApplicationException rethrow(Throwable ex) {

        // Serviço fora do ar
        if (ex instanceof jakarta.ws.rs.ProcessingException) {
            return new WebApplicationException(
                    Response.status(503)
                            .entity(Map.of("status", 503, "error", "Serviço indisponível"))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }

        String raw = ex.getMessage();

        // --- ERRO NO FORMATO: 400::{ "error": "...", "status":400, "errorId":"..." } ---
        if (raw != null && raw.contains("::")) {
            int sep = raw.indexOf("::");
            int status = Integer.parseInt(raw.substring(0, sep));
            String body = raw.substring(sep + 2);

            // devolve EXATAMENTE o JSON original do serviço chamado
            return new WebApplicationException(
                    Response.status(status)
                            .entity(body)
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }

        // --- Se chegar aqui, é um erro genérico ---
        return new WebApplicationException(
                Response.status(500)
                        .entity(Map.of(
                                "status", 500,
                                "error", raw != null ? raw : "Erro interno"
                        ))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
        );
    }

}

