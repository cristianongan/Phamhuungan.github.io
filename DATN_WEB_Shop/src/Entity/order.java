package Entity;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class order {
	private int madh;
	private List<Product> list;
	private int tong_gia;
	private Date ngay_tao_don;
	private int id_user;
	public order(int madh, Date ngay_tao_don, int id_user) {
		super();
		this.madh = madh;
		this.ngay_tao_don = ngay_tao_don;
		this.id_user = id_user;
	}
	
}
