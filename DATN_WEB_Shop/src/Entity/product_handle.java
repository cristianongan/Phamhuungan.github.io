package Entity;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/*
id int not null auto_increment primary key,
name nvarchar(100) not null,
color nvarchar(10),
material nvarchar(20),
price varchar(50)
 */
public class product_handle {
	private int id;
	private String name;
	private String color;
	private String material;
	@Pattern(regexp = "^[0-9]*$", message = "phai la so nguyen duong")
	private String price;
	private CommonsMultipartFile img;
	private int NumOfProduct=0;
	private String id_c="";
	private int id_cat;
	private int id_cat2;
	private String note;
	private String list_size;
	public void get_cat()
	{
		String[] s =id_c.split(",");
		if(s.length>0)
		{
			this.id_cat=Integer.parseInt(s[0]);
			this.id_cat2=Integer.parseInt(s[1]);
		}

	}
}
