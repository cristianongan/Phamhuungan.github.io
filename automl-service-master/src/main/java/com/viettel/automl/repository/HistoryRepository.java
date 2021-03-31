package com.viettel.automl.repository;

import com.viettel.automl.dto.object.CurentStatusOfHistoryDTO;
import com.viettel.automl.dto.object.InterpreterDTO;
import com.viettel.automl.dto.object.ItemOfInterpreterDTO;
import com.viettel.automl.model.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
	@Query(value = "select CURRENT_STATUS as statusCode , count(CURRENT_STATUS) as percent from history group by CURRENT_STATUS", nativeQuery = true)
	List<CurentStatusOfHistoryDTO> getPercent();

	List<HistoryEntity> findByModelId(Long modelId);

	@Query(value = "Select distinct interpreter from history", nativeQuery = true)
	List<InterpreterDTO> getInterpreter();

	@Query(value = "Select m.MODEL_NAME as modelName , m.CREATE_USER as createUser, p.PROJECT_NAME as projectName ,cf.TASK as task, h.CURRENT_STATUS as currentStatus, h.LAST_UPDATE_TIME as lastUpdateTime\n"
			+ "from PROJECT p\n" + "INNER JOIN CONFIG_FLOW cf ON p.PROJECT_ID = cf.PROJECT_ID\n"
			+ "INNER JOIN MODEL m ON cf.MODEL_ID = m.MODEL_ID\n" + "INNER JOIN HISTORY h ON m.MODEL_ID = h.MODEL_ID\n"
			+ "WHERE h.INTERPRETER = ? ", nativeQuery = true)
	List<ItemOfInterpreterDTO> getQueueByInterpreter(String interpreter);

	List<HistoryEntity> findByCurrentStatus(Long currentStaus);
}
