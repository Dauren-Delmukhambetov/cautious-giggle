package kz.toko.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.toko.api.model.CreateStoreRequest;
import kz.toko.api.model.Store;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Validated
public interface StoresApi {

    @Operation(summary = "Create a new store", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Stores" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New store has been successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Store.class))),

            @ApiResponse(responseCode = "401", description = "The request requires user authentication"),

            @ApiResponse(responseCode = "400", description = "The request is not valid and cannot be acccepted") })
    @RequestMapping(value = "/stores",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Store> createStore(@Parameter(in = ParameterIn.DEFAULT, description = "New store creation request", required=true, schema=@Schema())
                                      @Valid @RequestBody CreateStoreRequest body
    );


    @Operation(summary = "Retrieve user's stores", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Stores" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of user's stores has been successfuly fetched", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Store.class)))),

            @ApiResponse(responseCode = "401", description = "The request requires user authentication") })
    @RequestMapping(value = "/stores",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Store>> getStores();
}
