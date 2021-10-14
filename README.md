# Order Planning Problem

[Task](https://lydian-girdle-3a2.notion.site/Order-Planning-Problem-242803404c754e3a857b881228043361)

[API](https://lydian-girdle-3a2.notion.site/API-8c955cdf3b1c4ee384da0a6084697753)

## Key decisions

- Customers and warehouses are points with coordinates (x; y)

- This solution uses a database. The database has several predefined warehouses containing products

- When a new customer is added, the distance between customer and each warehouse is calculated and saved to a separate
  table

- In the same way, when a new warehouse is added, the distance between warehouse and each customer is calculated and
  saved to the very same table

- When a new order is placed, the nearest to the customer warehouse containing the product is found. The process is
  fairly quick, since we've already calculated all the distances. If there is no such warehouse containing the product,
  the corresponding message is displayed

- API for all main entities (Customer, Warehouse, Order & Product) has been implemented. There is a possibility to
  create and read all of them. There is also a possibility to update and delete customers and warehouses

- The developed solution is a microservice built using Java and Spring Boot

- The solution also contains unit and integration tests for key scenarios

## Technology stack

Java 11, Spring Boot, Spring Data, H2 database.

There is also another version of the app that uses PostgreSQL and Docker. See branch ```v2-postgres```.