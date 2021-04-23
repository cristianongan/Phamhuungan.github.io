/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

import com.evotek.qlns.common.UserWorkspace;
import com.evotek.qlns.model.User;

/**
 *
 * @author linhlh2
 */
public class BasicController<T extends Component> extends GenericForwardComposer<T>
        implements Serializable {

    

    /**
     * Get the params map that are overhanded at creation time. <br>
     * Reading the params that are binded to the createEvent.<br>
     *
     * @param event
     * @return params map
     */
    @SuppressWarnings("unchecked")
    public Map getCreationArgsMap(Event event) {
        final CreateEvent ce = (CreateEvent) ((ForwardEvent) event).getOrigin();

        return ce.getArg();
    }

    @SuppressWarnings("unchecked")
    public void doOnCreateCommon(Window w, Event fe) throws Exception {
        final CreateEvent ce = (CreateEvent) ((ForwardEvent) fe).getOrigin();

        this.args = ce.getArg();
    }
    
    

    /**
     * Workaround! Do not use it otherwise!
     */
    @Override
    public void onEvent(Event evt) throws Exception {
        final Object controller = getController();

        final Method mtd = ComponentsCtrl.getEventMethod(controller.getClass(),
                evt.getName());

        if (mtd != null) {
            isAllowed(mtd);
        }
        
        super.onEvent(evt);
    }

    /**
     * With this method we get the @Secured Annotation for a method.<br>
     * Captured the method call and check if it's allowed. <br>
     * sample: @Secured({"rightName"}) <br>
     * <pre>
     * @Secured({ "button_BranchMain_btnNew" })
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
                        throw new SecurityException("Call of this method is not allowed! Missing right: \n\n" + "needed RightName: " + rightName + "\n\n" + "Method: " + mtd);
                    }
                }
                
                return;
            }
        }
    }

    final public UserWorkspace getUserWorkspace() {
        return this.userWorkspace;
    }

    public void setUserWorkspace(UserWorkspace userWorkspace) {
        this.userWorkspace = userWorkspace;
    }

    public User getUser() {
        if(this.user!=null){
            return this.user;
        } else {
            return userWorkspace.getUserPrincipal().getUser();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId(){
        return getUser().getUserId();
    }

    public String getUserName(){
        return getUser().getUserName();
    }

    public Long getDeptId(){
        return getUser().getDeptId();
    }

    private static final long serialVersionUID = -1171206258809472640L;
    
    private transient UserWorkspace userWorkspace;

    private transient User user;

    protected transient Map args;
}


