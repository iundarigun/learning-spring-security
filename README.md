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
_ note_: This is because Java use a sessionId after first login

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