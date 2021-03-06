# flight-advisor
##### Find the cheapest flight between two airports. 
 
 
## Development
 
 
 ## H2 Console
 
 H2 console can be reached at http://localhost:8080/h2
 Credentials (username/password): sa / 
 
 ### Predefined users:
 
 Admin Role (username/password): admin / admin
 
 Guest (User) Role (username/password): guest / guest
 
 ## Swagger
 
 http://localhost:8080/swagger-ui.html#/
 
 ## Postman Collection
 
 Postman collection is  in project directory  postman.
 
  ## Algorithm
  
  Bidijkstra algorithm with binomial heap
  
  O(ElogV)
  If a binary min-heap is used to implement the Frontier set, you have log(|v|) for all operations, resulting in O(|E|log|V| + |V|log|V|), but since it is reasonable to assume |E| > |V|, you have O(|E|log|V|).
 
 ### Security
  Spring Security package was used with JWT tokens.
  User can  have two roles role, ADMIN or USER.  
 
 ### Initial data - routes and airports
 CSV files with ORACLE format are supported.
  There are two endpoints for importing data:
 1. /api/airports/import
 2. /api/routes/import
 
 Example data in data folder.
 
 ## Docker
 
 Container image.
 Local build and run.
 
 From project directory run script `docker.sh`: 
 ```
 sh docker.sh
 ```
 Pull image from Docker Hub: 
 ```
 docker pull acailic/flight-advisor
 ```
 Run image:
 ```
 docker run -p 8080:8080 flight-advisor
 ```
 
 TODO:
 
 -  Save graph into DB (mongo or graph type db).
 -  Pagination for more endpoints.
 -  Different profiles for running (at the moment only dev,swagger profile)
 -  Exception handling messages in body.
 -  Add and fix tests
