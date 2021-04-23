/*
 * Copyright 2013 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.model.render;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author hungnt81
 */
public class UserRoleRender implements ListitemRenderer<Role> {

    @Override
	public void render(Listitem items, Role role, int index) throws Exception {
        items.setAttribute("data",role);

        items.appendChild(ComponentUtil.createListcell(StringPool.BLANK,
                Constants.STYLE_TEXT_ALIGN_CENTER));
        items.appendChild(ComponentUtil.createListcell(
                Integer.toString(index+1), Constants.STYLE_TEXT_ALIGN_CENTER));
        items.appendChild(new Listcell(role.getRoleName()));
        items.appendChild(new Listcell(role.getDescription()));
    }

}
