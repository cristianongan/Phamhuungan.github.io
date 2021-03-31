package com.viettel.automl.service.impl;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.ConnectionDTO;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.model.ConnectionEntity;
import com.viettel.automl.repository.ConnectionRepository;
import com.viettel.automl.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConnectionServiceImpl extends BaseService implements ConnectionService {

	private final ConnectionRepository connectionRepository;

	public ConnectionServiceImpl(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}

	@Override
	public ConnectionDTO create(ConnectionDTO dto) {
		this.validateConnection(dto);
		ConnectionEntity entity = connectionRepository.save(super.map(dto, ConnectionEntity.class));
		return super.map(entity, ConnectionDTO.class);
	}

	@Override
	public List<ConnectionDTO> getAll() {
		return super.mapList(connectionRepository.findAll(), ConnectionDTO.class);
	}

	private void validateConnection(ConnectionDTO dto) {
		List<ConnectionEntity> entities = connectionRepository.findByConnectionName(dto.getConnectionName());

		if (entities.size() != 0) {
			ConnectionEntity entity = entities.get(0);
			if ((dto.getConnectionId() == null) || dto.getConnectionId().equals(entity.getConnectionId())) {
				throw new ServerException(ErrorCode.ALREADY_EXIST, "Connection Name");
			}
		}
	}
}
