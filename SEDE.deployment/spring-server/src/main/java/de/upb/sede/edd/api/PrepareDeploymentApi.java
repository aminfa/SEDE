/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.9).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.upb.sede.edd.api;

import de.upb.sede.edd.model.Remote;
import de.upb.sede.edd.model.PrepareRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-03T11:35:15.474Z[GMT]")
@Api(value = "prepareDeployment", description = "the prepareDeployment API")
public interface PrepareDeploymentApi {

    @ApiOperation(value = "Prepares the required services by installing them.", nickname = "prepareDeploymentPost", notes = "", tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Installed"),
        @ApiResponse(code = 404, message = "Services could not all be installed.") })
    @RequestMapping(value = "/prepareDeployment",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<String> prepareDeploymentPost(@ApiParam(value = "", required = true) @Valid @RequestBody PrepareRequest body);


    @ApiOperation(value = "Prepares the required services by installing them on the given remote.", nickname = "prepareDeploymentRemoteNamePost", notes = "", tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Installed"),
        @ApiResponse(code = 404, message = "Services could not all be installed.") })
    @RequestMapping(value = "/prepareDeployment/{remoteName}",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<String> prepareDeploymentRemoteNamePost(@ApiParam(value = "", required = true) @Valid @RequestBody PrepareRequest body, @ApiParam(value = "Remote name of a previosuly managed machine with an EDD server.", required = true) @PathVariable("remoteName") String remoteName);

}
