package Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CRUD.order_CRUD;
import Entity.order;

@Service
public class admin_service {
	@Autowired
	private order_CRUD o;
	public int get_num_page_order()
	{
		int a=o.get_row_order();
		return a/6 +(a%6>0?1:0);
	}
	public List<order> get_list_order(int page)
	{
		int a= o.get_row_order();
		return o.admin_get_list_order((a-6*page)>0?a-6*page:0,a-6*(page-1));
	}

}
