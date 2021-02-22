package io.openslice.oas.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.Error;
import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.model.RuleSpecificationCreate;
import io.openslice.oas.model.RuleSpecificationUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "ruleSpecification")
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

	@ApiOperation(value = "Creates a RuleSpecification", nickname = "createRuleSpecification", notes = "This operation creates a RuleSpecification entity.", response = RuleSpecification.class, tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = RuleSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/ruleSpecification", produces = { "application/json;charset=utf-8" }, consumes = {
			"application/json;charset=utf-8" }, method = RequestMethod.POST)
	default ResponseEntity<RuleSpecification> createRuleSpecification(
			@ApiParam(value = "The RuleSpecification to be created", required = true) @Valid @RequestBody RuleSpecificationCreate body) {

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@ApiOperation(value = "Deletes a RuleSpecification", nickname = "deleteRuleSpecification", notes = "This operation deletes a RuleSpecification entity.", tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Deleted"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/ruleSpecification/{id}", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.DELETE)
	default ResponseEntity<Void> deleteRuleSpecification(
			@ApiParam(value = "Identifier of the RuleSpecification", required = true) @PathVariable("id") String id) {
		if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
		} else {
			log.warn(
					"ObjectMapper or HttpServletRequest not configured in default RuleSpecificationApi interface so no example is generated");
		}
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@ApiOperation(value = "List or find RuleSpecification objects", nickname = "listRuleSpecification", notes = "This operation list or find RuleSpecification entities", response = RuleSpecification.class, responseContainer = "List", tags = {
			"ruleSpecification", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = RuleSpecification.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/ruleSpecification", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.GET)
	default ResponseEntity<List<RuleSpecification>> listRuleSpecification(
			@ApiParam(value = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(hidden = true) @Valid @RequestParam Map<String, String> allParams) {

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@ApiOperation(value = "Updates partially a RuleSpecification", nickname = "patchRuleSpecification", notes = "This operation updates partially a RuleSpecification entity.", response = RuleSpecification.class, tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Updated", response = RuleSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/ruleSpecification/{id}", produces = {
			"application/json;charset=utf-8" }, consumes = {
					"application/json;charset=utf-8" }, method = RequestMethod.PATCH)
	default ResponseEntity<RuleSpecification> patchRuleSpecification(
			@ApiParam(value = "The RuleSpecification to be updated", required = true) @Valid @RequestBody RuleSpecificationUpdate body,
			@ApiParam(value = "Identifier of the RuleSpecification", required = true) @PathVariable("id") String id) {

		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@ApiOperation(value = "Retrieves a RuleSpecification by ID", nickname = "retrieveRuleSpecification", notes = "This operation retrieves a RuleSpecification entity. Attribute selection is enabled for all first level attributes.", response = RuleSpecification.class, tags = {
			"ruleSpecification", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = RuleSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/ruleSpecification/{id}", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.GET)
	default ResponseEntity<RuleSpecification> retrieveRuleSpecification(
			@ApiParam(value = "Identifier of the RuleSpecification", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields) {
		
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

}

