Altamira Data Persistence Services
==================================

This project is responsible to provide transactional data persistence to business data entities. 

This is a shared library used for most of BPM projects (altamira-bpm/*). It can be used as rest services, POJO entities, JPA entities or embedded into target project (depends on altamira-data-x.x.x-SNAPSHOT-classes).

To build this project:

```
$ mvn clean install -Parq-jbossas-remote
```

To run the tests with Arquillian is necessary install altamira-data-x.x.x.SNAPSHOT.war in jboss because the rest service tests are make in client perspective.


