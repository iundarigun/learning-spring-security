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

