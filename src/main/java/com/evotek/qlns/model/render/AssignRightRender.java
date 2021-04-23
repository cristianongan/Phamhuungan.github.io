/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import java.util.Set;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.Right;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author linhlh2
 */
public class AssignRightRender implements ListitemRenderer<Right>{

    private Set<Right> rights;

    public AssignRightRender(Set<Right> rights) {
        this.rights = rights;
    }

    @Override
	public void render(Listitem items, Right right, int index) throws Exception {
        items.appendChild(ComponentUtil.createListcell(StringPool.BLANK,
                Constants.STYLE_TEXT_ALIGN_CENTER));
        items.appendChild(new Listcell(right.getRightName()));
        items.appendChild(new Listcell(getRightTypeName(right.getRightType())));
        items.appendChild(new Listcell(right.getDescription()));

        items.setSelected(this.rights.contains(right));

        items.setAttribute("data", right);
    }

//    private boolean isAssigned(Right right){
//        for(Right _right: rights){
//            if(right.equals(_right)){
//                return true;
//            }
//        }
//
//        return false;
//    }

    private String getRightTypeName(Long type){
        String statusName = StringPool.BLANK;

        if(Validator.isNotNull(type)
                && type< _type.length){
            statusName = _type[type.intValue()];
        }

        return statusName;
    }

    private static final String[] _type = StaticUtil.MENU_RIGHT_TYPE;
}
