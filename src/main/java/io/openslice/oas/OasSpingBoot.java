/*-
 * ========================LICENSE_START=================================
 * io.openslice.osom
 * %%
 * Copyright (C) 2019 openslice.io
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

import java.util.Collections;
import java.util.List;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ctranoris
 *
 */

@EnableSwagger2
@SpringBootApplication
public class OasSpingBoot implements CommandLineRunner {

	private static ApplicationContext applicationContext;

	@Override
	public void run(String... arg0) throws Exception {
		if (arg0.length > 0 && arg0[0].equals("exitcode")) {
			throw new ExitException();
		}

	}

	public static void main(String[] args) throws Exception {

		applicationContext = new SpringApplication(OasSpingBoot.class).run(args);

//        for (String beanName : applicationContext.getBeanDefinitionNames()) {
//            System.out.println(beanName);
//        }
	}

	class ExitException extends RuntimeException implements ExitCodeGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {
			return 10;
		}
	}
	
    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService) {

        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
//                System.out.println("Number of process definitions : " + repositoryService.createProcessDefinitionQuery().count());
//                System.out.println("Number of active process definitions : " + repositoryService.createProcessDefinitionQuery().active().count() );
//                System.out.println("Number of active/suspended process definitions : " + repositoryService.createProcessDefinitionQuery().active().suspended().count());
//                System.out.println("Number of suspended process definitions : " + repositoryService.createProcessDefinitionQuery().suspended().count() );
//                System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
//                System.out.println("Number of process instances : " + runtimeService.createProcessInstanceQuery().count() );
//                System.out.println("Number of suspended process instances : " + runtimeService.createProcessInstanceQuery().suspended().count() );
//        		System.out.println("Number of tasks after process start: " + taskService.createTaskQuery().count());
//        		
//        		
//        		List<ProcessDefinition> pr = repositoryService.createProcessDefinitionQuery().list();
//        		for (ProcessDefinition processDefinition : Collections.unmodifiableList(pr) ) {
//            		System.out.println("Number of tasks after process start: " + processDefinition.getDeploymentId() );
//            		System.out.println("Number of tasks after process start: " + processDefinition.toString()  );
//            		System.out.println("Number of tasks after process start: " + processDefinition.isSuspended()  );
////            		try {
////                		repositoryService.deleteDeployment( processDefinition.getDeploymentId(), true );            			
////            		}finally {
////            			
////            		}
//					
//				}
//        		
//        		List<Deployment> dq = repositoryService.createDeploymentQuery().list();
//        		for (Deployment deployment : dq) {
//            		System.out.println("deployment: " + deployment.getName());
//            		System.out.println("deployment: " + deployment.toString() );
//            		System.out.println("deployment: " + deployment.isNew() );
////            		repositoryService.deleteDeployment( deployment.getId() , true );   
//					
//				}
            }
        };
    }

}
