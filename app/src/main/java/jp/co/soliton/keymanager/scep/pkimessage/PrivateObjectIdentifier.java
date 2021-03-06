package jp.co.soliton.keymanager.scep.pkimessage;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/*
 *   The OIDs used in SCEP are VeriSign self-maintained OIDs.
   +-------------------+-----------------------------------------------+
   | Name              | ASN.1 Definition                              |
   +-------------------+-----------------------------------------------+
   | id-VeriSign       | OBJECT_IDENTIFIER ::= {2 16 US(840) 1         |
   |                   | VeriSign(113733)}                             |
   | id-pki            | OBJECT_IDENTIFIER ::= {id-VeriSign pki(1)}    |
   | id-attributes     | OBJECT_IDENTIFIER ::= {id-pki attributes(9)}  |
   | id-messageType    | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | messageType(2)}                               |
   | id-pkiStatus      | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | pkiStatus(3)}                                 |
   | id-failInfo       | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | failInfo(4)}                                  |
   | id-senderNonce    | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | senderNonce(5)}                               |
   | id-recipientNonce | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | recipientNonce(6)}                            |
   | id-transId        | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | transId(7)}                                   |
   | id-extensionReq   | OBJECT_IDENTIFIER ::= {id-attributes          |
   |                   | extensionReq(8)}                              |
   +-------------------+-----------------------------------------------+
 */
public interface PrivateObjectIdentifier {
	ASN1ObjectIdentifier veriSign = new ASN1ObjectIdentifier("2.16.840.1.113733");
	ASN1ObjectIdentifier pki = veriSign.branch("1");
	ASN1ObjectIdentifier attributes = pki.branch("9");
	ASN1ObjectIdentifier messageType = attributes.branch("2");
	ASN1ObjectIdentifier pkiStatus = attributes.branch("3");
	ASN1ObjectIdentifier failInfo = attributes.branch("4");
	ASN1ObjectIdentifier senderNonce = attributes.branch("5");
	ASN1ObjectIdentifier recipientNonce = attributes.branch("6");
	ASN1ObjectIdentifier transId = attributes.branch("7");
	ASN1ObjectIdentifier extensionReq = attributes.branch("8");

	ASN1ObjectIdentifier getASN1ObjectIdentifier();
}
