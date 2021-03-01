package io.openslice.oas.reposervices;

import java.util.ArrayList;
import java.util.HashMap;
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
import io.openslice.oas.model.ActionSpecificationRef;
import io.openslice.oas.model.Condition;
import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.model.RuleSpecificationCreate;
import io.openslice.oas.model.RuleSpecificationUpdate;
import io.openslice.oas.repo.RuleSpecificationRepository;
import io.openslice.tmf.am642.model.AlarmRef;


@Service
public class RuleSpecificationRepoService {


	@Autowired
	RuleSpecificationRepository ruleSpecificationRepository;
	

	private SessionFactory sessionFactory;

	@Autowired
	public RuleSpecificationRepoService(EntityManagerFactory factory) {
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	

	@Transactional
	public RuleSpecification addRuleSpecification(@Valid RuleSpecificationCreate ruleSpecificationCreate) {
		
		RuleSpecification as = new RuleSpecification();
		as = updateRuleSpecificationFromAPICall( as, ruleSpecificationCreate);
		as = this.ruleSpecificationRepository.save(as);
		return as;
	}



	public List<RuleSpecification> findAll() {
		return (List<RuleSpecification>) this.ruleSpecificationRepository.findAll();
	}


	public Void deleteById(String id) {
		Optional<RuleSpecification> optionalCat = this.ruleSpecificationRepository.findByUuid(id);
		this.ruleSpecificationRepository.delete(optionalCat.get());
		return null;
	}


	@Transactional
	public RuleSpecification updateRuleSpecification(String id, @Valid RuleSpecificationUpdate body) {

		RuleSpecification s = this.findByUuid(id);
		if (s == null) {
			return null;
		}
		RuleSpecification alm = s;
		alm = this.updateRuleSpecificationFromAPICall(alm, body);

		alm = this.ruleSpecificationRepository.save(alm);
		return alm;
	}


	public RuleSpecification findByUuid(String id) {
		Optional<RuleSpecification> optionalCat = this.ruleSpecificationRepository.findByUuid(id);
		return optionalCat.orElse(null);
	}
	
	public List<RuleSpecification> findAll(String myfields, @Valid Map<String, String> allParams) {
		return findAll();
	}

	public RuleSpecification findById(String id) {
		Optional<RuleSpecification> optionalCat = this.ruleSpecificationRepository.findByUuid(id);
		return optionalCat.orElse(null);
	}
	
	public List<RuleSpecification> findByScopeUuid(String uuid) {
		return this.ruleSpecificationRepository.findByScopeEntityUUID( uuid );		
	}
	

	private RuleSpecification updateRuleSpecificationFromAPICall(RuleSpecification as,
			@Valid RuleSpecificationUpdate ruleSpecificationUpdate) {

		if ( ruleSpecificationUpdate.getName()  != null) {
			as.setName( ruleSpecificationUpdate.getName() );
		}
		
		if ( ruleSpecificationUpdate.getDescription() != null) {
			as.setDescription( ruleSpecificationUpdate.getDescription() );
		}

		if ( ruleSpecificationUpdate.getOpensliceEventType()  != null) {
			as.setOpensliceEventType( ruleSpecificationUpdate.getOpensliceEventType() );

		}
		
		if ( ruleSpecificationUpdate.getScope()  != null) {
			as.setScope( ruleSpecificationUpdate.getScope() );

		}

		if ( ruleSpecificationUpdate.getCondition()  != null) {
			Map<String, Boolean> idAddedUpdated = new HashMap<>();

			for (Condition ar : ruleSpecificationUpdate.getCondition()) {
				// find by id and reload it here.

				boolean idexists = false;
				for (Condition orinalCom : as.getCondition()) {
					if (ar.getUuid()!=null && orinalCom.getUuid().equals(ar.getUuid())) {
						idexists = true;
						idAddedUpdated.put(orinalCom.getUuid(), true);
						break;
					}
				}

				if (!idexists) {
					as.getCondition().add(ar);
					idAddedUpdated.put(ar.getUuid(), true);
				}
			}

			List<Condition> toRemove = new ArrayList<>();
			for (Condition ss : as.getCondition()) {
				if (idAddedUpdated.get(ss.getUuid()) == null) {
					toRemove.add(ss);
				}
			}

			for (Condition ar : toRemove) {
				as.getCondition().remove(ar);
			}

		}
		
		if ( ruleSpecificationUpdate.getActions()  != null) {
			Map<String, Boolean> idAddedUpdated = new HashMap<>();

			for (ActionSpecificationRef ar : ruleSpecificationUpdate.getActions()) {
				// find by id and reload it here.

				boolean idexists = false;
				for (ActionSpecificationRef orinalCom : as.getActions()) {
					if (orinalCom.getUuid().equals(ar.getUuid())) {
						idexists = true;
						idAddedUpdated.put(orinalCom.getUuid(), true);
						break;
					}
				}

				if (!idexists) {
					as.getActions().add(ar);
					idAddedUpdated.put(ar.getUuid(), true);
				}
			}

			List<ActionSpecificationRef> toRemove = new ArrayList<>();
			for (ActionSpecificationRef ss : as.getActions()) {
				if (idAddedUpdated.get(ss.getUuid()) == null) {
					toRemove.add(ss);
				}
			}

			for (ActionSpecificationRef ar : toRemove) {
				as.getActions().remove(ar);
			}
		}
		
		return as;
	}




}
