# Spring Boot Process Engine and Business Data

This example project shows how to configure an Spring boot application using to two 
separated data sources. One data source serves the business data, the second one serves 
the process engine tables.

## The Business Data

It's a JPA example using a single entity Customer with a repository configured in 
Spring Boot. Only two classes are required here: `com.camunda.consulting.two_ds.data.Customer` 
and `com.camunda.consulting.two_ds.data.CustomerRepository`.

The required dependency to get it running is `org.springframework.boot:spring-boot-starter-data-jpa` 
together with the database driver `com.h2database:h2`.

A rest controller is added to check the content with a REST client. The implementation is 
`com.camunda.consulting.two_ds.rest.CustomerRestService`.

A simple test is to get all data with [http://localhost:8080/customer/all](http://localhost:8080/customer/all)

## The Business Datasource

A custom data source is added to save the file in a separated database. `customer.datasource` 
is the configuration prefix used in `application.properties`.

As shown in the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.data-access.configure-custom-datasource), 
you have to separate the `DatasourceProperties` from the datasource initialization 
to combine the common data source configuration attributes with the Hikari connection pool properties.

The configuration is in the `com.camunda.consulting.two_ds.configuration.TwoDatasourcesConfiguration` class, adopted from
[this Spring boot Howto document](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.data-access.configure-two-datasources).

The customer data source has the `@Primary` annotation on both methods.

## The Camunda Engine

A simple process with two service tasks is used to add data to the customer database. 
The name is given when the process instance starts. A Service task with a Delegate expression 
creates a new customer in the database. If an error is forced, the second service task throws 
an exception to roll back the customer from the database.

The data source is configured in the `com.camunda.consulting.two_ds.configuration.TwoDatasourcesConfiguration` 
class, too. It uses the same pattern as the primary customer data source, with two differences:
1. No `@Primary` annotation
2. The `camundaDataSource` Bean is qualified with the `camundaDataSourceProperties` to separate the setup.

The bean name is [documented here](https://docs.camunda.org/manual/7.18/user-guide/spring-boot-integration/configuration/#defaultdatasourceconfiguration)

## Successful configuration

This line from the console output shows a successful configuration of the two datasources with separated databases:

```
INFO 35076 --- [           main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Databases available at 'jdbc:h2:./customer-db1', 'jdbc:h2:./camunda-db2'
```

If you see only a single database here, something in the configuration is missing. 
I stumbled over the missing `@Qualifier` on the beans.


## More resources

* Reference for Spring Boot Transaction Management: https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-motivation 
 
