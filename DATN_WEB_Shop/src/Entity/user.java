package Entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class user {
	@NotNull
	@Size(min = 4, max = 50, message = "invalid username")
	private String username;
	@NotNull
	@Size(min = 1, max = 50, message = "invalid password")
	private String password;
	private int id;
	public user(String username, String password)
	{
		this.username=username;
		this.password=password;
	}
}
