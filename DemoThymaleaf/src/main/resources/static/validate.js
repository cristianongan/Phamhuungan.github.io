/**
 * 
 */
function validation() {
	var status = true;
	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("email").value;
	var userName = document.getElementById("userName").value;
	var password = document.getElementById("password").value;
	var Repass = document.getElementById("Re-password").value;
	var focus = "";

	if (password.length < 1) {
		document.getElementById("passwordE").innerHTML = "size > 1!!! Oke?";
		focus = "password";
		status = false;
	} else
		if (password !== Repass) {
			document.getElementById("Re-passwordE").innerHTML = "password & Re-password is not the same";
			focus = "Re-password";
			status = false;
		}
	if (userName.length < 1) {
		document.getElementById("userNameE").innerHTML = "size > 1!!! Oke?";
		focus = "userName";
		status = false;
	}
	if (!/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
		.test(email)) {
		document.getElementById("emailE").innerHTML = "--> ...@...com";
		focus = "email";
		status = false;
	}
	if (lastName.length < 1) {
		document.getElementById("lastNameE").innerHTML = "size > 1!!! Oke?";
		focus = "lastName";
		status = false;
	} else
		if (!/^[A-Z]/.test(lastName)) {
			document.getElementById("lastNameE").innerHTML = "--> P...";
			focus = "lastName";
			status = false;
		}
	if (firstName.length < 1) {
		document.getElementById("firstNameE").innerHTML = "size > 1!!! Oke?";
		focus = "firstName";
		status = false;
	} else
		if (!/^[A-Z]/.test(firstName)) {
			document.getElementById("firstNameE").innerHTML = "--> P...";
			focus = "firstName";
			status = false;
		}

	document.getElementById(focus).focus();
	return status;
}
