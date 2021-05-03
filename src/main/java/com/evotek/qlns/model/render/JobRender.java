/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.Job;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class JobRender implements ListitemRenderer<Job> {

	private Window _window;

	public JobRender(Window _window) {
		this._window = _window;
	}

	private Listcell _getEditAction(final Listitem item, final Job job, final int index) {

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		Button btnSave = ComponentUtil.createButton(Labels.getLabel(LanguageKeys.BUTTON_SAVE), Constants.Tooltip.EDIT,
				Constants.Zicon.SAVE, Constants.Sclass.BLUE);

		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				Component cmp = item.getFellowIfAny(job.getJobId() + "-" + "job-title");

				if (cmp instanceof Textbox) {
					job.setJobTitle(GetterUtil.getString(((Textbox) cmp).getValue()));

					Events.sendEvent("onUpdateJob", JobRender.this._window, job);
				}

				item.getChildren().clear();

				renderNoneEditing(item, job, index);
			}
		});

		hlayout.appendChild(btnSave);

		Button btnCancel = ComponentUtil.createButton(Labels.getLabel(LanguageKeys.BUTTON_CANCEL),
				Constants.Tooltip.CALCEL, Constants.Zicon.TIMES, Constants.Sclass.RED);

		btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				item.getChildren().clear();

				renderNoneEditing(item, job, index);
			}
		});

		hlayout.appendChild(btnCancel);

		action.appendChild(hlayout);

		return action;
	}

	private Listcell _getNoneEditAction(final Listitem item, final Job job, final int index) {

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		Button btnEdit = ComponentUtil.createButton(Labels.getLabel(LanguageKeys.EDIT), Constants.Tooltip.EDIT,
				Constants.Zicon.PENCIL, Constants.Sclass.BLUE);

		btnEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				item.getChildren().clear();

				renderEditing(item, job, index);
			}
		});

		hlayout.appendChild(btnEdit);

		hlayout.appendChild(
				ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.DELETE), Constants.Tooltip.DEL,
						Events.ON_CLICK, "onDelete", job, Constants.Zicon.TRASH_O, Constants.Sclass.RED));

		action.appendChild(hlayout);

		return action;
	}

	@Override
	public void render(Listitem item, Job job, int index) throws Exception {
		item.setAttribute("data", job);

		this.renderNoneEditing(item, job, index);

	}

	private void renderEditing(Listitem item, Job job, int index) {
		item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.Style.TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createTextboxListcell(job.getJobTitle(), job.getJobId() + "-" + "job-title"));
		item.appendChild(_getEditAction(item, job, index));
	}

	private void renderNoneEditing(Listitem item, Job job, int index) {
		item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.Style.TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(job.getJobTitle()));
		item.appendChild(_getNoneEditAction(item, job, index));
	}
}
