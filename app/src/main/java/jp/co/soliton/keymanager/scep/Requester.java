package jp.co.soliton.keymanager.scep;

import jp.co.soliton.keymanager.LogCtrl;
import jp.co.soliton.keymanager.scep.pkimessage.CertRep;
import jp.co.soliton.keymanager.scep.request.CertificateEnrollmentRequest;
import jp.co.soliton.keymanager.scep.request.GetCACertificateRequest;
import jp.co.soliton.keymanager.scep.response.CertRepReponseHandler;
import jp.co.soliton.keymanager.scep.response.GetCACertificateResponseHandler;
import jp.co.soliton.keymanager.scep.transaction.Transaction;
import jp.co.soliton.keymanager.scep.transaction.TransactionException;
import org.bouncycastle.jce.PKCS10CertificationRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.CertStore;
import java.security.cert.X509Certificate;

public class Requester {
	public Requester() {
	}

	public CertStore getCACertificate(String url) throws RequesterException {
		try {
			return getCACertificate(new URL(url));
		} catch (MalformedURLException e) {
			throw new RequesterException(e.getMessage());
		}
	}
	
	public CertStore getCACertificate(URL url) throws RequesterException {
		try {
			LogCtrl.getInstance().info("SCEP: Get CA Certificate");
			Transaction<CertStore> transaction = new Transaction<CertStore>();
			GetCACertificateRequest request = new GetCACertificateRequest(url);
			CertStore cACertificateStore = transaction.start(request, new GetCACertificateResponseHandler());
			if (cACertificateStore == null) {
				throw new RequesterException("No CA Certificcate");
			}
			return cACertificateStore;
		} catch (Exception e) {
			throw new RequesterException(e.getMessage());
		}
	}
	
	public CertRep certificateEnrollment(
			String url,
			PKCS10CertificationRequest certificateSigningRequest,
			X509Certificate myCertificate,
			PrivateKey privateKey,
			CertStore cACertificateStore) throws RequesterException {
		
		try {
			LogCtrl.getInstance().info("SCEP: Request Certificate Enroll");
			return certificateEnrollment(
					new URL(url),
					certificateSigningRequest,
					myCertificate,
					privateKey,
					cACertificateStore);
		} catch (MalformedURLException e) {
			throw new RequesterException(e.getMessage());
		}
	}
	
	public CertRep certificateEnrollment(
			URL url,
			PKCS10CertificationRequest certificateSigningRequest,
			X509Certificate myCertificate,
			PrivateKey privateKey,
			CertStore cACertificateStore) throws RequesterException {
		
		CertificateEnrollmentRequest request = new CertificateEnrollmentRequest(url);
		request.setCertificateSigningRequest(certificateSigningRequest);
		request.setMyCertificate(myCertificate);
		request.setPrivateKey(privateKey);
		request.setCACertificateStore(cACertificateStore);
		try {
			LogCtrl.getInstance().info("SCEP: Request Certificate Enroll2");
			Transaction<CertRep> transaction = new Transaction<CertRep>();
			CertRepReponseHandler certRepResponseHandler = new CertRepReponseHandler();
			return transaction.start(request, certRepResponseHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RequesterException(e.getMessage());
		}
	}
}
