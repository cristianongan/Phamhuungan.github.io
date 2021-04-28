/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.ContractType;

/**
 *
 * @author My PC
 */
public interface ContractTypeDAO {

	public void delete(ContractType ct);

	public List<ContractType> getContract();

	public void saveOrUpdate(ContractType ct);

}
