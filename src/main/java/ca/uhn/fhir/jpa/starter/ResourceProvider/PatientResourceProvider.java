package ca.uhn.fhir.jpa.starter.ResourceProvider;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.starter.services.CommonServices;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r5.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Resource provider contains one or more methods which have been annotated with special annotations indicating
 * which RESTful operation that method supports.
 */
@Component
public class PatientResourceProvider implements IResourceProvider {

	private final IFhirResourceDao<Patient> patientDao;
	private final CommonServices commonServices;


	public PatientResourceProvider(IFhirResourceDao<Patient> patientDao, CommonServices commonServices) {
		this.patientDao = patientDao;
		this.commonServices = commonServices;
	}

	/**
	 * Create method to create a patient
	 * usage: POST to /fhir/Patient with JSON body with Patient resource
	 *
	 * @param thePatient
	 *    Resource of the to be created patient
	 * @return
	 *    DAO create method outcome
	 */
	@Create
	public MethodOutcome createPatient(@ResourceParam Patient thePatient, RequestDetails theRequestDetails) throws Exception {
		String uuid = commonServices.registerPseudonym();
		Extension ext = new Extension();
		ext.setUrl("https://example.com/extensions#pseudonym");
		ext.setValue(new UuidType(uuid));
		thePatient.addExtension(ext);
		return patientDao.create(thePatient, theRequestDetails);
	}

	/**
	 * Method to find patients with pseudonym
	 *
	 * @param pseudonym
	 * UuidType of pseudonym
	 * @return
	 *    List of patients with pseudonym
	 */
	List<Patient> searchPatientByPseudonym(UuidType pseudonym, RequestDetails theRequestDetails) {
		SearchParameterMap params = new SearchParameterMap();
		return patientDao.searchForResources(params, theRequestDetails).stream()
				.filter(patient -> patient.getExtension().stream()
						.anyMatch(ext -> Objects.equals(ext.getValue().primitiveValue(), pseudonym.getValue())))
				.collect(Collectors.toList());
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}


}
