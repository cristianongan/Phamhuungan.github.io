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
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class ContractTypeRender implements ListitemRenderer<ContractType> {

	private Window _window;

	public ContractTypeRender(Window _window) {
		this._window = _window;
	}

	private Listcell _getEditAction(final Listitem item, final ContractType ct, final int index) {

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		Button btnSave = ComponentUtil.createButton(Labels.getLabel(LanguageKeys.BUTTON_SAVE), Constants.Zicon.SAVE,
				Constants.BLUE);

		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				Component cmp1 = item.getFellowIfAny(ct.getContractTypeId() + "-" + "contract-type");
				Component cmp2 = item.getFellowIfAny(ct.getContractTypeId() + "-" + "month-duration");

				if (cmp1 instanceof Textbox) {
					ct.setContractTypeName(GetterUtil.getString(((Textbox) cmp1).getValue()));
					ct.setMonthDuration(GetterUtil.getLong(((Spinner) cmp2).getValue()));

					Events.sendEvent("onUpdateJob", ContractTypeRender.this._window, ct);
				}

				item.getChildren().clear();

				renderNoneEditing(item, ct, index);
			}
		});

		hlayout.appendChild(btnSave);

		Button btnCancel = ComponentUtil.createButton(Labels.getLabel(LanguageKeys.BUTTON_CANCEL),
				Constants.Zicon.TIMES, Constants.RED);

		btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				item.getChildren().clear();

				renderNoneEditing(item, ct, index);
			}
		});

		hlayout.appendChild(btnCancel);

		action.appendChild(hlayout);

		return action;
	}

	private Listcell _getNoneEditAction(final Listitem item, final ContractType ct, final int index) {

		Listcell action = new Listcell();

		Hlayout hlayout = new Hlayout();

		hlayout.setSpacing("0");

		Button btnEdit = ComponentUtil.createButton(Labels.getLabel(LanguageKeys.EDIT),
				Constants.Tooltip.EDIT, Constants.Zicon.PENCIL, Constants.BLUE);

		btnEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				item.getChildren().clear();

				renderEditing(item, ct, index);
			}
		});

		hlayout.appendChild(btnEdit);

		hlayout.appendChild(ComponentUtil.createButton(this._window, Labels.getLabel(LanguageKeys.DELETE),
				Constants.Tooltip.DEL, Events.ON_CLICK, "onDelete", ct, Constants.Zicon.TRASH_O,
				Constants.RED));

		action.appendChild(hlayout);

		return action;
	}

	@Override
	public void render(Listitem item, ContractType ct, int index) throws Exception {
		item.setAttribute("data", ct);

		this.renderNoneEditing(item, ct, index);

	}

	private void renderEditing(Listitem item, ContractType ct, int index) {
		item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createTextboxListcell(ct.getContractTypeName(),
				ct.getContractTypeId() + "-" + "contract-type"));
		item.appendChild(ComponentUtil.createSpinnerListcell(ct.getMonthDuration(),
				ct.getContractTypeId() + "-" + "month-duration", Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(_getEditAction(item, ct, index));
	}

	private void renderNoneEditing(Listitem item, ContractType ct, int index) {
		item.appendChild(ComponentUtil.createListcell(Integer.toString(index + 1), Constants.STYLE_TEXT_ALIGN_CENTER));
		item.appendChild(ComponentUtil.createListcell(ct.getContractTypeName()));

		if (ct.getMonthDuration() == null) {
			item.appendChild(ComponentUtil.createListcell(StringPool.N_A));
		} else {
			item.appendChild(ComponentUtil.createListcell(GetterUtil.getString(ct.getMonthDuration())));
		}

		item.appendChild(_getNoneEditAction(item, ct, index));
	}

}
