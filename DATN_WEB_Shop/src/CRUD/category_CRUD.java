package CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import Entity.Category;
import Entity.Category2;
@Repository
public class category_CRUD {
	@Autowired
	private JdbcTemplate template;
	public List<Category2> getlist_cat2(int id_1)
	{
		return template.query("select * from category2 where id_1 = "+id_1,
				new ResultSetExtractor<List<Category2>>() {

			@Override
			public List<Category2> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Category2> r = new ArrayList<Category2>();
				while(rs.next())
				{
					r.add(new Category2(id_1, rs.getInt(2), rs.getString(3)));
				}
				return r;
			}
		});
	}
	public List<Category> get_cat()
	{
		return template.query("select * from category", new ResultSetExtractor<List<Category>>() {

			@Override
			public List<Category> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Category> r =new ArrayList<Category>();
				while(rs.next())
				{
					r.add(new Category(rs.getInt(1), rs.getString(2), getlist_cat2(rs.getInt(1))));
				}
				return r;
			}
		});
	}

}
