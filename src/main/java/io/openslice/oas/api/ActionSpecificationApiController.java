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

import io.openslice.oas.model.ActionSpecification;
import io.openslice.oas.model.ActionSpecificationCreate;
import io.openslice.tmf.stm653.model.ServiceTestSpecification;


@Controller
@RequestMapping("/assuranceServicesManagement/v1/")
public class ActionSpecificationApiController  implements ActionSpecificationApi {

	private static final Logger log = LoggerFactory.getLogger(ActionSpecificationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	ActionSpecificationApiController serviceTestSpecificationRepoService;

	@Value("${spring.application.name}")
	private String compname;

	@org.springframework.beans.factory.annotation.Autowired
	public ActionSpecificationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
	
	
//	@Override
//	public ResponseEntity<ActionSpecification> createActionSpecification(@Valid ActionSpecificationCreate body) {
//		return new ResponseEntity<ActionSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//	
//	@Override
//	public ResponseEntity<Void> deleteActionSpecification(String id) {
//		// TODO Auto-generated method stub
//		return ActionSpecificationApi.super.deleteActionSpecification(id);
//	}
//	
//	@Override
//	public ResponseEntity<ActionSpecification> retrieveActionSpecification(String id, @Valid String fields) {
//		return new ResponseEntity<ActionSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//	
//	
//	@Override
//	public ResponseEntity<List<ActionSpecification>> listActionSpecification(@Valid String fields,
//			@Valid Integer offset, @Valid Integer limit, @Valid Map<String, String> allParams) {
//		// TODO Auto-generated method stub
//		return ActionSpecificationApi.super.listActionSpecification(fields, offset, limit, allParams);
//	}
}
