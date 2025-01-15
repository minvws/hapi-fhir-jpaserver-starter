package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

import java.util.ArrayList;
import java.util.List;

public class EnablePlainProvider extends RestfulServer {
	public EnablePlainProvider() {
		List<IResourceProvider> resourceProviders = new ArrayList<>();
		registerProviders(resourceProviders);
	}
}
