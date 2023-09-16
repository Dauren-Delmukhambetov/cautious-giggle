package kz.toko.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.StoreItem;
import kz.toko.api.model.StoreItemFilteringCriteria;
import kz.toko.api.model.StoreItemsPageableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
public interface StoreItemsApi {

    @Operation(summary = "Adds store item to store", description = "", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Store Items" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New store item has been successfully added", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreItem.class))),

            @ApiResponse(responseCode = "401", description = "The request requires user authentication"),

            @ApiResponse(responseCode = "400", description = "The request is not valid and cannot be acccepted") })
    @RequestMapping(value = "/store-items",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<StoreItem> addStoreItem(
            @Parameter(in = ParameterIn.DEFAULT, description = "New store item creation request", required=true, schema=@Schema())
            @Valid @RequestBody CreateStoreItemRequest body
    );

    @Operation(summary = "Retrieves store items with paging and filtering", description = "", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Store Items" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of store items has been successfuly fetched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreItemsPageableResponse.class))),
            @ApiResponse(responseCode = "401", description = "The request requires user authentication") })
    @RequestMapping(value = "/store-items",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<StoreItemsPageableResponse> getStoreItems(
            @Min(1)
            @Parameter(in = ParameterIn.QUERY, description = "The number of page to request" ,schema=@Schema(allowableValues={ "1" }, minimum="1", defaultValue="1"))
            @Valid @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,

            @Min(10) @Max(100)
            @Parameter(in = ParameterIn.QUERY, description = "The numbers of items on a page" ,schema=@Schema(allowableValues={ "10", "100" }, minimum="10", maximum="100", defaultValue="20"))
            @Valid @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,

            @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema())
            @Valid @RequestParam(value = "storeItemFilteringCriteria", required = false) StoreItemFilteringCriteria storeItemFilteringCriteria
    );
}
