package CRUD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import Entity.Product;
import Entity.order;
import Entity.user_full;

@Repository
public class order_CRUD {
	@Autowired
	private SimpleJdbcCall call2;
	@Autowired
	private JdbcTemplate template;
	@Transactional
	public boolean delete_order(int madh)
	{
		boolean b= template.execute("delete from order_pd  where "
				+ "order_pd.id = ?", new PreparedStatementCallback<Boolean>() {
			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, madh);
				return ps.executeUpdate()==1?true:false;
			}
		});
		if(b)
		{
			return template.execute("delete from order_ma  where "
					+ "order_ma.id = ?", new PreparedStatementCallback<Boolean>() {
						@Override
						public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
							ps.setInt(1, madh);
							return ps.executeUpdate()==1?true:false;
						}
					});
		}
		return b;
	}
	@Transactional
	public boolean update_order(order o)
	{
		return template.execute("update order_ma set addr=?,status=? where id=?", new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, o.getAddr());
				ps.setString(2, o.getStatus());
				ps.setInt(3, o.getMadh());
				return ps.executeUpdate()==1?true:false;
			}
		});
	}
	@Transactional
	public int order(user_full u)
	{
		MapSqlParameterSource in = new MapSqlParameterSource();
		in.addValue("u", u.getId());
		in.addValue("a", u.getAddress()+u.getCity());
		return (int) call2.execute(in).get("i");
	}
	@Transactional
	public boolean add_product(Product p,int ma)
	{
		return template.execute("insert into order_pd(id,nameofproduct,price,numofproduct)values(?,?,?,?)",
				new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				System.out.println("CRUD:-"+p.getNumOfProduct());
				ps.setInt(1, ma);
				ps.setString(2, p.getName()+"-color:"+p.getColor()+"-size:"+p.getList_size().get(0));
				ps.setString(3, p.getPrice());
				ps.setInt(4, p.getNumOfProduct());
				return (ps.executeUpdate()==1)?true:false;
			}
		});
	}
	public List<Product> get_product_ordered(int ma)
	{
		 return template.query("select * from order_pd where id =?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, ma);
				
			}
		}, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Product> li =new ArrayList<Product>();
				
				while(rs.next())
				{
					Product p = new Product();
					p.setName(rs.getString(3));
					p.setNumOfProduct(rs.getInt(5));
					p.setPrice(rs.getString(4));
					li.add(p);
				}
				return li;
			}
		});
	}
	public List<order> get_list_order(int id)
	{
		return template.query("select id,created from order_MA where user = ?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
				
			}
		}, new ResultSetExtractor<List<order>>() {

			@Override
			public List<order> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<order> lo = new ArrayList<order>();
				while(rs.next())
				{
					order o= new order();
					o.setMadh(rs.getInt(1));
					o.setNgay_tao_don(rs.getDate(2));
					lo.add(o);
				}
				return lo;
			}
		});
	}
	public List<order> search(int id)
	{
		return template.query("select id,created,user from order_MA where id = ?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		}, new ResultSetExtractor<List<order>>() {

			@Override
			public List<order> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<order> lo = new ArrayList<order>();
				while(rs.next())
				{
					order o= new order();
					o.setMadh(rs.getInt(1));
					o.setNgay_tao_don(rs.getDate(2));
					o.setId_user(rs.getInt(3));
					lo.add(o);
				}
				return lo;
			}
		});
	}
	public Map<String, Object> get_order_MA(int ma)
	{
		return template.query("select user,created,status,addr from order_MA where id = ?",new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, ma);
				
			}
		}, new ResultSetExtractor<Map<String, Object>>() {

			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, Object> m = new HashMap<String, Object>();
				if(rs.next())
					m.put("user", rs.getInt(1));
					m.put("date", rs.getDate(2));
					m.put("status", rs.getString(3));
					m.put("addr", rs.getString(4));
				return m;
			}
		} );
	}
	public List<order> admin_get_list_order(int min,int max)
	{
		return template.query("select * from order_MA limit ?,?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, min);
				ps.setInt(2, max);
			}
		}, new ResultSetExtractor<List<order>>() {

			@Override
			public List<Entity.order> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Entity.order> r =new ArrayList<order>();
				while(rs.next())
				{
					r.add(new order(rs.getInt(1), rs.getDate(3), rs.getInt(2)));
				}
				return r;
			}
		});
	}
	public int get_row_order()
	{
		return template.query("select r from row_num_order",new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int a = 0;
				if(rs.next())
					a=rs.getInt(1);
				return a;
			}
		});
	}
}
