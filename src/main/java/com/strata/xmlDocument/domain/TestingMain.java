package com.strata.xmlDocument.domain;

import com.strata.xmlDocument.infrastructure.adapter.output.utils.Encrypter;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.GenerateKey;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.Signer;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.XmlStringCryptoService;
import org.springframework.beans.factory.annotation.Value;

import java.security.PrivateKey;
import java.security.PublicKey;

public class TestingMain {
    @Value("${nibss.private.key.path}")
    private static String privateKeyPath;

    @Value("${nibss.public.key.path}")
    private static String publicKeyPath;

    private static final String PRIVATE_KEY_PATH = "C:\\\\Users\\\\semicolon\\\\Downloads\\\\xmlDocument\\\\xmlDocument\\\\banks_private.pem";
    private static final String PUBLIC_KEY_PATH = "C:\\\\Users\\\\semicolon\\\\Downloads\\\\xmlDocument\\\\xmlDocument\\\\banks_public.pem";

    public static void main(String[] args) throws Exception {
        PrivateKey privateKey = GenerateKey.loadPrivateKey(PRIVATE_KEY_PATH);
        PublicKey publicKey = GenerateKey.loadPublicKey(PUBLIC_KEY_PATH);
        publicKey.toString();
        String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <ns2:Document xmlns:ns2="urn:iso:std:iso:20022:tech:xsd:pacs.008.001.12">
                    <FIToFICstmrCdtTrf>
                        <GrpHdr>
                            <MsgId>99903320251014113548000833068385030</MsgId>
                            <CreDtTm>2025-10-14T11:35:48.000Z</CreDtTm>
                            <BtchBookg>false</BtchBookg>
                            <NbOfTxs>1</NbOfTxs>
                            <SttlmInf>
                                <SttlmMtd>CLRG</SttlmMtd>
                            </SttlmInf>
                            <InstgAgt>
                                <FinInstnId>
                                    <BICFI>999033</BICFI>
                                    <ClrSysMmbId>
                                        <MmbId>999033</MmbId>
                                    </ClrSysMmbId>
                                </FinInstnId>
                            </InstgAgt>
                            <InstdAgt>
                                <FinInstnId>
                                    <BICFI/>
                                    <ClrSysMmbId>
                                        <MmbId>999999</MmbId>
                                    </ClrSysMmbId>
                                </FinInstnId>
                            </InstdAgt>
                        </GrpHdr>
                        <CdtTrfTxInf>
                            <PmtId>
                                <InstrId>99903399999920251014113548068385030</InstrId>
                                <EndToEndId>99903320251014113548000833068385030</EndToEndId>
                                <TxId>99903320251014113548000833068385030</TxId>
                            </PmtId>
                            <PmtTpInf>
                                <ClrChanl>RTNS</ClrChanl>
                                <SvcLvl>
                                    <Prtry>0100</Prtry>
                                </SvcLvl>
                                <LclInstrm>
                                    <Prtry>CTAA</Prtry>
                                </LclInstrm>
                                <CtgyPurp>
                                    <Prtry>001</Prtry>
                                </CtgyPurp>
                            </PmtTpInf>
                            <IntrBkSttlmAmt Ccy="NGN">100.00</IntrBkSttlmAmt>
                            <IntrBkSttlmDt>2025-10-14T11:35:48.000Z</IntrBkSttlmDt>
                            <ChrgBr>SLEV</ChrgBr>
                            <InstgAgt>
                                <FinInstnId>
                                    <BICFI/>
                                    <ClrSysMmbId>
                                        <MmbId>999033</MmbId>
                                    </ClrSysMmbId>
                                </FinInstnId>
                            </InstgAgt>
                            <InstdAgt>
                                <FinInstnId>
                                    <BICFI/>
                                    <ClrSysMmbId>
                                        <MmbId>999999</MmbId>
                                    </ClrSysMmbId>
                                </FinInstnId>
                            </InstdAgt>
                            <Dbtr>
                                <Nm>SUNDAY KUSORO OLADAYO</Nm>
                            </Dbtr>
                            <DbtrAcct>
                                <Id>
                                    <IBAN>1005847601</IBAN>
                                </Id>
                                <Nm>SUNDAY KUSORO OLADAYO</Nm>
                            </DbtrAcct>
                            <DbtrAgt>
                                <FinInstnId>
                                    <ClrSysMmbId>
                                        <MmbId>999033</MmbId>
                                    </ClrSysMmbId>
                                </FinInstnId>
                            </DbtrAgt>
                            <CdtrAgt>
                                <FinInstnId>
                                    <ClrSysMmbId>
                                        <MmbId>999999</MmbId>
                                    </ClrSysMmbId>
                                </FinInstnId>
                            </CdtrAgt>
                            <Cdtr>
                                <Nm>PAUL CHIZOBA AGU</Nm>
                            </Cdtr>
                            <CdtrAcct>
                                <Id>
                                    <IBAN>1400023728</IBAN>
                                </Id>
                                <Nm>PAUL CHIZOBA AGU</Nm>
                            </CdtrAcct>
                            <InstrForNxtAgt>
                                <InstrInf>/BNF/Beneficiary info</InstrInf>
                            </InstrForNxtAgt>
                            <InstrForNxtAgt>
                                <InstrInf>/SMPL/Sample data</InstrInf>
                            </InstrForNxtAgt>
                            <RmtInf>
                                <Ustrd>MOB UTO ADEWALE ALONGE rent 22797082689</Ustrd>
                            </RmtInf>
                        </CdtTrfTxInf>
                        <SplmtryData>
                            <PlcAndNm>AdditionalVerificationDetails</PlcAndNm>
                            <Envlp>
                                <CustomData>
                                    <DebtorInfo>
                                        <AccountDesignation>1</AccountDesignation>
                                        <IdType>bvn</IdType>
                                        <IdValue>22248785427</IdValue>
                                        <AccountTier>1</AccountTier>
                                    </DebtorInfo>
                                    <DebtorMetadata>
                                    </DebtorMetadata>
                                    <CreditorInfo>
                                        <AccountDesignation>1</AccountDesignation>
                                        <IdType>bvn</IdType>
                                        <IdValue>22248598727</IdValue>
                                        <AccountTier>1</AccountTier>
                                    </CreditorInfo>
                                    <CreditorMetadata>
                                    </CreditorMetadata>
                                    <TransactionInfo>
                                        <TransactionLocation></TransactionLocation>
                                        <NameEnquiryMsgId></NameEnquiryMsgId>
                                        <ChannelCode>1</ChannelCode>
                                        <RiskRating>R000000000000000000B9</RiskRating>
                                    </TransactionInfo>
                                </CustomData>
                            </Envlp>
                        </SplmtryData>
                    </FIToFICstmrCdtTrf>
                </ns2:Document>
                """;
        String rootTag = "FIToFICstmrCdtTrf";
        System.out.println("SIGN =======================================>>>>>>>>>>>>>>>>>>>");
        String signedXml = XmlStringCryptoService.sign(xml, privateKey);
        System.out.println(signedXml);

        System.out.println("VALIDATE SIGNATURE (SIGNED XML) =======================================>>>>>>>>>>>>>>>>>>>");
        boolean signatureValid = XmlStringCryptoService.validateSignature(signedXml, publicKey);

        System.out.printf("Signature valid before encryption: %s%n", signatureValid);

        System.out.println("ENCRYPT =======================================>>>>>>>>>>>>>>>>>>>");
        String encryptedData = XmlStringCryptoService.encrypt(signedXml, publicKey, rootTag);
        System.out.printf("EncryptedData  %s%n", encryptedData);

        System.out.println("DECRYPT =======================================>>>>>>>>>>>>>>>>>>>");
        String decryptedData = XmlStringCryptoService.decrypt(encryptedData, privateKey);
        System.out.printf("Decrypted Data :%s%n", decryptedData);

        System.out.println("VALIDATE SIGNATURE (DECRYPTED XML) =======================================>>>>>>>>>>>>>>>>>>>");
        boolean signatureValidAfterDecrypt = XmlStringCryptoService.validateSignature(decryptedData, publicKey);
        System.out.printf("Signature valid after decrypt: %s%n", signatureValidAfterDecrypt);




    }
}
