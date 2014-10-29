Altamira Data Persistence Services
==================================

This project is responsible to provide transactional data persistence to business data entities. 

This is a shared library used for most of BPM projects. 

It can be used as rest services, POJO entities, JPA entities or embedded into target project (depends on altamira-data-x.x.x-SNAPSHOT-classes).

To build this project:

```
$ mvn clean install -Parq-wildfly-remote
```

To build without run the tests:

```
mvn clean install -DskipTests
```

To run the tests with Arquillian is necessary deploy altamira-data-x.x.x.SNAPSHOT.war first because the rest service tests was  made in client perspective.


