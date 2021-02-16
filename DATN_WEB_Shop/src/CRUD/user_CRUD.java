package CRUD;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import Entity.user;
import Entity.user_full;

@Repository
public class user_CRUD {
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private SimpleJdbcCall call;
	@Transactional
	public boolean delete(int id)
	{
		String sql="delete from info where info.id = ?";
		String sql2="delete from user where user.id = ?";
		boolean a =template.execute(sql, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, id);
				return ps.executeUpdate()==1?true:false;
			}
		});
		if(a)
			return template.execute(sql2, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					ps.setInt(1, id);
					return ps.executeUpdate()==1?true:false;
				}
			});
		else
			return false;
		
	}
	public int num_user()
	{
		return template.query("select num from num_user", new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		});
	}
	public List<user> list_user(int min, int max)
	{
		String sql="select id,username from user limit ?,?";
		return template.query(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, min);
				ps.setInt(2, max);
			}
		}, new ResultSetExtractor<List<user>>() {

			@Override
			public List<user> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<user> r =new ArrayList<user>();
				while(rs.next())
				{
					r.add(new user(rs.getString(2), "", rs.getInt(1)));
				}
				return r;
			}
		});
	}
	public boolean login(user u)
	{
		return template.query("select password from user where username='"+u.getUsername()+"'", new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next())
					if(rs.getString(1).equals(u.getPassword()))
						return true;
				return false;
			}
			});
	}
	@Transactional
	public boolean register(user_full u)
	{
		MapSqlParameterSource in = new MapSqlParameterSource();
		in.addValue("u", u.getUsername());
		in.addValue("p", u.getPassword());
		try {
			Map<String, Object> out = call.execute(in);
			return template.execute("insert into info(id,title,firstname,Lastname,DOB,Address,city,"
					+ "additional_information,homephone,mobilephone)"
					+ "values(?,?,?,?,?,?,?,?,?,?)", 
					new PreparedStatementCallback<Boolean>() {
						@Override
						public Boolean doInPreparedStatement(PreparedStatement ps)
								throws SQLException, DataAccessException {
							ps.setInt(1, (int) out.get("i"));
							ps.setString(2,u.getTitle());
							ps.setString(3,u.getFirstname());
							ps.setString(4,u.getLastname());
							try {
								ps.setDate(5, new java.sql.Date(new SimpleDateFormat("yyyy/mm/dd").parse(u.getDOB()).getTime()));
							} catch (ParseException e) {
								System.out.println("!");
								e.printStackTrace();
								return false;
							}
							ps.setString(6,u.getAddress());
							ps.setString(7,u.getCity());
							ps.setString(8,u.getAdditional_information());
							ps.setString(9,u.getHomephone());
							ps.setString(10,u.getMobilephone());
							return (ps.executeUpdate()==1)?true:false;
						}
					});
		} catch (Exception e) {
			System.out.println("e:"+e);
			return false;
		}
		
	}
	public user_full get_full_info_user(user u)
	{
		return template.query("with ss as (select id from user where username= ?)"
				+ "select info.* from info right join ss on ss.id = info.id", new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1,u.getUsername());
						
					}
				}, new ResultSetExtractor<user_full>() {

					@Override
					public user_full extractData(ResultSet rs) throws SQLException, DataAccessException {
						if(rs.next())
						{
							user_full us = new user_full(u.getUsername(), u.getPassword(), Integer.parseInt(rs.getString("id")), 
									rs.getString("DOB"), rs.getString("title"), rs.getString("firstname"),
									rs.getString("lastname"), rs.getString("address"),
									rs.getString("city"), rs.getNString("additional_information"),
									rs.getString("homephone"), rs.getString("mobilephone"));
							return us;
						}
						return null;
					}
				});
	}
	public user_full get_full_info_user(int u)
	{
		return template.query("select * from info right join user on user.id = info.id where user.id=?", new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1,u);
						
					}
				}, new ResultSetExtractor<user_full>() {

					@Override
					public user_full extractData(ResultSet rs) throws SQLException, DataAccessException {
						if(rs.next())
						{
							user_full us = new user_full(rs.getString("username"), rs.getString("password"), u, 
									rs.getString("DOB"), rs.getString("title"), rs.getString("firstname"),
									rs.getString("lastname"), rs.getString("address"),
									rs.getString("city"), rs.getNString("additional_information"),
									rs.getString("homephone"), rs.getString("mobilephone"));
							return us;
						}
						return null;
					}
				});
	}
	public boolean update_info(user_full u)
	{
		String sql ="update info left join user on info.id=user.id set "
				+ "info.title =?,info.firstname =?, info.Lastname=?,info.DOB=?,info.Address=?,info.city=?"
				+ ",info.additional_information=?,info.homephone=?,info.mobilephone=?"
				+ " where user.username=?";
		
		return template.execute(sql, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1,u.getTitle());
				ps.setString(2,u.getFirstname());
				ps.setString(3,u.getLastname());
				try {
					ps.setDate(4,(Date) new SimpleDateFormat("yyyy/mm/dd").parse(u.getDOB()));
				} catch (ParseException e) {
					e.printStackTrace();
					return false;
				}
				ps.setString(5,u.getAddress());
				ps.setString(6,u.getCity());
				ps.setString(7,u.getAdditional_information());
				ps.setString(8,u.getHomephone());
				ps.setString(9,u.getMobilephone());
				return ps.execute();
			}
		});
	}
	

}
