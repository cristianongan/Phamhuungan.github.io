package Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	private List<Product> list= new ArrayList<Product>();
	private int numofproduct=0;
	private int price=0;
	public boolean add(Product p , int max)
	{
		if(list ==null)
			list = new ArrayList<Product>();
		if(list.size()==0)
		{
			list.add(p);
			price+=Integer.parseInt(p.getPrice())*p.getNumOfProduct();
			numofproduct +=p.getNumOfProduct();
			return true;
		}
		if(list.stream().noneMatch(produ -> produ.getId()== p.getId() && produ.getList_size().get(0).equals(p.getList_size().get(0))) )
		{
			list.add(p);
			price+=Integer.parseInt(p.getPrice())*p.getNumOfProduct();
			numofproduct +=p.getNumOfProduct();
			return true;
		}else
		{
			for(int i=0; i<list.size();i++)
			{
				if(list.get(i).getId()==p.getId() && list.get(i).getList_size().get(0).equals(p.getList_size().get(0)))
				{
					if(list.get(i).getNumOfProduct()==max)
						return false;
					else
					{
						list.get(i).setNumOfProduct(list.get(i).getNumOfProduct()+p.getNumOfProduct());
						price+=Integer.parseInt(p.getPrice())*p.getNumOfProduct();
						numofproduct +=p.getNumOfProduct();
						return true;
					}
				}
			}
		}
		
		return false;
	}
	public boolean remove(Product pp)
	{
		if(list.stream().noneMatch(produ -> produ.getId()== pp.getId() && produ.getList_size().get(0).equals(pp.getList_size().get(0))) )
		{

			for(int i=0; i<list.size();i++)
			{
				if(list.get(i).getId()==pp.getId())
				{
					Product p= list.get(i);
					list.remove(i);
					price-=Integer.parseInt(p.getPrice())*p.getNumOfProduct();
					numofproduct -=p.getNumOfProduct();
					return true;
				}
			}
		}else
		{
		}
		return false;
	}
	public boolean subtraction(Product pp)
	{
		if(list.stream().noneMatch(produ -> produ.getId()== pp.getId() && produ.getList_size().get(0).equals(pp.getList_size().get(0)) ) )
		{
			for(int i=0; i<list.size();i++)
			{
				if(list.get(i).getId()==pp.getId())
				{
					Product p= list.get(i);
					int n =list.get(i).getNumOfProduct()-1;
					if(n>0) {
					list.get(i).setNumOfProduct(n);
					price-=Integer.parseInt(p.getPrice());
					numofproduct -=1;}
					else
					{
						remove(pp);
					}
					
					return true;
				}
			}
		}else
		{
			
		}
		return false;
	}
	
}
