<!-- Generated from the same source as rest-apis.pptx -->

# REST APIs

*Evolution, constraints, the Richardson Maturity Model, HTTP semantics, design best practices and versioning*

*Speaker notes:* A comprehensive tour of REST: where it came from, what it actually requires, how HTTP methods, status codes and headers are used in practice, and how to design, secure, document and evolve an API.

---

## Agenda

1. **Evolution**: RPC, SOAP, Fielding's dissertation, Web APIs, OpenAPI
2. **Characteristics**: The six constraints, resources, representations, URIs
3. **Richardson Maturity Model**: Levels 0 to 3, HATEOAS, hypermedia formats
4. **HTTP methods & status codes**: Safe, idempotent, cacheable; the codes that matter
5. **HTTP headers**: Negotiation, caching, conditional requests, security
6. **Design best practices**: URIs, pagination, errors, security, performance
7. **Versioning & deprecation**: Strategies, compatibility rules, Sunset headers
8. **Tooling & wrap-up**: OpenAPI, testing, REST vs GraphQL/gRPC, checklist

---

# 01. Evolution

How the Web's architecture became the dominant style for APIs

Topics: RPC and SOAP · Fielding's REST · Web 2.0 APIs · OpenAPI & modern standards

## Before REST: the RPC era

- 1990s distributed computing hid the network behind local-looking calls: CORBA (1991), DCOM (1996), Java RMI (1997)
- Tight coupling: binary protocols, generated stubs, language and platform lock-in, painful through firewalls
- 1998: XML-RPC, XML payloads POSTed over HTTP
- 1998 to 2000: SOAP, with an XML envelope, WSDL contracts, UDDI registries and the WS-* stack (WS-Security, WS-ReliableMessaging)
- Powerful, but verbose, tooling-dependent and complex

> **HTTP as a tunnel.** SOAP and XML-RPC used HTTP only as transport: one endpoint, POST for everything. URIs, verbs, status codes and caching were ignored.

*Speaker notes:* The key contrast with REST: RPC styles expose operations (getCustomer, createOrder), while REST exposes resources and uses HTTP's own semantics.

## Evolution timeline (1/2)

- **1991 · World Wide Web**: URLs, HTTP and HTML go public; the architecture REST will later describe.
- **1996-99 · HTTP/1.0 and 1.1**: RFC 1945, RFC 2068 and RFC 2616: methods, status codes, caching, persistent connections.
- **1998-00 · XML-RPC and SOAP**: Enterprise web services: WSDL, UDDI, WS-*.
- **2000 · Fielding's dissertation**: REST defined in Chapter 5 of his UC Irvine PhD thesis.
- **2000-02 · First commercial Web APIs**: Salesforce and eBay (2000), Amazon Web Services (2002).
- **2004-07 · Web 2.0 and JSON**: Flickr, Twitter and Facebook APIs; JSON overtakes XML; Rails popularises resource routing.
- **2008-10 · Richardson Maturity Model**: Leonard Richardson's 2008 talk; Martin Fowler's 2010 article.
- **2011-17 · Swagger to OpenAPI**: Swagger (2011), OpenAPI Initiative (2015), OpenAPI 3.0 (2017).

*Speaker notes:* Fielding co-authored the HTTP/1.1 specification while developing REST; REST is, in effect, a description of why the Web scaled.

## Evolution timeline (2/2)

- **2014-15 · HTTP/1.1 revised, HTTP/2**: RFC 7230 to 7235 clarify HTTP/1.1; HTTP/2 (RFC 7540) adds multiplexing.
- **2015 · GraphQL and gRPC**: Both open-sourced: REST gets alternatives for flexible queries and internal RPC.
- **2016 · Problem Details**: RFC 7807 standardises machine-readable API errors (later RFC 9457).
- **2021 · OpenAPI 3.1**: Full alignment with JSON Schema 2020-12.
- **2022 · HTTP semantics consolidated**: RFC 9110 to 9112 and HTTP/3 (RFC 9114): one authoritative HTTP semantics spec.
- **2023-25 · RFC 9457, RFC 9745**: Problem Details update; the Deprecation header joins Sunset (RFC 8594).

*Speaker notes:* The trend is consolidation: HTTP semantics are now stable and well specified, and the surrounding ecosystem (OpenAPI, problem details, deprecation signalling) has caught up.

## SOAP vs REST

| Aspect | SOAP | REST |
|---|---|---|
| Nature | Protocol with a formal specification | Architectural style (a set of constraints) |
| Format | XML only | JSON, XML, HTML, binary, any media type |
| Contract | WSDL, strict and mandatory | OpenAPI, descriptive and optional |
| Transport | HTTP, SMTP, JMS and others | HTTP |
| Caching | Practically none (POST to one endpoint) | Native HTTP caching |
| Errors | SOAP Fault element | HTTP status codes plus problem details |
| Security | WS-Security (message level) | TLS plus OAuth 2.0 / OpenID Connect |
| Best for | Enterprise integration, formal contracts | Public and internal APIs, mobile, microservices |

*Speaker notes:* SOAP is not obsolete everywhere: banking, telecom and government integrations still use it where WS-* features and formal contracts matter.

## What is REST?

- REpresentational State Transfer: an architectural style for distributed hypermedia systems, defined by Roy Fielding (2000)
- Derived from the architecture of the Web; a set of constraints, not a protocol, standard or product
- Clients and servers exchange representations of resources; application state moves forward by following hypermedia links
- Fielding has been explicit that an API which is not hypertext-driven is not REST
- In practice the word covers resource-oriented HTTP + JSON APIs

> **REST vs RESTful vs HTTP API.** REST is the style. RESTful describes a system that follows it. Most "REST APIs" are really HTTP APIs at Richardson level 2.

## Core terminology

- **Resource**: Any named concept: a customer, an order, a collection of orders. Identified by a URI.
- **Representation**: A snapshot of a resource's state in some format (JSON, XML, HTML), chosen via media types.
- **URI**: A stable identifier of a resource (RFC 3986), for example /orders/42. A name, not an action.
- **Media type**: Tells the client how to parse the body: application/json, application/problem+json, application/vnd.example+json.
- **Hypermedia**: Links and controls inside representations that tell clients what they can do next.
- **Uniform interface**: The same small set of methods with the same semantics for every resource.

---

# 02. Characteristics

The architectural constraints that give REST its properties

Topics: Six constraints · Statelessness · Uniform interface · Resource modelling

## The six REST constraints

1. **Client-server**: Separate UI concerns from data storage; each side evolves independently.
2. **Stateless**: Every request carries all the context needed; the server keeps no session.
3. **Cacheable**: Responses declare whether they may be cached, cutting latency and load.
4. **Uniform interface**: Identification, manipulation through representations, self-descriptive messages, hypermedia.
5. **Layered system**: Proxies, gateways and CDNs can sit between client and server, invisibly.
6. **Code on demand (optional)**: Servers may send executable code, such as JavaScript, to extend clients.

*Speaker notes:* Only code on demand is optional. Each constraint trades something (for example, efficiency or simplicity) for a desirable property such as scalability, evolvability or visibility.

## Client-server and statelessness

**Client-server**

- Separation of concerns: UI and user state on the client; data, rules and storage on the server
- Web, mobile and partner clients share one API
- Teams release and scale each side independently
- The API contract is the only coupling

**Stateless**

- No client session is stored between requests
- Any server instance can handle any request: easy horizontal scaling and failover
- Easier to cache, debug and reason about
- Cost: repeated credentials and context in every request

> Resource state lives on the server and is fine. Session state does not.

## Statelessness in practice

- Authentication travels with every request, usually as a bearer token
- Pagination position travels as a cursor, not as a server-side iterator
- Avoid sticky sessions and server-side session stores
- Multi-step workflows keep their state in resources (a cart, an order status), not in a session

**Two independent requests, any server instance**

```http
GET /carts/7 HTTP/1.1
Host: api.example.com
Authorization: Bearer eyJhbGciOi...

POST /carts/7/items HTTP/1.1
Host: api.example.com
Authorization: Bearer eyJhbGciOi...
Content-Type: application/json

{ "sku": "A-1001", "quantity": 2 }
```

## Cacheable, layered, code on demand

- **Cacheable**
   - Responses are explicitly cacheable or not
   - Cache-Control, ETag, Last-Modified
   - Browsers, proxies and CDNs reuse responses
   - Poor cache design is a top cause of slow APIs
- **Layered system**
   - Client cannot tell whether it talks to the origin
   - API gateways, load balancers, WAFs, CDNs
   - Security and caching policies enforced in layers
   - Each layer only sees its neighbour
- **Code on demand**
   - The only optional constraint
   - Server ships scripts the client executes
   - Seen on the Web (JavaScript), rare in JSON APIs
   - Reduces what clients must pre-implement

## The uniform interface

- **1. Identification of resources**: Every resource has a stable URI. Clients refer to resources by identifier, never by internal implementation details.
- **2. Manipulation through representations**: Clients change state by sending a representation (JSON, XML) of the desired state, not by calling server procedures.
- **3. Self-descriptive messages**: Each message says how to process it: method, status code, media type, caching and conditional headers.
- **4. Hypermedia as the engine of application state**: Responses contain links and controls; clients discover next steps instead of hardcoding URIs (HATEOAS).

*Speaker notes:* Uniformity is what lets generic intermediaries (caches, proxies, browsers, gateways) work with any API without knowing its domain.

## Anatomy of an HTTP exchange

**Request**

```http
GET /orders/42?expand=customer HTTP/1.1
Host: api.example.com
Accept: application/json
Authorization: Bearer eyJhbGciOi...
If-None-Match: "v7"
```

**Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json
ETag: "v8"
Cache-Control: private, max-age=30

{
  "id": 42,
  "status": "shipped",
  "total": { "amount": "99.90", "currency": "EUR" }
}
```

> Request line + headers + optional body; status line + headers + optional body.

## Modelling resources

- **`/orders`**: Collection. GET lists, POST creates a member.
- **`/orders/42`**: Item (member). GET, PUT, PATCH and DELETE apply.
- **`/orders/42/items`**: Sub-collection owned by a parent; keep nesting shallow.
- **`/users/me`**: Singleton or alias for the current caller's resource.
- **`/orders/42/cancellation`**: Action modelled as a resource when no CRUD verb fits.
- **`/search?q=laptop`**: Computed or virtual resource backed by a query.

*Speaker notes:* Resources are domain concepts, not database tables. One resource may span several tables, and a table may be exposed as several resources.

---

# 03. Richardson Maturity Model

A four-step ladder for grading how RESTful an HTTP API is

Topics: Levels 0 to 3 · HATEOAS · Hypermedia formats · Which level to target

## The Richardson Maturity Model

- **Level 0: The swamp of POX.** One URI, one verb (POST). HTTP is only a tunnel for remote procedure calls.
- **Level 1: Resources.** Many URIs, one per resource, but usually still POST for everything.
- **Level 2: HTTP verbs and status codes.** Correct methods and status codes: caching, idempotency and error semantics work.
- **Level 3: Hypermedia controls (HATEOAS).** Responses include links that tell clients the next possible actions.

> Proposed by Leonard Richardson (QCon 2008), popularised by Martin Fowler (2010). A grading aid, not a standard.

*Speaker notes:* Each level adds one of the Web's core ideas: resources, verbs, hypermedia. Fowler's article uses a doctor's appointment booking example.

## Level 0: the swamp of POX

**Request**

```http
POST /api HTTP/1.1
Content-Type: application/json

{
  "action": "getOrder",
  "orderId": 42
}
```

**Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "error": "ORDER_NOT_FOUND",
  "message": "No order 42"
}
```

> One endpoint, one verb, HTTP 200 even for failures. Nothing for caches, proxies or monitoring to understand.

## Level 1: resources

- Introduces individual URIs per resource: divide and conquer
- Identity is explicit, so links and bookmarks are possible
- But the verb is still POST and the action is in the body
- Errors are still often 200; no caching, no idempotent retries

**Resource URIs, but everything is a POST**

```http
POST /orders/42 HTTP/1.1
Content-Type: application/json

{ "action": "get" }

POST /orders/42 HTTP/1.1
Content-Type: application/json

{ "action": "cancel" }

POST /customers/7/orders HTTP/1.1
Content-Type: application/json

{ "action": "list" }
```

## Level 2: HTTP verbs and status codes

- Use each method for what it means: GET reads, POST creates, PUT replaces, PATCH changes, DELETE removes
- Status codes tell the outcome; headers tell caches and clients what to do
- Safe reads become cacheable; PUT and DELETE become safely retryable
- This is where most production "REST" APIs live

**Same resource, different verbs**

```http
GET /orders/42          -> 200 OK
GET /orders/999         -> 404 Not Found

POST /orders            -> 201 Created
                           Location: /orders/43

PUT /orders/42          -> 200 OK
PATCH /orders/42        -> 200 OK
DELETE /orders/42       -> 204 No Content

DELETE /orders/42       -> 404 Not Found
                           (or 204; both are idempotent)
```

## Level 3: hypermedia controls (HATEOAS)

**Request**

```http
GET /orders/42 HTTP/1.1
Host: api.example.com
Accept: application/hal+json
```

**Response (HAL)**

```json
{
  "id": 42,
  "status": "pending",
  "total": 99.90,
  "_links": {
    "self":     { "href": "/orders/42" },
    "customer": { "href": "/customers/7" },
    "payment":  { "href": "/orders/42/payment",
                  "method": "PUT" },
    "cancel":   { "href": "/orders/42",
                  "method": "DELETE" }
  }
}
```

> Links reflect state: once the order ships, the cancel link disappears. Clients follow links instead of building URIs.

## Hypermedia formats

| Format | Media type | Notes |
|---|---|---|
| HAL | application/hal+json | Simple: _links and _embedded; widely supported |
| JSON:API | application/vnd.api+json | Opinionated: resource objects, relationships, pagination, errors |
| Siren | application/vnd.siren+json | Entities with actions: method, fields, type |
| Hydra / JSON-LD | application/ld+json | Semantic-web vocabulary for describing operations |
| Collection+JSON | application/vnd.collection+json | Collections, items, queries and templates |
| Link header | any (RFC 8288) | Links carried in HTTP headers; works with any body format |

*Speaker notes:* None of these is universally adopted. Many teams use plain JSON with a small, documented link convention (for example next/prev pagination links) as a pragmatic middle ground.

## Which level should you target?

**Why level 2 is the sweet spot**

- Delivers most of REST's practical benefits
- Works with every HTTP client, cache and gateway
- Straightforward to document with OpenAPI
- Hardcoded URIs are acceptable for a controlled set of clients

**When level 3 pays off**

- Long-lived public APIs with many unknown clients
- Workflows whose available actions depend on state
- Servers that need freedom to move or rename URIs
- Generic clients: browsers, crawlers, API explorers

> Level 3 is what Fielding calls REST. Level 2 is what most of the industry ships. Know what you are choosing.

---

# 04. HTTP methods and status codes

Using HTTP's semantics (RFC 9110) correctly is most of level 2

Topics: Methods · Safe and idempotent · PUT vs PATCH · Status codes

## HTTP methods at a glance

| Method | Purpose | Safe | Idempotent | Cacheable | Request body |
|---|---|---|---|---|---|
| GET | Retrieve a representation | Yes | Yes | Yes | No |
| HEAD | Like GET, headers only | Yes | Yes | Yes | No |
| OPTIONS | Discover capabilities, CORS preflight | Yes | Yes | No | Rare |
| POST | Create a subordinate; process data | No | No | Rarely | Yes |
| PUT | Replace the resource at this URI | No | Yes | No | Yes |
| PATCH | Apply a partial modification | No | Not guaranteed | No | Yes |
| DELETE | Remove the resource | No | Yes | No | Not used |

> Safe: the client does not request a state change. Idempotent: N identical requests have the same intended effect as one (RFC 9110, section 9.2).

*Speaker notes:* Safe implies idempotent, but not the reverse. Idempotency describes the intended effect on server state, not the response: a second DELETE may return 404 and still be idempotent.

## GET, HEAD and OPTIONS

- **GET · Read**
   - Never changes state
   - Cacheable and bookmarkable
   - Parameters in the query string
   - Do not rely on a request body
- **HEAD · Metadata only**
   - Same headers as GET, no body
   - Check existence, size or ETag cheaply
   - Cache validation without download
- **OPTIONS · Capabilities**
   - Returns Allow with supported methods
   - Browsers send it as the CORS preflight
   - Access-Control-Allow-* headers in the response

*Speaker notes:* Never perform state changes on GET. Crawlers, link prefetchers and caches assume GET is safe and will happily trigger a GET /orders/42/delete.

## POST

- Creates a new subordinate resource; the server chooses the URI
- Success: 201 Created with a Location header (and usually the new representation)
- Also used for actions and processing that do not map to other methods
- Neither safe nor idempotent: duplicate submissions create duplicates; use an Idempotency-Key for retries

**Create an order**

```http
POST /orders HTTP/1.1
Host: api.example.com
Content-Type: application/json

{ "customerId": 7,
  "items": [ { "sku": "A-1001", "quantity": 2 } ] }

HTTP/1.1 201 Created
Location: https://api.example.com/orders/43
Content-Type: application/json

{ "id": 43, "status": "pending" }
```

## PUT vs PATCH

**PUT: replace**

- Sends the complete new representation
- Idempotent by definition
- Missing fields are reset or removed
- Can create at a client-chosen URI (201) or update (200/204)
- Use for whole-document replacement

**PATCH: modify**

- Sends only the changes (RFC 5789)
- Not guaranteed idempotent; depends on the patch format
- JSON Merge Patch: application/merge-patch+json (RFC 7396)
- JSON Patch: application/json-patch+json (RFC 6902)
- Use for partial updates of large resources

> Never use PUT for partial updates: clients that omit a field will silently wipe it.

## PATCH formats

**JSON Merge Patch**

```http
PATCH /users/7 HTTP/1.1
Content-Type: application/merge-patch+json

{
  "name": "Ana",
  "phone": null
}
```

**JSON Patch**

```http
PATCH /users/7 HTTP/1.1
Content-Type: application/json-patch+json

[
  { "op": "replace", "path": "/name",
    "value": "Ana" },
  { "op": "remove", "path": "/phone" }
]
```

> Merge patch is simple (null removes a member) but cannot patch single array elements; JSON Patch is explicit and ordered.

## DELETE

- Removes the resource; idempotent: deleting twice leaves the same state
- Success: 204 No Content (or 200 with a body, or 202 if deletion is asynchronous)
- A repeated DELETE may return 404 or 204; both are acceptable, document which
- Consider soft deletion (a status or deletedAt field) when history matters
- Avoid request bodies on DELETE and avoid bulk deletes through one URI

> **Cascading effects.** Define what happens to subordinate resources: cascade, block with 409 Conflict, or detach. Document it in the contract.

## Idempotency in practice

- Networks fail: clients retry after timeouts and cannot tell whether the first attempt succeeded
- GET, PUT and DELETE are safe to retry; POST is not
- Idempotency-Key header (IETF draft): client sends a unique key per logical operation
- Server stores key and response for a window (for example 24 hours) and replays it
- Same key with a different payload is an error (409 or 422)

**Safe retry of a payment**

```http
POST /payments HTTP/1.1
Host: api.example.com
Idempotency-Key: 8e03978e-40d5-43e8-bc93-6894a57f9324
Content-Type: application/json

{ "orderId": 42, "amount": "99.90" }

-- network timeout, client retries with the same key --

HTTP/1.1 201 Created
Location: /payments/9001

(same response as the first attempt; no second charge)
```

## Status code classes

- **1xx · Informational**: Interim: 100 Continue, 101 Switching Protocols, 103 Early Hints.
- **2xx · Success**: The request was received, understood and accepted.
- **3xx · Redirection**: Further action is needed: moved, not modified, see other.
- **4xx · Client error**: The request is wrong: fix it before retrying.
- **5xx · Server error**: The server failed on a valid request: retrying may help.

> Never return 200 OK with an error in the body. Clients, caches and monitoring rely on the status line.

*Speaker notes:* A useful rule: 4xx means the caller must change something; 5xx means the provider has a problem. Alerting and SLOs are typically built on the 5xx rate.

## Success and redirect codes

| Code | Name | Typical use in a REST API |
|---|---|---|
| 200 | OK | Successful GET, PUT or PATCH with a representation in the body |
| 201 | Created | POST created a resource; send Location (and often the body) |
| 202 | Accepted | Accepted for asynchronous processing; point to a status resource |
| 204 | No Content | Success with no body: DELETE, PUT, PATCH |
| 303 | See Other | After processing, fetch the result with GET at another URI |
| 304 | Not Modified | Conditional GET: cached copy is still valid, no body |
| 307 | Temporary Redirect | Temporary move; the method and body must be repeated |
| 308 | Permanent Redirect | Permanent move; the method and body must be repeated |

## Client error codes (1/2)

| Code | Name | When to use it |
|---|---|---|
| 400 | Bad Request | Malformed syntax or a generic invalid request |
| 401 | Unauthorized | Missing or invalid credentials; send WWW-Authenticate |
| 403 | Forbidden | Authenticated, but not permitted to do this |
| 404 | Not Found | No such resource; also used to hide existence from the unauthorised |
| 405 | Method Not Allowed | Method unsupported on this resource; send Allow |
| 406 | Not Acceptable | Cannot produce any format listed in Accept |
| 410 | Gone | Permanently removed, for example a retired API version |

> 401 means unauthenticated despite its name; 403 means authenticated but not authorised.

## Client error codes (2/2)

| Code | Name | When to use it |
|---|---|---|
| 409 | Conflict | State conflict: duplicate, version clash, invalid transition |
| 412 | Precondition Failed | An If-Match or If-Unmodified-Since check failed |
| 415 | Unsupported Media Type | Request Content-Type is not supported |
| 422 | Unprocessable Content | Well-formed but semantically invalid (validation errors) |
| 428 | Precondition Required | Server demands a conditional request (If-Match) |
| 429 | Too Many Requests | Rate limit exceeded; send Retry-After |

> 400 vs 422 is a style choice: pick one convention for validation failures and apply it consistently.

## Server error codes

| Code | Name | When to use it |
|---|---|---|
| 500 | Internal Server Error | Unexpected failure; never leak stack traces or SQL |
| 501 | Not Implemented | The server does not support the requested method |
| 502 | Bad Gateway | A gateway received an invalid response from upstream |
| 503 | Service Unavailable | Overloaded or in maintenance; send Retry-After |
| 504 | Gateway Timeout | A gateway timed out waiting for the upstream service |

> Clients may retry 502, 503 and 504 with exponential backoff and jitter. Retry 500 only for idempotent requests.

*Speaker notes:* Pair these with timeouts, circuit breakers and bulkheads in the services behind the API.

---

# 05. HTTP headers

Headers carry the metadata that makes messages self-descriptive

Topics: Content negotiation · Caching · Conditional requests · Security · Control headers

## Headers at a glance

- **Representation**: Content-Type, Content-Length, Content-Encoding, Content-Language, Content-Location
- **Negotiation**: Accept, Accept-Language, Accept-Encoding, Vary
- **Caching**: Cache-Control, ETag, Last-Modified, Expires, Age
- **Conditional**: If-Match, If-None-Match, If-Modified-Since, If-Unmodified-Since
- **Authentication and security**: Authorization, WWW-Authenticate, Strict-Transport-Security, CORS headers
- **Control and hypermedia**: Location, Link, Allow, Retry-After, Prefer, RateLimit, Idempotency-Key

## Content negotiation

**Request**

```http
GET /products/5 HTTP/1.1
Host: api.example.com
Accept: application/json, application/xml;q=0.5
Accept-Language: sr, en;q=0.7
Accept-Encoding: gzip, br
```

**Response**

```http
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Language: sr
Content-Encoding: br
Vary: Accept, Accept-Language, Accept-Encoding
```

> 406 Not Acceptable: nothing matches Accept. 415 Unsupported Media Type: the request body format is unsupported. Always send Vary so caches key on the negotiated headers.

## Caching headers

| Header | Example | Meaning |
|---|---|---|
| Cache-Control | max-age=60, private | Freshness lifetime and who may cache; also no-cache, no-store, s-maxage, immutable |
| ETag | "v8" | Opaque validator for this representation version |
| Last-Modified | Tue, 06 Oct 2026 10:00:00 GMT | Time-based validator; coarser than ETag |
| Expires | Wed, 07 Oct 2026 10:00:00 GMT | Legacy absolute expiry; max-age takes precedence |
| Vary | Accept-Encoding | Request headers that select the representation |
| Age | 37 | Seconds the response has already spent in caches |

*Speaker notes:* Rule of thumb: prefer Cache-Control plus ETag. Remember that no-cache does not mean do not cache: it means revalidate before reuse. no-store is the directive that forbids storing.

## Caching strategies

- **`public, max-age=3600`**: Reference data shared by all users (countries, currencies, product catalog). CDN and browser caching.
- **`private, no-cache + ETag`**: Per-user data that must stay fresh. Cache stores a copy but revalidates every time; 304 saves bandwidth.
- **`no-store`**: Sensitive responses (tokens, personal or financial data). Nothing is written to any cache.
- **`public, max-age=31536000, immutable`**: Versioned, content-addressed assets: the URL changes when the content changes.

> POST responses are rarely cached; design GET endpoints so that caching is safe and effective.

## Conditional requests

**Revalidation (If-None-Match)**

```http
GET /products/5 HTTP/1.1
If-None-Match: "v8"

HTTP/1.1 304 Not Modified
ETag: "v8"
Cache-Control: max-age=60
```

**Optimistic locking (If-Match)**

```http
PUT /products/5 HTTP/1.1
If-Match: "v7"
Content-Type: application/json

{ "price": 19.90 }

HTTP/1.1 412 Precondition Failed
```

> If-Match prevents the lost-update problem. Servers can enforce it with 428 Precondition Required.

## Authentication and security headers

| Header | Example | Notes |
|---|---|---|
| Authorization | Bearer eyJhbGci... | OAuth 2.0 bearer token (RFC 6750); preferred |
| Authorization | Basic dXNlcjpwYXNz | Only over TLS; avoid for user-facing APIs |
| WWW-Authenticate | Bearer error="invalid_token" | Accompanies 401; tells the client how to authenticate |
| Strict-Transport-Security | max-age=31536000 | Forces HTTPS for future requests |
| X-Content-Type-Options | nosniff | Stops MIME sniffing of API responses |
| Access-Control-Allow-Origin | https://app.example.com | CORS: use an explicit allowlist, not \* |
| Cache-Control | no-store | Keep sensitive responses out of caches |

> Never put tokens or API keys in the URL: query strings end up in logs, history and referrers.

## Control and hypermedia headers

| Header | Used with | Purpose |
|---|---|---|
| Location | 201, 202, 3xx | URI of the created resource, status monitor or redirect target |
| Link | any | Typed links (rel="next", "prev", "deprecation"), RFC 8288 |
| Allow | 405, OPTIONS | Methods supported on the resource |
| Retry-After | 429, 503 | Seconds or HTTP date after which to retry |
| Prefer | any request | return=minimal or representation, respond-async (RFC 7240) |
| RateLimit-\* | any response | Quota, remaining and reset (IETF draft; X-RateLimit-\* still common) |
| Idempotency-Key | POST, PATCH | Client-chosen key to make retries safe (IETF draft) |
| traceparent | any request | W3C Trace Context: correlates a request across services |

---

# 06. Design best practices

Consistent, predictable, secure and fast APIs

Topics: URIs and naming · Pagination and filtering · Errors · Security · Performance

## URI design

**Do**

- Nouns, plural collections: /orders
- Hierarchy for ownership: /customers/7/orders
- Lowercase with hyphens: /order-items
- Stable, opaque identifiers
- Keep nesting to two levels; link beyond that

**Don't**

- Verbs in paths: /getOrder, /createUser
- Mixed case, underscores, trailing slashes
- File extensions: /orders.json
- Deep nesting: /a/1/b/2/c/3/d/4
- Database internals: /tbl_orders?id=42

> The URI identifies the resource; the HTTP method says what to do with it.

## Naming and modelling

- Pick one convention and apply it everywhere: plural nouns, one JSON casing (camelCase or snake_case)
- Model domain concepts, not tables; hide persistence details
- Actions that do not fit CRUD: model them as resources (POST /orders/42/cancellation) or as state changes (PATCH status)
- Custom methods are acceptable when needed, for example Google style POST /orders/42:cancel; document them
- Use consistent names for the same concept across endpoints (customerId, never custId in one place)

> **Predictability.** A developer who has seen two endpoints should be able to guess the third.

## Pagination

- Offset/limit: ?offset=40&limit=20. Simple, allows jumping; slow and unstable on large, changing data
- Cursor (keyset): ?after=abc&limit=20. Stable and fast, no random page access; cursors are opaque
- Always cap limit, with a sensible default and maximum
- Navigate with Link headers (rel=next, prev) or a links object
- Total counts are expensive; make them optional

**Cursor pagination response**

```http
HTTP/1.1 200 OK
Content-Type: application/json
Link: <https://api.example.com/orders?after=abc&limit=20>;
      rel="next"

{
  "data": [ { "id": 41 }, { "id": 42 } ],
  "page": {
    "limit": 20,
    "next": "abc"
  }
}
```

## Filtering, sorting, field selection

- Filter with query parameters named after fields; document operators
- Sort with sort=field; a leading minus means descending
- Sparse fieldsets (fields=) cut payload size
- expand= embeds related resources and avoids N+1 round trips
- Whitelist filterable and sortable fields; index them
- Complex search: a documented query syntax or POST /orders/search

**Query conventions**

```http
GET /orders?status=shipped&created_after=2026-01-01

GET /orders?sort=-created_at,total

GET /orders?fields=id,status,total

GET /orders/42?expand=customer,items

GET /products?q=laptop&price_max=1500
```

## Error handling: RFC 9457 problem details

- One error shape across the whole API, with media type application/problem+json
- type (URI), title, status, detail and instance are standard members
- Extension members carry field-level errors and a trace id
- Status code in the body must match the HTTP status
- Never expose stack traces, SQL or internal hostnames

**Validation failure**

```http
HTTP/1.1 422 Unprocessable Content
Content-Type: application/problem+json

{
  "type": "https://api.example.com/problems/validation",
  "title": "Request validation failed",
  "status": 422,
  "detail": "1 field is invalid",
  "instance": "/orders",
  "errors": [
    { "pointer": "/items/0/quantity",
      "message": "must be at least 1" }
  ],
  "traceId": "4bf92f3577b34da6"
}
```

## Payload conventions

- **JSON by default**: UTF-8, RFC 8259. Return an object at the top level, not a bare array, so you can add fields later.
- **Dates and times**: RFC 3339 / ISO 8601, in UTC: 2026-10-11T14:30:00Z.
- **Identifiers**: Opaque strings or UUIDs. Avoid exposing sequential integers on public APIs.
- **Money**: Decimal strings or integer minor units plus an ISO 4217 currency code. Never floats.
- **Null vs absent**: Define the semantics. In merge patch, null means remove.
- **Enums**: Strings, documented, and treated as open sets by clients.

## Long-running operations

1. **Submit.** POST /reports returns 202 Accepted with Location: /jobs/123.
2. **Poll.** GET /jobs/123 returns 200 with status and progress; honour Retry-After.
3. **Complete.** Finished job returns 303 See Other pointing to the result, or embeds a link to it.
4. **Cancel.** DELETE /jobs/123 cancels a job that is still running.

> Alternatives to polling: webhooks, Server-Sent Events or WebSockets.

*Speaker notes:* A 202 response should tell the client how to monitor the operation. Do not simply hold the connection open for minutes.

## Security best practices

- **TLS everywhere**: HTTPS only, HSTS, modern protocol versions.
- **Authentication**: OAuth 2.0 / OpenID Connect with PKCE; short-lived tokens; no secrets in URLs.
- **Authorization**: Check permissions on every object and every function, server side.
- **Input validation**: Validate against the schema, enforce size and type limits, reject unknown input where appropriate.
- **Limit abuse**: Rate limits, quotas, request size and timeout limits, pagination caps.
- **Minimise exposure**: Return only needed properties, generic errors, explicit CORS allowlists, audit logs.

## OWASP API Security Top 10 (2023)

| # | Risk | Mitigation in one line |
|---|---|---|
| 1 | Broken Object Level Authorization | Check ownership on every object access |
| 2 | Broken Authentication | Standard flows, short-lived tokens, lockouts |
| 3 | Broken Object Property Level Auth | Whitelist readable and writable properties |
| 4 | Unrestricted Resource Consumption | Rate limits, quotas, size and time caps |
| 5 | Broken Function Level Authorization | Deny by default; role checks per endpoint |
| 6 | Unrestricted Access to Sensitive Flows | Detect automation of purchases, signups |
| 7 | Server Side Request Forgery | Validate and allowlist outbound URLs |
| 8 | Security Misconfiguration | Hardened defaults, CORS, headers, patching |
| 9 | Improper Inventory Management | Track versions and hosts; retire old ones |
| 10 | Unsafe Consumption of APIs | Treat third-party API data as untrusted |

## Rate limiting

- Protects availability and cost; fixes unrestricted resource consumption
- Algorithms: fixed window, sliding window, token bucket, leaky bucket
- Limit per API key, user, IP and sometimes per endpoint or cost
- Return 429 Too Many Requests with Retry-After
- Expose quota with RateLimit headers so clients can self-throttle

**Throttled request**

```http
HTTP/1.1 429 Too Many Requests
Content-Type: application/problem+json
Retry-After: 30
RateLimit-Limit: 100
RateLimit-Remaining: 0
RateLimit-Reset: 30

{
  "type": "https://api.example.com/problems/rate-limit",
  "title": "Rate limit exceeded",
  "status": 429
}
```

## Performance

- **Compress**: gzip or Brotli for text payloads; negotiate with Accept-Encoding.
- **Cache**: Cache-Control and ETags; put a CDN in front of public GETs.
- **Shrink payloads**: Pagination, sparse fieldsets, no over-fetching.
- **Avoid chatty APIs**: expand, batch endpoints and well-shaped resources reduce round trips and N+1 calls.
- **Modern transport**: HTTP/2 or HTTP/3 multiplexing, keep-alive and connection pooling.
- **Go async**: Move slow work behind 202 Accepted and a status resource.

## Observability and operations

- Propagate a correlation id: W3C traceparent (or X-Request-ID) through every hop
- Structured logs with method, path template, status and latency
- Track the RED metrics: rate, errors, duration
- Separate liveness and readiness endpoints for orchestrators
- Measure usage per API version to drive deprecation decisions

**Trace context propagation**

```http
GET /orders/42 HTTP/1.1
Host: api.example.com
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01

HTTP/1.1 200 OK
X-Request-ID: 7f1c2d9e
Server-Timing: db;dur=12, app;dur=31
```

---

# 07. Versioning and deprecation

Changing an API without breaking the people who depend on it

Topics: Breaking vs additive · Versioning strategies · Deprecation lifecycle · Compatibility rules

## Breaking vs non-breaking changes

**Breaking: needs a new version**

- Removing or renaming an endpoint, field or parameter
- Changing a type, format or meaning of a field
- Making an optional input required
- Tightening validation or changing error behaviour
- Changing authentication or authorization rules

**Non-breaking: additive**

- Adding a new endpoint or resource
- Adding an optional request field or query parameter
- Adding a response field
- Adding an enum value (only if clients tolerate unknown values)
- Relaxing validation

> Document that clients must ignore unknown fields (the Tolerant Reader pattern); without it, even additive changes can break.

## Versioning strategies

| Strategy | Example | Strengths | Weaknesses |
|---|---|---|---|
| URI path | /v2/orders | Visible, easy to route and cache | Version in the identifier; URI churn |
| Query parameter | ?api-version=2024-06-01 | Easy to add; used by Azure | Easily lost; cache keys |
| Custom header | X-GitHub-Api-Version: 2022-11-28 | Clean URIs | Hidden; harder in a browser |
| Media type | Accept: application/vnd.example.v2+json | Purist; versions the representation | Tooling and caching friction |
| Date-based | Stripe-Version: 2024-06-20 | Fine-grained, pinned per account | Heavy server-side effort |
| Evolution only | No version; additive changes | No fragmentation | Needs discipline and hypermedia |

## Choosing a strategy

- **Public API, many unknown clients**: URI major version (/v1, /v2). Simple to document, route, cache and sunset. Version majors only.
- **Partner or B2B API, long-lived clients**: Header or date-based versions with a published changelog; clients pin the version they tested against.
- **Internal microservices**: Evolve compatibly, add consumer-driven contract tests (for example Pact), and avoid parallel versions.
- **Hypermedia-driven API**: Version media types or rely on links so that the server can move without breaking clients.

> Whatever you choose, apply it uniformly and make it part of the contract.

## Deprecation lifecycle

1. **Announce.** Changelog, email and developer portal notice with the replacement and migration guide.
2. **Signal in responses.** Add Deprecation and Sunset headers plus a Link to the migration guide.
3. **Monitor usage.** Track which clients still call the old version; contact them directly.
4. **Brown out.** Short, scheduled outages of the old version to flush out forgotten clients.
5. **Retire.** After the sunset date return 410 Gone with a problem-details body.

> Give clients a realistic window: months for public APIs, not days.

## Deprecation and Sunset headers

- Deprecation (RFC 9745): the resource is or will be deprecated; value is a date (structured field)
- Sunset (RFC 8594): when the resource is expected to stop responding
- Link with rel="deprecation" and rel="successor-version" points to documentation and the replacement
- Machine-readable signals let clients and gateways alert automatically

**Response from a deprecated v1**

```http
HTTP/1.1 200 OK
Deprecation: @1767225600
Sunset: Wed, 30 Jun 2027 23:59:59 GMT
Link: <https://developer.example.com/migrate-v2>;
      rel="deprecation"; type="text/html"
Link: <https://api.example.com/v2/orders>;
      rel="successor-version"
```

## Compatibility rules of thumb

- Prefer additive evolution; version only for genuine breaking changes
- Be liberal in what you accept and tolerant of unknown fields
- Never reuse a field name or enum value with a different meaning
- Give new fields safe defaults for old clients
- Run OpenAPI diff checks (for example oasdiff) and contract tests in CI
- Keep a public changelog and an explicit support policy

> **Cost of versions.** Every live version is code, tests, docs and support. Keep the number of supported versions small.

---

# 08. Tooling and wrap-up

Documenting, testing, choosing alternatives and avoiding pitfalls

Topics: OpenAPI · Testing and gateways · REST vs GraphQL vs gRPC · Anti-patterns · Checklist

## OpenAPI and documentation

- OpenAPI Specification: a machine-readable contract in YAML or JSON
- Contract-first or code-first: either way, the spec is the source of truth
- Generates docs (Swagger UI, Redoc), SDKs, mocks and validators
- OpenAPI 3.1 uses JSON Schema 2020-12 directly
- Lint with Spectral; describe events with AsyncAPI

**OpenAPI 3.1 fragment**

```yaml
openapi: 3.1.0
info:
  title: Orders API
  version: 1.0.0
paths:
  /orders/{id}:
    get:
      summary: Get an order
      parameters:
        - name: id
          in: path
          required: true
          schema: { type: string }
      responses:
        "200":
          description: The order
        "404":
          description: Not found
```

## Testing and tooling

- **Explore and debug**: curl, HTTPie, Postman, Bruno, Insomnia.
- **Contract testing**: Pact for consumer-driven contracts; Schemathesis or Dredd to test an implementation against its OpenAPI spec.
- **Load and resilience**: k6, Gatling, JMeter; chaos experiments for timeouts and retries.
- **API gateways**: Kong, Apigee, AWS API Gateway, Azure API Management: auth, throttling, routing, analytics.
- **Mocking**: Prism, WireMock and Mockoon for contract-first development.
- **Governance**: Spectral linting, oasdiff breaking-change checks, style guides enforced in CI.

## REST vs GraphQL vs gRPC vs events

|  | REST | GraphQL | gRPC | Events |
|---|---|---|---|---|
| Style | Resources over HTTP | Query language, one endpoint | RPC with Protobuf over HTTP/2 | Messages via a broker |
| Payload | JSON, XML, others | JSON | Binary Protobuf | Any |
| Caching | Native HTTP | Harder | Not HTTP caching | Not applicable |
| Best for | Public APIs, CRUD, compatibility | Varied clients, nested data | Internal low-latency calls, streaming | Decoupling, fan-out |
| Watch out | Chatty, over-fetching | Query cost, per-field authz | Browser support, tooling | Eventual consistency |

*Speaker notes:* These are complements, not rivals: many systems expose REST publicly, use gRPC between internal services and events for asynchronous workflows.

## Common anti-patterns

- **Verbs in URIs**: /getUser, /deleteOrder: the method already says it.
- **200 with error body**: Hides failures from clients, caches and monitoring.
- **Everything is POST**: Loses caching, idempotency and semantics.
- **Exposing the database**: Tables become endpoints; schema changes become breaking changes.
- **Breaking silently**: Changing contracts without a version or notice.
- **Leaky errors**: Stack traces, SQL and internals in responses.
- **Server-side sessions**: Sticky sessions undermine statelessness and scaling.
- **Inconsistency**: Different names, formats and errors from endpoint to endpoint.

## Design checklist

- **Contract**
   - Resources are nouns with stable URIs
   - Methods and status codes used correctly
   - One error format (RFC 9457)
   - OpenAPI spec published and linted
- **Behaviour**
   - POST retries are safe (Idempotency-Key)
   - Pagination, filtering, sorting defined
   - Caching headers and ETags set
   - Conditional updates with If-Match
- **Operations**
   - TLS, OAuth 2.0, object-level authorization
   - Rate limits with 429 and Retry-After
   - Versioning and deprecation policy
   - Tracing, logs, metrics, usage by version

## Key takeaways

- REST is an architectural style from Fielding's 2000 dissertation, built on six constraints
- The Richardson Maturity Model grades adoption of resources, verbs and hypermedia; level 2 is the practical baseline
- Respect HTTP semantics: safe and idempotent methods, honest status codes, meaningful headers
- Design for consumers: consistent URIs, pagination, problem-details errors, and OpenAPI documentation
- Secure by default and protect capacity: TLS, OAuth 2.0, object-level authorization, rate limits
- Evolve additively; when you must break, version, signal deprecation and sunset, and give clients time

> **Remember.** A good API is a product: its contract is promised to every client, so change it carefully.

## References

- **Standards**
   - RFC 9110 HTTP Semantics; RFC 9111 Caching
   - RFC 3986 URI; RFC 8259 JSON; RFC 3339
   - RFC 9457 Problem Details
   - RFC 8288 Web Linking
   - RFC 5789, 6902, 7396 PATCH formats
   - RFC 8594 Sunset; RFC 9745 Deprecation
   - RFC 6749, 6750, 7636 OAuth 2.0
- **Guidelines**
   - OpenAPI Specification (spec.openapis.org)
   - OWASP API Security Top 10 (2023)
   - Microsoft REST API Guidelines
   - Google API Design Guide / AIP
   - Zalando RESTful API Guidelines
   - JSON:API specification
- **Reading**
   - R. Fielding, Architectural Styles and the Design of Network-based Software Architectures (2000)
   - M. Fowler, Richardson Maturity Model (2010)
   - Richardson and Amundsen, RESTful Web APIs
