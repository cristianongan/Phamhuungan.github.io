/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Notification;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class NotificationRender implements ListitemRenderer<Notification>{

    private Window _window;

    public NotificationRender(Window window) {
        this._window = window;
    }
    
    public void render(Listitem item, Notification notify, int index) throws Exception {
        item.setAttribute("data", notify);
        
        Long type = notify.getNotificationType();
        
        StringBuilder sb = new StringBuilder();
        
        if(Values.NOTI_CONTRACT_EXPIRED.equals(type)){
            item.setSclass(Constants.CLASS_TASK_PINK);
            
            sb.append(Labels.getLabel(LanguageKeys.CONTRACT_EXPIRED));
            sb.append(StringPool.SPACE);
            sb.append(StringPool.DASH);
            sb.append(StringPool.SPACE);
            
            item.appendChild(ComponentUtil.createListcell(
                    ComponentUtil.createAIcon(CONTRACT_S_CLASS)));
//            item.appendChild(ComponentUtil.createListcell(
//                    Labels.getLabel(LanguageKeys.CONTRACT_EXPIRED)));
        } else if (Values.NOTI_BIRTHDAY.equals(type)){
            item.setSclass(Constants.CLASS_TASK_GREEN);
            
            sb.append(Labels.getLabel(LanguageKeys.BIRTH_DAY));
            sb.append(StringPool.SPACE);
            sb.append(StringPool.DASH);
            sb.append(StringPool.SPACE);
            
            item.appendChild(ComponentUtil.createListcell(
                    ComponentUtil.createAIcon(DATE_S_CLASS)));
//            item.appendChild(ComponentUtil.createListcell(
//                Labels.getLabel(LanguageKeys.BIRTH_DAY)));
        } else {
            item.setSclass(Constants.CLASS_TASK_DEFAULT);
        }
        
        sb.append(notify.getMessage());
        
        
        item.appendChild(ComponentUtil.createListcell(sb.toString()));
        item.appendChild(ComponentUtil.createListcell(GetterUtil.getDate(
                notify.getEventDate(), DateUtil.SHORT_DATE_PATTERN),
                Constants.STYLE_TEXT_ALIGN_CENTER));
        
        item.appendChild(_getAction(notify, type));
    }

    private Listcell _getAction(Notification notify, Long type) {
        Listcell action = new Listcell();
        
        Hlayout hlayout = new Hlayout();
        
        hlayout.setSpacing("0");
        
        if (Values.NOTI_CONTRACT_EXPIRED.equals(type)) {
            hlayout.appendChild(ComponentUtil.createButton(null,
                    Labels.getLabel(LanguageKeys.EXTEND_CONTRACT),
                    "extendContractTooltip", Events.ON_CLICK,
                    EDIT_STAFF_PAGE, _createParameterMap(notify,
                            Labels.getLabel(LanguageKeys.TITLE_EDIT_STAFF)),
                    Constants.Z_ICON_LEVEL_UP, Constants.BLUE));
        } else {
            hlayout.appendChild(ComponentUtil.createButton(_window,
                    Labels.getLabel(LanguageKeys.DELETE), ComponentUtil.DEL_TOOLTIP,
                    Events.ON_CLICK, "onDeleteNotify", notify,
                    Constants.Z_ICON_TRASH_O, Constants.RED));
        }
        
        action.appendChild(hlayout);
        
        return action;
    }
    
    private Map<String, Object> _createParameterMap(
            Notification notify, String title) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, _window);
        parameters.put(Constants.TITLE, title);
        parameters.put(Constants.OBJECT_ID, notify.getClassPk());

        return parameters;
    }
    
    private static final String CONTRACT_S_CLASS = 
            "btn btn-xs no-hover btn-pink z-icon-copy";
    private static final String DATE_S_CLASS = 
            "btn btn-xs no-hover btn-success z-icon-calendar";
    private static final String EDIT_STAFF_PAGE =
            "/html/pages/manager_human_resource/edit.zul";
}
