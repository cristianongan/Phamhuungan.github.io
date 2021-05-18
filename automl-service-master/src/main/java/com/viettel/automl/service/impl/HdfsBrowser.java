package com.viettel.automl.service.impl;

import com.viettel.automl.dto.response.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HdfsBrowser {
	private static HashMap<String, Configuration> confMap;
	@Value("${hdfs}")
	private String hdfs;
	@Value("${hdfs.status.port}")
	private String hdfsStatusPort;

	private HdfsBrowser() {
		confMap = new HashMap<>();
	}

	public List<FileInfo> listMember(String path) throws IOException, InterruptedException, URISyntaxException {
		String[] arrHdfs = this.hdfs.split(",");
		String hdfsUrl = "";
		for (String address : arrHdfs) {
			log.info("Checking hdfs: " + address);
			try {
				if ("active".equals(checkStatus(address))) {
					hdfsUrl = "hdfs://" + address;
					break;
				}
//                hdfsUrl = "hdfs://" + address;
//                break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Configuration conf = getConfiguration(hdfsUrl);
		FileSystem fs = FileSystem.get(new URI(path), conf, "admin");
		FileStatus[] fsStatus = fs.listStatus(new Path(path));
		return Arrays.stream(fsStatus).map(FileInfo::new).collect(Collectors.toList());
	}

	private Configuration getConfiguration(String hdfsUrl) {
		Configuration conf = confMap.get(hdfsUrl);
		if (conf == null) {
			conf = createNewConfiguration(hdfsUrl);
		}
		return conf;
	}

	private Configuration createNewConfiguration(String hdfsUrl) {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsUrl);
		confMap.put(hdfsUrl, conf);
		return conf;
	}

	public String checkStatus(String ipNameNode) throws Exception {
		String url = "http://" + ipNameNode.split(":")[0] + ":" + this.hdfsStatusPort
				+ "/jmx?qry=Hadoop:service=NameNode,name=NameNodeStatus";
		log.info("Check status url: " + url);
		JSONObject json = readJsonFromUrl(url);
		return (String) json.getJSONArray("beans").getJSONObject(0).get("State");
	}

	public JSONObject readJsonFromUrl(String urlPath) {
		try {
			URL url = new URL(urlPath);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(3000);
			urlConnection.setReadTimeout(3000);
			InputStream is = urlConnection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			String jsonText = readAll(rd);

			return new JSONObject(jsonText);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}

	public String readAll(Reader rd) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		int code;
		while ((code = rd.read()) != -1) {
			stringBuilder.append((char) code);
		}
		return stringBuilder.toString();
	}

}
