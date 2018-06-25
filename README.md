[![Build Status](https://travis-ci.org/bcgov/jag-efiling-hub.svg?branch=master)](https://travis-ci.org/bcgov/jag-efiling-hub)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jag-efiling%3Ahub&metric=coverage)](https://sonarcloud.io/dashboard?id=jag-efiling%3Ahub)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=jag-efiling%3Ahub&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=jag-efiling%3Ahub)

[![Duplication](https://sonarcloud.io/api/project_badges/measure?project=jag-efiling%3Ahub&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=jag-efiling%3Ahub)

[![Code smells](https://sonarcloud.io/api/project_badges/measure?project=jag-efiling%3Ahub&metric=code_smells)](https://sonarcloud.io/dashboard?id=jag-efiling%3Ahub)


To run locally:
1. start wildfly with the following env variables or system properties set

  * COA_USER
  * COA_PASSWORD
  * COA_SEARCH_ENDPOINT
  * COA_NAMESPACE
  * COA_SEARCH_SOAP_ACTION
  * COA_VIEW_CASE_PARTY_SOAP_ACTION

something like
```
.../standalone.sh --server-config=standalone.xml -P .../cao.properties
```

2. start the app locally 

`mvn clean wildfly:deploy -Popenshift`

3. then access

`http://localhost:8080/hub/search?caseNumber=any`

as <any> is not a known case number, you should receive the following:
```xml
<soap:Envelope>
  <soap:Body>
    <SearchByCaseNumberResponse/>
  </soap:Body>
</soap:Envelope>
```