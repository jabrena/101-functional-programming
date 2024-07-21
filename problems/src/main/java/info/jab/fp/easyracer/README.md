# Easy Racer

A scenario server validates the implementations of 10 scenarios:

```
Race 2 concurrent requests

GET /1
The winner returns a 200 response with a body containing right

Race 2 concurrent requests, where one produces a connection error

GET /2
The winner returns a 200 response with a body containing right

Race 10,000 concurrent requests

GET /3
The winner returns a 200 response with a body containing right

Race 2 concurrent requests but 1 of them should have a 1 second timeout

GET /4
The winner returns a 200 response with a body containing right

Race 2 concurrent requests where a non-200 response is a loser

GET /5
The winner returns a 200 response with a body containing right

Race 3 concurrent requests where a non-200 response is a loser

GET /6
The winner returns a 200 response with a body containing right

Start a request, wait at least 3 seconds then start a second request (hedging)

GET /7
The winner returns a 200 response with a body containing right

Race 2 concurrent requests that "use" a resource which is obtained and released through other requests. The "use" request can return a non-20x request, in which case it is not a winner.

GET /8?open
GET /8?use=
GET /8?close=
The winner returns a 200 response with a body containing right

Make 10 concurrent requests where 5 return a 200 response with a letter

GET /9
When assembled in order of when they responded, form the "right" answer
```