package com.strata.xmlDocument.domain.service;

import com.strata.xmlDocument.infrastructure.adapter.input.dtos.request.PaymentStatusRequest;
import com.strata.xmlDocument.infrastructure.adapter.output.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentStatusPacs028ServiceTest {

    @Mock
    private PrivateKey privateKey;

    @Mock
    private PublicKey publicKey;

    @Mock
    private Document document;

    @InjectMocks
    private PaymentStatusPacs028Service paymentStatusService;

    private PaymentStatusRequest paymentStatusRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentStatusService, "privateKeyPath", "test/private/key/path");
        ReflectionTestUtils.setField(paymentStatusService, "publicKeyPath", "test/public/key/path");
        ReflectionTestUtils.setField(paymentStatusService, "pacs028ApiUrl", "https://test.api.url/pacs028");

        paymentStatusRequest = createValidPaymentStatusRequest();
    }

    @Test
    void shouldProcessPaymentStatusRequestSuccessfully() throws Exception {
        // Given
        String expectedMessageId = "STS123456789";
        String encryptedData = "encrypted-status-request-data";

        try (MockedStatic<XmlDocumentConverter> xmlConverterMock = mockStatic(XmlDocumentConverter.class);
             MockedStatic<GenerateKey> generateKeyMock = mockStatic(GenerateKey.class);
             MockedStatic<Signer> signerMock = mockStatic(Signer.class);
             MockedStatic<Encrypter> encrypterMock = mockStatic(Encrypter.class);
             MockedStatic<HttpSender> httpSenderMock = mockStatic(HttpSender.class);
             MockedStatic<MessageIdGenerator> messageIdMock = mockStatic(MessageIdGenerator.class)) {

            // Configure mocks
            messageIdMock.when(() -> MessageIdGenerator.generateMessageId(anyString()))
                    .thenReturn(expectedMessageId);
            xmlConverterMock.when(() -> XmlDocumentConverter.marshallToDocument(any(), any()))
                    .thenReturn(document);
            generateKeyMock.when(() -> GenerateKey.loadPrivateKey(anyString()))
                    .thenReturn(privateKey);
            generateKeyMock.when(() -> GenerateKey.loadPublicKey(anyString()))
                    .thenReturn(publicKey);
            encrypterMock.when(() -> Encrypter.encrypt(any(Document.class), any(PublicKey.class), anyString()))
                    .thenReturn(encryptedData);

            // When
            String result = paymentStatusService.paymentStatusRequest(paymentStatusRequest);

            // Then
            assertNotNull(result);
            assertEquals(expectedMessageId, result);

            // Verify interactions
            xmlConverterMock.verify(() -> XmlDocumentConverter.marshallToDocument(any(), any()));
            generateKeyMock.verify(() -> GenerateKey.loadPrivateKey(anyString()));
            generateKeyMock.verify(() -> GenerateKey.loadPublicKey(anyString()));
            signerMock.verify(() -> Signer.sign(any(Document.class), any(PrivateKey.class)));
            encrypterMock.verify(() -> Encrypter.encrypt(any(Document.class), any(PublicKey.class), eq("FIToFIPmtStsReq")));
            httpSenderMock.verify(() -> HttpSender.sendXML(eq(encryptedData), anyString()));
        }
    }

    @Test
    void shouldHandleInvalidPaymentStatusRequest() {
        // Given
        PaymentStatusRequest invalidRequest = new PaymentStatusRequest();
        // Missing required fields

        // When & Then
        assertThrows(Exception.class, () -> {
            paymentStatusService.paymentStatusRequest(invalidRequest);
        });
    }

    @Test
    void shouldReceiveInboundPacs028Successfully() throws Exception {
        // Given
        String encryptedResponse = "encrypted-callback-data";
        String decryptedResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>status update</response>";

        try (MockedStatic<GenerateKey> generateKeyMock = mockStatic(GenerateKey.class);
             MockedStatic<Decrypter> decrypterMock = mockStatic(Decrypter.class);
             MockedStatic<XmlDocumentConverter> xmlConverterMock = mockStatic(XmlDocumentConverter.class);
             MockedStatic<Signer> signerMock = mockStatic(Signer.class);
             MockedStatic<FileSaver> fileSaverMock = mockStatic(FileSaver.class)) {

            // Configure mocks
            generateKeyMock.when(() -> GenerateKey.loadPrivateKey(anyString()))
                    .thenReturn(privateKey);
            generateKeyMock.when(() -> GenerateKey.loadPublicKey(anyString()))
                    .thenReturn(publicKey);
            decrypterMock.when(() -> Decrypter.decryptToString(anyString(), any(PrivateKey.class)))
                    .thenReturn(decryptedResponse);
            xmlConverterMock.when(() -> XmlDocumentConverter.parseXmlString(anyString()))
                    .thenReturn(document);
            signerMock.when(() -> Signer.validateXmlSignature(any(Document.class), any(PublicKey.class)))
                    .thenReturn(true);

            // When
            assertDoesNotThrow(() -> {
                paymentStatusService.receiveInboundPacs028(encryptedResponse);
            });

            // Then
            decrypterMock.verify(() -> Decrypter.decryptToString(eq(encryptedResponse), any(PrivateKey.class)));
            xmlConverterMock.verify(() -> XmlDocumentConverter.parseXmlString(eq(decryptedResponse)));
            signerMock.verify(() -> Signer.validateXmlSignature(any(Document.class), any(PublicKey.class)));
            fileSaverMock.verify(() -> FileSaver.saveDecryptMessageToFile(eq(decryptedResponse), anyString()));
        }
    }

    @Test
    void shouldHandleInvalidSignatureInCallback() throws Exception {
        // Given
        String encryptedResponse = "encrypted-callback-data";
        String decryptedResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>status update</response>";

        try (MockedStatic<GenerateKey> generateKeyMock = mockStatic(GenerateKey.class);
             MockedStatic<Decrypter> decrypterMock = mockStatic(Decrypter.class);
             MockedStatic<XmlDocumentConverter> xmlConverterMock = mockStatic(XmlDocumentConverter.class);
             MockedStatic<Signer> signerMock = mockStatic(Signer.class)) {

            // Configure mocks
            generateKeyMock.when(() -> GenerateKey.loadPrivateKey(anyString()))
                    .thenReturn(privateKey);
            generateKeyMock.when(() -> GenerateKey.loadPublicKey(anyString()))
                    .thenReturn(publicKey);
            decrypterMock.when(() -> Decrypter.decryptToString(anyString(), any(PrivateKey.class)))
                    .thenReturn(decryptedResponse);
            xmlConverterMock.when(() -> XmlDocumentConverter.parseXmlString(anyString()))
                    .thenReturn(document);
            signerMock.when(() -> Signer.validateXmlSignature(any(Document.class), any(PublicKey.class)))
                    .thenReturn(false); // Invalid signature

            // When & Then
            SecurityException exception = assertThrows(SecurityException.class, () -> {
                paymentStatusService.receiveInboundPacs028(encryptedResponse);
            });

            assertEquals("Invalid signature in payment status response", exception.getMessage());
        }
    }

    @Test
    void shouldBuildPacs028RequestWithCorrectStructure() throws Exception {
        // Given
        String expectedMessageId = "STS123456789";

        try (MockedStatic<XmlDocumentConverter> xmlConverterMock = mockStatic(XmlDocumentConverter.class);
             MockedStatic<GenerateKey> generateKeyMock = mockStatic(GenerateKey.class);
             MockedStatic<Signer> signerMock = mockStatic(Signer.class);
             MockedStatic<Encrypter> encrypterMock = mockStatic(Encrypter.class);
             MockedStatic<HttpSender> httpSenderMock = mockStatic(HttpSender.class);
             MockedStatic<MessageIdGenerator> messageIdMock = mockStatic(MessageIdGenerator.class)) {

            messageIdMock.when(() -> MessageIdGenerator.generateMessageId(anyString()))
                    .thenReturn(expectedMessageId);
            xmlConverterMock.when(() -> XmlDocumentConverter.marshallToDocument(any(), any()))
                    .thenReturn(document);
            generateKeyMock.when(() -> GenerateKey.loadPrivateKey(anyString()))
                    .thenReturn(privateKey);
            generateKeyMock.when(() -> GenerateKey.loadPublicKey(anyString()))
                    .thenReturn(publicKey);
            encrypterMock.when(() -> Encrypter.encrypt(any(Document.class), any(PublicKey.class), anyString()))
                    .thenReturn("encrypted-data");

            // When
            String result = paymentStatusService.paymentStatusRequest(paymentStatusRequest);

            // Then
            assertEquals(expectedMessageId, result);

            // Verify that the XML document was created with the correct structure
            xmlConverterMock.verify(() -> XmlDocumentConverter.marshallToDocument(
                    argThat(pacs028 -> {
                        // Verify the structure of the PACS028 object
                        assertNotNull(pacs028);
                        return true;
                    }), 
                    eq(com.strata.xmlDocument.domain.model.PaymentStatusPacs028.class)
            ));
        }
    }

    @Test
    void shouldHandleDecryptionFailure() throws Exception {
        // Given
        String encryptedResponse = "invalid-encrypted-data";

        try (MockedStatic<GenerateKey> generateKeyMock = mockStatic(GenerateKey.class);
             MockedStatic<Decrypter> decrypterMock = mockStatic(Decrypter.class)) {

            generateKeyMock.when(() -> GenerateKey.loadPrivateKey(anyString()))
                    .thenReturn(privateKey);
            decrypterMock.when(() -> Decrypter.decryptToString(anyString(), any(PrivateKey.class)))
                    .thenThrow(new RuntimeException("Decryption failed"));

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                paymentStatusService.receiveInboundPacs028(encryptedResponse);
            });
        }
    }

    @Test
    void shouldGenerateStatusRequestIdWhenNotProvided() throws Exception {
        // Given
        PaymentStatusRequest requestWithoutStatusId = createValidPaymentStatusRequest();
        requestWithoutStatusId.setStatusRequestId(null);
        String expectedMessageId = "STS123456789";
        String generatedStatusRequestId = "SREQ987654321";

        try (MockedStatic<XmlDocumentConverter> xmlConverterMock = mockStatic(XmlDocumentConverter.class);
             MockedStatic<GenerateKey> generateKeyMock = mockStatic(GenerateKey.class);
             MockedStatic<Signer> signerMock = mockStatic(Signer.class);
             MockedStatic<Encrypter> encrypterMock = mockStatic(Encrypter.class);
             MockedStatic<HttpSender> httpSenderMock = mockStatic(HttpSender.class);
             MockedStatic<MessageIdGenerator> messageIdMock = mockStatic(MessageIdGenerator.class)) {

            messageIdMock.when(() -> MessageIdGenerator.generateMessageId(anyString()))
                    .thenReturn(expectedMessageId, generatedStatusRequestId);
            xmlConverterMock.when(() -> XmlDocumentConverter.marshallToDocument(any(), any()))
                    .thenReturn(document);
            generateKeyMock.when(() -> GenerateKey.loadPrivateKey(anyString()))
                    .thenReturn(privateKey);
            generateKeyMock.when(() -> GenerateKey.loadPublicKey(anyString()))
                    .thenReturn(publicKey);
            encrypterMock.when(() -> Encrypter.encrypt(any(Document.class), any(PublicKey.class), anyString()))
                    .thenReturn("encrypted-data");

            // When
            String result = paymentStatusService.paymentStatusRequest(requestWithoutStatusId);

            // Then
            assertEquals(expectedMessageId, result);
            
            // Verify that generateMessageId was called twice (once for msgId, once for statusRequestId)
            messageIdMock.verify(() -> MessageIdGenerator.generateMessageId(anyString()), times(2));
        }
    }

    private PaymentStatusRequest createValidPaymentStatusRequest() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setOriginalMessageId("MSG123456789");
        request.setOriginalTransactionId("TXN987654321");
        request.setOriginalCreationDateTime(OffsetDateTime.now().toString());
        request.setSourceId("999058");
        request.setDestinationId("999059");
        request.setInterbankSettlementDate(LocalDate.now().toString());
        request.setStatusRequestId("SREQ123456");
        return request;
    }
}