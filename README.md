# GraphQL API Proof of concept

This project provides a proof of concept around using GraphQL in spring boot as described in 
[DT-2876](https://dsdmoj.atlassian.net/browse/DT-2876)

## PROBLEM:

- Clients consuming HMPPS data don't know what service to call to get the right data
- Documentation of those APIs are spread over many different services
- This will get worse as services move data out of NOMIS and Delius
- When services move data out of legacy, clients currently consuming data from legacy will need changing to consume from the new service
- Our ubiquitous language is less than ideal and is confusing with same the things called different things in different services, this creates confusion

## Potential solution:

- Front the legacy (prison-api, community-api) and new services with an edge server that delegates to the other services. GraphQL seems to fit our needs, since it can:
- Define a HMPPS schema with a consistent ubiquitous language
- Hide the underlining service delivering the data
- Can be performant since the client can define what data they want so additional calls to backend  services can be minimised
- Allow data to be migrated to new services with a only change to the GraphQL service not the consuming services

### The proof of concept will:

- A time boxed PoC (2 weeks/ 2 people) could prove:
- The technology is mature enough to use in the very near future
- We can model existing services using a difference ubiquitous language
- We can talk to more than one backend service
- We can talk to more than one endpoint for a given backend service and can be made performant
- The service can be secured using HMPPS Auth
- A possible working example is the DPS Offender Profile and the backend calls it currently makes
