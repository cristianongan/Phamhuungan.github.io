package com.viettel.automl.controller;

import com.viettel.automl.dto.request.UserInfoRequest;
import com.viettel.automl.security.CustomUserDetails;
import com.viettel.automl.security.jwt.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UserJWTController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/authenticate")
	public Object authenticateUser(@RequestBody UserInfoRequest dto) {
//         Xác thực từ username và password.
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

		// Nếu không xảy ra exception tức là thông tin hợp lệ
		// Set thông tin authentication vào Security Context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
		Map<String, String> obj = new HashMap<>();
		obj.put("id_token", jwt);
		return obj;

//        throw new RuntimeException("bla bla");
	}

//    @ExceptionHandler(value = {Exception.class})
//    protected ResponseEntity<Object> handleAuthorizedError(Exception ex, WebRequest request) {
////        logger.error("Unexpected error: ", ex);
////        ActionAuditResponse res = new ActionAuditResponse();
////        res.setErrorCode("1");
////        res.setDescription("Badd request");
//        String str = "400 - Bad request";
//        return new ResponseEntity<>(str, HttpStatus.BAD_REQUEST);
//    }
}
