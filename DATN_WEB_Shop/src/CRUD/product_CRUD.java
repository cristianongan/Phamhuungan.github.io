package CRUD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import Entity.*;
@Component
public class product_CRUD {
	@Autowired
	private JdbcTemplate template;
	@Transactional
	public boolean update_product(Product p)
	{
		return template.execute("update product set name=?,color=?,material=?,price=?,urlimg=?,numofproduct=?,category=?,category2=?,note=?,size=? where id=?", new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, p.getName());
				ps.setString(2, p.getColor());
				ps.setString(3, p.getMaterial());
				ps.setString(4, p.getPrice());
				ps.setString(5, p.getUrlimg());
				ps.setInt(6, p.getNumOfProduct());
				ps.setInt(7, p.getId_cat());
				ps.setInt(8, p.getId_cat2());
				ps.setString(9, p.getNote());
				ps.setString(10, p.getList_size().get(0));
				ps.setInt(11, p.getId());
				return null;
			}
		});
	}
	public List<Product> search(String s,int id)
	{
		return template.query("select * from product where product.name like ? and product.category =? limit 50", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, s);
				ps.setInt(2, id);
			}
		}, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Product> r =new ArrayList<Product>();
				while(rs.next())
				{
					List<String> ls = Arrays.asList(rs.getString(11).split(","));
					
					r.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), 
							rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10),ls));
				}
				return r;
			}
		});
	}
	public List<Product> searchAll(String s)
	{
		return template.query("select * from product where product.name like ? limit 50", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, s);
			}
		}, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Product> r =new ArrayList<Product>();
				while(rs.next())
				{
					r.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), 
							rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10),Arrays.asList(rs.getString(11).split(","))));
				}
				return r;
			}
		});
	}
	public boolean addProduct(Product p)
	{
		return template.execute("insert into product(name,color,material,price,urlimg,numofproduct,size,category,category2)"
				+ "VALUES(?,?,?,?,?,?,?,?,?)",
				new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, p.getName());
				ps.setString(2, p.getColor());
				ps.setString(3, p.getMaterial());
				ps.setString(4, p.getPrice());
				ps.setString(5, p.getUrlimg());
				ps.setInt(6, p.getNumOfProduct());
				ps.setString(7, p.getList_size().get(0));
				ps.setInt(8, p.getId_cat());
				ps.setInt(9, p.getId_cat2());
				return ps.execute();
			}
		});
	}
	public List<Product> get_product1(int min, int max)
	{
		return template.query("select * from product  order by id asc limit ?,?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(2, max);ps.setInt(1, min);
				
			}
		}, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Product> p = new ArrayList<Product>();
				while(rs.next())
				{
					p.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), 
							rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10),Arrays.asList(rs.getString(11).split(","))));
				}
				return p;
			}
		});
	}
	public Product get_product_by_id(int id)
	{
		String sql="select * from product where id = "+id;
		return template.query(sql, new ResultSetExtractor<Product>() {

			@Override
			public Product extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
				{
					return new Product(rs.getInt(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), 
							rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10),Arrays.asList(rs.getString(11).split(",")));
				}
				return null;
			}
		});
	}
	public List<Product> get_product_by_cat(int id, int min, int max)
	{
		String sql="select * from product where category = ? order by id desc limit ?,?";
		return template.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
				ps.setInt(2, min);
				ps.setInt(3, max);
			}
		}, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Product> r =new ArrayList<Product>();
				while(rs.next())
				{
					try {
						r.add(new Product(rs.getInt("id"),
								rs.getString("name"),
								rs.getString("color"),
								rs.getString("material"),
								rs.getString("price"),
								rs.getString("urlimg"),
								rs.getInt("numofproduct"),
								rs.getInt("category"),
								rs.getInt("category2"), rs.getString("note"),Arrays.asList(rs.getString(11).split(","))));
					} catch (Exception e) {
						System.out.println("y");
					}
				}
				return r;
			}
		});
	}
	public List<Product> get_product_by_cat2(int id,int id2, int min, int max)
	{
		String sql="select * from product where category2 = ? and category=? order by id desc limit ?,?";
		return template.query(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id2);
				ps.setInt(2, id);
				ps.setInt(3, min);
				ps.setInt(4, max);
			}
		}, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Product> r =new ArrayList<Product>();
				while(rs.next())
				{
					r.add(new Product(rs.getInt(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getInt(7),
							rs.getInt(8),
							rs.getInt(9), rs.getString(10),Arrays.asList(rs.getString(11).split(","))));
				}
				return r;
			}
		});
	}
	public int getmax_id()
	{
		return template.query("select * from max_id_product", new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		});
	}
	public int get_row_num_by_cat(int id)
	{
		String sql="select * from row_num_cat where cat = "+id;
		return template.query(sql, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getInt(2);
				return 0;
			}
		});
	}
	public int get_row_num_by_cat2(int id, int id2)
	{
		String sql="select num from row_num_cat2 where cat = "+id +" and cat2="+id2;
		return template.query(sql, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		});
	}
	public String get_name_product(int id)
	{
		return template.query("select name from product where id  = "+id, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getString(1);
				return null;
			}
		});
	}
	public String get_name_cat(int id)
	{
		return template.query("select name from category where id  = "+id, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getString(1);
				return null;
			}
		});
	}
	public String get_name_cat2(int id)
	{
		return template.query("select name from category2 where id  = "+id, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getString(1);
				return null;
			}
		});
	}
	public Product get_product_only(int id)
	{
		return template.query("select * from product where id = ?",new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
				
			}
		}, new ResultSetExtractor<Product>() {

			@Override
			public Product extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return new Product(rs.getInt(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getInt(7),
							rs.getInt(8),
							rs.getInt(9), rs.getString(10),Arrays.asList(rs.getString(11).split(",")));
				return null;
			}
		});
	}
}
