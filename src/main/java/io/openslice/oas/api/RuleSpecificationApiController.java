package io.openslice.oas.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.model.RuleSpecificationCreate;


@Controller
@RequestMapping("/assuranceServicesManagement/v1/")
public class RuleSpecificationApiController  implements RuleSpecificationApi {

	private static final Logger log = LoggerFactory.getLogger(RuleSpecificationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	RuleSpecificationApiController serviceTestSpecificationRepoService;

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
	public ResponseEntity<RuleSpecification> createRuleSpecification(@Valid RuleSpecificationCreate body) {
		return new ResponseEntity<RuleSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<Void> deleteRuleSpecification(String id) {
		// TODO Auto-generated method stub
		return RuleSpecificationApi.super.deleteRuleSpecification(id);
	}
	
	@Override
	public ResponseEntity<RuleSpecification> retrieveRuleSpecification(String id, @Valid String fields) {
		return new ResponseEntity<RuleSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@Override
	public ResponseEntity<List<RuleSpecification>> listRuleSpecification(@Valid String fields,
			@Valid Integer offset, @Valid Integer limit, @Valid Map<String, String> allParams) {
		// TODO Auto-generated method stub
		return RuleSpecificationApi.super.listRuleSpecification(fields, offset, limit, allParams);
	}
}
