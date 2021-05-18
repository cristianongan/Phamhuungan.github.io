
/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.automl.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.exception.ServerException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Admin
 * @version 1.0
 */
@Slf4j
@UtilityClass
public class DataUtil {

	private static final String PHONE_PATTERN = "^[0-9]*$";
	private String saltSHA256 = "1";
	private String AES = "AES";
	private String DES = "DES";
	private static final String MISS_ENVIREMENT_SETTING = "{0} must be set in environment variable";
	private static final String YYYY_PT = "yyyy";
	private static final String YYYYmm_PT = "yyyyMM";

	/**
	 * Copy du lieu tu bean sang bean moi Luu y chi copy duoc cac doi tuong o ngoai
	 * cung, list se duoc copy theo tham chieu
	 * <p>
	 * Chi dung duoc cho cac bean java, khong dung duoc voi cac doi tuong dang nhu
	 * String, Integer, Long...
	 *
	 * @param source
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T cloneBean(T source) {
		try {
			if (source == null) {
				return null;
			}
			T dto = (T) source.getClass().getConstructor().newInstance();
			BeanUtils.copyProperties(source, dto);
			return dto;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/*
	 * Kiem tra Long bi null hoac zero
	 *
	 * @param value
	 * 
	 * @return
	 */
	public boolean isNullOrZero(Long value) {
		return (value == null || value.equals(0L));
	}

	public boolean isNullOrZero(Integer value) {
		return (value == null || value.equals(0));
	}

	/*
	 * Kiem tra Long bi null hoac zero
	 *
	 * @param value
	 * 
	 * @return
	 */

	/**
	 * Upper first character
	 *
	 * @param input
	 * @return
	 */
	public String upperFirstChar(String input) {
		if (DataUtil.isNullOrEmpty(input)) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	public Long safeToLong(Object obj1, Long defaultValue) {
		Long result = defaultValue;
		if (obj1 != null) {
			if (obj1 instanceof BigDecimal) {
				return ((BigDecimal) obj1).longValue();
			}
			if (obj1 instanceof BigInteger) {
				return ((BigInteger) obj1).longValue();
			}
			try {
				result = Long.parseLong(obj1.toString());
			} catch (Exception ignored) {
				log.error(ignored.getMessage(), ignored);
			}
		}

		return result;
	}

	/**
	 * @param obj1 Object
	 * @return Long
	 */
	public Long safeToLong(Object obj1) {
		return safeToLong(obj1, null);
	}

	public Double safeToDouble(Object obj1, Double defaultValue) {
		Double result = defaultValue;
		if (obj1 != null) {
			try {
				result = Double.parseDouble(obj1.toString());
			} catch (Exception ignored) {
				log.error(ignored.getMessage(), ignored);
			}
		}

		return result;
	}

	public Double safeToDouble(Object obj1) {
		return safeToDouble(obj1, 0.0);
	}

	public Short safeToShort(Object obj1, Short defaultValue) {
		Short result = defaultValue;
		if (obj1 != null) {
			try {
				result = Short.parseShort(obj1.toString());
			} catch (Exception ignored) {
				log.error(ignored.getMessage(), ignored);
			}
		}

		return result;
	}

	/**
	 * @param obj1
	 * @param defaultValue
	 * @return
	 * @author phuvk
	 */
	public int safeToInt(Object obj1, int defaultValue) {
		int result = defaultValue;
		if (obj1 != null) {
			try {
				result = Integer.parseInt(obj1.toString());
			} catch (Exception ignored) {
				log.error(ignored.getMessage(), ignored);
			}
		}

		return result;
	}

	/**
	 * @param obj1 Object
	 * @return int
	 */
	public int safeToInt(Object obj1) {
		return safeToInt(obj1, 0);
	}

	/**
	 * @param obj1 Object
	 * @return String
	 */
	public String safeToString(Object obj1, String defaultValue) {
		if (obj1 == null || obj1.toString().isEmpty()) {
			return defaultValue;
		}

		return obj1.toString();
	}

	public Boolean safeToBoolean(Object obj1) {
		if (obj1 == null || obj1 instanceof Boolean) {
			return (Boolean) obj1;
		}
		return false;
	}

	/**
	 * @param obj1 Object
	 * @return String
	 */
	public String safeToString(Object obj1) {
		return safeToString(obj1, "");
	}

	/**
	 * safe equal
	 *
	 * @param obj1 String
	 * @param obj2 String
	 * @return boolean
	 */
	public boolean safeEqual(String obj1, String obj2) {
		if (obj1 == obj2)
			return true;
		return ((obj1 != null) && (obj2 != null) && obj1.equals(obj2));
	}

	/**
	 * check null or empty Su dung ma nguon cua thu vien StringUtils trong apache
	 * common lang
	 *
	 * @param cs String
	 * @return boolean
	 */
	public boolean isNullOrEmpty(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean isNullOrEmpty(final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public boolean isNullOrEmpty(final Object obj) {
		return obj == null || obj.toString().isEmpty();
	}

	public boolean isNullOrEmpty(final Object[] collection) {
		return collection == null || collection.length == 0;
	}

	public boolean isNullOrEmpty(final Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Ham nay mac du nhan tham so truyen vao la object nhung gan nhu chi hoat dong
	 * cho doi tuong la string Chuyen sang dung isNullOrEmpty thay the
	 *
	 * @param obj1
	 * @return
	 */
	@Deprecated
	public boolean isStringNullOrEmpty(Object obj1) {
		return obj1 == null || "".equals(obj1.toString().trim());
	}

	public BigInteger length(BigInteger from, BigInteger to) {
		return to.subtract(from).add(BigInteger.ONE);
	}

	public BigDecimal add(BigDecimal number1, BigDecimal number2, BigDecimal... numbers) {
		List<BigDecimal> realNumbers = Lists.newArrayList(number1, number2);
		if (!DataUtil.isNullOrEmpty(numbers)) {
			Collections.addAll(realNumbers, numbers);
		}
		return realNumbers.stream().filter(x -> x != null).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public Long add(Long number1, Long number2, Long... numbers) {
		List<Long> realNumbers = Lists.newArrayList(number1, number2);
		if (!DataUtil.isNullOrEmpty(numbers)) {
			Collections.addAll(realNumbers, numbers);
		}
		return realNumbers.stream().filter(x -> x != null).reduce(0L, (x, y) -> x + y);
	}

	/**
	 * add
	 *
	 * @param obj1 BigDecimal
	 * @param obj2 BigDecimal
	 * @return BigDecimal
	 */
	public BigInteger add(BigInteger obj1, BigInteger obj2) {
		if (obj1 == null) {
			return obj2;
		} else if (obj2 == null) {
			return obj1;
		}

		return obj1.add(obj2);
	}

	/**
	 * Collect values of a property from an object list instead of doing a for:each
	 * then call a getter Consider using stream -> map -> collect of java 8 instead
	 *
	 * @param source       object list
	 * @param propertyName name of property
	 * @param returnClass  class of property
	 * @return value list of property
	 */
	@Deprecated
	public <T> List<T> collectProperty(Collection<?> source, String propertyName, Class<T> returnClass) {
		List<T> propertyValues = Lists.newArrayList();
		try {
			String getMethodName = "get" + upperFirstChar(propertyName);
			for (Object x : source) {
				Class<?> clazz = x.getClass();
				Method getMethod = clazz.getMethod(getMethodName);
				Object propertyValue = getMethod.invoke(x);
				if (propertyValue != null && returnClass.isAssignableFrom(propertyValue.getClass())) {
					propertyValues.add(returnClass.cast(propertyValue));
				}
			}
			return propertyValues;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Lists.newArrayList();
		}
	}

	/**
	 * Collect distinct values of a property from an object list instead of doing a
	 * for:each then call a getter Consider using stream -> map -> collect of java 8
	 * instead
	 *
	 * @param source       object list
	 * @param propertyName name of property
	 * @param returnClass  class of property
	 * @return value list of property
	 */
	@Deprecated
	public <T> Set<T> collectUniqueProperty(Collection<?> source, String propertyName, Class<T> returnClass) {
		List<T> propertyValues = collectProperty(source, propertyName, returnClass);
		return Sets.newHashSet(propertyValues);
	}

	public boolean isNullObject(Object obj1) {
		if (obj1 == null) {
			return true;
		}
		if (obj1 instanceof String) {
			return isNullOrEmpty(obj1.toString());
		}
		return false;
	}

	public boolean isCollection(Object ob) {
		return ob instanceof Collection || ob instanceof Map;
	}

	public String makeLikeParam(String s) {
		if (StringUtils.isEmpty(s))
			return s;
		s = s.trim().toLowerCase().replace("&", Const.DEFAULT_ESCAPE_CHAR + "&")
				.replace("%", Const.DEFAULT_ESCAPE_CHAR + "%").replace("_", Const.DEFAULT_ESCAPE_CHAR + "_");
		return "%" + s + "%";
	}

	/**
	 * @param date
	 * @param format yyyyMMdd, yyyyMMddhhmmss,yyyyMMddHHmmssSSS only
	 * @return
	 */
	public Integer getDateInt(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return Integer.parseInt(dateStr);
	}

	public Long getDateLong(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return Long.parseLong(dateStr);
	}

	public Integer getAbsoluteDate(Integer date, Integer relativeTime, Long timeType) throws ParseException {
		if (date == null)
			return 0;
		if (relativeTime == null)
			return date;
		SimpleDateFormat sdf = new SimpleDateFormat(Const.DATE_FORMAT_YYYYMMDD);
		Date newDate = sdf.parse(date.toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(newDate);
		if (Const.TIME_TYPE.DATE.equals(timeType) || Const.TIME_TYPE.DATE.toString().equals(timeType)) {
			cal.add(Calendar.DATE, relativeTime);
		} else if (Const.TIME_TYPE.MONTH.equals(timeType) || Const.TIME_TYPE.MONTH.toString().equals(timeType)) {
			cal.add(Calendar.MONTH, relativeTime);
		} else if (Const.TIME_TYPE.QUARTER.equals(timeType) || Const.TIME_TYPE.QUARTER.toString().equals(timeType)) {
			cal.add(Calendar.MONTH, relativeTime * 3);
		} else if (Const.TIME_TYPE.YEAR.equals(timeType) || Const.TIME_TYPE.YEAR.toString().equals(timeType)) {
			cal.add(Calendar.YEAR, relativeTime);
		}
		return Integer.parseInt(sdf.format(cal.getTime()));
	}

	private void resetTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
	}

	public Date getFirstDateOfMonth(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		resetTime(cal);
		return cal.getTime();
	}

	public Date getFirstDayOfQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		resetTime(cal);
		return cal.getTime();
	}

	public Date getFirstDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// Thang 1 thi calendar.MONTH = 0
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		resetTime(cal);
		return cal.getTime();
	}

	public Date getAbsoluteDate(Date date, Integer relativeTime, Object timeType) {
		if (relativeTime == null)
			return date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (Const.TIME_TYPE.DATE.equals(timeType) || Const.TIME_TYPE.DATE.toString().equals(timeType)) {
			cal.add(Calendar.DATE, relativeTime);
		} else if (Const.TIME_TYPE.MONTH.equals(timeType) || Const.TIME_TYPE.MONTH.toString().equals(timeType)) {
			cal.add(Calendar.MONTH, relativeTime);
		} else if (Const.TIME_TYPE.QUARTER.equals(timeType) || Const.TIME_TYPE.QUARTER.toString().equals(timeType)) {
			cal.add(Calendar.MONTH, (relativeTime) * 3);
		} else if (Const.TIME_TYPE.YEAR.equals(timeType) || Const.TIME_TYPE.YEAR.toString().equals(timeType)) {
			cal.add(Calendar.YEAR, relativeTime);
		}
		return cal.getTime();
	}

	public boolean isDate(String str, String format) {
		if (StringUtils.isEmpty(str))
			return false;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(str);
			return str.equals(sdf.format(date));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public Date getDatePattern(String date, String pattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}

	public String formatDatePattern(Integer prdId, String pattern) {
		String result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(prdId.toString());

			SimpleDateFormat sdf2 = new SimpleDateFormat(pattern);
			result = sdf2.format(date);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}

	public String formatQuarterPattern(Integer prdId) {
		String result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(prdId.toString());

			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
			String result2 = sdf2.format(date);

			result = (date.getMonth() / 3 + 1) + "/" + result2;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}

	public Date add(Date fromDate, int num, int type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(type, num);
		return cal.getTime();
	}

	public String dateToString(Date fromDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(fromDate);
	}

	public String dateToStringQuater(Date fromDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(fromDate);
		return (fromDate.getMonth() / 3 + 1) + "/" + year;
	}

	public String getTimeValue(Date date, Integer timeType) {
		SimpleDateFormat YYYY = new SimpleDateFormat(YYYY_PT);
		SimpleDateFormat YYYYMM = new SimpleDateFormat(YYYYmm_PT);
		String value = null;
		if (Const.TIME_TYPE.YEAR.equals(timeType)) {
			value = YYYY.format(date);
		} else if (Const.TIME_TYPE.MONTH.equals(timeType)) {
			value = YYYYMM.format(date);
		} else if (Const.TIME_TYPE.QUARTER.equals(timeType)) {
			value = YYYY.format(date);
			int quarter = date.getMonth() / 3 + 1;
			value = value + "" + quarter;
		}
		return value;
	}

	public String listStrToString(List<String> listStr) {
		StringJoiner stringJoiner = new StringJoiner(", ");
		listStr.forEach(e -> stringJoiner.add(e));
		return stringJoiner.toString();
	}

//
//    public String getExternalUrl(Environment env) {
//        String protocol = "http";
//        if (env.getProperty("server.ssl.key-store") != null) {
//            protocol = "https";
//        }
//        String serverPort = env.getProperty("server.port");
//        String contextPath = env.getProperty("server.servlet.context-path");
//        if (StringUtils.isBlank(contextPath)) {
//            contextPath = "/";
//        }
//        String hostAddress = "localhost";
//        try {
//            hostAddress = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException ignored) {
//            log.error(ignored.getMessage(), ignored);
//        }
//        return String.format("%s://%s:%s%s", protocol, hostAddress, serverPort, contextPath);
//    }
//
//    public Instant toInstant(Object object) {
//        if (object == null) {
//            return null;
//        }
//        if (object instanceof java.sql.Date) {
//            return Instant.ofEpochMilli(((java.sql.Date) object).getTime());
//        } else if (object instanceof Timestamp) {
//            return Instant.ofEpochMilli(((Timestamp) object).getTime());
//        }
//        return null;
//    }
//
//
//    /**
//     * Validate thong tin import
//     *
//     * @param value
//     * @param configColumn
//     * @return
//     */
//    public String validateValue(Object value, ConfigReportColumnDTO configColumn) {
//        if (Constants.IS_SHOW.equals(configColumn.getIsShow())
//            && Constants.IS_REQUIRED.equals(configColumn.getIsRequire())
//            && DataUtil.isNullOrEmpty(value)) {
//            return configColumn.getTitle() + " " + Translator.toLocale("error.configReport.isRequired");
//        }
//
//        if (DataUtil.isNullOrEmpty(value)) {
//            return Constants.VALIDATE_OK;
//        }
//
//        if (Constants.DATA_TYPE.DOUBLE.equalsIgnoreCase(configColumn.getDataType())) {
//            try {
//                String doubleValue = value.toString();
//                if(doubleValue.contains(",")) {
//                    doubleValue = doubleValue.replace(",",".");
//                }
//                Double.parseDouble(doubleValue);
//            } catch (Exception ex) {
//                log.error(ex.getMessage(), ex);
//                return configColumn.getTitle() + " " + Translator.toLocale("error.configReport.mustBeType") + " " + configColumn.getDataType();
//            }
//        } else if (Constants.DATA_TYPE.INT.equalsIgnoreCase(configColumn.getDataType()) ||
//            Constants.DATA_TYPE.BIGINT.equalsIgnoreCase(configColumn.getDataType()) ||
//            Constants.DATA_TYPE.LONG.equalsIgnoreCase(configColumn.getDataType())) {
//            try {
//                value = Double.valueOf(value.toString()).longValue();
//            } catch (Exception ex) {
//                log.error(ex.getMessage(), ex);
//                return configColumn.getTitle() + " " + Translator.toLocale("error.configReport.mustBeType") + " " + configColumn.getDataType();
//            }
//        } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(configColumn.getDataType())) {
//            if (value instanceof String) {
//                try {
//                    SimpleDateFormat sdfDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
//                    sdfDDMMYYYY.parse((String) value);
//                } catch (Exception ex) {
//                    log.error(ex.getMessage(), ex);
//                    try {
//                        SimpleDateFormat sdfDDMMYYYYHHmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                        sdfDDMMYYYYHHmmss.parse((String) value);
//                    } catch (Exception e) {
//                        log.error(e.getMessage(), e);
//                        return configColumn.getTitle() + " " + Translator.toLocale("error.configReport.mustBeType") + " " + configColumn.getDataType();
//                    }
//                }
//            }
//        }
//        if (!DataUtil.isNullOrZero(configColumn.getMaxLength())) {
//            if (value != null && value.toString().length() > configColumn.getMaxLength()) {
//                return configColumn.getTitle() + " " + Translator.toLocale("error.configReport.maxLengthColumn") + " " + configColumn.getMaxLength() + " " + Translator.toLocale("common.character");
//            }
//        }
//        if (!DataUtil.isNullOrEmpty(configColumn.getRegexPattern())) {
//            String regexPattern;
//            String message;
//            if (configColumn.getRegexPattern().contains(Constants.SEPARATE_REGEX)) {
//                regexPattern = configColumn.getRegexPattern().split(Constants.SEPARATE_REGEX)[0];
//                message = configColumn.getRegexPattern().split(Constants.SEPARATE_REGEX)[1];
//            } else {
//                regexPattern = configColumn.getRegexPattern().trim();
//                message = configColumn.getRegexPattern().trim();
//            }
//            Pattern pattern = Pattern.compile(regexPattern);
//            if (value != null && !pattern.matcher(value.toString()).matches()) {
//                return configColumn.getTitle() + " " + Translator.toLocale("error.configReport.invalidPattern") + " " + message;
//            }
//        }
//
//
//        return Constants.VALIDATE_OK;
//    }
//
//    /**
//     * validate ten tung cot cua template xem co dung thu tu khong?
//     *
//     * @param lstColumnConfig
//     * @param headers
//     */
//    public void validateTemplate(List<ConfigReportColumnDTO> lstColumnConfig, List<String> headers, String entityName, List<List<String>> lstData) {
//        if (!DataUtil.isNullOrEmpty(lstData) && !DataUtil.isNullOrEmpty(lstColumnConfig) && lstData.get(0).size() != lstColumnConfig.size()) {
//            throw new BadRequestAlertException(Translator.toLocale("error.configReport.templateInvalid"), entityName, "configReport.templateInvalid");
//        }
//        if (lstColumnConfig == null) {
//            throw new BadRequestAlertException(Translator.toLocale("error.configReport.templateInvalid"), entityName, "configReport.templateInvalid");
//        }
//    }
//
//
//    /**
//     * Build list column xuat file
//     *
//     * @param lstColumnCurrent
//     * @return
//     */
//    public List<ExcelColumn> getListExcelColumn(List<ConfigReportColumnDTO> lstColumnCurrent) {
//        List<ExcelColumn> lstColumn = lstColumnCurrent.stream().map(col -> {
//            ExcelColumn excelColumn = new ExcelColumn();
//            excelColumn.setColumn(col.getColumnName());
//            excelColumn.setTitle(col.getTitle());
//            if (Constants.DATA_TYPE.STRING.equalsIgnoreCase(col.getDataType())) {
//                excelColumn.setAlign(ExcelColumn.ALIGN_MENT.LEFT);
//            } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(col.getDataType())) {
//                excelColumn.setAlign(ExcelColumn.ALIGN_MENT.CENTER);
//            } else {
//                excelColumn.setAlign(ExcelColumn.ALIGN_MENT.RIGHT);
//            }
//            return excelColumn;
//        }).collect(Collectors.toList());
//        return lstColumn;
//    }
//
//
//    /**
//     * Lay danh sach cac cot IS_SHOW=1 cua bao bao
//     *
//     * @param lstAllColumn
//     * @return
//     */
//    public List<ConfigReportColumnDTO> getListShowColumByReportId(List<ConfigReportColumnDTO> lstAllColumn) {
//        List<ConfigReportColumnDTO> lstColumnCurrent = lstAllColumn.stream()
//            .filter(e -> Constants.IS_SHOW.equals(e.getIsShow())).collect(Collectors.toList());
//        lstColumnCurrent.sort(Comparator.comparing(ConfigReportColumnDTO::getPos));
//        return lstColumnCurrent;
//    }
//
//
//    public ConfigReportColumnDTO getPrimaryKeyColumn(List<ConfigReportColumnDTO> lstPrimaryKeyCol) {
//        ConfigReportColumnDTO primaryKeyColumn = null;
//        if (!DataUtil.isNullOrEmpty(lstPrimaryKeyCol)) {
//            primaryKeyColumn = lstPrimaryKeyCol.get(0);
//            primaryKeyColumn.setIsPrimaryKey(Constants.IS_PRIMARY_KEY);
//            primaryKeyColumn.setTitle(primaryKeyColumn.getColumnName());
//            primaryKeyColumn.setIsShow(Constants.IS_SHOW);
//            primaryKeyColumn.setIsTimeColumn(0);
//            primaryKeyColumn.setStatus(0);
//        }
//        return primaryKeyColumn;
//    }
//
//    /**
//     * Lay cac cot khong show len cho nguoi dung
//     *
//     * @param lstAllColumn
//     * @return
//     */
//    public List<ConfigReportColumnDTO> getListHiddenColumByReportId(List<ConfigReportColumnDTO> lstAllColumn) {
//        List<ConfigReportColumnDTO> lstColumnCurrent = lstAllColumn.stream()
//            .filter(e -> !Constants.IS_SHOW.equals(e.getIsShow())).collect(Collectors.toList());
//        return lstColumnCurrent;
//    }
//
//
//
//    /**
//     * Lay danh sach cac cot can hien thi va khong phai cot primary_key
//     *
//     * @param lstAllColumn
//     * @return
//     */
//    public List<ConfigReportColumnDTO> getListShowColumByReportIdIgnoreKey(List<ConfigReportColumnDTO> lstAllColumn) {
//        List<ConfigReportColumnDTO> lstColumnCurrent = lstAllColumn.stream()
//            .filter(e -> Constants.IS_SHOW.equals(e.getIsShow()) && !Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey())).collect(Collectors.toList());
//        lstColumnCurrent.sort(Comparator.comparing(ConfigReportColumnDTO::getPos));
//        return lstColumnCurrent;
//    }
//
//    public List<ConfigReportColumnDTO> getListColumByReportIdIgnoreKey(List<ConfigReportColumnDTO> lstAllColumn) {
//        List<ConfigReportColumnDTO> lstColumnCurrent = lstAllColumn.stream()
//            .filter(e -> !Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey())).collect(Collectors.toList());
//        lstColumnCurrent.sort(Comparator.comparing(ConfigReportColumnDTO::getPos));
//        return lstColumnCurrent;
//    }
//
//    public void updateRowDataConfigReportsDefaultValue(List<ConfigReportColumnDTO> lstColumn, ConfigReportImportForm configReportDataForm) throws Exception {
//        //Lay danh sach cac cot co truong gia tri mac dinh.
//        Optional<ConfigReportColumnDTO> timeStampColumn = lstColumn.stream().filter(e -> Constants.DATA_DB_TYPE.TIMESTAMP.equalsIgnoreCase(e.getColumnName())).findFirst();
//        if (timeStampColumn.isPresent()) {
//            configReportDataForm.getMapValue().put(Constants.DATA_DB_TYPE.TIMESTAMP, new Date().getTime());
//        }
//        lstColumn = lstColumn.stream().filter(e -> !DataUtil.isNullObject(e.getDefaultValue()))
//            .collect(Collectors.toList());
//
//        for (ConfigReportColumnDTO columnDTO : lstColumn) {
//            if ((!configReportDataForm.getMapValue().containsKey(columnDTO.getColumnName())
//                || DataUtil.isNullObject(configReportDataForm.getMapValue().get(columnDTO.getColumnName())))
//                && !Constants.IS_REQUIRED.equals(columnDTO.getIsRequire())) {
//
//                configReportDataForm.getMapValue().put(columnDTO.getColumnName(), columnDTO.getDefaultValue());
//            }
//        }
//    }
//
//
//    public Long buildPrdId(Object timePrdId, String timeType) {
//        Long result = 20150101L;
//        if (Constants.TIME_TYPE_MONTH.equals(Integer.valueOf(timeType))) {
//            try {
//                result = Long.valueOf(timePrdId.toString()+"01");
//            } catch (Exception ex) {
//                log.error(ex.getMessage(), ex);
//            }
//        }
//        if (Constants.TIME_TYPE_QUARTER.equals(Integer.valueOf(timeType))) {
//            try {
//                String timePrdStr = timePrdId.toString();
//                String year = timePrdStr.length() > 4 ? timePrdStr.substring(0, 4) : timePrdStr;
//                String quarter = timePrdStr.length() > 4 ? timePrdStr.substring(4) : timePrdStr;
//                Long month = (Long.valueOf(quarter) - 1) * 3 + 1;
//                if(month<10) {
//                    result = Long.valueOf(year + "0"+month + "01");
//                } else {
//                    result = Long.valueOf(year + month + "01");
//                }
//            } catch (Exception ex) {
//                log.error(ex.getMessage(), ex);
//            }
//        }
//        if (Constants.TIME_TYPE_YEAR.equals(Integer.valueOf(timeType))) {
//            try {
//                String timePrdStr = timePrdId.toString();
//                result = Long.valueOf(timePrdStr + "0101");
//            } catch (Exception ex) {
//                log.error(ex.getMessage(), ex);
//            }
//        }
//        return result;
//    }

	// ----- start ----
	public Integer transformDateByTimeType(Integer date, Long timeType) {
		if (Const.TIME_TYPE.DATE.equals(timeType)) {
			return date;
		} else if (Const.TIME_TYPE.MONTH.equals(timeType)) {
			return date - (date % 100) + 1;
		} else if (Const.TIME_TYPE.QUARTER.equals(timeType)) {
			return date - (date % 100) + 1;
		} else if (Const.TIME_TYPE.YEAR.equals(timeType)) {
			return date - (date % 10000) + 101;
		} else {
			throw new ServerException(ErrorCode.NOT_VALID, "timeType");
		}
	}
}
