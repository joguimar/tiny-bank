package es.jguimar.tinybankAPI.application.port.inbound;

import es.jguimar.tinybankAPI.adapter.rest.dto.UserRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.UserResponseDto;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public interface UserWeb {

    @Operation(summary = "Creates a new user", description = "Creates a new user based on a non-empty name and a password. ", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User correctly created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
        
        @ApiResponse(responseCode = "409", description = "User already exists"),
        
        @ApiResponse(responseCode = "422", description = "Malformed expected data"),
        
        @ApiResponse(responseCode = "500", description = "Unexpected API error") })
    @RequestMapping(value = "/user",
        produces = { "application/json" },
        consumes = { "application/json" },
        method = RequestMethod.POST)
    UserResponseDto userPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UserRequestDto body) throws ResourceExistsException;

}

