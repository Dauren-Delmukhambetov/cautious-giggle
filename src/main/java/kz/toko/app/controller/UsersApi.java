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
import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.ErrorDetails;
import kz.toko.api.model.UpdateUserRequest;
import kz.toko.api.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
public interface UsersApi {

    @Operation(summary = "Create user", description = "This method creates a new user.", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New user has been successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "The request requires user authentication") })
    @PostMapping(value = "/users",
            produces = { "application/json" },
            consumes = { "application/json" })
    ResponseEntity<User> createUser(
            @Parameter(in = ParameterIn.DEFAULT, description = "Create user request", required=true, schema=@Schema())
            @Valid @RequestBody CreateUserRequest body
    );

    @Operation(summary = "Delete user by ID", description = "For valid response try integer IDs with positive integer value. Negative or non-integer values will generate API errors", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User has been successfully deleted"),
            @ApiResponse(responseCode = "401", description = "The request requires user authentication"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "410", description = "User has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @DeleteMapping(value = "/users/{id}",
            produces = { "application/problem+json" })
    ResponseEntity<Void> deleteUser(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id);

    @Operation(summary = "Get user by ID", description = "This method returns user with given ID ", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User with a given ID has not beed found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "410", description = "The user has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @RequestMapping(value = "/users/{id}",
            produces = { "application/json", "application/problem+json" },
            method = RequestMethod.GET)
    ResponseEntity<User> getUser(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id);

    @Operation(summary = "Retrieve all Users", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of users has been successfully fetched", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "401", description = "The request requires user authentication") })
    @RequestMapping(value = "/users",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<User>> getUsers();

    @Operation(summary = "Update user", description = "This can only be done by the logged in user.", security = {
            @SecurityRequirement(name = "OpenIDConnectAuth")    }, tags={ "Users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The user has been successfully updated"),
            @ApiResponse(responseCode = "401", description = "The request requires user authentication"),
            @ApiResponse(responseCode = "404", description = "User with a given ID has not beed found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "410", description = "The user has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @RequestMapping(value = "/users/{id}",
            produces = { "application/problem+json" },
            consumes = { "application/json" },
            method = RequestMethod.PATCH)
    ResponseEntity<Void> updateUser(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id,
            @Parameter(in = ParameterIn.DEFAULT, description = "Updated user object", required=true, schema=@Schema())
            @Valid @RequestBody UpdateUserRequest body);
}
