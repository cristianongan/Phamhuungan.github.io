package com.viettel.automl.service.impl;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.*;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.model.*;
import com.viettel.automl.repository.*;
import com.viettel.automl.service.ModelService;
import com.viettel.automl.task.HistoryTask;
import it.sauronsoftware.cron4j.Scheduler;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.stream.Stream;

@Service
public class ModelServiceImpl extends BaseService implements ModelService {

	private final ModelRepository modelRepository;
	private final HistoryRepository historyRepository;
	private final ConfigFlowRepository configFlowRepository;
	private final ProjectRepository projectRepository;
	private final ConnectionRepository connectionRepository;
	private final ModelModelTypeMapRepository modelModelTypeMapRepository;
	private final SubModelRepository subModelRepository;
	private final ParameterInUseRepository parameterInUseRepository;

	public ModelServiceImpl(ModelRepository modelRepository, HistoryRepository historyRepository,
			ConfigFlowRepository configFlowRepository, ProjectRepository projectRepository,
			ConnectionRepository connectionRepository, ModelModelTypeMapRepository modelModelTypeMapRepository,
			SubModelRepository subModelRepository, ParameterInUseRepository parameterInUseRepository) {
		this.modelRepository = modelRepository;
		this.historyRepository = historyRepository;
		this.configFlowRepository = configFlowRepository;
		this.projectRepository = projectRepository;
		this.connectionRepository = connectionRepository;
		this.modelModelTypeMapRepository = modelModelTypeMapRepository;
		this.subModelRepository = subModelRepository;
		this.parameterInUseRepository = parameterInUseRepository;
	}

	@Override
	public ModelDTO create(ModelDTO modelDTO) {
		this.validateModel(modelDTO);
		modelDTO.setCreateTime(Instant.now());
		modelDTO.setCreateUser(super.getCurrentUser());
		ModelEntity modelEntity = modelRepository.save(super.map(modelDTO, ModelEntity.class));
		return super.map(modelEntity, ModelDTO.class);
	}

	@Override
	public List<ModelDTO> getAll() {
		return super.mapList(modelRepository.findAll(), ModelDTO.class);
	}

	@Override
	public List<ModelDTO> getRecent(Pageable pageable) {
		List<ModelDTO> modelDTOS = modelRepository.getRecent(pageable);
		modelDTOS.forEach(m -> {
			String[] taskStr = m.getTask().split(",");
			int[] taskInt = Stream.of(taskStr).mapToInt(Integer::parseInt).toArray();
			m.setTasks(taskInt);
		});
		return modelDTOS;
	}

	@Override
	public List<ModelDTO> searchModel(ProjectDTO projectDTO) {
		List<ModelDTO> modelDTOS = modelRepository.searchModel(projectDTO);
//        modelDTOS.forEach(m -> {
//            String[] taskStr = m.getTask().split("/");
//            int[] taskInt = Stream.of(taskStr).mapToInt(Integer::parseInt).toArray();
//            m.setTasks(taskInt);
//        });
		return modelDTOS;
	}

	@Override
	public List<ModelDTO> searchModelHaveModelType(ProjectDTO projectDTO) {
		List<ModelDTO> modelDTOS = modelRepository.searchModelHaveModelType(projectDTO);
//        modelDTOS.forEach(m -> {
//            String[] taskStr = m.getTask().split("/");
//            int[] taskInt = Stream.of(taskStr).mapToInt(Integer::parseInt).toArray();
//            m.setTasks(taskInt);
//        });
		return modelDTOS;
	}

	@Override
	public ModelDTO findOne(Long id) {
		ModelEntity entity = modelRepository.findById(id)
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Model"));
		ModelDTO dto = super.map(entity, ModelDTO.class);
		List<HistoryDTO> historyDTOS = super.mapList(historyRepository.findByModelId(dto.getModelId()),
				HistoryDTO.class);
		historyDTOS.forEach(m -> {
			String[] taskStr = m.getTask().split(",");
			int[] taskInt = Stream.of(taskStr).mapToInt(Integer::parseInt).toArray();
			m.setTasks(taskInt);
		});
		dto.setHistoryDTOS(historyDTOS);

		// get config_flow
		List<ConfigFlowEntity> configFlowEntity = configFlowRepository.findByModelId(id);
		if (configFlowEntity.size() != 0) {
			ConfigFlowEntity e = configFlowEntity.get(0);
			ProjectEntity projectEntity = projectRepository.findById(e.getProjectId())
					.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Project for Model"));
			ConnectionEntity connectionEntity = connectionRepository.findById(e.getConnectionId())
					.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Connection for Model"));
//            dto.setProjectDTO(super.map(projectRepository.findById(e.getProjectId()).get(), ProjectDTO.class));
			dto.setConfigFlowDTO(super.map(e, ConfigFlowDTO.class));
			dto.setConnectionDTO(super.map(connectionEntity, ConnectionDTO.class));
			dto.setProjectName(projectEntity.getProjectName());
		}

		return dto;
	}

	@Override
	@Transactional
	public ModelHolderDTO runNewModel(ModelHolderDTO holderDTO) {
		ModelHolderDTO newHolder = new ModelHolderDTO();
		ModelDTO modelDTO = holderDTO.getModelDTO();
		modelDTO.setCreateTime(Instant.now());
		modelDTO.setCreateUser(super.getCurrentUser());
		ModelEntity modelEntity = modelRepository.save(super.map(modelDTO, ModelEntity.class));
		newHolder.setModelDTO(super.map(modelEntity, ModelDTO.class));

		ConfigFlowDTO configFlowDTO = holderDTO.getConfigFlowDTO();

//        if (holderDTO.getProjectDTO().getProjectId() == null) {
//            ProjectEntity projectEntity = projectRepository.save(super.map(holderDTO.getProjectDTO(), ProjectEntity.class));
//            configFlowDTO.setProjectId(projectEntity.getProjectId());
//        } else {
//            configFlowDTO.setProjectId(holderDTO.getProjectDTO().getProjectId());
//        }

		// save configFlow
		configFlowDTO.setModelId(modelEntity.getModelId());
		configFlowDTO.setConnectionId(holderDTO.getConnectionId());
		configFlowDTO.setCreateTime(Instant.now());
		configFlowDTO.setCreateUser(super.getCurrentUser());
		configFlowDTO.setRunType(0L);
		ConfigFlowEntity configFlowEntity = configFlowRepository
				.save(super.map(holderDTO.getConfigFlowDTO(), ConfigFlowEntity.class));
		newHolder.setConfigFlowDTO(super.map(configFlowEntity, ConfigFlowDTO.class));

		// model mode = 0 and 1
		if (modelEntity.getModelMode() == 0) {
//            holderDTO.getModelTypeDTOS().forEach(e -> {
//                ModelModelTypeMapEntity modelModelTypeMapEntity = new ModelModelTypeMapEntity();
//                modelModelTypeMapEntity.setModelId(modelEntity.getModelId());
////                modelModelTypeMapEntity.setModelTypeId(e.getModelTypeId());
//                modelModelTypeMapEntity.setModelTypeId(e.getParameterDTOS().get(0).getModelTypeId());
//                modelModelTypeMapRepository.save(modelModelTypeMapEntity);
//
//                e.getParameterDTOS().forEach(param -> {
//                    ParameterInUseEntity parameterInUseEntity = super.map(param, ParameterInUseEntity.class);
//                    parameterInUseEntity.setConfigFlowId(configFlowEntity.getConfigFlowId());
//                    parameterInUseEntity.setModelId(modelEntity.getModelId());
//                    parameterInUseRepository.save(parameterInUseEntity);
//                });
//            });
			holderDTO.getModelTypeDTOS().getParameterDTOS().forEach(p -> {
				if (p.size() == 0)
					return;
				ModelModelTypeMapEntity modelModelTypeMapEntity = new ModelModelTypeMapEntity();
				modelModelTypeMapEntity.setModelId(modelEntity.getModelId());
//                modelModelTypeMapEntity.setModelTypeId(e.getModelTypeId());
				modelModelTypeMapEntity.setModelTypeId(p.get(0).getModelTypeId());
//                modelModelTypeMapEntity.setModelTypeId(p.size() > 0 ? p.get(0).getModelTypeId() : );
				modelModelTypeMapRepository.save(modelModelTypeMapEntity);

				p.forEach(param -> {
					ParameterInUseEntity parameterInUseEntity = super.map(param, ParameterInUseEntity.class);
					if (param.getDataType().equals(0L)) {
						String[] strT = param.getParameterValue().split(",");
						StringJoiner stringJoiner = new StringJoiner(",");
						for (String str : strT) {
							stringJoiner.add("'" + str + "'");
						}
						parameterInUseEntity.setParameterValue(stringJoiner.toString());
					}
					parameterInUseEntity.setConfigFlowId(configFlowEntity.getConfigFlowId());
					parameterInUseEntity.setModelId(modelEntity.getModelId());
					parameterInUseRepository.save(parameterInUseEntity);
				});
			});

		} else {
			holderDTO.getSubModelDTOS().forEach(s -> {
				SubModelEntity subModelEntity = subModelRepository.save(super.map(s, SubModelEntity.class));
				s.getParameterDTOS().forEach(param -> {
					ParameterInUseEntity parameterInUseEntity = super.map(param, ParameterInUseEntity.class);
					parameterInUseEntity.setConfigFlowId(configFlowEntity.getConfigFlowId());
					parameterInUseEntity.setModelId(modelEntity.getModelId());
					parameterInUseRepository.save(parameterInUseEntity);
				});
			});
		}

		HistoryEntity historyEntity = new HistoryEntity();
		historyEntity.setConfigFlowId(configFlowEntity.getConfigFlowId());
		historyEntity.setModelId(modelEntity.getModelId());
		historyEntity.setRunNote(configFlowEntity.getRunNote());
		historyEntity.setTask(configFlowEntity.getTask());
		historyEntity.setCurrentStatus(0L);
		historyRepository.save(historyEntity);

		return newHolder;
	}

	@Override
	@Transactional
	public ConfigFlowDTO runExistModel(ConfigFlowDTO dto) {
		dto.setCreateTime(Instant.now());
		dto.setCreateUser(super.getCurrentUser());
		dto.setRunType(1L);
		ConfigFlowEntity oldEntity = configFlowRepository.findByModelIdAndRunType(dto.getModelId(), 0L).get(0);

		ConfigFlowDTO dtoClone = super.map(oldEntity, ConfigFlowDTO.class);
		ConfigFlowEntity configFlowEntity = super.map(dtoClone, ConfigFlowEntity.class);
		configFlowEntity.setConfigFlowId(null);
		configFlowEntity.setProjectId(dto.getProjectId());
		configFlowEntity.setRunNote(dto.getRunNote());
		configFlowEntity.setTask(dto.getTask());
		configFlowEntity.setSchedule(dto.getSchedule());
		configFlowEntity.setTrainingTable(dto.getTrainingTable());
		configFlowEntity.setTestingTable(dto.getTestingTable());
		configFlowEntity.setInferenceTable(dto.getInferenceTable());
		configFlowEntity.setValidationTable(dto.getValidationTable());
		configFlowEntity.setOutputTableName(dto.getOutputTableName());
		configFlowEntity.setOutputTableMode(dto.getOutputTableMode());
		configFlowEntity.setOutputTablePartition(dto.getOutputTablePartition());
		configFlowEntity.setLocation(dto.getLocation());
		configFlowEntity.setRunType(1L);

		configFlowEntity = configFlowRepository.save(configFlowEntity);

		List<ParameterInUseEntity> list = parameterInUseRepository.findByConfigFlowId(oldEntity.getConfigFlowId());

		if (list.size() == 0) {
			throw new ServerException(ErrorCode.NOT_FOUND, "Parameter In Use for Model");
		} else {
//            ParameterInUserDTO newParam = super.map(list.get(0), ParameterInUserDTO.class);
//            newParam.setParameterInUseId(null);
//            newParam.setConfigFlowId(configFlowEntity.getConfigFlowId());
//            parameterInUseRepository.save(super.map(newParam, ParameterInUseEntity.class));
			List<ParameterInUserDTO> dtos = super.mapList(list, ParameterInUserDTO.class);
			ConfigFlowEntity finalConfigFlowEntity = configFlowEntity;
			dtos.forEach(e -> {
				e.setParameterInUseId(null);
				e.setConfigFlowId(finalConfigFlowEntity.getConfigFlowId());
				parameterInUseRepository.save(super.map(e, ParameterInUseEntity.class));
			});
		}

		// save to history
		HistoryEntity historyEntity = new HistoryEntity();
		historyEntity.setConfigFlowId(configFlowEntity.getConfigFlowId());
		historyEntity.setModelId(configFlowEntity.getModelId());
		historyEntity.setRunNote(configFlowEntity.getRunNote());
		historyEntity.setTask(configFlowEntity.getTask());
		historyEntity.setCurrentStatus(0L);
		historyEntity.setCreateTime(Instant.now());

		if (null == dto.getSchedule()) {
			historyRepository.save(historyEntity);
		} else {
			Scheduler scheduler = new Scheduler();
			String taskId = scheduler.schedule(configFlowEntity.getSchedule(),
					new HistoryTask(historyEntity, historyRepository));
			scheduler.start();
		}

		return super.map(configFlowEntity, ConfigFlowDTO.class);
	}

	private void validateModel(ModelDTO dto) {
		List<ModelEntity> entities = modelRepository.findByModelName(dto.getModelName());

		if (entities.size() != 0) {
			ModelEntity entity = entities.get(0);
			if ((dto.getModelId() == null) || dto.getModelId().equals(entity.getModelId()))
				throw new ServerException(ErrorCode.ALREADY_EXIST, "Model Name");
		}
	}
}
