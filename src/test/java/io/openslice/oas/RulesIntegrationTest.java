/*-
 * ========================LICENSE_START=================================
 * io.openslice.tmf.api
 * %%
 * Copyright (C) 2019 - 2021 openslice.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package io.openslice.oas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.ActionParam;
import io.openslice.oas.model.ActionSpecification;
import io.openslice.oas.model.ActionSpecificationCreate;
import io.openslice.oas.model.ActionSpecificationRef;
import io.openslice.oas.model.Condition;
import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.model.RuleSpecificationCreate;
import io.openslice.oas.model.RuleSpecificationUpdate;
import io.openslice.oas.model.Scope;
import io.openslice.oas.reposervices.ActionSpecificationRepoService;
import io.openslice.oas.reposervices.RuleSpecificationRepoService;
import io.openslice.tmf.am642.model.AffectedService;
import io.openslice.tmf.am642.model.Alarm;
import io.openslice.tmf.am642.model.AlarmCreateEvent;
import lombok.extern.apachecommons.CommonsLog;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = OasSpingBoot.class)
@AutoConfigureMockMvc
@ActiveProfiles("testing")
@CommonsLog
public class RulesIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	RuleSpecificationRepoService ruleSpecificationRepoService;

	@Autowired
	ActionSpecificationRepoService actionSpecificationRepoService;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private AlarmHandling alarmHandling;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@WithMockUser(username = "osadmin", roles = { "ADMIN", "USER" })
	@Test
	public void testRuleCreateAndUpdate() throws UnsupportedEncodingException, IOException, Exception {

		ActionSpecificationCreate actionCreate = new ActionSpecificationCreate();
		actionCreate.setName("scaleEqualy");
		
		ActionParam param = new ActionParam();
		param.setParamName("Service");
		param.setParamValue("UUID");
		actionCreate.getParams().add(param );

		String responseAction = mvc
				.perform(MockMvcRequestBuilders.post("/assuranceServicesManagement/v1/actionSpecification")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
						.content(toJson(actionCreate)))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name", is("scaleEqualy"))).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		assertThat(actionSpecificationRepoService.findAll().size()).isEqualTo(1);

		ActionSpecification anAction = toJsonObj(responseAction, ActionSpecification.class);
		assertThat(anAction.getParams().size()).isEqualTo(1);
		
		ActionSpecificationRef aref = new ActionSpecificationRef();
		aref.setActionId(anAction.getUuid());

		RuleSpecificationCreate rule = new RuleSpecificationCreate();
		rule.setName("aRule");
		rule.getActions().add(aref);
		rule.setDescription("Descr");
		rule.setOpensliceEventType("AlarmCreateEvent");
		Scope scope = new Scope();
		scope.setEntityUUID("UUIDREFTEST");
		rule.setScope(scope);

		Condition c1 = new Condition();
		c1.setBooleanOperator("AND");
		c1.setOpensliceEventAttributeName("probableCause");
		c1.setOperator("EQUALS");
		c1.setEventAttributeValue("thresholdCrossed");

		Condition c2 = new Condition();
		c2.setBooleanOperator("AND");
		c2.setOpensliceEventAttributeName("severity");
		c2.setOperator("EQUALS");
		c2.setEventAttributeValue("critical");

		rule.getCondition().add(c1);
		rule.getCondition().add(c2);

		String responseRule = mvc
				.perform(MockMvcRequestBuilders.post("/assuranceServicesManagement/v1/ruleSpecification")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
						.content(toJson(rule)))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name", is("aRule"))).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		assertThat(ruleSpecificationRepoService.findAll().size()).isEqualTo(1);

		RuleSpecification ruleSpec = toJsonObj(responseRule, RuleSpecification.class);

		assertThat(ruleSpec.getName()).isEqualTo("aRule");
		assertThat(ruleSpec.getOpensliceEventType()).isEqualTo("AlarmCreateEvent");
		assertThat(ruleSpec.getActions().stream().findFirst().get().getActionId()).isEqualTo(anAction.getUuid());
		assertThat(ruleSpec.getScope().getEntityUUID()).isEqualTo("UUIDREFTEST");
		assertThat(ruleSpec.getCondition().size()).isEqualTo(2);

		RuleSpecificationUpdate ruleUpd = new RuleSpecificationUpdate();
		ruleUpd.setName("aRule2");
		ruleUpd.getActions().add(aref);
		ruleUpd.setDescription("Descr2");
		ruleUpd.setOpensliceEventType("AlarmCreateEvent2");
		Scope scope2 = new Scope();
		scope2.setEntityUUID("UUIDREFTEST2");
		ruleUpd.setScope(scope2);

		responseRule = mvc
				.perform(MockMvcRequestBuilders
						.patch("/assuranceServicesManagement/v1/ruleSpecification/" + ruleSpec.getUuid())
						.with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
						.content(toJson(ruleUpd)))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name", is("aRule2"))).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		assertThat(ruleSpecificationRepoService.findAll().size()).isEqualTo(1);

		RuleSpecification ruleSpec2 = toJsonObj(responseRule, RuleSpecification.class);

		assertThat(ruleSpec2.getName()).isEqualTo("aRule2");
		assertThat(ruleSpec2.getOpensliceEventType()).isEqualTo("AlarmCreateEvent2");
		assertThat(ruleSpec2.getActions().stream().findFirst().get().getActionId()).isEqualTo(anAction.getUuid());
		assertThat(ruleSpec2.getScope().getEntityUUID()).isEqualTo("UUIDREFTEST2");
		assertThat(ruleSpec2.getCondition().size()).isEqualTo(2);

		assertThat(ruleSpecificationRepoService.findByScopeUuid("UUIDREFTEST2").size()).isEqualTo(1);

		scope.setEntityUUID("UUIDREFTEST2");
		rule.setScope(scope);
		responseRule = mvc
				.perform(MockMvcRequestBuilders.post("/assuranceServicesManagement/v1/ruleSpecification")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
						.content(toJson(rule)))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name", is("aRule"))).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		assertThat(ruleSpecificationRepoService.findByScopeUuid("UUIDREFTEST2").size()).isEqualTo(2);
	}

	@Test
	public void testAlarmHandling() {
		// create some Actions Specs
		ActionSpecificationCreate actionCreate = new ActionSpecificationCreate();
		actionCreate.setName("sendEmail");
		var act1 = actionSpecificationRepoService.addActionSpecification(actionCreate);
		actionCreate = new ActionSpecificationCreate();
		actionCreate.setName("scaleEqualy");
		var act2 = actionSpecificationRepoService.addActionSpecification(actionCreate);
		actionCreate = new ActionSpecificationCreate();
		actionCreate.setName("callHuman");
		var act3 = actionSpecificationRepoService.addActionSpecification(actionCreate);
		assertThat(actionSpecificationRepoService.findAll().size()).isEqualTo(3);
		// create some RuleSpecs and add to repo

		RuleSpecificationCreate rule01wConditions = new RuleSpecificationCreate();
		rule01wConditions.setName("aRule01");
		var aref = new ActionSpecificationRef();
		aref.setActionId(act1.getUuid());
		rule01wConditions.getActions().add(aref);
		var aref2 = new ActionSpecificationRef();
		aref2.setActionId(act2.getUuid());
		rule01wConditions.getActions().add(aref2);
		rule01wConditions.setDescription("Descr");
		rule01wConditions.setOpensliceEventType("AlarmCreateEvent");
		Scope scope = new Scope();
		scope.setEntityUUID("service-uuid");
		rule01wConditions.setScope(scope);

		Condition c1 = new Condition();
		c1.setBooleanOperator("AND");
		c1.setOpensliceEventAttributeName("probableCause");
		c1.setOperator("EQUALS");
		c1.setEventAttributeValue("thresholdCrossed");
		Condition c2 = new Condition();
		c2.setBooleanOperator("AND");
		c2.setOpensliceEventAttributeName("perceivedSeverity");
		c2.setOperator("EQUALS");
		c2.setEventAttributeValue("critical");

		rule01wConditions.getCondition().add(c1);
		rule01wConditions.getCondition().add(c2);
		
		ruleSpecificationRepoService.addRuleSpecification(rule01wConditions);

		// create an Alarm and check the related actions are two
		var alarm = new Alarm();
		alarm.setProbableCause("thresholdCrossed");
		alarm.setPerceivedSeverity("critical");

		AffectedService affectedService = new AffectedService();
		affectedService.setUuid("service-uuid");
		affectedService.setId("service-uuid");
		alarm.getAffectedService().add(affectedService);
		var actions = alarmHandling.decideForExecutionAction(alarm);
		assertThat(actions.size()).isEqualTo(2);

		// create an irrelevant Alarm and check the related actions are zero
		alarm = new Alarm();
		alarm.setProbableCause("notknown");
		alarm.setPerceivedSeverity("critical");
		alarm.getAffectedService().add(affectedService);
		actions = alarmHandling.decideForExecutionAction(alarm);
		assertThat(actions.size()).isEqualTo( 0 );		

		// create an irrelevant Alarm and check the related actions are zero
		alarm = new Alarm();
		alarm.setProbableCause("thresholdCrossed");
		alarm.setPerceivedSeverity("warning");
		alarm.getAffectedService().add(affectedService);
		actions = alarmHandling.decideForExecutionAction(alarm);
		assertThat(actions.size()).isEqualTo( 0 );
		

		// create an irrelevant Alarm and check the related actions are zero
		alarm = new Alarm();
		alarm.setProbableCause("thresholdCrossed");
		alarm.setPerceivedSeverity("warning");
		actions = alarmHandling.decideForExecutionAction(alarm);
		assertThat(actions.size()).isEqualTo( 0 );
		
		//create a rule that will be true with no conditions
		RuleSpecificationCreate rule02NoConditions = new RuleSpecificationCreate();
		rule02NoConditions.setName("aRule02");
		var aref3 = new ActionSpecificationRef();
		aref3.setActionId(act3.getUuid());
		rule02NoConditions.getActions().add(aref3);
		rule02NoConditions.setDescription("Descr");
		rule02NoConditions.setOpensliceEventType("AlarmCreateEvent");
		scope = new Scope();
		scope.setEntityUUID("service-uuid");
		rule02NoConditions.setScope(scope);

		ruleSpecificationRepoService.addRuleSpecification(rule02NoConditions);
		
		// create an Alarm and check the related actions are three
		alarm = new Alarm();
		alarm.setProbableCause("thresholdCrossed");
		alarm.setPerceivedSeverity("critical");
		affectedService = new AffectedService();
		affectedService.setUuid("service-uuid");
		affectedService.setId("service-uuid");
		alarm.getAffectedService().add(affectedService);		
		actions = alarmHandling.decideForExecutionAction(alarm);
		assertThat(actions.size()).isEqualTo(3); //all three actions!
		

		Condition c3 = new Condition();
		c3.setBooleanOperator("AND");
		c3.setOpensliceEventAttributeName("threshold");
		c3.setOperator("GREATER_THAN");
		c3.setEventAttributeValue("50");

	}

	static byte[] toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

	static String toJsonString(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

	static <T> T toJsonObj(String content, Class<T> valueType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.readValue(content, valueType);
	}

	static <T> T toJsonObj(InputStream content, Class<T> valueType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.readValue(content, valueType);
	}

}
