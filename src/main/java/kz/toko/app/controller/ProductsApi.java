package kz.toko.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.toko.api.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
public interface ProductsApi {

    @Operation(summary = "Creates a new product", tags={ "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New product has been successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))) })
    @RequestMapping(value = "/products",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Product> createProduct(
            @Parameter(in = ParameterIn.DEFAULT, description = "New product creation request", required=true, schema=@Schema())
            @Valid @RequestBody CreateProductRequest body
    );

    @Operation(summary = "Softly deletes a given product", description = "This method allows you to mark a product with a given `id` as deleted and stores the date of deletion. Deleted products won't be available for users anymore. ", tags={ "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product has been successfully deleted"),

            @ApiResponse(responseCode = "404", description = "Product with a given ID has not beed found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),

            @ApiResponse(responseCode = "410", description = "The product has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @RequestMapping(value = "/products/{id}",
            produces = { "application/problem+json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteProduct(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id
    );

    @Operation(summary = "Get product by ID", description = "This method returns product with given ID ", tags={ "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),

            @ApiResponse(responseCode = "404", description = "Product with a given ID has not beed found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),

            @ApiResponse(responseCode = "410", description = "The product has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @RequestMapping(value = "/products/{id}",
            produces = { "application/json", "application/problem+json" },
            method = RequestMethod.GET)
    ResponseEntity<Product> getProduct(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id);

    @Operation(summary = "Retrieve all products", tags={ "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of products has been successfully fetched", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))) })
    @RequestMapping(value = "/products",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Product>> getProducts();


    @Operation(summary = "Partially updates a given product", description = "This method allows you to partially update a product with a given `id`. In case some properties are not set in the request, they will not be updated. ", tags={ "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The product has been successfully updated"),

            @ApiResponse(responseCode = "404", description = "Product with a given ID has not beed found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),

            @ApiResponse(responseCode = "410", description = "The product has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @RequestMapping(value = "/products/{id}",
            produces = { "application/problem+json" },
            consumes = { "application/json" },
            method = RequestMethod.PATCH)
    ResponseEntity<Void> updateProduct(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id,
            @Parameter(in = ParameterIn.DEFAULT, description = "Product update request", required=true, schema=@Schema())
            @Valid @RequestBody UpdateProductRequest body
    );

    @Operation(summary = "Uploads the product image", description = "This method allows you to upload an image with a size up to 10 Mb and set it as a cover image for the product with a given `id`. In case there is already an image for the product it will be replaced by the new one ", tags={ "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image has been successfully uploaded and bound to the product", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Link.class))),

            @ApiResponse(responseCode = "404", description = "Product with a given ID has not beed found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))),

            @ApiResponse(responseCode = "410", description = "The product has already been deleted", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorDetails.class))) })
    @RequestMapping(value = "/products/{id}/image",
            produces = { "application/json", "application/problem+json" },
            consumes = { "multipart/form-data" },
            method = RequestMethod.POST)
    ResponseEntity<Link> uploadImage(
            @Min(1L)
            @Parameter(in = ParameterIn.PATH, description = "Entity ID", required=true, schema=@Schema(allowableValues={ "1" }, minimum="1"))
            @PathVariable("id") Long id,
            @Parameter(description = "file detail") @Valid @RequestPart("file") MultipartFile file
    );
}
