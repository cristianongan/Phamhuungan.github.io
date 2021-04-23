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
        if (_cache == null || index < _beginOffset || index >= _beginOffset + _pageSize) {
            _beginOffset = index;
            loadToCache(index, _pageSize);
        }
        return _cache.get(index - _beginOffset);
    }

    public abstract int getSize();

//    public abstract boolean remove(int index);

    @Override
    public void sort(Comparator comparator, boolean flag) {
        if (comparator instanceof FieldComparator) {
            _orderByType = flag ? "asc" : "desc";
            _cache = null;
            _orderByColumn = ((FieldComparator) comparator).getRawOrderBy();
            fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
        }
    }

    @Override
    public String getSortDirection(Comparator comparator) {
        return ((FieldComparator) comparator).isAscending() ? "asc" : "desc";
    }
}
