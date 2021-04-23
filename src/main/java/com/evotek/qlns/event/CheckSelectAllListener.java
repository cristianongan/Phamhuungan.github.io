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
public class CheckSelectAllListener implements EventListener<Event>{

    private Checkbox masterChb;
    private List<Checkbox> slaveChbs;
    private Checkbox slaveChb;

    public CheckSelectAllListener(Checkbox masterChb, List<Checkbox> slaveChbs) {
        this.masterChb = masterChb;
        this.slaveChbs = slaveChbs;
    }

    public CheckSelectAllListener(Checkbox masterChb, List<Checkbox> slaveChbs, 
            Checkbox slaveChb) {
        this.masterChb = masterChb;
        this.slaveChbs = slaveChbs;
        this.slaveChb = slaveChb;
    }

    public void onEvent(Event event) throws Exception {
        if (slaveChb == null) {
            for (Checkbox cb : slaveChbs) {
                cb.setChecked(masterChb.isChecked());
            }

            return;
        }

        if (!slaveChb.isChecked()) {
            masterChb.setChecked(false);

            return;
        }

        boolean checkAll = true;

        for (Checkbox cb : slaveChbs) {
            checkAll = checkAll && cb.isChecked();

            if(!checkAll){
                break;
            }
        }

        masterChb.setChecked(checkAll);
    }
}
