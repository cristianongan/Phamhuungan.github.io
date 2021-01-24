package Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import CRUD.category_CRUD;
import CRUD.product_CRUD;
import Entity.Cart;
import Entity.Category;
import Entity.Product;
import exception.user_erro;

@Component
public class product_service implements product_Int{
	@Autowired
	private product_CRUD p;
	@Autowired
	private category_CRUD c;
	public enum page_by {cat, cat2, id}
	public List<Product> search(String s,int id)
	{
		s="%"+s+"%";
		if(id!=0)
		return p.search(s,id);
		else
			return p.searchAll(s);
	}
	public Product get_product_only(int id)
	{
		Product pr = p.get_product_by_id(id);
		if(p==null)
			throw new user_erro("id san pham khong ton tai!");
		else
			return pr;
	}
	public List<Product> get_list_propuct(int page)
	{
		int a = p.getmax_id();
		return p.get_product1((a-6*page)>0?a-6*page:0,a-6*(page-1));
	}
	public List<Category> get_cat()
	{
		return c.get_cat();
	}
	public int get_num_page()
	{
		int a= p.getmax_id();
		return a/6 + (a%6>0 ?1:0);
	}
	public int get_num_page_by_cat(page_by cat,int id,int id2)
	{
		int a= 0;
		if(cat.equals(page_by.cat))	
			a= p.get_row_num_by_cat(id);
		else
			if(cat.equals(page_by.cat2))
				a=p.get_row_num_by_cat2(id,id2);
		return a/6 +( a%6>0 ?1:0);
	}
	public List<Product> get_list_product_by_cat(int id ,int id2, int page)
	{
		int a= p.get_row_num_by_cat(id);
		if(page> a || page<1)
			page=1;
		return p.get_product_by_cat(id,(a-6*page)>0?a-6*page:0,a-6*(page-1));
	}
	public List<Product> get_list_product_by_cat2(int id , int page,int id2)
	{
		int a= p.get_row_num_by_cat2( id,id2 );
		if(page> a || page<1)
			page=1;
		return p.get_product_by_cat2(id,id2,(a-6*page)>0?a-6*page:0,a-6*(page-1));
	}
	public String get_name(int id, page_by pa)
	{
		if(pa== page_by.cat)
		{
			return p.get_name_cat(id);
		}else
			if(pa== page_by.cat2)
			{
				return p.get_name_cat2(id);
			}else
				if(pa== page_by.id)
				{
					return p.get_name_product(id);
				}
		return null;
	}
	//cart
	public enum cart_act {add, sub, remove}
	public Cart cart_add_product(Cart c, int id, cart_act ca, int num,String size)
	{
		Product pp= p.get_product_by_id(id);
		List<String> si= new ArrayList<String>(); 
		si.add(size);
		pp.setList_size(si);
		int a = pp.getNumOfProduct();
		pp.setNumOfProduct(num);
		if(pp!=null)
		{
			if(ca==cart_act.add)
				if(pp.getNumOfProduct()>0)
					if(c.add(pp, a))
						return c;
			if(ca== cart_act.sub)
				if(c.subtraction(pp))
					return c;
			if(ca== cart_act.remove)
				if(c.remove(pp))
					return c;
		}
		return new Cart();
	}
	
	
	
	
}
