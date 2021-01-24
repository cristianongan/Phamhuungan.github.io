package Entity;


import org.springframework.stereotype.Component;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component("category")
public class Category2 {
	private int categoryId;
	private int categoryId2;
	private String name;
}
