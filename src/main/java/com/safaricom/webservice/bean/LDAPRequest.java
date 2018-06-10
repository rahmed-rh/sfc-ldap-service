package com.safaricom.webservice.bean;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class LDAPRequest {

    @NotNull(message = "Reference Id is a required field, preferrably UUID generated.")
    private String id;

    @NotNull(message = "Username is a required field.")
    private String username;

    @NotNull(message = "Password is a required field.")
    private String password;

	@NotNull(message = "Scope is a required field possible options include auth,profile,email,phone etc.")
	private String scope;

    @NotNull(message = "authType is a required field e.g. ldap, stk, otp etc.")
    private String authType;

    public LDAPRequest() {
        super();
    }

    public LDAPRequest(String id, String username, String password, String scope, String authType) {

        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.scope = scope;
        this.authType = authType;

    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        return "LDAPRequest [id=" + id + ", username=" + username + ", password=" + password + ", scope=" + ", authType=" + authType + "]";
    }

}
