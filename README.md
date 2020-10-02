# Chatter



## Description *(TBA)*

## Objective
This project focuses on learning and exploring pure functional programming to create a basic application, but is meant to have a lot of functionality, like a web chat.

Also, this project is meant to learn about the *Hexagonal Architecture*, which pure functional programming, with category theory, is more inclined towards following.

Pure functional programming was also chosen for the creation of a web application to observe how flexible enough it is to design and create solutions. This is a context that may be more fitted to approach it with another paradigm like actor-based programming.

## Roadmap
- [x] Starting point
  - [x] Hello Http4s World!
  - [x] Abstracting the domain
    - [x] Create Entities for messages and exchange of them
    - [x] Create algebras for handling messages.
    - [x] Create http routes
  - [x] Working chat routes
- [x] Does it work?
  - [x] Write tests for the code created
- [x] Make it visible
  - [x] Page skeleton (HTML / CSS)
  - [x] Send/Receive a message (Show it to user)
  - [x] *Bootstrap* for easier design
- [ ] Stateful server
  - [x] Messages
    - [x] Repository to handle messages in the server (Create + Read)
  - [x] More than messages
    - [x] Entities to model users of the application and the information exchange about them
    - [x] Entities to model authentication on the sever and autorizations
    - [x] Entities and Algebra to model generation of JWT tokens 
    - [x] JSON decoders and encoders for the created entities
  - [x] Users
    - [x] Algebra for handling the users inside a repository
    - [x] Repository to store users inside the server (CRD)
    - [x] Route to handle the creation of a new user
  - [ ] Authentication
    - [x] Repository to handle JWT inside the server
    - [x] Route to handle logout of a user
    - [ ] Middleware for handling JWT
    - [ ] Add authentication to messaging routes
- [ ] Errors? What are those? *(TBA)*
  - [ ] Add errors in places needed (Read more in resources)
- [ ] Refactor of Front end
  - [ ] Add login/sign up (landing) page
  - [ ] Save JWT token in session storage
  - [ ] Use token in every request
  - [ ] Add logout button
- [ ] Passwords can't be stored in plaintext!
  - [ ] Algebra and Interpreter for handling cryptography
  - [ ] Integrate it with the Users Algebra and Interpreters
- [ ] This needs to work correclty
  - [ ] Write more tests
- [ ] Reload page to show new message?
  - [ ] Add websockets to the server to handle petitions
  - [ ] Make frontend work with sockets too
- [ ] Docker
  - [ ] Use SBT to generate a docker image of the server
- [ ] Docker compose
  - [ ] Adding Databases to the docker image
    - [ ] Postgres
    - [ ] Redis
  - [ ] Postgres Interpreter in the server
  - [ ] Redis Interpreter in the server
  - [ ] Change Inmemory interpreters to DB  interpreters
- [ ] Is it alive?
  - [ ] Add healthcheck to the application
    - [ ] Postgres
    - [ ] Redis
- [ ] Watch it grow
  - [ ] Deploy somewhere
  - [ ] Link to Cloudfront
    - [ ] Create subdomain
  - [ ] Tell friends to test it out

## Libraries *(TBA)*

## Resources Used

### Server creation + directories structure
- [Practical FP in Scala: A hands-on approach](https://leanpub.com/pfp-scala)
  - [Pfp shopping cart repo](https://github.com/gvolpe/pfps-shopping-cart)
- [Scala pet store repo](https://github.com/pauljamescleary/scala-pet-store)
- [Pure functional HTTP APIs in Scala book](https://leanpub.com/pfhais)
- [Live Coding a Chat Server with WebSockets and http4s](https://youtu.be/py_V_7gD5WU)

### Category Theory
- [Scala with Cats](https://underscore.io/books/scala-with-cats/)
- [Functional programming for Mortals](https://leanpub.com/fpmortals)
  - [Functional Programming for Mortals with Cats](https://leanpub.com/fpmortals-cats)

### Hexagonal Architecture
  - [The Clean Code Blog](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
  - [Hexagonal architecture](https://alistair.cockburn.us/hexagonal-architecture/)
  - [Clean Architecture – Make Your Architecture Scream](https://www.codingblocks.net/podcast/clean-architecture-make-your-architecture-scream/)
  - [Clean Architecture: A Craftsman's Guide to Software Structure and Design](https://www.amazon.com/-/es/Robert-Martin/dp/0134494164)
  - [Functional architecture - The pits of success - Mark Seemann](https://youtu.be/US8QG9I1XW0)

### Libraries
  - Respective documentation
  - Books mentioned before