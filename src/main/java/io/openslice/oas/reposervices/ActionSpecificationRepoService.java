package io.openslice.oas.reposervices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.openslice.oas.model.ActionParam;
import io.openslice.oas.model.ActionSpecification;
import io.openslice.oas.model.ActionSpecificationCreate;
import io.openslice.oas.model.ActionSpecificationUpdate;
import io.openslice.oas.repo.ActionSpecificationRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.Valid;


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
		
		
		if ( actionSpecificationUpdate.getParams()  != null) {
			Map<String, Boolean> idAddedUpdated = new HashMap<>();

			for (ActionParam ar : actionSpecificationUpdate.getParams() ) {
				// find by id and reload it here.

				boolean idexists = false;
				for (ActionParam orinalCom : as.getParams()) {
					if (ar.getUuid()!=null && orinalCom.getUuid().equals(ar.getUuid())) {
						idexists = true;
						idAddedUpdated.put(orinalCom.getUuid(), true);
						break;
					}
				}

				if (!idexists) {
					as.getParams().add(ar);
					idAddedUpdated.put(ar.getUuid(), true);
				}
			}

			List<ActionParam> toRemove = new ArrayList<>();
			for (ActionParam ss : as.getParams()) {
				if (idAddedUpdated.get(ss.getUuid()) == null) {
					toRemove.add(ss);
				}
			}

			for (ActionParam ar : toRemove) {
				as.getParams().remove(ar);
			}

		}

		return as;
	}
}
