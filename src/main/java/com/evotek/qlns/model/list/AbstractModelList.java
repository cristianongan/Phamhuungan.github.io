/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.list;

import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

/**
 *
 * @author linhlh2
 */
public abstract class AbstractModelList<T> extends AbstractListModel implements
        Sortable<T>{
    protected String _orderByType;
    protected String _orderByColumn;
    protected int _beginOffset;
    protected List<T> _cache;
    protected int _pageSize;
    protected int _cachedSize = -1;

    public abstract void loadToCache(int itemStartNumber, int pageSize);

    @Override
    public Object getElementAt(int index) {
        if (this._cache == null || index < this._beginOffset || index >= this._beginOffset + this._pageSize) {
            this._beginOffset = index;
            loadToCache(index, this._pageSize);
        }
        return this._cache.get(index - this._beginOffset);
    }

    @Override
	public abstract int getSize();

//    public abstract boolean remove(int index);

    @Override
    public void sort(Comparator comparator, boolean flag) {
        if (comparator instanceof FieldComparator) {
            this._orderByType = flag ? "asc" : "desc";
            this._cache = null;
            this._orderByColumn = ((FieldComparator) comparator).getRawOrderBy();
            fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
        }
    }

    @Override
    public String getSortDirection(Comparator comparator) {
        return ((FieldComparator) comparator).isAscending() ? "asc" : "desc";
    }
}
