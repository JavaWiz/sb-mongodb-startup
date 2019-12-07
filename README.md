## Accessing Data with MongoDB

This project walks us through the process of using Spring Data MongoDB to build an application that stores data in and retrieves it from MongoDB, a document-based database.

## What is MongoDB?

MongoDB is a free and open-source cross-platform document-oriented database. Classified as a NoSQL database, MongoDB avoids the traditional table-based relational database structure in favor of JSON-like documents with dynamic schemas, making the integration of data in certain types of applications easier and faster.

The best way we learn anything is by practice and exercise questions. I have started so we will familiar with NoSQL and MongoDB with spring data. Hope, these exercises help us to improve our MongoDB query skills with spring boot data. Currently, following exercises are available based on 'restaurants' collection, we are working hard to add more exercises. Happy Coding! 


### What You Will build

We will store Customer POJOs (Plain Old Java Objects) in a MongoDB database by using Spring Data MongoDB.

### What You Need

Starting with Spring Initializr

For all Spring applications, you should start with the https://start.spring.io[Spring Initializr]. The Initializr offers a fast way to pull in all the dependencies you need for an application and does a lot of the set up for you. This example needs only the Spring Data MongoDB dependency. 


### Install and Launch MongoDB

With our project set up, we can install and launch the MongoDB database or we can use atlas cluster.

### Define a Simple Entity

MongoDB is a NoSQL document store. In this example, you store `Customer` objects.

```
import org.springframework.data.annotation.Id;

public class Customer {

	@Id
	public String id;
	public String firstName;
	public String lastName;

	public Customer() {}

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format(
				"Customer[id=%s, firstName='%s', lastName='%s']",
				id, firstName, lastName);
	}
}
```

Here you have a `Customer` class with three attributes: `id`, `firstName`, and `lastName`. The `id` is mostly for internal use by MongoDB. You also have a single constructor to populate the entities when creating a new instance.


NOTE: In this guide, the typical getters and setters have been left out for brevity.

`id` fits the standard name for a MongoDB ID, so it does not require any special annotation to tag it for Spring Data MongoDB.

The other two properties, `firstName` and `lastName`, are left unannotated. It is assumed that they are mapped to fields that share the same name as the properties themselves.

The convenient `toString()` method prints out the details about a customer.

NOTE: MongoDB stores data in collections. Spring Data MongoDB maps the `Customer` class into a collection called `customer`. If you want to change the name of the collection, you can use Spring Data MongoDB's https://docs.spring.io/spring-data/data-mongodb/docs/current/api/org/springframework/data/mongodb/core/mapping/Document.html[`@Document`]
annotation on the class.

### Create Simple Queries

Spring Data MongoDB focuses on storing data in MongoDB. It also inherits functionality from the Spring Data Commons project, such as the ability to derive queries. Essentially, you need not learn the query language of MongoDB. We can write a handful of methods and the queries are written for us.

To see how this works, create a repository interface that queries `Customer` documents,

```
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.javawiz.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

	public Customer findByFirstName(String firstName);

	public List<Customer> findByLastName(String lastName);

}
```

`CustomerRepository` extends the `MongoRepository` interface and plugs in the type of values and ID that it works with: `Customer` and `String`, respectively. This interface comes with many operations, including standard CRUD operations (create, read, update, and delete).

You can define other queries by declaring their method signatures. In this case, add `findByFirstName`, which essentially seeks documents of type `Customer` and finds the documents that match on `firstName`.

You also have `findByLastName`, which finds a list of people by last name.

In a typical Java application, you write a class that implements `CustomerRepository` and craft the queries yourself. What makes Spring Data MongoDB so useful is the fact that you need not create this implementation. Spring Data MongoDB creates it on the fly when you run the application.

Now you can wire up this application and see what it looks like!

### Create an Application Class

```
import com.javawiz.entity.Customer;
import com.javawiz.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataMongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		log.debug("Customers found with findAll():");
		log.debug("-------------------------------");
		repository.findAll().forEach(customer -> log.debug("{}", customer));
		
		// fetch an individual customer
		log.debug("Customer found with findByFirstName('Alice'):");
		log.debug("--------------------------------");
		log.debug("{}", repository.findByFirstName("Alice"));

		log.debug("Customers found with findByLastName('Smith'):");
		log.debug("--------------------------------");
		repository.findByLastName("Smith").forEach(customer -> log.debug("{}", customer));
	}
}
```

Spring Boot automatically handles those repositories as long as they are included in the same package (or a sub-package) of our `@SpringBootApplication` class. For more control over the registration process, we can use the `@EnableMongoRepositories` annotation.

NOTE: By default, `@EnableMongoRepositories` scans the current package for any interfaces that extend one of Spring Data's repository interfaces. We can use its `basePackageClasses=MyRepository.class` to safely tell Spring Data MongoDB to scan a different root package by type if our project layout has multiple projects and it does not find your repositories.

Spring Data MongoDB uses the `MongoTemplate` to execute the queries behind your `find*` methods. You can use the template yourself for more complex queries, but this guide does not cover that. See the [Spring Data
MongoDB Reference Guide](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)

We need to set up some data and use it to generate output.

`AccessingDataMongodbApplication` includes a `main()` method that autowires an instance of `CustomerRepository`. Spring Data MongoDB dynamically creates a proxy and injects it there. We use the `CustomerRepository` through a few tests. First, it saves a handful of `Customer` objects, demonstrating the `save()` method and setting up some data to use. Next, it calls `findAll()` to fetch all `Customer` objects from the database. Then it calls `findByFirstName()` to fetch a single `Customer` by her first name. Finally, it calls `findByLastName()` to find all customers whose last name is `Smith`.

NOTE: By default, Spring Boot tries to connect to a locally hosted instance of MongoDB. 
Read the (https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-mongodb) for details on pointing your application to an instance of MongoDB hosted elsewhere.

As `AccessingDataMongodbApplication` implements `CommandLineRunner`, the `run` method is automatically invoked when Spring Boot starts.

### Summary

Congratulations! We set up a MongoDB server and wrote a simple application that uses Spring Data MongoDB to save objects to and fetch them from a database, all without writing a concrete repository implementation.

NOTE: If you want to expose MongoDB repositories with a hypermedia-based RESTful front end with little effort, read link: [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/).

### See Also

The following guides may also be helpful:

* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Data with Gemfire](https://spring.io/guides/gs/accessing-data-gemfire/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with Neo4j](https://spring.io/guides/gs/accessing-data-neo4j/)
