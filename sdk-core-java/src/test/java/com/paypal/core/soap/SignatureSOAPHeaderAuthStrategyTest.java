package com.paypal.core.soap;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.paypal.core.credential.SignatureCredential;
import com.paypal.core.credential.SubjectAuthorization;
import com.paypal.core.credential.TokenAuthorization;

public class SignatureSOAPHeaderAuthStrategyTest {

	
	
	@Test
	public void generateHeaderStrategyForTokenTest() throws Exception{
		SignatureCredential signatureCredential = new SignatureCredential("testusername","testpassword","testsignature");
		SignatureSOAPHeaderAuthStrategy signatureSOAPHeaderAuthStrategy = new SignatureSOAPHeaderAuthStrategy();
		TokenAuthorization tokenAuthorization = new TokenAuthorization("accessToken","tokenSecret");
		signatureSOAPHeaderAuthStrategy.setThirdPartyAuthorization(tokenAuthorization);
		signatureCredential.setThirdPartyAuthorization(tokenAuthorization);
		String payload = signatureSOAPHeaderAuthStrategy.generateHeaderStrategy(signatureCredential);
		Assert.assertEquals("<ns:RequesterCredentials/>", payload);
	}
	
	@Test
	public void generateHeaderStrategyForSubjectTest() throws Exception{
		SignatureCredential signatureCredential = new SignatureCredential("testusername","testpassword","testsignature");
		SignatureSOAPHeaderAuthStrategy signatureSOAPHeaderAuthStrategy = new SignatureSOAPHeaderAuthStrategy();
		SubjectAuthorization subjectAuthorization = new SubjectAuthorization("testsubject"); 
		signatureSOAPHeaderAuthStrategy.setThirdPartyAuthorization(subjectAuthorization);
		signatureCredential.setThirdPartyAuthorization(subjectAuthorization);
		String payload = signatureSOAPHeaderAuthStrategy.generateHeaderStrategy(signatureCredential);
		
		
		Document dom = loadXMLFromString(payload);
		Element docEle = dom.getDocumentElement();
		NodeList credential = docEle.getElementsByTagName("ebl:Credentials");
		NodeList user = ((Element)credential.item(0)).getElementsByTagName("ebl:Username");
		NodeList psw = ((Element)credential.item(0)).getElementsByTagName("ebl:Password");
		NodeList sign = ((Element)credential.item(0)).getElementsByTagName("ebl:Signature");
		NodeList subject = ((Element)credential.item(0)).getElementsByTagName("ebl:Subject");
		
		String username= user.item(0).getTextContent();
		String password = psw.item(0).getTextContent();
		String signature = sign.item(0).getTextContent();
		String sub =  subject.item(0).getTextContent();
		
		Assert.assertEquals("testusername", username);
		Assert.assertEquals("testpassword", password);
		Assert.assertEquals("testsignature", signature);
		Assert.assertEquals("testsubject",sub);
	}
	
	@Test
	public void generateHeaderStrategyForNonThirdPartyTest() throws Exception{
		SignatureCredential signatureCredential = new SignatureCredential("testusername","testpassword","testsignature");
		SignatureSOAPHeaderAuthStrategy signatureSOAPHeaderAuthStrategy = new SignatureSOAPHeaderAuthStrategy();
		String payload = signatureSOAPHeaderAuthStrategy.generateHeaderStrategy(signatureCredential);
		
		Document dom = loadXMLFromString(payload);
		Element docEle = dom.getDocumentElement();
		NodeList credential = docEle.getElementsByTagName("ebl:Credentials");
		NodeList user = ((Element)credential.item(0)).getElementsByTagName("ebl:Username");
		NodeList psw = ((Element)credential.item(0)).getElementsByTagName("ebl:Password");
		NodeList sign = ((Element)credential.item(0)).getElementsByTagName("ebl:Signature");
		NodeList subject = ((Element)credential.item(0)).getElementsByTagName("ebl:Subject");
		
		String username= user.item(0).getTextContent();
		String password = psw.item(0).getTextContent();
		String signature = sign.item(0).getTextContent();
		Object sub =  subject.item(0);
		
		Assert.assertEquals("testusername", username);
		Assert.assertEquals("testpassword", password);
		Assert.assertEquals("testsignature", signature);
		Assert.assertNull(sub);
	}
	
	@Test
	public void setGetThirdPartyAuthorization(){
		SignatureSOAPHeaderAuthStrategy signatureSOAPHeaderAuthStrategy = new SignatureSOAPHeaderAuthStrategy();
		SubjectAuthorization subjectAuthorization = new SubjectAuthorization("testsubject"); 
		signatureSOAPHeaderAuthStrategy.setThirdPartyAuthorization(subjectAuthorization);
		Assert.assertNotNull(signatureSOAPHeaderAuthStrategy.getThirdPartyAuthorization());
	}
	
	private  Document loadXMLFromString(String xml) throws Exception
    {
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(stream);
    }
	
	
}
