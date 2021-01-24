package Entity;

import java.util.List;

import javax.validation.constraints.Pattern;

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
public class Product {
	private int id;
	private String name;
	private String color;
	private String material;
	@Pattern(regexp = "^[0-9]*$", message = "phai la so nguyen duong")
	private String price;
	private String urlimg;
	private int NumOfProduct=0;
	private int id_cat;
	private int id_cat2;
	private String note;
	private List<String> list_size;
	
	@Override
		public boolean equals(Object obj) {
			Product p= (Product) obj;
			if(p.getId()==this.getId())
				return true;
			
			return false;
		}
}
