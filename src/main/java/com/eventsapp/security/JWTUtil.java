package com.eventsapp.security;

import com.eventsapp.valueobjects.Token;

public interface JWTUtil {
	public boolean verifyToken(String jwt_token);
	public String getScopes(String jwt_token) ;
	public Token createToken(String scopes) ;
}