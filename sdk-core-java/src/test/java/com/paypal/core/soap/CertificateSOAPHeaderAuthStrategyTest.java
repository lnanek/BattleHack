package com.paypal.core.soap;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.paypal.core.credential.CertificateCredential;
import com.paypal.core.credential.SubjectAuthorization;
import com.paypal.core.credential.TokenAuthorization;

public class CertificateSOAPHeaderAuthStrategyTest {
	@Test
	public void generateHeaderStrategyForTokenTest() throws Exception{
		CertificateCredential certCredential = new CertificateCredential("testusername","testpassword","certkey","certpath");
		CertificateSOAPHeaderAuthStrategy certificateSOAPHeaderAuthStrategy = new CertificateSOAPHeaderAuthStrategy();
		TokenAuthorization tokenAuthorization = new TokenAuthorization("accessToken","tokenSecret");
		certificateSOAPHeaderAuthStrategy.setThirdPartyAuthorization(tokenAuthorization);
		certCredential.setThirdPartyAuthorization(tokenAuthorization);
		String payload = certificateSOAPHeaderAuthStrategy.generateHeaderStrategy(certCredential);
		Assert.assertEquals("<ns:RequesterCredentials/>", payload);
	}
	
	@Test
	public void generateHeaderStrategyForSubjectTest() throws Exception{
		CertificateCredential certCredential = new CertificateCredential("testusername","testpassword","certkey","certpath");
		CertificateSOAPHeaderAuthStrategy certificateSOAPHeaderAuthStrategy = new CertificateSOAPHeaderAuthStrategy();
		SubjectAuthorization subjectAuthorization = new SubjectAuthorization("testsubject"); 
		certificateSOAPHeaderAuthStrategy.setThirdPartyAuthorization(subjectAuthorization);
		certCredential.setThirdPartyAuthorization(subjectAuthorization);
		String payload = certificateSOAPHeaderAuthStrategy.generateHeaderStrategy(certCredential);
		
		
		Document dom = loadXMLFromString(payload);
		Element docEle = dom.getDocumentElement();
		NodeList credential = docEle.getElementsByTagName("ebl:Credentials");
		NodeList user = ((Element)credential.item(0)).getElementsByTagName("ebl:Username");
		NodeList psw = ((Element)credential.item(0)).getElementsByTagName("ebl:Password");
		NodeList sign = ((Element)credential.item(0)).getElementsByTagName("ebl:Signature");
		NodeList subject = ((Element)credential.item(0)).getElementsByTagName("ebl:Subject");
		
		String username= user.item(0).getTextContent();
		String password = psw.item(0).getTextContent();
		Object signature = sign.item(0);
		String sub =  subject.item(0).getTextContent();
		
		Assert.assertEquals("testusername", username);
		Assert.assertEquals("testpassword", password);
		Assert.assertNull(signature);
		Assert.assertEquals("testsubject",sub);
	}
	
	@Test
	public void generateHeaderStrategyForNonThirdPartyTest() throws Exception{
		CertificateCredential certCredential = new CertificateCredential("testusername","testpassword","certkey","certpath");
		CertificateSOAPHeaderAuthStrategy certificateSOAPHeaderAuthStrategy = new CertificateSOAPHeaderAuthStrategy();
		String payload = certificateSOAPHeaderAuthStrategy.generateHeaderStrategy(certCredential);
		
		Document dom = loadXMLFromString(payload);
		Element docEle = dom.getDocumentElement();
		NodeList credential = docEle.getElementsByTagName("ebl:Credentials");
		NodeList user = ((Element)credential.item(0)).getElementsByTagName("ebl:Username");
		NodeList psw = ((Element)credential.item(0)).getElementsByTagName("ebl:Password");
		NodeList sign = ((Element)credential.item(0)).getElementsByTagName("ebl:Signature");
		NodeList subject = ((Element)credential.item(0)).getElementsByTagName("ebl:Subject");
		
		String username= user.item(0).getTextContent();
		String password = psw.item(0).getTextContent();
		Object signature = sign.item(0);
		Object sub =  subject.item(0);
		
		Assert.assertEquals("testusername", username);
		Assert.assertEquals("testpassword", password);
		Assert.assertNull(signature);
		Assert.assertNull(sub);
	}
	
	@Test
	public void setGetThirdPartyAuthorization(){
		CertificateSOAPHeaderAuthStrategy certificateSOAPHeaderAuthStrategy = new CertificateSOAPHeaderAuthStrategy();
		SubjectAuthorization subjectAuthorization = new SubjectAuthorization("testsubject"); 
		certificateSOAPHeaderAuthStrategy.setThirdPartyAuthorization(subjectAuthorization);
		Assert.assertNotNull(certificateSOAPHeaderAuthStrategy.getThirdPartyAuthorization());
	}
	
	private  Document loadXMLFromString(String xml) throws Exception
    {
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(stream);
    }
}
