package com.viettel.automl.service.impl;

import com.viettel.automl.dto.base.BaseDTO;
import com.viettel.automl.model.UsersEntity;
import com.viettel.automl.util.Const;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService {

	@Autowired
	private ModelMapper modelMapper;

//    @Autowired
//    private ActionAuditRepository actionAuditRepository;

	protected <T, E> T map(E entity, Class<T> clazz) {
		return modelMapper.map(entity, clazz);
	}

	protected <T, E extends BaseDTO> T map(E dto, Class<T> clazz) {
		return modelMapper.map(dto, clazz);
	}

	protected <T, E> void mapData(T source, E destination) {
		modelMapper.map(source, destination);
	}

	protected <T, E> List<T> mapList(List<E> inputData, Class<T> clazz) {
		return CollectionUtils.isEmpty(inputData) ? Collections.emptyList()
				: inputData.stream().map(i -> modelMapper.map(i, clazz)).collect(Collectors.toList());
	}

	protected String getCurrentUser() {
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}
//
//    protected Long getCurrentUserId() {
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        UserEntity user = (UserEntity) auth.getCredentials();
////        return user.getId();
//        return this.getCurrentUser().getId();
//    }
//
//    protected String getCurrentUsername() {
//        return this.getCurrentUser().getUsername();
//    }
//
//    protected String getClientIp() {
//        // TODO getUsername by Authen
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String xfHeader = request.getHeader("X-Forwarded-For");
//        if (xfHeader == null) {
//            return request.getRemoteAddr();
//        }
//        return xfHeader.split(",")[0];
//    }
////
////    protected String getServerIp() {
////        // TODO getUsername by Authen
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        return request.getLocalAddr();
////    }
////
////    protected Integer getServerPort() {
////        // TODO getUsername by Authen
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        return request.getServerPort();
////    }
//
//    protected Long getCurrentUserDeptId() {
//        return this.getCurrentUser().getDeptId();
//    }
//
//    protected Long getCurrentUserDeptLevel() {
//        return 1L;
//    }
//
//    public ActionAuditDto createLogDto(String table, String action, String id, String user, String ip,
//                                       Object oldValue, Object newValue) {
//        return new ActionAuditDto.Builder()
//                .tableName(table)
//                .action(action)
//                .objectId(id)
//                .user(user)
//                .userIP(ip)
//                .oldValue(oldValue)
//                .newValue(newValue)
//                .build();
//    }
//
//    public ActionAuditDto createLogDto(String table, String action, Long id, Object oldValue, Object newValue) {
//        return this.createLogDto(
//                table,
//                action,
//                id != null ? String.valueOf(id) : null,
//                this.getCurrentUsername(),
//                this.getClientIp(),
//                oldValue,
//                newValue);
//    }
//
//    protected String toJson(Object obj) {
//        return ReflectionToStringBuilder.toString(obj, ToStringStyle.JSON_STYLE);
//    }
//
//    protected ActionAuditDto.Builder defaultLogBuilder() {
//        return new ActionAuditDto.Builder()
//                .user(this.getCurrentUsername())
//                .userIP(this.getClientIp())
//                .createDate();
//    }
//
//    protected ActionAuditDto insertLog(String table, Long id, Object inserted) {
//        return this.defaultLogBuilder()
//                .tableName(table)
//                .action(Const.ACTION.INSERT)
//                .objectId(id)
//                .oldValueNull()
//                .newValue(inserted)
//                .build();
//    }
//
//    protected ActionAuditDto deleteLog(String table, Long id, Object deleted) {
//        return this.defaultLogBuilder()
//                .tableName(table)
//                .action(Const.ACTION.DELETE)
//                .objectId(id)
//                .oldValue(deleted)
//                .newValueNull()
//                .build();
//    }
//
//    protected ActionAuditDto updateLog(String table, Long id, Object old, Object updated) {
//        return this.defaultLogBuilder()
//                .tableName(table)
//                .action(Const.ACTION.UPDATE)
//                .objectId(id)
//                .oldValue(old)
//                .newValue(updated)
//                .build();
//    }
//
//    protected void saveLog(List<ActionAuditDto> actionAuditDtos) {
//        actionAuditRepository.saveAll(this.mapList(actionAuditDtos, ActionAuditEntity.class));
//    }
//
//    protected void saveLog(ActionAuditDto actionAuditDto) {
//        actionAuditRepository.save(this.map(actionAuditDto, ActionAuditEntity.class));
//    }

	protected Long prepareStatus(Long statusInput) {
		return null != statusInput ? statusInput : Const.STATUS.ACTIVE;
	}
}
