/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

import com.evotek.qlns.application.UserWorkspace;
import com.evotek.qlns.model.User;
import com.evotek.qlns.security.policy.impl.UserPrincipalImpl;

/**
 *
 * @author linhlh2
 */
@Controller
public class BasicController<T extends Component> extends GenericForwardComposer<T> implements Serializable {

	private static final long serialVersionUID = -1171206258809472640L;

	protected Map<?, ?> args;

	private User user;

	@Autowired
	@Qualifier("userWorkspace")
	private UserWorkspace userWorkspace;

	public void doOnCreateCommon(Window w, Event fe) throws Exception {
		final CreateEvent ce = (CreateEvent) ((ForwardEvent) fe).getOrigin();

		this.args = ce.getArg();
	}

	/**
	 * Get the params map that are overhanded at creation time. <br>
	 * Reading the params that are binded to the createEvent.<br>
	 *
	 * @param event
	 * @return params map
	 */
	public Map<?, ?> getCreationArgsMap(Event event) {
		final CreateEvent ce = (CreateEvent) ((ForwardEvent) event).getOrigin();

		return ce.getArg();
	}

	public Long getDeptId() {
		return getUser().getDeptId();
	}

	public User getUser() {
		if (this.user != null) {
			return this.user;
		} else {
			UserPrincipalImpl userPrincipalImpl = this.userWorkspace.getUserPrincipal();

			return userPrincipalImpl != null ? userPrincipalImpl.getUser() : null;
		}
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUserName() {
		return getUser().getUserName();
	}

	final public UserWorkspace getUserWorkspace() {
		return this.userWorkspace;
	}

	/**
	 * With this method we get the @Secured Annotation for a method.<br>
	 * Captured the method call and check if it's allowed. <br>
	 * sample: @Secured({"rightName"}) <br>
	 * 
	 * <pre>
	 * &#64;Secured({ "button_BranchMain_btnNew" })
	 * public void onClick$btnNew(Event event) throws Exception {
	 *   [...]
	 * }
	 * </pre>
	 *
	 * @param mtd
	 * @exception SecurityException
	 */
	private void isAllowed(Method mtd) {
		final Annotation[] annotations = mtd.getAnnotations();

		for (final Annotation annotation : annotations) {
			if (annotation instanceof Secured) {
				final Secured secured = (Secured) annotation;

				for (final String rightName : secured.value()) {
					if (!this.userWorkspace.isAllowed(rightName)) {
						throw new SecurityException("Call of this method is not allowed! Missing right: \n\n"
								+ "needed RightName: " + rightName + "\n\n" + "Method: " + mtd);
					}
				}

				return;
			}
		}
	}

	/**
	 * Workaround! Do not use it otherwise!
	 */
	@Override
	public void onEvent(Event evt) throws Exception {
		final Object controller = getController();

		final Method mtd = ComponentsCtrl.getEventMethod(controller.getClass(), evt.getName());

		if (mtd != null) {
			isAllowed(mtd);
		}

		super.onEvent(evt);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserWorkspace(UserWorkspace userWorkspace) {
		this.userWorkspace = userWorkspace;
	}
}
