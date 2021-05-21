package com.evotek.qlns.model.render;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.Constants;

public class ApplicantRender implements ListitemRenderer<ApplicantInformation>{
	private Component target;
	
	
	public ApplicantRender(Component target) {
		super();
		this.target = target;
	}
	@Override
	public void render(Listitem item, ApplicantInformation data, int index) throws Exception {
		Listcell c = ComponentUtil.createListcell("");
		c.setStyle("width:50px;text-align:center;");
		item.appendChild(c);
		new Listcell(data.getName()!=null?data.getName():"N/A").setParent(item);
		new Listcell(data.getApplyPosition()!=null?data.getApplyPosition().getName():"N/A").setParent(item);
		new Listcell(data.getApplyDate()!=null?formatDate(data.getApplyDate()):"N/A").setParent(item);
		new Listcell(data.getStatus()!=null?data.getStatus():"N/A").setParent(item);
		new Listcell(data.getExp()!=null?data.getExp():"N/A").setParent(item);
		new Listcell(data.getInterviewResult()!=null?data.getInterviewResult():"N/A").setParent(item);
		Listcell but = new Listcell();
		but.setStyle("text-align: center");
		but.setParent(item);
		but.appendChild(ComponentUtil.createButton(this.target, "","" , "onClick", "onDeleteApplicant", data,"z-icon-trash-o","blue"));
		but.appendChild(ComponentUtil.createButton(this.target, "","" , "onClick", "onUpdateApplicant", data,"z-icon-pencil-square-o","blue"));
		item.setValue(data);
	}
	private String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

}
