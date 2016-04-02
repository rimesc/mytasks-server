package uk.co.zoneofavoidance.my.tasks;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

   @Override
   public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
      final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      converter.setObjectMapper(objectMapper);
      converters.add(converter);
      super.configureMessageConverters(converters);
   }

}
