/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.PermissionUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH2
 */
public class RoleRender<Component> implements ListitemRenderer<Role> {

    private Div winTemp;

    public RoleRender(Div winTemp) {
        this.winTemp = winTemp;
    }

    @Override
    public void render(Listitem items, final Role role, int index)
            throws Exception {
        items.appendChild(ComponentUtil.createListcell(
                Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
        items.appendChild(new Listcell(role.getRoleName()));
        items.appendChild(new Listcell(Values.getLockStatus(role.getStatus())));
        items.appendChild(new Listcell(role.getDescription()));
        items.appendChild(_getAction(role));
    }

    private Listcell _getAction(Role role) {
        Listcell action = new Listcell();

        if (!PermissionUtil.isAdministrator(role)) {
            Hlayout hlayout = new Hlayout();
        
            hlayout.setSpacing("0");

            Long immune = role.getImmune();

            if (Values.NOT_IMMUNE.equals(immune)) {
                hlayout.appendChild(ComponentUtil.createButton(null,
                        Labels.getLabel(LanguageKeys.EDIT), 
                        ComponentUtil.EDIT_TOOLTIP, Events.ON_CLICK, 
                        EDIT_PAGE, _createParameterMap(role), 
                        Constants.Z_ICON_PENCIL, Constants.BLUE));

                Long status = role.getStatus();

                if (Values.STATUS_ACTIVE.equals(status)) {
                    //Thêm action "Khóa"
                    hlayout.appendChild(ComponentUtil.createButton(winTemp,
                            Labels.getLabel(LanguageKeys.BUTTON_LOCK), 
                            ComponentUtil.LOCK_TOOLTIP, Events.ON_CLICK,
                            "onLockRole", role, Constants.Z_ICON_LOCK,
                            Constants.ORANGE));
                } else {
                    hlayout.appendChild(ComponentUtil.createButton(winTemp,
                            Labels.getLabel(LanguageKeys.BUTTON_UNLOCK), 
                            ComponentUtil.UNLOCK_TOOLTIP, Events.ON_CLICK,
                            "onUnlockRole", role, Constants.Z_ICON_UNLOCK,
                            Constants.ORANGE));

                    //Thêm action "Xóa"
                    hlayout.appendChild(ComponentUtil.createButton(winTemp,
                            Labels.getLabel(LanguageKeys.BUTTON_DELETE), 
                            ComponentUtil.DEL_TOOLTIP, Events.ON_CLICK,
                            "onDeleteRole", role, Constants.Z_ICON_TRASH_O,
                            Constants.RED));
                }
            }

            hlayout.appendChild(ComponentUtil.createButton(winTemp,
                    Labels.getLabel(LanguageKeys.ASSIGN_RIGHT), 
                    ComponentUtil.ASSIGN_RIGHT_TOOLTIP, Events.ON_CLICK,
                    PERMISSION_PAGE, _createParameterMap(role),
                    Constants.Z_ICON_SHARE, Constants.PURPLE));

            action.appendChild(hlayout);

        }

        return action;
    }

    private Map<String, Object> _createParameterMap(Role role) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, winTemp);
        parameters.put(Constants.TITLE, Labels.getLabel(
                LanguageKeys.TITLE_EDIT_ROLE));
        parameters.put(Constants.EDIT_OBJECT, role);

        return parameters;
    }

    private static final String EDIT_PAGE =
            "/html/pages/manager_role/edit.zul";
    private static final String PERMISSION_PAGE =
            "/html/pages/manager_role/permission.zul";
}
