package io.openslice.oas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.model.RuleSpecificationCreate;
import io.openslice.oas.model.RuleSpecificationUpdate;
import io.openslice.oas.reposervices.RuleSpecificationRepoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/assuranceServicesManagement/v1/")
public class RuleSpecificationApiController  implements RuleSpecificationApi {

	private static final Logger log = LoggerFactory.getLogger(RuleSpecificationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired	
	RuleSpecificationRepoService ruleSpecificationRepoService;
	
	@Value("${spring.application.name}")
	private String compname;

	@org.springframework.beans.factory.annotation.Autowired
	public RuleSpecificationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}
	
	
	@Override
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')" )
	public ResponseEntity<RuleSpecification> createRuleSpecification(@Valid RuleSpecificationCreate body) {
		try {

    		RuleSpecification c = ruleSpecificationRepoService.addRuleSpecification( body );

			return new ResponseEntity<RuleSpecification>(c, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<RuleSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@Override
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')" )
	public ResponseEntity<Void> deleteRuleSpecification(String id) {
		try {

			return new ResponseEntity<Void>( ruleSpecificationRepoService.deleteById( id ), HttpStatus.OK);
		} catch ( Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')" )
	public ResponseEntity<RuleSpecification> retrieveRuleSpecification(String id, @Valid String fields) {
		try {

			return new ResponseEntity<RuleSpecification>( ruleSpecificationRepoService.findById( id ), HttpStatus.OK);
		} catch ( Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<RuleSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')" )
	public ResponseEntity<RuleSpecification> patchRuleSpecification(@Valid RuleSpecificationUpdate body, String id) {
		RuleSpecification c = ruleSpecificationRepoService.updateRuleSpecification( id, body );

		return new ResponseEntity<RuleSpecification>(c, HttpStatus.OK);
	}
	
	
	@Override
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')" )
	public ResponseEntity<List<RuleSpecification>> listRuleSpecification(@Valid String fields,
			@Valid Integer offset, @Valid Integer limit, @Valid Map<String, String> allParams) {
		try {
			if (allParams != null) {
				allParams.remove("fields");
				allParams.remove("offset");
				allParams.remove("limit");
			} else {
				allParams = new HashMap<>();
			}
			if ((fields == null) && (allParams.size() == 0)) {

				String myfields = null;
				return new ResponseEntity<List<RuleSpecification>>(						
						ruleSpecificationRepoService.findAll( myfields, allParams), HttpStatus.OK);
				
				
			} else {

				
				return new ResponseEntity<List<RuleSpecification>>(
						ruleSpecificationRepoService.findAll(fields, allParams), HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<List<RuleSpecification>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
