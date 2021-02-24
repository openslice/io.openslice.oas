package io.openslice.oas.api;

import java.util.HashMap;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.ActionSpecification;
import io.openslice.oas.model.ActionSpecificationCreate;
import io.openslice.oas.model.ActionSpecificationUpdate;
import io.openslice.oas.reposervices.ActionSpecificationRepoService;


@Controller
@RequestMapping("/assuranceServicesManagement/v1/")
public class ActionSpecificationApiController  implements ActionSpecificationApi {

	private static final Logger log = LoggerFactory.getLogger(ActionSpecificationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;


	@Autowired	
	ActionSpecificationRepoService actionSpecificationRepoService;

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
	
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<ActionSpecification> createActionSpecification(@Valid ActionSpecificationCreate body) {
		try {

    		ActionSpecification c = actionSpecificationRepoService.addActionSpecification( body );

			return new ResponseEntity<ActionSpecification>(c, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<ActionSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<Void> deleteActionSpecification(String id) {
		try {

			return new ResponseEntity<Void>( actionSpecificationRepoService.deleteById( id ), HttpStatus.OK);
		} catch ( Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<ActionSpecification> retrieveActionSpecification(String id, @Valid String fields,
			Map<String, String> allParams) {

		try {

			return new ResponseEntity<ActionSpecification>( actionSpecificationRepoService.findById( id ), HttpStatus.OK);
		} catch ( Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<ActionSpecification>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<ActionSpecification> patchActionSpecification(@Valid ActionSpecificationUpdate body,
			String id) {
		ActionSpecification c = actionSpecificationRepoService.updateActionSpecification( id, body );

		return new ResponseEntity<ActionSpecification>(c, HttpStatus.OK);
	}
	
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<List<ActionSpecification>> listActionSpecification(@Valid String fields,
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
				return new ResponseEntity<List<ActionSpecification>>(						
						actionSpecificationRepoService.findAll( myfields, allParams), HttpStatus.OK);
				
				
			} else {

				
				return new ResponseEntity<List<ActionSpecification>>(
						actionSpecificationRepoService.findAll(fields, allParams), HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<List<ActionSpecification>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
