package io.openslice.oas;



import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc - removed 30/7/2021
public class MvcConfig implements WebMvcConfigurer {
	@Autowired
	Environment env;

	public MvcConfig() {
		super();
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/index.html");

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");
		DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
		LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(formatter);
		LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(formatter);

		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
		module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
			@Override
			public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator,
					SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
				jsonGenerator.writeString(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(offsetDateTime));

			}
		});
		
		
		mapper.registerModule(simpleModule);

		// add converter at the very front
		// if there are same type mappers in converters, setting in first mapper
		// is used.
		converters.add(0, new MappingJackson2HttpMessageConverter(mapper));
		
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	converters.add(new StringHttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter());
	}
}
