# learning Spring Security

## Introduction
Spring security fucuses on **Application Security**, no in other levels of security (harware, operation system, etc)

Some key terms in application security, not only specific for Spring Security:
- Identity: A unique actor. Typically an user, but not only, can be an other application
- Credentials: Usually a user id and password
- Authentication: Is how the application verifies the identity of the requestor. Spring Security has a variety of methods for Authentication, but typically the user provides credentials, which are validated
- Authorization: Using the user’s identity, Spring Security determines if they are authorized to perform action

Spring Security supports several **Authentication Providers**:
- In Memory
- JDBC / Database
- Custom
- LDAP / Active Directory
- Keycloak
- ACL (Access Control List)
- OpenID
- CAS

It supports too several methods to **store passwods**:
- NoOp Password Encoder - plain text, not recommended - for legacy systems
- BCrypt - uses bcrypt password hashing
- Argon2 - Uses Argon2 algorithm
- Pbkdf2 - Uses PBKDF2 algorithm
- SCrypt - Uses scrypt algorithm
- Custom - Roll your own? Not recommended! 

### Spring Security modules
Spring security is formed for some modules, not all of them are mandatories:
- Core: Core modules of Spring Security
- Remoting: Only needed for support of RMI operations
- Web? Support of web applications
- Config: Provides support for XML and Java configuration
- LDAP: for integration with LDAP identity providers
- OAuth 2.0 Core: Core of OAuth 2.0 Authorization and OpenID
- OAuth 2.0 Client: Client support for OAuth 2.0 and OpenID clients 
- OAuth 2.0 JOSE: Provides support for JOSE (Javascript Object Signing and Encryption)
- OAuth 2.0 Resource Server: Support for OAuth 2.0 Resource Servers
- ACL: Support for Access Control Lists
- CAS: Support for Central Authentication Service
- OpenID: Authenticate users with external OpenID server
- Test: Testing Support for Spring Security 

### OWASP
It is an organization to help improve security on web. Exists a top 10 volnerabilities on this link: https://owasp.org/www-project-top-ten/

Spring Security try to resolve the next especific points:
- Cross-site Scripting (XSS)
- Cross-Site Request Forgery (CSRF)
- Security HTTP Response Headers: Variety of headers can be set to improve browser security
- Redirect to HTTPS

## Basic Authentication
Provides a standard way for HTTP clients to submit user name and password. Two ways:
- URL Encoding: https://username:password@www.example.com
- • HTTP Header - Key: Authorization, Value: Basic \<Base64 encoded username:password\>

It is not secure, because is simple to decode Base64. To protect credentials, HTTPS is recommended.

Sending credentials for every request increases risk of compromise

### Adding spring security dependency
Only adding the next dependency, our application gains security:

```
  implementation("org.springframework.boot:spring-boot-starter-security")
```
The Spring boot Autoconfiguration generates a random UUID password and add login form page to access.

![password](images/randompassword.png)
username is `user`

![loginpage](images/loginpage.png)

Furthermore, Spring gives us a logout page:

![logout](images/logout.png)

_note_: This is because Java use a sessionId after first login

We can customize this user and password using spring properties:
```
spring:
  security:
    user:
      name: admin
      password: supersecret
```

## Spring Security Authentication process

Spring security works like the next diagram:
![spring filters](images/securityFilter.png)

There are many components that allow flexibility:
- **Authentication Filter** - A filter for a specific Authentication type in the Spring Security filter
chain. (ie basic auth, remember me cookie, etc)
- **Authentication Manager** - Standard API interface used by filter
- **Authentication Provider** - The implementation of Authentication - (in memory, database, etc)
- **User Details Service** - Service to provide information about user
- **Password Encoder** - Service to encrypt and verify passwords
- **Security Context** - Holds details about authenticated entity 

### Password encoding
Legacy systems sometimes store passwords in plain text and, obviously, is not ideal

Other systems encrypt the password in the database, then decrypt to verify. Again is bad, because we can be decrypted.

We can use hash values. It is a one-way mathematical algorithm applied to the password, and we can not decrypted. Is not good enough because we can use dictionary and pass to hash trying to get the original password. So, a solution can be adding a "salt" text after original password, to dificult dictionary attacks

Sha-256 was a default password encoder for Spring security in past. Today is no longer default, because is too fast and permit a brute force attack

`DelegatingPasswordEncoder` be able your application to use several password encoders at same time. So, one user can be encode in a especific password encoder and other user may use an other. For this, password is save/storage with a brackets prefix, according with the algorithm register on `DelegatingPasswordEncode`. For example:
- Using bcrpyt: `{bcrypt}$2a$10$FyCPTgRSUeiT2oMMBLaH.eaYm0XME5XlLeOXQErHkCrioZ/Uv/2qC`
- Using sha256: `{sha256}c1c8f3ccee9c70be6f77b02e1dca988c043bc022568eeb35bdd066bf5c2c520b6846911fcdf5bdbe`

## Authorization in Spring Security
Authorization is tge approval to perform an action within the application. 

Spring Security has roles and authorities. Roles start with `ROLE_` keyword, and Authorities are any String. 

Typically, role is a group of one or more authorities, but we can use roles or authorities to allow or denny any action, so basically are the same thing on Spring Security.

Spring Security uses a decision tree to allow or denny actions based on votes for differents origins

![decision votes](images/decisionVotes.png)

## Sessions and Tokens
Today, when we work with Rest, one of it characterists is be stateless. Wherever, when we use `session id`, like is used by thymeleaf or others templates engines, the session is saved on server, so this behaivor affect scalability.

We can use tokens to replace session id, and this token can be opaque or self-contained. 

### JWT - JSON Web Token
A common pattern is JWT like a token to replace session id. It is self-contained, that means it has all information about user and permissions for the request, and it has a signature to garantee the veracity of data. Two kinde of JWT:
- JWS: JSON Web Signature - More common
- JWE: JSON Web Encryption

JWT (JWS) has three parts separete by point, coded in Base64: Header, Payload and Signature. Some common parts of the payload: 
- iss: Emission entity
- sub: Subject or user identification
- iat: Emission date  
- exp: Expiration date

### OAuth
We can use JWT with framework `oauth2`. 

We have the next **roles** defined for oauth:
- _Resource owner_:  Is an entity that allow access to it resources. When is a person, it is referred to as an end-user.
- _Resource server_: Is the server with the user data. Only can access this data with a token created by the Authorization server.
- _Authorization server_: It is responsable for the authentication and to generate the access tokens.
- _Client_: The application or server that is using tokens to access to Resource Server.

It has too diferents ways to use access, called **grant types**:
- _Password_: When client and Authorization Server belong to the same organization, we let user and password to the client, and join to clientId and clientSecret to do authorization. Not recomended.
- _Client Credentials_: Only used when a sistem call other sistem, only servers envolved.
- _Authorization code_:  Used for traditional web pages. When a sistem ask for authorization, the user put on the _Authorization Server_ his credentials (user/password). The authorization server generate a authorization code that client will use to get de access token.
- _Implicit_: The redirect to _client_ is directly with access token after login, without authorization code.

## Coding

I put some projects on this repo adding new parts of spring security. Here I will describe some attention points for each other

### google-2fa
Add Two Factor Autentication based on Google Auth. To test you can use:
- GET http://localhost:1980/user/register2fa (passing user/password) to enable 2FA. The return body is a link to a QR image to scan from Google Authenticator App or an other similar app.
- POST http://localhost:1980/user/register2fa?verifyCode=<verifyCode> Put verify code from application. If return 200/OK, this user enables 2FA.
- POST http://localhost:1980/user/verify2fa?verifyCode=<verifyCode> To generate a sessionId autenticated with user/password and Google Auth code

_WIP_: At this moment, the code is not working

### Gateway with oauth and resources
I put an example to use Gateway and an Authorization Server with Oauth2 pattern, and a service resource example. You can see on `oauth-with-gateway` folder

To test, you need to start the three projects. Authenticate a client:
- POST http://localhost:2020/auth/oauth/token with request params `scope=any` and grant_type=`client_credentials`, and basic authentication `app1:supersecret`
- GET http://localhost:2020/resource-example/test with Bearer token from oauth response. 

To use user credentials:
- POST http://localhost:2020/auth/oauth/token with request params `scope=any`, `grant_type=password`, `username=admin` and `password=supersecret`, and basic authentication `app1:supersecret`

_WIP_: some improves in this project can be do. 

## References:

- https://www.udemy.com/course/spring-security-core-beginner-to-guru
- https://github.com/caelum/apostila-microservices-com-spring-cloud/blob/master/14-seguranca.md
- https://blog.marcosbarbero.com/oauth2-centralized-authorization-opaque-jdbc-spring-boot2/
- https://gitlab.com/aovs/projetos-cursos/fj33-authorization-server/
- https://www.brunobrito.net.br/oauth2/
- https://microservices.io/patterns/security/access-token.html
- https://medium.com/@igabhagya/spring-oauth2-authorization-server-jwt-jpa-data-model-1e458dcdac04
