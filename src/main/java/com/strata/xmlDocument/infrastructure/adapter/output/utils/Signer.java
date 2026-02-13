package com.strata.xmlDocument.infrastructure.adapter.output.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.security.*;
import java.util.Collections;

public final class Signer {
    static {
        Security.addProvider(new BouncyCastleProvider());
        org.apache.xml.security.Init.init();
    }

    private Signer() {
    }


    public static void sign(Document document, PrivateKey privateKey) throws Exception {
        XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM");
        Reference ref = sigFactory.newReference(
                "",
                sigFactory.newDigestMethod(DigestMethod.SHA256, null),
                Collections.singletonList(
                    sigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)
                ),
                null,
                null
        );
        SignedInfo signedInfo = sigFactory.newSignedInfo(
                sigFactory.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA256, null),
                Collections.singletonList(ref)
        );
        DOMSignContext dsc = new DOMSignContext(privateKey, document.getDocumentElement());
        XMLSignature signature = sigFactory.newXMLSignature(signedInfo, null);
        signature.sign(dsc);
    }

    public static boolean validateXmlSignature(Document doc, PublicKey publicKey) throws Exception {

        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nl == null || nl.getLength() == 0) {
            throw new Exception("Cannot find Signature element");
        }

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        DOMValidateContext valContext = new DOMValidateContext(publicKey, nl.item(0));

        XMLSignature signature = fac.unmarshalXMLSignature(valContext);

        return signature.validate(valContext);
    }
}