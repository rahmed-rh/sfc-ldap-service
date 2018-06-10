package com.safaricom.webservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safaricom.webservice.bean.ResponseData;
import com.safaricom.webservice.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Service
public class LDAPAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(LDAPAuthenticationService.class);
	private static final String SUCCESS_RESPONSE_CODE = "4000";
	private static final String GENERIC_RESPONSE_CODE = "4001";
	private static final String EXCEPTION_RESPONSE_CODE = "4999";

	@SuppressWarnings("unchecked")
	public ResponseData validateAuthDummy(String username, String password) {
		String responseCode;
		String responseMessage;
		String errorMessage;
		if (username
				.contains("raif")) {

			responseCode = SUCCESS_RESPONSE_CODE;
			responseMessage = "User " + username + " authenticated successfully.";
			errorMessage = "Operation successful.";

		} else {

			responseCode = GENERIC_RESPONSE_CODE;
			responseMessage = "Authentication for User " + username + " failed.";
			errorMessage = "Exception : sdksdnlkansdklna";
		}

		return new ResponseData(responseCode, responseMessage, errorMessage);
	}

	@SuppressWarnings("unchecked")
	public ResponseData validateAuth(String username, String password) {

		String responseCode;
		String responseMessage;
		String errorMessage;

		try {

			/**
			 * Consider migrating these configs to applications.properties file
			 */

			@SuppressWarnings("unused")
			LdapContext ctx = null;
			Hashtable env = new Hashtable();
			env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
			env.put("java.naming.security.authentication", "Simple");
			env.put("java.naming.security.principal", "safaricom\\" + username);
			env.put("java.naming.security.credentials", password);
			env.put("java.naming.provider.url", "ldap://172.29.120.112:389");

			ctx = new InitialLdapContext(env, null);

			responseCode = SUCCESS_RESPONSE_CODE;
			responseMessage = "User " + username + " authenticated successfully.";
			errorMessage = "Operation successful.";

			logger.info("Response Code : {} | Response Message {} | Error Message {} ", responseCode, responseMessage,
					errorMessage);

		} catch (NamingException nex) {

			/*
			 * This is a fix for some certain bug noted on LDAP for some users.
			 */
			if (System.err.toString()
					.contains("LdapErr: DSID-0C0903A9, comment: AcceptSecurityContext error, data 775")) {

				responseCode = SUCCESS_RESPONSE_CODE;
				responseMessage = "User " + username + " authenticated successfully.";
				errorMessage = "Operation successful.";

			} else {

				responseCode = GENERIC_RESPONSE_CODE;
				responseMessage = "Authentication for User " + username + " failed.";
				errorMessage = "Exception : " + nex.getMessage();
			}

		} catch (Exception ex) {

			responseCode = EXCEPTION_RESPONSE_CODE;
			responseMessage = "Authentication for User " + username + " failed.";
			errorMessage = "Exception : " + ex.getMessage();

		}

		return new ResponseData(responseCode, responseMessage, errorMessage);
	}

	public ResponseData getUserProfile(String username, String password) {

		String responseCode;
		String responseMessage;
		String errorMessage;
		User user;
		String[] userAtt = new String[4];

		try {

			/**
			 * Consider migrating these configs to applications.properties file
			 */

			@SuppressWarnings("unused")
			LdapContext ctx = null;
			Hashtable env = new Hashtable();
			env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
			env.put("java.naming.security.authentication", "Simple");
			env.put("java.naming.security.principal", "safaricom\\" + username);
			env.put("java.naming.security.credentials", password);
			env.put("java.naming.provider.url", "ldap://172.29.120.112:389");

			ctx = new InitialLdapContext(env, null);

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "distinguishedName", "sn", "givenname", "mail", "telephonenumber" };
			constraints.setReturningAttributes(attrIDs);

			NamingEnumeration<?> answer = ctx.search("OU=safaricom departments,DC=safaricom,DC=net",
					"sAMAccountName=" + username, constraints);

			System.out.println(answer);

			if (answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				/*
				 * --------------------- convert attributes to string
				 * equivalent---------------------------
				 */
				String firstName1 = attrs.get("givenname").toString();
				String lastname1 = attrs.get("sn").toString();
				String mail1 = attrs.get("mail").toString();
				// String telphone1 = attrs.get("telephonenumber").toString();
				String telphone1 = "#:#";
				/*
				 * --------------------- split the parameters at the colon and extract relevant
				 * data---------------------------
				 */
				String[] fnameAfterSplit = firstName1.split(":");
				String[] lnameAfterSplit = lastname1.split(":");
				String[] mailAfterSplit = mail1.split(":");
				String[] telephoneAfterSplit = telphone1.split(":");
				/*
				 * ---------------------clean data by removing leading
				 * spaces---------------------
				 */
				String fullname = fnameAfterSplit[1].trim() + " " + lnameAfterSplit[1].trim();
				String telephone1 = telephoneAfterSplit[1].trim();
				String mail = mailAfterSplit[1].trim();
				/*
				 * ---------------------extract username from mail ---------------------
				 */
				String[] userNameAfterSplit = mail.split("@");
				String username1 = userNameAfterSplit[0];

				// @SuppressWarnings("unused")
				// com.AddAD_Details2DB addToDB=new
				// AddAD_Details2DB(username1,fullname,mail,telephone1);
				userAtt[0] = username1.replace("\"", "");
				userAtt[1] = fullname.replace("\"", "");
				userAtt[2] = mail.replace("\"", "");
				userAtt[3] = telephone1.replace("\"", "");

				ObjectMapper mapper = new ObjectMapper();
				user = new User(username1, fullname, mail, telephone1);

				responseCode = SUCCESS_RESPONSE_CODE;
				// convert the user POJO to a JSON String
				responseMessage = mapper.writeValueAsString(user);
				errorMessage = "Operation successful.";

				logger.info("Response Code : {} | Response Message {} | Error Message {} ", responseCode,
						responseMessage, errorMessage);

				return new ResponseData(responseCode, responseMessage, errorMessage);
			} else {

				// throw new Exception("Invalid Auth")
				userAtt[0] = "DUMMY";
				userAtt[1] = "DUMMY";
				userAtt[2] = "DUMMY";
				userAtt[3] = "DUMMY";

				responseCode = EXCEPTION_RESPONSE_CODE;
				responseMessage = "Authentication for User " + username + " failed. Profile attrs are empty";
				errorMessage = "Exception : " + "Auth failure user attributes are empty";

			}
		} catch (NamingException nex) {

			/*
			 * This is a fix for some certain bug noted on LDAP for some users.
			 */
			if (System.err.toString()
					.contains("LdapErr: DSID-0C0903A9, comment: AcceptSecurityContext error, data 775")) {

				responseCode = SUCCESS_RESPONSE_CODE;
				responseMessage = "User " + username + " authenticated successfully.";
				errorMessage = "Operation successful.";

			} else {

				responseCode = GENERIC_RESPONSE_CODE;
				responseMessage = "Authentication for User " + username + " failed.";
				errorMessage = "Exception : " + nex.getMessage();
			}

		} catch (Exception ex) {

			responseCode = EXCEPTION_RESPONSE_CODE;
			responseMessage = "Authentication for User " + username + " failed.";
			errorMessage = "Exception : " + ex.getMessage();
			ex.printStackTrace();

		}

		return new ResponseData(responseCode, responseMessage, errorMessage);
	}

	private String toString(Attribute attribute) {
		// TODO Auto-generated method stub
		String xs = attribute.toString();
		return xs;
	}

}
