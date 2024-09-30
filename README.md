**API Rate Limiting Solution**

Problem Statement:

We need to interact with an API that enforces a strict limit of 15 calls per minute. Exceeding this limit results in a penalty of an additional minute during which no further calls can be made. 
The API is defined as follows:
function String callme(String input);

Proposed Solution:

1. Utilizing Rate Limits
To stay within the safe limits, we implement a rate limiter on the server side based on the client making the requests. This ensures that individual clients do not exceed the predefined threshold.

2. Handling Excess Calls
If we attempt to call the API 20 times within a minute, we would exceed the rate limit. To manage this, we can introduce a client-side queue that ensures the API is only called up to 15 times per minute.
Any additional calls will be queued and executed after the appropriate waiting period.

4. API Design Considerations
If I were the API designer, I would implement the following:

Rate Limiter: A robust rate-limiting mechanism that monitors client requests and applies penalties appropriately.
Client-Level Rate Limits: Rate limits should be enforced per client, allowing other clients to continue making requests without being impacted by one client exceeding their limit.
Example API Call
You can test the API rate limiting using the following curl command:
```
curl --location 'http://localhost:8080/api/call?input=Request1' \
--header 'client: akash1'
```

In this implementation, we apply a client-level rate limit. If a single client sends more than 15 requests within a minute, the client will experience a one-minute waiting period before making further requests. 
Other clients will continue to function as usual without interruption.


How To run:-

Run Server side Application first which is a spring boot application.

once it is up and running, you can run the client side application which is java based application.

Once both applications are running, the client will start making requests to the server, adhering to the rate limits defined in the solution.


Output:-

```
Response for Request 1: Data retrieved for: Request 1
Response for Request 2: Data retrieved for: Request 2
Response for Request 3: Data retrieved for: Request 3
Response for Request 4: Data retrieved for: Request 4
Response for Request 5: Data retrieved for: Request 5
Response for Request 6: Data retrieved for: Request 6
Response for Request 7: Data retrieved for: Request 7
Response for Request 8: Data retrieved for: Request 8
Response for Request 9: Data retrieved for: Request 9
Response for Request 10: Data retrieved for: Request 10
Response for Request 11: Data retrieved for: Request 11
Response for Request 12: Data retrieved for: Request 12
Response for Request 13: Data retrieved for: Request 13
Response for Request 14: Data retrieved for: Request 14
Response for Request 15: Data retrieved for: Request 15
Waiting for 1 min to avoid rate limit
Response for Request 16: Data retrieved for: Request 16
Response for Request 17: Data retrieved for: Request 17
Response for Request 18: Data retrieved for: Request 18
Response for Request 19: Data retrieved for: Request 19
Response for Request 20: Data retrieved for: Request 20

```



