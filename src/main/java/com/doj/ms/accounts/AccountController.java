/**
 * 
 */
package com.doj.ms.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author Dinesh.Rajput
 *
 */
@RestController
public class AccountController {

	protected Logger logger = Logger
			.getLogger(AccountController.class.getName());
	
	@Autowired
	AccountRepository accountRepository;
	
	//@RequestMapping("/accounts")
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "getDataFallBack")
	public Account[] all() throws Exception {
		logger.info("accounts-microservice all() invoked");
		List<Account> accounts = accountRepository.getAllAccounts();
		
		
		/*	 if(true){
				
				System.out.println("Inside if..");
				throw new ArithmeticException(); 
			}
			
		*/
			System.out.println("Inside account Call.class...");
		logger.info("accounts-microservice all() found: " + accounts.size());
		return accounts.toArray(new Account[accounts.size()]);
	}
	
	private Map<String, Account> accountsByNumber = new HashMap<String, Account>();
public Account[] getDataFallBack() {
		System.out.println("inside fall back method....");
		Account account = new Account(000l, "Karunakar Reddy --fallback" , "00");
		accountsByNumber.put("00", account);
		account = new Account(111l, "Anamika  -- fallback" , "11");
		accountsByNumber.put("11", account);
		account = new Account(222l, "Dinesh" , "22");
		accountsByNumber.put("22", account);
		
		List<Account> accounts = new ArrayList<Account>(accountsByNumber.values());
		return accounts.toArray(new Account[accounts.size()]);
		
	}
/**public String getDataFallBack() {
	System.out.println("inside fall back method....");

	List<Account> accounts;
	
	ArrayList al = new ArrayList();
	al.add("Karunakar Reddy...");
	
	System.out.println("inside fall back method..;after.....");
	
	return al.toArray(new Account[al.size()]);
	
}*/

	@RequestMapping("/accounts/{id}")
	public Account byId(@PathVariable("id") String id) {
		logger.info("accounts-microservice byId() invoked: " + id);
		Account account = accountRepository.getAccount(id);
		logger.info("accounts-microservice byId() found: " + account);
		return account;
	}
}
