package io.openslice.oas.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.openslice.oas.model.Error;
import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.model.RuleSpecificationCreate;
import io.openslice.oas.model.RuleSpecificationUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Tag(name = "ruleSpecification")
public interface RuleSpecificationApi {


	Logger log = LoggerFactory.getLogger(RuleSpecificationApi.class);

	default Optional<ObjectMapper> getObjectMapper() {
		return Optional.empty();
	}

	default Optional<HttpServletRequest> getRequest() {
		return Optional.empty();
	}

	default Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	@Operation(summary = "Creates a RuleSpecification", operationId = "createRuleSpecification", description = "This operation creates a RuleSpecification entity.",  tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created" ),
			@ApiResponse(responseCode = "400", description = "Bad Request" ),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ),
			@ApiResponse(responseCode = "403", description = "Forbidden" ),
			@ApiResponse(responseCode = "405", description = "Method Not allowed" ),
			@ApiResponse(responseCode = "409", description = "Conflict" ),
			@ApiResponse(responseCode = "500", description = "Internal Server Error" ) })
	@RequestMapping(value = "/ruleSpecification", produces = { "application/json;charset=utf-8" }, consumes = {
			"application/json;charset=utf-8" }, method = RequestMethod.POST)
	default ResponseEntity<RuleSpecification> createRuleSpecification(
			@Parameter(description = "The RuleSpecification to be created", required = true) @Valid @RequestBody RuleSpecificationCreate body) {

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Operation(summary = "Deletes a RuleSpecification", operationId = "deleteRuleSpecification", description = "This operation deletes a RuleSpecification entity.", tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Deleted" ),
			@ApiResponse(responseCode = "400", description = "Bad Request" ),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ),
			@ApiResponse(responseCode = "403", description = "Forbidden" ),
			@ApiResponse(responseCode = "404", description = "Not Found" ),
			@ApiResponse(responseCode = "405", description = "Method Not allowed" ),
			@ApiResponse(responseCode = "409", description = "Conflict" ),
			@ApiResponse(responseCode = "500", description = "Internal Server Error" ) })
	@RequestMapping(value = "/ruleSpecification/{id}", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.DELETE)
	default ResponseEntity<Void> deleteRuleSpecification(
			@Parameter(description = "Identifier of the RuleSpecification", required = true) @PathVariable("id") String id) {
		if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
		} else {
			log.warn(
					"ObjectMapper or HttpServletRequest not configured in default RuleSpecificationApi interface so no example is generated");
		}
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Operation(summary = "List or find RuleSpecification objects", operationId = "listRuleSpecification", description = "This operation list or find RuleSpecification entities", tags = {
			"ruleSpecification", })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success" ),
			@ApiResponse(responseCode = "400", description = "Bad Request" ),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ),
			@ApiResponse(responseCode = "403", description = "Forbidden" ),
			@ApiResponse(responseCode = "404", description = "Not Found" ),
			@ApiResponse(responseCode = "405", description = "Method Not allowed" ),
			@ApiResponse(responseCode = "409", description = "Conflict" ),
			@ApiResponse(responseCode = "500", description = "Internal Server Error" ) })
	@RequestMapping(value = "/ruleSpecification", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.GET)
	default ResponseEntity<List<RuleSpecification>> listRuleSpecification(
			@Parameter(description = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@Parameter(description = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@Parameter(description = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@Parameter(hidden = true) @Valid @RequestParam Map<String, String> allParams) {

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Operation(summary = "Updates partially a RuleSpecification", operationId = "patchRuleSpecification", description = "This operation updates partially a RuleSpecification entity.", tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated" ),
			@ApiResponse(responseCode = "400", description = "Bad Request" ),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ),
			@ApiResponse(responseCode = "403", description = "Forbidden" ),
			@ApiResponse(responseCode = "404", description = "Not Found" ),
			@ApiResponse(responseCode = "405", description = "Method Not allowed" ),
			@ApiResponse(responseCode = "409", description = "Conflict" ),
			@ApiResponse(responseCode = "500", description = "Internal Server Error" ) })
	@RequestMapping(value = "/ruleSpecification/{id}", produces = {
			"application/json;charset=utf-8" }, consumes = {
					"application/json;charset=utf-8" }, method = RequestMethod.PATCH)
	default ResponseEntity<RuleSpecification> patchRuleSpecification(
			@Parameter(description = "The RuleSpecification to be updated", required = true) @Valid @RequestBody RuleSpecificationUpdate body,
			@Parameter(description = "Identifier of the RuleSpecification", required = true) @PathVariable("id") String id) {

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Operation(summary = "Retrieves a RuleSpecification by ID", operationId = "retrieveRuleSpecification", description = "This operation retrieves a RuleSpecification entity. Attribute selection is enabled for all first level attributes.", tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success" ),
			@ApiResponse(responseCode = "400", description = "Bad Request" ),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ),
			@ApiResponse(responseCode = "403", description = "Forbidden" ),
			@ApiResponse(responseCode = "404", description = "Not Found" ),
			@ApiResponse(responseCode = "405", description = "Method Not allowed" ),
			@ApiResponse(responseCode = "409", description = "Conflict" ),
			@ApiResponse(responseCode = "500", description = "Internal Server Error" ) })
	@RequestMapping(value = "/ruleSpecification/{id}", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.GET)
	default ResponseEntity<RuleSpecification> retrieveRuleSpecification(
			@Parameter(description = "Identifier of the RuleSpecification", required = true) @PathVariable("id") String id,
			@Parameter(description = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields) {
		
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

}

