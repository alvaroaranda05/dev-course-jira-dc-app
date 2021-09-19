package com.deiser.jira.dc.rest;

import com.deiser.jira.dc.model.Configuration;
import com.deiser.jira.dc.model.ConfigurationOut;
import com.deiser.jira.dc.service.ConfigurationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/configuration")
public class ConfigurationRest {

    private ConfigurationService configurationService;

    public ConfigurationRest(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get() {
        List<ConfigurationOut> configurationOuts = configurationService.get().stream()
                .map(ConfigurationOut::new)
                .collect(Collectors.toList());
        return Response.ok(configurationOuts).build();
    }

    @Path("/{key}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@PathParam("key") String key) {
        Configuration configuration = configurationService.get(key);
        return configuration == null
                //TODO decide between noContent/notFound
                ? Response.noContent().build()
                : Response.ok(new ConfigurationOut(configuration)).build();
    }

    @Path("/{key}")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(@PathParam("key") String key, String value) {
        Configuration configuration = configurationService.create(key, value);
        return configuration == null
                //TODO decide between noContent/notFound
                ? Response.noContent().build()
                : Response.ok(new ConfigurationOut(configuration)).build();
    }

    @Path("/{key}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("key") String key, String value) {
        Configuration configuration = configurationService.update(key, value);
        return configuration == null
                //TODO decide between noContent/notFound
                ? Response.noContent().build()
                : Response.ok(new ConfigurationOut(configuration)).build();
    }

    @Path("/{key}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("key") String key) {
        boolean deleted = configurationService.delete(key);
        return deleted
                //TODO decide between noContent/notFound
                ? Response.ok().build()
                : Response.noContent().build();
    }
}
