# server-management-system
This is a management system created using Java, Spring Boot, JPA Repostiory, Spring Security and JWT. This is a management system created especially for managing servers. The user needs only to input an IP and a server will be created. Each user has its own set of servers. This API also offers a ping service, which will try to ping the server through the IP. If it is reachable then its status will be updated from INACTIVE to ACTIVE. The other fields such as location, company will also be completed. This is done via the GEO-IP database. If this IP is found on the database then via its methods I can extract the location and the enterprise and simply fill the fields with this data (whether the ping was successful or unsuccessful)

For the security feature this API offers register, login, logout features. Once the user logs in (after registered of course) a JWT will be generated which once put as an Authorization header offers access to the other secure endpoints. The logout feature invalidates the session and puts the token into an in-memory blacklist which cannot be used anymore to access the endpoints. The user has to log in again and the new token has to be put as a header to once again be able to access the endpoints. 

The frontend for this API:
https://github.com/ErtiRakipaj/server-app-frontend
