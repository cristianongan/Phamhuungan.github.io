/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.event;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;

/**
 *
 * @author linhlh2
 */
public class CheckSelectAllListener implements EventListener<Event> {

	private Checkbox masterChb;
	private Checkbox slaveChb;
	private List<Checkbox> slaveChbs;

	public CheckSelectAllListener(Checkbox masterChb, List<Checkbox> slaveChbs) {
		this.masterChb = masterChb;
		this.slaveChbs = slaveChbs;
	}

	public CheckSelectAllListener(Checkbox masterChb, List<Checkbox> slaveChbs, Checkbox slaveChb) {
		this.masterChb = masterChb;
		this.slaveChbs = slaveChbs;
		this.slaveChb = slaveChb;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (this.slaveChb == null) {
			for (Checkbox cb : this.slaveChbs) {
				cb.setChecked(this.masterChb.isChecked());
			}

			return;
		}

		if (!this.slaveChb.isChecked()) {
			this.masterChb.setChecked(false);

			return;
		}

		boolean checkAll = true;

		for (Checkbox cb : this.slaveChbs) {
			checkAll = checkAll && cb.isChecked();

			if (!checkAll) {
				break;
			}
		}

		this.masterChb.setChecked(checkAll);
	}
}
