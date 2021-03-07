package Services;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import CRUD.order_CRUD;
import CRUD.product_CRUD;
import CRUD.user_CRUD;
import Entity.Product;
import Entity.order;
import Entity.product_handle;
import Entity.user;
import Entity.user_full;

@Service
public class admin_service {
	@Autowired
	private order_CRUD o;
	@Autowired
	private product_CRUD pc;
	@Autowired
	private user_CRUD uc;
	public boolean delete_user(int id)
	{
		return uc.delete(id);
	}
	public user_full get_full_info_user(int id)
	{
		return uc.get_full_info_user(id);
	}
	public int get_num_page_user()
	{
		int a= uc.num_user();
		return a/6 +(a%6>0?1:0);
	}
	public List<user> get_list_user(int page)
	{
		int a = uc.num_user();
		return uc.list_user((a-6*page)>0?a-6*page:0,(page==a/6 +(a%6>0?1:0))?a%6:6);
	}
	public int get_num_page_order()
	{
		int a=o.get_row_order();
		return a/6 +(a%6>0?1:0);
	}
	public List<order> get_list_order(int page)
	{
		int a= o.get_row_order();
		return o.admin_get_list_order((a-6*page)>0?a-6*page:0,(page==a/6 +(a%6>0?1:0))?a%6:6);
	}
	public boolean update_order(order or)
	{
			return o.update_order(or);
	}
	public boolean delete_order(int madh)
	{
		return o.delete_order(madh);
	}
	public List<order> search_order(int id)
	{
		return o.search(id);
	}
	public boolean add_product(String dirFile,product_handle ph)
	{
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
		ph.get_cat();
		CommonsMultipartFile c =ph.getImg();
		System.out.println(dirFile);
		String fileName =c.getName();
		File file = new File(dirFile+"\\"+fileName+df.format(new Date()));
		System.out.println(file.getAbsolutePath());
		try {
			c.transferTo(file);
			List<String> ls = new ArrayList<String>();
			ls.add(ph.getList_size());
			pc.addProduct(new Product(ph.getId(), ph.getName(), ph.getColor(), ph.getMaterial(), ph.getPrice(),
					"imagesP"+file.separator+fileName+df.format(new Date()), ph.getNumOfProduct(), ph.getId_cat(),
					ph.getId_cat2(), ph.getNote(), ls));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean update_product(String dirFile,product_handle ph)
	{
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
		ph.get_cat();
		CommonsMultipartFile c =ph.getImg();
		System.out.println(dirFile);
		String fileName =c.getName();
		File file = new File(dirFile+"\\"+fileName+df.format(new Date()));
		System.out.println(file.getAbsolutePath());
		try {
			c.transferTo(file);
			List<String> ls = new ArrayList<String>();
			ls.add(ph.getList_size());
			pc.update_product(new Product(ph.getId(), ph.getName(), ph.getColor(), ph.getMaterial(), ph.getPrice(),
					"imagesP"+file.separator+fileName, ph.getNumOfProduct(), ph.getId_cat(),
					ph.getId_cat2(), ph.getNote(), ls));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
//	public get_file_order(order o)
//	{
//		
//	}

}
