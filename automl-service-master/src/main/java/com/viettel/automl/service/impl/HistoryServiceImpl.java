package com.viettel.automl.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.*;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.model.*;
import com.viettel.automl.repository.*;
import com.viettel.automl.service.HistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl extends BaseService implements HistoryService {

	private final HistoryRepository historyRepository;
	private final ModelRepository modelRepository;
	private final ConfigFlowRepository configFlowRepository;
	private final ConnectionRepository connectionRepository;
	private final ModelTypeRepository modelTypeRepository;
	private final ParameterInUseRepository parameterInUseRepository;
	private final HiveDBService hiveDBService;

	public HistoryServiceImpl(HistoryRepository historyRepository, ModelRepository modelRepository,
			ConfigFlowRepository configFlowRepository, ConnectionRepository connectionRepository,
			ModelTypeRepository modelTypeRepository, ParameterInUseRepository parameterInUseRepository,
			HiveDBService hiveDBService) {
		this.historyRepository = historyRepository;
		this.modelRepository = modelRepository;
		this.configFlowRepository = configFlowRepository;
		this.connectionRepository = connectionRepository;
		this.modelTypeRepository = modelTypeRepository;
		this.parameterInUseRepository = parameterInUseRepository;
		this.hiveDBService = hiveDBService;
	}

	@Override
	public List<CurentStatusOfHistoryDTO> getPercent() {
		return historyRepository.getPercent();
	}

	@Override
	public List<SubmitQueueDTO> getSubmitedQueues() {
		List<InterpreterDTO> interpreters = historyRepository.getInterpreter();
		List<ItemOfInterpreterDTO> listItems = new ArrayList<>();
		List<SubmitQueueDTO> list = new ArrayList<>();
		for (InterpreterDTO item : interpreters) {
			if (null != item) {
				listItems = historyRepository.getQueueByInterpreter(item.getInterpreter());
				SubmitQueueDTO submitQueueDTO = new SubmitQueueDTO();
				submitQueueDTO.setInterpreter(item.getInterpreter());
				submitQueueDTO.setItem(listItems);
				list.add(submitQueueDTO);
			}
		}

		return list;
	}

	@Override
	public List<HistoryDTO> getHistories(Long currentStatus) {
		List<HistoryDTO> dtos = super.mapList(historyRepository.findByCurrentStatus(currentStatus), HistoryDTO.class);
		dtos.forEach(dto -> {
			ConfigFlowEntity configFlowEntity = configFlowRepository.findById(dto.getConfigFlowId())
					.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "ConfigFlow for History"));
			dto.setConfigFlowDTO(super.map(configFlowEntity, ConfigFlowDTO.class));
		});
		return dtos;
	}

	@Override
	public HistoryDTO findOne(Long id) throws JsonProcessingException {
		HistoryEntity entity = historyRepository.findById(id)
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "History"));
		HistoryDTO dto = super.map(entity, HistoryDTO.class);

		ModelEntity modelEntity = modelRepository.findById(dto.getModelId())
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Model for this History"));

		ConfigFlowDTO configFlowDTO = super.map(
				configFlowRepository.findById(entity.getConfigFlowId())
						.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "ConfigFlow for this History")),
				ConfigFlowDTO.class);
		configFlowDTO.setFeatureColumnArr(this.splitBy(configFlowDTO.getFeatureColumns(), ","));
		configFlowDTO.setOutputColumnArr(this.splitBy(configFlowDTO.getOutputColumns(), ","));

		ConnectionEntity connectionEntity = connectionRepository.findById(configFlowDTO.getConnectionId())
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Connection for this History"));

		List<ParameterInUseEntity> parameterInUseEntities = parameterInUseRepository
				.findByConfigFlowId(configFlowDTO.getConfigFlowId());

		if (configFlowDTO.getRunType() != null && configFlowDTO.getRunType().equals(0L)) {
			List<Map<String, Object>> columns = hiveDBService.executeQuery(
					configFlowDTO.getTrainingTable() + " limit 5 ", new HashMap<>(), configFlowDTO.getConnectionId());
			if (columns.size() != 0) {
				Map<String, Object> columnsFeature = new HashMap<>(columns.get(0));
				Map<String, Object> columnsOutput = new HashMap<>(columns.get(0));
				configFlowDTO.getFeatureColumnArr().forEach(columnsFeature::remove);
				configFlowDTO.getOutputColumnArr().forEach(columnsOutput::remove);
				dto.setColumnsFeature(new ArrayList<String>(columnsFeature.keySet()));
				dto.setColumnsOutput(new ArrayList<String>(columnsOutput.keySet()));
			}
		}

		dto.setModelDTO(super.map(modelEntity, ModelDTO.class));
		dto.setConfigFlowDTO(configFlowDTO);
		dto.setConnectionDTO(super.map(connectionEntity, ConnectionDTO.class));
		List<ParameterInUserDTO> parameterInUserDTOS = super.mapList(parameterInUseEntities, ParameterInUserDTO.class);
		Set<Long> ids = new HashSet<>();
		parameterInUserDTOS.forEach(e -> {
			ids.add(e.getModelTypeId());
			ModelTypeEntity modelTypeEntity = modelTypeRepository.findById(e.getModelTypeId())
					.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "ModelType for ParameterInUse"));
			e.setModelTypeName(modelTypeEntity.getModelTypeName());
		});

		List<List<ParameterInUserDTO>> temp = new ArrayList<>();

		ids.forEach(e -> {
			temp.add(parameterInUserDTOS.stream().filter(v -> v.getModelTypeId().equals(e))
					.collect(Collectors.toList()));
		});

		dto.setSearchSpace(temp);

		return dto;
	}

	@Override
	public HistoryDTO update(HistoryDTO dto) {
		HistoryEntity entity = super.map(dto, HistoryEntity.class);
		entity = historyRepository.save(entity);
		return super.map(entity, HistoryDTO.class);
	}

	private List<String> splitBy(String str, String splitChar) {
		if (!StringUtils.isEmpty(str)) {
			return Arrays.asList(str.split(splitChar));
		} else {
			return new ArrayList<>();
		}
	}

	private String createSearchSpace(List<ParameterInUseEntity> entities) throws JsonProcessingException {
		Set<Long> ids = new HashSet<>();
		entities.forEach(e -> {
			ids.add(e.getModelTypeId());
		});
		List<SearchSpaceDTO> dtos = new ArrayList<>();
		ids.forEach(id -> {
			ModelTypeEntity modelTypeEntity = modelTypeRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Resource not found"));

			SearchSpaceDTO dto = new SearchSpaceDTO();
			dto.setType(modelTypeEntity.getModelTypeName());
			Optional<ParameterInUseEntity> entityF = entities.stream()
					.filter(e -> "featureSubsetStrategy".equals(e.getParameterName())).findFirst();
			entityF.ifPresent(e -> {
				if (null != e.getParameterValue()) {
					String str = "hp.choice('" + dto.getType() + "',[";
					str += e.getParameterValue() + "])";
					dto.setFeatureSubsetStrategy(str);
				}
			});

			Optional<ParameterInUseEntity> entityN = entities.stream()
					.filter(e -> "numTrees".equals(e.getParameterName())).findFirst();
			entityN.ifPresent(e -> {
				String str = "";
				if (e.getUniform().equals(0L)) {
					str += "hp.quniform('";
				} else {
					str += "hp.qnormal('";
				}
				str += dto.getType() + "_numTrees',";
				str += e.getMin() + "," + e.getMax() + "," + e.getStep() + ")";
				dto.setNumTrees(str);
			});

			Optional<ParameterInUseEntity> entityMD = entities.stream()
					.filter(e -> "maxDepth".equals(e.getParameterName())).findFirst();
			entityMD.ifPresent(e -> {
				String str = "";
				if (e.getUniform().equals(0L)) {
					str += "hp.quniform('";
				} else {
					str += "hp.qnormal('";
				}
				str += dto.getType() + "_maxDepth',";
				str += e.getMin() + "," + e.getMax() + "," + e.getStep() + ")";
				dto.setMaxDepth(str);
			});

			Optional<ParameterInUseEntity> entityMB = entities.stream()
					.filter(e -> "maxBins".equals(e.getParameterName())).findFirst();
			entityMB.ifPresent(e -> {
				String str = "";
				if (e.getUniform().equals(0L)) {
					str += "hp.quniform('";
				} else {
					str += "hp.qnormal('";
				}
				str += dto.getType() + "_maxBins',";
				str += e.getMin() + "," + e.getMax() + "," + e.getStep() + ")";
				dto.setMaxBins(str);
			});

			Optional<ParameterInUseEntity> entityMI = entities.stream()
					.filter(e -> "minInstancesPerNode".equals(e.getParameterName())).findFirst();
			entityMI.ifPresent(e -> {
				String str = "";
				if (e.getUniform().equals(0L)) {
					str += "hp.quniform('";
				} else {
					str += "hp.qnormal('";
				}
				str += dto.getType() + "_minInstancesPerNode',";
				str += e.getMin() + "," + e.getMax() + "," + e.getStep() + ")";
				dto.setMinInstancesPerNode(str);
			});

			Optional<ParameterInUseEntity> entityS = entities.stream()
					.filter(e -> "subsamplingRate".equals(e.getParameterName())).findFirst();
			entityS.ifPresent(e -> {
				String str = "";
				if (e.getUniform().equals(0L)) {
					str += "hp.uniform('";
				} else {
					str += "hp.normal('";
				}
				str += dto.getType() + "_subsamplingRate',";
				str += e.getMin() + "," + e.getMax() + ")";
				dto.setSubsamplingRate(str);
			});

			Optional<ParameterInUseEntity> entityMIG = entities.stream()
					.filter(e -> "minInfoGain".equals(e.getParameterName())).findFirst();
			entityMIG.ifPresent(e -> {
				String str = "";
				if (e.getUniform().equals(0L)) {
					str += "hp.uniform('";
				} else {
					str += "hp.normal('";
				}
				str += dto.getType() + "_minInfoGain',";
				str += e.getMin() + "," + e.getMax() + ")";
				dto.setMinInfoGain(str);
			});
			dtos.add(dto);
		});

		// to json
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(dtos);
	}
}
