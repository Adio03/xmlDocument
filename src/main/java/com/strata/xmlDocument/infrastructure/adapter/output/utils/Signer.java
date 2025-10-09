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

    public static boolean validateXmlSignature(Document document, PublicKey publicKey) throws Exception {
        // 1. Debug log: Show modulus of public key used

        // 2. Locate the Signature element in the XML
        NodeList nodeList = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nodeList == null || nodeList.getLength() == 0) {
            throw new Exception("Cannot find Signature element");
        }

        // 3. Create an XMLSignatureFactory for DOM processing
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        // 4. Create a validation context using the provided public key
        DOMValidateContext valContext = new DOMValidateContext(publicKey, nodeList.item(0));

        // 5. Unmarshal the XMLSignature into a usable object
        XMLSignature signature = fac.unmarshalXMLSignature(valContext);

        // 6. Validate the signature
        return signature.validate(valContext);
    }
}