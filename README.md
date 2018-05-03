To run locally:
1. start wildfly with the following env variables or system properties set
  . COA_USER
  . COA_PASSWORD
  . COA_SEARCH_ENDPOINT
  . COA_NAMESPACE
  . COA_SEARCH_SOAP_ACTION

something like
`.../standalone.sh --server-config=standalone.xml -P .../cao.properties`

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