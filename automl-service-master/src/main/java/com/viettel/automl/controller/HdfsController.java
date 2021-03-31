package com.viettel.automl.controller;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.dto.response.FileInfo;
import com.viettel.automl.service.impl.HdfsBrowser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hdfs")
public class HdfsController {

	private final HdfsBrowser hdfsBrowser;

	public HdfsController(HdfsBrowser hdfsBrowser) {
		this.hdfsBrowser = hdfsBrowser;
	}

	@GetMapping
	public DataListResponse<FileInfo> getMember(@RequestParam(value = "path") Optional<String> optionalPath)
			throws MissingServletRequestParameterException {
		String path = optionalPath.orElseThrow(() -> new MissingServletRequestParameterException("path", "String"));
		DataListResponse<FileInfo> res = new DataListResponse<>();
		List<FileInfo> data = new ArrayList<>();
		try {
			data = hdfsBrowser.listMember(path);
			res.setCode(ErrorCode.OK.getCode());
		} catch (InterruptedException | IOException | URISyntaxException e) {
			res.setCode(ErrorCode.SERVER_ERROR.getCode());
			res.setMessage(e.getMessage());
		}
		res.setData(data);
		return res;
	}

	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	public DataListResponse<?> handlerMissingServletRequestParameterException(
			MissingServletRequestParameterException e) {
		DataListResponse res = new DataListResponse();
		res.setCode(ErrorCode.MISSING_PARAMS.getCode());
		res.setMessage(e.getMessage());
		return res;
	}
}
