package com.colvir.ms.sys.rms.generated.web.rest;

import com.colvir.ms.generated.svc.base.web.rest.vm.LoggerVM;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/management/loggers")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class LoggersEndpoint {

    @GET
    public LoggersWrapper getLoggers() {
        Enumeration<String> loggerNames = LogManager.getLogManager().getLoggerNames();

        Map<String, LoggerVM> loggers = Collections
            .list(loggerNames)
            .stream()
            .filter(name -> !name.isBlank())
            .map(this::getLogger)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(logger -> logger.name, Function.identity()));

        return new LoggersWrapper(loggers);
    }

    private LoggerVM getLogger(String loggerName) {
        return Optional.ofNullable(Logger.getLogger(loggerName)).map(logger -> new LoggerVM(loggerName, logger)).orElse(null);
    }

    @POST
    @Path("/{name}")
    public Response updateLoggerLevel(@PathParam("name") String name, LoggerVM loggerVM) {
        Logger logger = Logger.getLogger(name);

        if (logger == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Level level = Level.parse(loggerVM.configuredLevel);
        logger.setLevel(level);

        return Response.ok().build();
    }

    @RegisterForReflection
    public static class LoggersWrapper {

        public final Map<String, LoggerVM> loggers;

        public LoggersWrapper(Map<String, LoggerVM> loggers) {
            this.loggers = loggers;
        }
    }
}
