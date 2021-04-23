/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.list;

import java.util.Collection;
import java.util.List;

import org.zkoss.zul.ListModelList;

/**
 *
 * @author linhlh2
 */
public class SimpleModelList<E extends Object> extends ListModelList<E>{

    public SimpleModelList(int initialCapacity) {
        super(initialCapacity);

        setMultiple(true);
    }

    public SimpleModelList(E[] array) {
        super(array);

        setMultiple(true);
    }

    public SimpleModelList(Collection<? extends E> c) {
        super(c);

        setMultiple(true);
    }

    public SimpleModelList() {
        super();

        setMultiple(true);
    }

    public SimpleModelList(List<E> list, boolean live) {
        super(list, live);

        setMultiple(true);
    }

}
