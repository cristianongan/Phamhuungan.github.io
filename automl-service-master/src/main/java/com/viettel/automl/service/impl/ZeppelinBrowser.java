package com.viettel.automl.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viettel.automl.dto.response.BodyPara;
import com.viettel.automl.dto.response.ParaResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZeppelinBrowser {

	@Value("${zeppelin.username}")
	private String username;

	@Value("${zeppelin.password}")
	private String password;

	@Value("${zeppelin.server}")
	private String server;

	public String getToken() {
		String urlLogin = server + "api/login";

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(urlLogin);

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("userName", username));
		params.add(new BasicNameValuePair("password", password));
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = client.execute(httpPost);
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parseSessionID(response);
	}

	public ParaResponse getParaInfo(String notebookId, String paragraphId) {
		CloseableHttpClient client = HttpClients.createDefault();
		ParaResponse paraResponse = new ParaResponse();
//        String jSessionId = this.getToken();
		String jSessionId = "";
		HttpUriRequest request = RequestBuilder.create("GET")
				.setUri(server + "api/notebook/" + notebookId + "/paragraph/" + paragraphId)
				.setHeader("JSESSIONID", jSessionId).build();

		try {
			CloseableHttpResponse response = client.execute(request);
			String bodyResponse = parseBodyResponse(response);
			Gson gson = new GsonBuilder().create();
			paraResponse = gson.fromJson(bodyResponse, ParaResponse.class);
			paraResponse.setStatusCode(response.getStatusLine().getStatusCode());
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paraResponse;
	}

	public int updateCode(BodyPara bodyPara) {
		String url = server + "api/notebook/" + bodyPara.getNotebookId() + "/paragraph/" + bodyPara.getParaId();
		CloseableHttpClient client = HttpClients.createDefault();
//        String jSessionId = this.getToken();
		String jSessionId = "";

		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader("JSESSIONID", jSessionId);
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"text\":\"" + bodyPara.getText() + "\"");
		json.append("}");

		CloseableHttpResponse response = null;
		try {
			httpPut.setEntity(new StringEntity(json.toString()));
			response = client.execute(httpPut);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException ignored) {
			}
		}

		return response.getStatusLine().getStatusCode();
	}

	private String parseSessionID(CloseableHttpResponse response) {
		String jSessionId = "";
		try {
			Header[] header = response.getAllHeaders();
			for (int i = 0; i < header.length; i++) {
				String value = header[i].getValue();
				if (value.contains("JSESSIONID")) {
					int index = value.indexOf("JSESSIONID =");
					int endIndex = value.indexOf(";", index);
					String sessionID = value.substring(index + "JSESSIONID =".length(), endIndex);
					jSessionId = sessionID;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jSessionId;
	}

	private String parseBodyResponse(CloseableHttpResponse response) {
		String bodyJson = "";
		HttpEntity entity = response.getEntity();
		try {
			bodyJson = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bodyJson;
	}

}
