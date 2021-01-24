package Entity;

import java.sql.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class user_full {
/*id int not null,
title varchar(50) check (title in ("mr","mrs","miss")),
firstname nvarchar(100),
Lastname nvarchar(100),
DOB date,
Address varchar(50),
city nvarchar(50),
additional_information nvarchar(1000),
homephone varchar(11),
mobilephone varchar(10),*/
	@NotNull(message = "not null")
	@Size(min = 4, max = 50, message = "invalid username")
	private String username;
	@NotNull(message = "not null")
	@Size(min = 1, max = 50, message = "invalid password")
	private String password;
	private int id;
	@Pattern(regexp = "^[0-9][0-9][0-9][0-9]/[0-9][0-9]/[0-9][0-9]$", message = "invalid")
	private String DOB;
	@Pattern(message = "Miss|Mrs|Mr", regexp = "^(Miss|Mrs|Mr)$")
	@NotNull(message = "not null")
	private String title;
	@NotNull(message = "not null")
	private String firstname;
	@NotNull(message = "not null")
	private String lastname;
	@NotNull(message = "not null")
	private String Address;
	@NotNull(message = "not null")
	private String city;
	private String additional_information;
	@Size(min = 10, max = 13, message = "invalid")
	@Pattern(regexp = "^[0-9]*$", message = "invalid")
	private String homephone;
	@Size(min = 10, max = 11, message = "invalid")
	@Pattern(regexp = "^0[0-9]*$", message = "invalid(start with 0...)")
	private String mobilephone;
}
