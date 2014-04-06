package com.paypal.ipn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.core.ConfigManager;
import com.paypal.core.DataProviderClass;

public class IPNListenerTest {
	
	private String ipnSample = "fees_payer=EACHRECEIVER&payment_request_date=Thu+Dec+06+22%3A50%3A00+PST+2012&transaction[0].is_primary_receiver=false&transaction[0].pending_reason=NONE&cancel_url=http%3A%2F%2Flocalhost%3A9080%2Fadaptivepayments-sample%2Findex.html&status=COMPLETED&transaction_type=Adaptive+Payment+PAY&transaction[0].status=Completed&verify_sign=AM1sBeDL1IjnsgstrDz8f0QWZStzApiXR3gXXjJUE15uzMlXzQmgS-.C&charset=windows-1252&sender_email=jb-us-seller%40paypal.com&log_default_shipping_address_in_transaction=false&transaction[0].amount=USD+2.00&pay_key=AP-70354820B64901803&reverse_all_parallel_payments_on_error=false&ipn_notification_url=https%3A%2F%2Fnpi.pagekite.me%2Fadaptivepaymentssample%2FIPNListener&transaction[0].id=0UJ53158NW5107715&return_url=http%3A%2F%2Flocalhost%3A9080%2Fadaptivepayments-sample%2Findex.html&transaction[0].receiver=platfo_1255612361_per%40gmail.com&transaction[0].id_for_sender_txn=8UL93971B69293341&action_type=PAY&notify_version=UNVERSIONED&transaction[0].status_for_sender_txn=Completed&test_ipn=1";
	private IPNMessage ipnmsg;
	private Map<String,String[]> ipnMap;
	
	@BeforeClass
	public void initialization() throws IOException {
		ipnMap = new HashMap<String,String[]>();
		for(String element : ipnSample.split("&")){
			String[] params = element.split("=");
			ipnMap.put(params[0], new String [] {params[1]});
		}
	}
	
	@AfterClass
	public void destory(){
		ipnmsg = null;
	}
	
	@Test(dataProvider = "configParams", dataProviderClass = DataProviderClass.class)
	public void getIpnMapTest(ConfigManager config) throws IOException{
		ipnmsg=new IPNMessage(ipnMap);
		Map<String,String> map = ipnmsg.getIpnMap();
		Assert.assertNotNull(map);
	}
	
	@Test(dependsOnMethods = {"getIpnMapTest"})
	public void getIpnValueTest(){
		String feesPayer = ipnmsg.getIpnValue("fees_payer");
		Assert.assertEquals("EACHRECEIVER", feesPayer);
	}
	
	@Test(dependsOnMethods = {"getIpnValueTest"})
	public void validateTest(){
		Assert.assertEquals(false, ipnmsg.validate());
	}
	
	@Test(dependsOnMethods = {"validateTest"})
	public void getTransactionTypeTest(){
		String transactionType = ipnmsg.getTransactionType();
		Assert.assertEquals("Adaptive Payment PAY", transactionType);
	}
	
}
