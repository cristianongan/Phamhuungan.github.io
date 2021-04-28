/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.util.key;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;

import com.evotek.qlns.util.StringPool;

/**
 *
 * @author linhlh2
 */
public class Values {
	public static final int DEFAULT_OPTION_VALUE_INT = -1;

	public static final Long DEFAULT_OPTION_VALUE_LONG = -1L;

	public static final Long DEPENDENT = 1L;

	public static final Long DISABLE = 0L;

	public static final Long ENABLE = 1L;

	public static final String EXTENSION = "...";

	public static final Long FEMALE = 1L;

	public static final int FIRST_INDEX = 0;

	public static final int GREATE_LONG_LENGTH = 1000;

	public static final Long IMMUNE = 1L;

	public static final Long IS_VERSION = 1L;

	public static final int LONG_LENGTH = 500;

	public static final Long MALE = 0L;

	public static final int MAX_LENGTH = 1500;

	public static final int MEDIUM_LENGTH = 100;

	public static final Long MENU_TYPE_CATEGORY = 0L;

	public static final Long MENU_TYPE_ITEM = 1L;

	public static final int MIN_NAME_LENGTH = 3;

	public static final Long NOT_DEPENDENT = 0L;

	public static final Long NOT_IMMUNE = 0L;

	public static final Long NOT_SHAREABLE = 0L;

	public static final Long NOTI_BIRTHDAY = 2L;

	public static final Long NOTI_CONTRACT_EXPIRED = 1L;

	public static final Long SHAREABLE = 1L;

	public static final int SHORT_LENGTH = 50;

	public static final Long STATUS_ACTIVE = 1L;

	public static final Long STATUS_DEACTIVE = 0L;

	public static final Long STATUS_MAINTAINING = 3L;

	public static final Long STATUS_NEW = 0L;

	public static final Long STATUS_NOT_READY = 2L;

	public static final Long STATUS_PENDING = 1L;

	public static final Long STATUS_PUBLIC = 2L;

	public static final Long STATUS_REMOVED = 4L;

	public static final Long STATUS_RUNNING = 1L;

	public static final Long STATUS_TESTING = 2L;

	public static final String USER_NAME_PATTERN = "a-z,0-9,_";

	public static final Long VERIFY_ACTIVE_USER = 1L;

	public static final Long VERIFY_RESET_PWD = 2L;

	public static final String VERSION_PATTERN = "[0-9.]";

	public static final int VERY_SHORT_LENGTH = 20;

	public static String getDuplicateMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_DUPLICATE, new Object[] { fieldName });
	}

	public static String getFormatInvalidMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_FORMAT_INVALID, new Object[] { fieldName });
	}

	public static String getFormatInvalidMsg(String fieldName, String mark) {
		return Labels.getLabel(LanguageKeys.MESSAGE_VALID_CHARACTER_ONLY, new Object[] { fieldName, mark });
	}

	public static String getInputNumberOnlyMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_INPUT_NUMBER_ONLY, new Object[] { fieldName });
	}

	public static String getInUseMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_IN_USE_CANNOT_DELETE, new Object[] { fieldName.toLowerCase() });
	}

	public static String getInUseMsg(String fieldName, String detail) {
		return Labels.getLabel(LanguageKeys.MESSAGE_ALL_IN_USE_CANNOT_DELETE,
				new Object[] { fieldName.toLowerCase(), detail.toLowerCase() });
	}

	public static String getLockStatus(Long status) {
		String lockStatus = StringPool.BLANK;

		if (STATUS_ACTIVE.equals(status)) {
			lockStatus = Labels.getLabel(LanguageKeys.STATUS_ACTIVE);
		} else if (STATUS_DEACTIVE.equals(status)) {
			lockStatus = Labels.getLabel(LanguageKeys.STATUS_LOCK);
		} else if (STATUS_NOT_READY.equals(status)) {
			lockStatus = Labels.getLabel(LanguageKeys.STATUS_NOT_ACTIVE);
		}

		return lockStatus;
	}

	public static String getMaxLengthInvalidMsg(String fieldName, int maxValue) {

		return Labels.getLabel(LanguageKeys.MESSAGE_MAX_LENGTH_INVALID, new Object[] { fieldName, maxValue });
	}

	public static String getMinLengthInvalidMsg(String fieldName, int maxValue) {

		return Labels.getLabel(LanguageKeys.MESSAGE_MIN_LENGTH_INVALID, new Object[] { fieldName, maxValue });
	}

	public static String getNotExistMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_NOT_EXIST, new Object[] { fieldName });
	}

	public static String getNotSameMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_IS_NOT_SAME, new Object[] { fieldName });
	}

	public static String getPwdNotMatch(String fieldName, boolean forceLowerLetter, boolean forceUpperLetter,
			boolean forceForceDigit, boolean forceForceSymbol, String symbolRange) {
		List<String> custom = new ArrayList<String>();

		if (forceLowerLetter) {
			custom.add(Labels.getLabel(LanguageKeys.LOWERCASE_LETTER).toLowerCase());
		}

		if (forceUpperLetter) {
			custom.add(Labels.getLabel(LanguageKeys.UPPERCASE_LETTER).toLowerCase());
		}

		if (forceForceDigit) {
			custom.add(Labels.getLabel(LanguageKeys.DIGIT).toLowerCase());
		}

		if (forceForceSymbol && symbolRange.length() > 0) {
			custom.add(
					Labels.getLabel(LanguageKeys.MESSAGE_ONE_OF_CHARACTER, new Object[] { symbolRange }).toLowerCase());
		}

		return Labels.getLabel(LanguageKeys.MESSAGE_MUST_CONTAIN,
				new Object[] { fieldName, StringUtils.join(custom, StringPool.COMMA + StringPool.SPACE) });
	}

	public static String getRequiredInputMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_REQUIRED_INPUT, new Object[] { fieldName.toLowerCase() });
	}

	public static String getRequiredSelectMsg(String fieldName) {
		return Labels.getLabel(LanguageKeys.MESSAGE_REQUIRED_SELECT, new Object[] { fieldName.toLowerCase() });
	}

	public static String getStaffStatus(Long status) {
		String lockStatus = StringPool.BLANK;

		if (STATUS_ACTIVE.equals(status)) {
			lockStatus = Labels.getLabel(LanguageKeys.STATUS_WORKING);
		} else if (STATUS_DEACTIVE.equals(status)) {
			lockStatus = Labels.getLabel(LanguageKeys.STATUS_NOT_WORKING);
		}

		return lockStatus;
	}

	public static String getValueMustInRangeMsg(String fieldName, Object start, Object from) {
		return Labels.getLabel(LanguageKeys.MESSAGE_VALUE_MUST_IN_RANGE, new Object[] { fieldName, start, from });
	}
}
