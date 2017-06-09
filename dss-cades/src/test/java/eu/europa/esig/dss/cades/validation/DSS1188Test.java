package eu.europa.esig.dss.cades.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.FileDocument;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.reports.wrapper.DiagnosticData;
import eu.europa.esig.dss.validation.reports.wrapper.SignatureWrapper;

public class DSS1188Test {

	/*
	 * Retrieves the correct signing certificate (uses SID from CMS) but not able to process the crypto validation
	 * (original data is missing)
	 */
	@Test
	public void testSigWithoutAttached() {
		DSSDocument dssDocument = new FileDocument("src/test/resources/validation/dss-1188/Test.bin.sig");
		SignedDocumentValidator validator = SignedDocumentValidator.fromDocument(dssDocument);
		validator.setCertificateVerifier(new CommonCertificateVerifier());
		Reports reports = validator.validateDocument();

		DiagnosticData diagnosticData = reports.getDiagnosticData();

		SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
		assertEquals("A5518784E8001EF099F4BAEC5573BC965830079EDDED92752EA94B6548DFFC06", signature.getSigningCertificateId());
		assertFalse(diagnosticData.isBLevelTechnicallyValid(diagnosticData.getFirstSignatureId()));
	}

	@Test
	public void testSigWithAttached() {
		DSSDocument dssDocument = new FileDocument("src/test/resources/validation/dss-1188/Test.bin.sig");
		SignedDocumentValidator validator = SignedDocumentValidator.fromDocument(dssDocument);
		List<DSSDocument> detachedContents = new ArrayList<DSSDocument>();
		detachedContents.add(new FileDocument("src/test/resources/validation/dss-1188/Test.bin"));
		validator.setDetachedContents(detachedContents);
		validator.setCertificateVerifier(new CommonCertificateVerifier());
		Reports reports = validator.validateDocument();

		DiagnosticData diagnosticData = reports.getDiagnosticData();

		SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
		assertEquals("A5518784E8001EF099F4BAEC5573BC965830079EDDED92752EA94B6548DFFC06", signature.getSigningCertificateId());
		assertTrue(diagnosticData.isBLevelTechnicallyValid(diagnosticData.getFirstSignatureId()));
	}

}
