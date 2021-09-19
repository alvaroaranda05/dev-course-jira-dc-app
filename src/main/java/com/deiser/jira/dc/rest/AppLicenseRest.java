package com.deiser.jira.dc.rest;

import com.deiser.jira.dc.service.AppLicenseService;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/license")
public class AppLicenseRest {

    private AppLicenseService appLicenseService;

    public AppLicenseRest(AppLicenseService appLicenseService) {
        this.appLicenseService = appLicenseService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response isLicense() {
        return appLicenseService.hasLicense() ?
                Response.ok().build() :
                Response.status(Response.Status.FORBIDDEN).build();
    }

    @Path("/evaluation")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response isEvaluation() {
        return appLicenseService.isEvaluation() ?
                Response.ok().build() :
                Response.status(Response.Status.FORBIDDEN).build();
    }
}
