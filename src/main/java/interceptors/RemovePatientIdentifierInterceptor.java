package interceptors;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Interceptor
public class RemovePatientIdentifierInterceptor {

	private static final Logger log = LoggerFactory.getLogger(RemovePatientIdentifierInterceptor.class);

	
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void checkForPatient(IBaseResource resource) {
		log.info("Removing patient identifier with system 'http://fhir.nl/fhir/NamingSystem/bsn'");
		if (resource instanceof Patient patient) {
			removeIdentifier(patient);
		}
		if (resource instanceof Bundle bundle && bundle.hasEntry()) {
			for (Bundle.BundleEntryComponent entry: bundle.getEntry()){
				if (entry.getResource() instanceof Patient patient) {
					removeIdentifier(patient);
				}
			}
		}
	}

	/**
	 * @param patient
	 * 	The patient that could have an identifier with the specific system
	 */
	private void removeIdentifier(Patient patient) {
		List<Identifier> updatedIdentifiers = patient.getIdentifier().stream()
			.filter(identifier -> !identifier.getSystem().equals("http://fhir.nl/fhir/NamingSystem/bsn"))
			.toList();
		patient.setIdentifier(updatedIdentifiers);
		log.info("Patient identifier with system 'http://fhir.nl/fhir/NamingSystem/bsn' removed");
	}
}