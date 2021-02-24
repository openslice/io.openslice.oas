package io.openslice.oas.reposervices;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.openslice.oas.model.ActionSpecification;
import io.openslice.oas.model.ActionSpecificationCreate;
import io.openslice.oas.model.ActionSpecificationUpdate;
import io.openslice.oas.repo.ActionSpecificationRepository;


@Service
public class ActionSpecificationRepoService {


	@Autowired
	ActionSpecificationRepository actionSpecificationRepository;
	

	private SessionFactory sessionFactory;

	@Autowired
	public ActionSpecificationRepoService(EntityManagerFactory factory) {
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	

	@Transactional
	public ActionSpecification addActionSpecification(@Valid ActionSpecificationCreate actionSpecificationCreate) {
		
		ActionSpecification as = new ActionSpecification();
		as = updateActionSpecificationFromAPICall( as, actionSpecificationCreate);
		as = this.actionSpecificationRepository.save(as);
		return as;
	}



	public List<ActionSpecification> findAll() {
		return (List<ActionSpecification>) this.actionSpecificationRepository.findAll();
	}


	public Void deleteById(String id) {
		Optional<ActionSpecification> optionalCat = this.actionSpecificationRepository.findByUuid(id);
		this.actionSpecificationRepository.delete(optionalCat.get());
		return null;
	}


	@Transactional
	public ActionSpecification updateActionSpecification(String id, @Valid ActionSpecificationUpdate body) {

		ActionSpecification s = this.findByUuid(id);
		if (s == null) {
			return null;
		}
		ActionSpecification alm = s;
		alm = this.updateActionSpecificationFromAPICall(alm, body);

		alm = this.actionSpecificationRepository.save(alm);
		return alm;
	}


	public ActionSpecification findByUuid(String id) {
		Optional<ActionSpecification> optionalCat = this.actionSpecificationRepository.findByUuid(id);
		return optionalCat.orElse(null);
	}
	
	public List<ActionSpecification> findAll(String myfields, @Valid Map<String, String> allParams) {
		return findAll();
	}

	public ActionSpecification findById(String id) {
		Optional<ActionSpecification> optionalCat = this.actionSpecificationRepository.findByUuid(id);
		return optionalCat.orElse(null);
	}
	

	private ActionSpecification updateActionSpecificationFromAPICall(ActionSpecification as,
			@Valid ActionSpecificationUpdate actionSpecificationUpdate) {

		if ( actionSpecificationUpdate.getName() != null) {
			as.setName( actionSpecificationUpdate.getName() );
		}
		
		if ( actionSpecificationUpdate.getDescription() != null) {
			as.setDescription( actionSpecificationUpdate.getDescription() );
		}

		return as;
	}
}
