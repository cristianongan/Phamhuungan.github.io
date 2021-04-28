/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.util;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author linhlh2
 */
public class CollectionUtil {
	public static Collection<?> subtract(Collection<?> first, Collection<?> second) {
		return CollectionUtils.subtract(first, second);
	}
}
