package com.viettel.automl.service;

import org.joda.time.DateTime;

public interface IDateService {
	/**
	 * @return current date at the moment of the call
	 */
	DateTime now();
}
