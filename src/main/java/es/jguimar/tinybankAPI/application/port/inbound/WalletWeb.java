package es.jguimar.tinybankAPI.application.port.inbound;

import es.jguimar.tinybankAPI.adapter.rest.dto.MoneyRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.TransferRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.WalletRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.WalletResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;

public interface WalletWeb {

    @Operation(summary = "Creates a new wallet", description = "Creates a new wallet . ", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User correctly created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponseDto.class))),
        
        @ApiResponse(responseCode = "409", description = "User already exists"),
        
        @ApiResponse(responseCode = "422", description = "Malformed expected data"),
        
        @ApiResponse(responseCode = "500", description = "Unexpected API error") })
    @RequestMapping(value = "/wallet",
        produces = { "application/json" },
        consumes = { "application/json" },
        method = RequestMethod.POST)
    WalletResponseDto walletPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema())
                                 @Valid @RequestBody WalletRequestDto body);

    @Operation(summary = "Put money to wallet", description = "Put money to wallet . ", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Put money correctly created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "Wallet not found"),

            @ApiResponse(responseCode = "422", description = "Malformed expected data"),

            @ApiResponse(responseCode = "500", description = "Unexpected API error") })
    @RequestMapping(value = "/wallet/{id}/money",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PUT)
    WalletResponseDto moneyToWalletPut(@Parameter(in = ParameterIn.PATH, description = "Wallet id", required=true, schema=@Schema())
                                   @PathVariable("id") String id,
                                   @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema())
                                   @Valid @RequestBody MoneyRequestDto body);

    @Operation(summary = "Show wallet data", description = "Show wallet data. ", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get wallet correctly", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "Wallet not found"),

            @ApiResponse(responseCode = "500", description = "Unexpected API error") })
    @RequestMapping(value = "/wallet/{id}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    WalletResponseDto walletGet(@Parameter(in = ParameterIn.PATH, description = "Wallet id", required=true, schema=@Schema())
                                   @PathVariable("id") String id);

    @Operation(summary = "Make a money transfer between", description = "Make a money transfer between . ", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money correctly tranfered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "Wallet not found"),

            @ApiResponse(responseCode = "422", description = "Malformed expected data"),

            @ApiResponse(responseCode = "500", description = "Unexpected API error") })
    @RequestMapping(value = "/wallet/money-tranfer",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    WalletResponseDto moneyTransferPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema())
                                 @Valid @RequestBody TransferRequestDto body);

}

