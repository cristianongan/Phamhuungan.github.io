/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Right;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.PermissionConstants;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class RightRender implements RowRenderer<Right>{

    public Window winTemp;

    public RightRender(Window winTemp) {
        this.winTemp = winTemp;
    }

    public void render(Row row, Right right, int index) throws Exception {
        row.appendChild(ComponentUtil.createCell(Integer.toString(index+1),
                Constants.STYLE_TEXT_ALIGN_CENTER));
        row.appendChild(new Label(right.getRightName()));
        row.appendChild(new Label(getRightTypeName(right.getRightType())));
//        row.appendChild(new Label(right.getUserName()));
//        row.appendChild(ComponentUtil.createCell(GetterUtil.getDate(
//                right.getModifiedDate(), DateUtil.SHORT_DATE_PATTERN),
//                Constants.STYLE_TEXT_ALIGN_CENTER));
        row.appendChild(new Label(right.getDescription()));
        row.appendChild(new Label(Values.getLockStatus(right.getStatus())));

        if(!PermissionConstants.TYPE_MENU_ITEM.equals(right.getRightType())){
            row.appendChild(this._getAction(right));
        }else{
            row.appendChild(new Label(StringPool.BLANK));
        }
    }

    private String getRightTypeName(Long type){
        String statusName = StringPool.BLANK;

        if(Validator.isNotNull(type)
                && type< _type.length){
            statusName = _type[type.intValue()];
        }

        return statusName;
    }

    private Hlayout _getAction(Right right){
        Hlayout hlayout = new Hlayout();
        
        hlayout.setSpacing("0");
        
        hlayout.appendChild(ComponentUtil.createButton(null,
                Labels.getLabel(LanguageKeys.EDIT), ComponentUtil.EDIT_TOOLTIP,
                Events.ON_CLICK, EDIT_RIGHT_PAGE,
                _createParameterMap(right), Constants.Z_ICON_PENCIL, 
                Constants.BLUE));
        
        Long status = right.getStatus();

        if(Values.STATUS_ACTIVE.equals(status)){
            //Thêm action "Khóa"
            hlayout.appendChild(ComponentUtil.createButton(winTemp,
                    Labels.getLabel(LanguageKeys.BUTTON_LOCK), 
                    ComponentUtil.LOCK_TOOLTIP, Events.ON_CLICK,
                    "onLockRight", right, Constants.Z_ICON_LOCK,
                    Constants.ORANGE));
        } else {
            hlayout.appendChild(ComponentUtil.createButton(winTemp,
                    Labels.getLabel(LanguageKeys.BUTTON_UNLOCK), 
                    ComponentUtil.UNLOCK_TOOLTIP, Events.ON_CLICK,
                    "onUnlockRight", right, Constants.Z_ICON_UNLOCK,
                    Constants.ORANGE));

            //Thêm action "Xóa"
            hlayout.appendChild(ComponentUtil.createButton(winTemp,
                    Labels.getLabel(LanguageKeys.BUTTON_DELETE), 
                    ComponentUtil.DEL_TOOLTIP, Events.ON_CLICK,
                    "onDeleteRight", right, Constants.Z_ICON_TRASH_O,
                    Constants.RED));
        }

        return hlayout;
    }

    private Map<String, Object> _createParameterMap(Right right) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, winTemp);
        parameters.put(Constants.TITLE, Labels.getLabel(
                LanguageKeys.TITLE_EDIT_RIGHT));
        parameters.put(Constants.EDIT_OBJECT, right);

        return parameters;
    }

    private static final String EDIT_RIGHT_PAGE =
            "/html/pages/manager_menu/edit_right.zul";

    private static final String[] _type = StaticUtil.MENU_RIGHT_TYPE;
}
