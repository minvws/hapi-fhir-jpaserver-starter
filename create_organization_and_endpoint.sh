#!/bin/bash


curl -X PUT http://localhost:8080/fhir/Organization/18420b5d-52a5-4825-b57c-b625146473a6 \
   -H "Content-Type: application/json" \
   --data @fhir_examples/organization.json > /dev/null


curl -X PUT http://localhost:8080/fhir/Endpoint/06192322-351d-432d-bb32-1a161f6e378f \
   -H "Content-Type: application/json" \
   --data @fhir_examples/endpoint.json > /dev/null