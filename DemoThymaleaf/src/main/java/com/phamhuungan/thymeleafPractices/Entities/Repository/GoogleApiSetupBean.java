package com.phamhuungan.thymeleafPractices.Entities.Repository;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

@Component
public class GoogleApiSetupBean {
	@Autowired
	private GoogleCredential googleCredential;
	@Bean
	public Drive getDrive() throws GeneralSecurityException, IOException
	{
		NetHttpTransport HTTP_Transport = GoogleNetHttpTransport.newTrustedTransport();
		return new Drive.Builder(HTTP_Transport, JacksonFactory.getDefaultInstance(), googleCredential).build();
	}
	@Bean
	public GoogleCredential getGoogleCredential() throws GeneralSecurityException, IOException
	{
        Collection<String> elenco = new ArrayList<String>();
        elenco.add("https://www.googleapis.com/auth/drive");
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleCredential.Builder().setTransport(httpTransport)
        		.setJsonFactory(jsonFactory)
        		.setServiceAccountId("demothymeleaf@demothymeleaf01.iam.gserviceaccount.com")
        		.setServiceAccountScopes(elenco)
        		.setServiceAccountPrivateKeyFromP12File(new File("/home/phngan/Desktop/demothymeleaf01-9f9e878c9c70.p12"))
        		.build();
	}

}
