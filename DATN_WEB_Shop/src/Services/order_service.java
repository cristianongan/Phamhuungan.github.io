package Services;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CRUD.order_CRUD;
import Entity.Product;
import Entity.order;
import Entity.user;
import Entity.user_full;
import exception.user_erro;

@Service
public class order_service {
	@Autowired
	private order_CRUD o;
	@Autowired
	private user_service us;
	
	public boolean add_order(List<Product> li, user u)
	{
		int ma = o.order(us.get_full_info_user(u));
		for(Product p:li)
		{
			System.out.println(p.getNumOfProduct());
			if(o.add_product(p, ma)==false)
				return false;
		}
		return true;
	}
	public List<order> get_list_order(user u)
	{
		return o.get_list_order(u.getId());
	}
	public order get_order_full(int ma)
	{
		List<Product> li = o.get_product_ordered(ma);
		if(li.size()==0)
		{
			throw new user_erro("ko co ma don hang");
		}else
		{
			Map<String, Object> m = o.get_order_MA(ma);
			int tonggia=0;
			for(Product p:li)
			{
				tonggia += Integer.parseInt(p.getPrice())*p.getNumOfProduct();
			}
			return new order(ma, li, tonggia,(Date) m.get("date"), (int)m.get("user"),
					(String)m.get("status"),(String)m.get("addr"));
		}
	}

}
