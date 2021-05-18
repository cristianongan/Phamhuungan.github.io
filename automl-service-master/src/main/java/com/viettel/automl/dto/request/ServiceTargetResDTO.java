package com.viettel.automl.dto.request;

import com.viettel.automl.dto.base.BaseDTO;
import com.viettel.automl.util.Const;
import com.viettel.automl.validator.FieldValue;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ServiceTargetResDTO extends BaseDTO {
	@NotNull
	@FieldValue(numbers = { Const.SERVICE_TYPE.CT_KINHDOANH, Const.SERVICE_TYPE.CT_NGUOIDUNG })
	public Integer checkServiceType;
	@NotNull
	public Long deptId;
}
