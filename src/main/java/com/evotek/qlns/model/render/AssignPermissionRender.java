/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model.render;

import java.util.Set;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.Group;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author linhlh2
 */
public class AssignPermissionRender implements ListitemRenderer<Object> {

	Set<Group> groups;

	public AssignPermissionRender(Set<Group> groups) {
		this.groups = groups;
	}

	@Override
	public void render(Listitem items, Object obj, int index) throws Exception {
		Group group = (Group) obj;

		items.appendChild(ComponentUtil.createListcell(StringPool.BLANK, Constants.Style.TEXT_ALIGN_CENTER));
		items.appendChild(new Listcell(group.getGroupName()));
		items.appendChild(new Listcell(group.getDescription()));

		items.setSelected(this.groups.contains(group));

		items.setValue(group);
	}
}
