package com.outsera.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.outsera.challenge.core.Startup;
import com.outsera.challenge.repository.MovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@SpyBean
    private MovieRepository movieRepository;

	private static final String AWARDS_URL_MAPPING = "/movies/producers";
	
	@Test
	public void defaultTest() throws Exception {
		String expectedResponse = "{\n"
				+ "		    \"min\": [\n"
				+ "		        {\n"
				+ "		            \"producer\": \"Joel Silver\",\n"
				+ "		            \"interval\": 1,\n"
				+ "		            \"previousWin\": 1990,\n"
				+ "		            \"followingWin\": 1991\n"
				+ "		        }\n"
				+ "		    ],\n"
				+ "		    \"max\": [\n"
				+ "		        {\n"
				+ "		            \"producer\": \"Matthew Vaughn\",\n"
				+ "		            \"interval\": 13,\n"
				+ "		            \"previousWin\": 2002,\n"
				+ "		            \"followingWin\": 2015\n"
				+ "		        }\n"
				+ "		    ]\n"
				+ "		}";
		
		mvc.perform(get(AWARDS_URL_MAPPING).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedResponse));
	}
}
