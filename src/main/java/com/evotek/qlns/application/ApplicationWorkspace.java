
package com.evotek.qlns.application;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.zul.Timer;

/**
 * Workspace for the application. One workspace per app instance. <br>
 * Stored several properties for the application. <br>
 */
public class ApplicationWorkspace implements Serializable {

	private static final Logger _log = LogManager.getLogger(ApplicationWorkspace.class);

	private static ApplicationWorkspace instance = new ApplicationWorkspace();

	private static final long serialVersionUID = 1L;

	private static String appName = "OT_Base";
	private static String serverIp = "1.2.3.4.5.6";

	/* Application real http-path on server. Filled on app startup. */
	/* @see org.opentruuls.InitApplication */
	private String applicationRealPath = "";

	private Timer timer;

	/**
	 * Default Constructor, cannot invoked from outer this class. <br>
	 */
	private ApplicationWorkspace() {
	}

	public static ApplicationWorkspace getInstance() {
		return instance;
	}

	public String getApplicationRealPath() {
		return this.applicationRealPath;
	}

	public void setApplicationRealPath(String applicationRealPath) {
		this.applicationRealPath = applicationRealPath;
	}

	public String getAppName() {
		return appName;
	}

	public String getServerip() {
		return serverIp;
	}

}
