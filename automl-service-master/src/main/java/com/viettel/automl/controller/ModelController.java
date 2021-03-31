package com.viettel.automl.controller;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.*;
import com.viettel.automl.dto.response.BodyPara;
import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.dto.response.GenericResponse;
import com.viettel.automl.service.ModelService;
import com.viettel.automl.service.impl.ZeppelinBrowser;
import org.python.util.PythonInterpreter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/models")
public class ModelController {

	private final ModelService modelService;
	private final ZeppelinBrowser zeppelinBrowser;

	public ModelController(ModelService modelService, ZeppelinBrowser zeppelinBrowser) {
		this.modelService = modelService;
		this.zeppelinBrowser = zeppelinBrowser;
	}

	@PostMapping
	public GenericResponse<ModelDTO> create(@RequestBody ModelDTO dto) {
		return GenericResponse.success(modelService.create(dto));
	}

	@GetMapping
	public DataListResponse<ModelDTO> getAll() {
		return DataListResponse.success(modelService.getAll());
	}

	@GetMapping("/recent")
	public DataListResponse<ModelDTO> getRecent(Pageable pageable) {
		return DataListResponse.success(modelService.getRecent(pageable));
	}

	@PostMapping("/do-search")
	public DataListResponse<ModelDTO> doSearch(@RequestBody ProjectDTO projectDTO) {
		DataListResponse<ModelDTO> res = new DataListResponse<>();
		res.setData(modelService.searchModel(projectDTO));
		res.setCode(ErrorCode.OK.getCode());
		return res;
	}

	@PostMapping("/do-search-model-type")
	public DataListResponse<ModelDTO> doSearchHaveModelType(@RequestBody ProjectDTO projectDTO) {
		DataListResponse<ModelDTO> res = new DataListResponse<>();
		res.setData(modelService.searchModelHaveModelType(projectDTO));
		res.setCode(ErrorCode.OK.getCode());
		return res;
	}

	@GetMapping("/{id}")
	public GenericResponse<ModelDTO> getModel(@PathVariable Long id) {
		return GenericResponse.success(modelService.findOne(id));
	}

	@PostMapping("/run-new")
	public GenericResponse<?> runNewModel(@RequestBody ModelHolderDTO holderDTO) {
		return GenericResponse.success(modelService.runNewModel(holderDTO));
	}

	@PostMapping("/run-exist")
	public GenericResponse<?> runExistModel(@RequestBody ConfigFlowDTO dto) {
		return GenericResponse.success(modelService.runExistModel(dto));
	}

	@PostMapping("/python")
	public GenericResponse<HashMap<String, String>> runPythonCode(@RequestBody TextAreaDTO dto) {
		System.out.println(dto.getContent());
		HashMap<String, String> result = new HashMap<>();
		String[] parts = dto.getContent().split("\n");
		PythonInterpreter pInterpreter = new PythonInterpreter();
		for (int i = 0; i < parts.length; i++) {
			pInterpreter.exec(parts[i]);
			String[] part2 = parts[i].split("=");
			if (part2.length > 1) {
				String var = part2[0].trim();
				result.put(var, pInterpreter.get(var).toString());
			}
		}
		return new GenericResponse<>(result);
	}

	// paragraph zeppelin \\

	@GetMapping("/get-para")
	public GenericResponse<?> getPara(@RequestParam String notebookId, @RequestParam String paraId) {
		return GenericResponse.success(zeppelinBrowser.getParaInfo(notebookId, paraId));
	}

	@PostMapping("/update-para")
	public GenericResponse<?> updatePara(@RequestBody BodyPara body) {
		return GenericResponse.success(zeppelinBrowser.updateCode(body));
	}
}
