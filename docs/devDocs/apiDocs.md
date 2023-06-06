# Developer Documentation - API Documentation
*[Click here to return to table of contents](../home.md)*

------------------------------------------------------------------------------------------

### User Interactions

<details>
    <summary>
        <code>GET</code>
        <code><b>/users</b></code>
        <code>(gets all users from the database)</code>
    </summary>

##### Parameters

> None

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `200` | `text/plain;charset=UTF-8` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
>  curl --location 'https://poste-388415.uc.r.appspot.com/users'
> ```
</details>

<details>
    <summary>
        <code>Java - OkHttp</code>
    </summary>
    
> ```java
> OkHttpClient client = new OkHttpClient().newBuilder()
>   .build();
> Request request = new Request.Builder()
>   .url("https://poste-388415.uc.r.appspot.com/users")
>   .build();
> Response response = client.newCall(request).execute();
> ```
</details>

<details>
    <summary>
        <code>Java - Unirest</code>
    </summary>
    
> ```java
> Unirest.setTimeouts(0, 0);
> HttpResponse<String> response = Unirest.get("https://poste-388415.uc.r.appspot.com/users")
>   .asString();
> ```
</details>

<details>
    <summary>
        <code>C# - HttpClient</code>
    </summary>
    
> ```cs
> var client = new HttpClient();
> var request = new HttpRequestMessage(HttpMethod.Get, "https://poste-388415.uc.r.appspot.com/users");
> var response = await client.SendAsync(request);
> response.EnsureSuccessStatusCode();
> Console.WriteLine(await response.Content.ReadAsStringAsync());
> ```
</details>

<details>
    <summary>
        <code>C# - RestSharp</code>
    </summary>
    
> ```cs
> var options = new RestClientOptions("https://poste-388415.uc.r.appspot.com")
> {
>   MaxTimeout = -1,
> };
> var client = new RestClient(options);
> var request = new RestRequest("/users", Method.Get);
> RestResponse response = await client.ExecuteAsync(request);
> Console.WriteLine(response.Content);
> ```
</details>

</details>

<details>
    <summary>
        <code>GET</code>
        <code><b>/users/id/{id}</b></code>
        <code>(gets a user with the specified id from the database)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int ($int64) | The specific user id |

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `200` | `text/plain;charset=UTF-8` | JSON String |
> | `400` | `application/json` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
>  curl --location 'https://poste-388415.uc.r.appspot.com/users/id/1'
> ```
</details>

<details>
    <summary>
        <code>Java - OkHttp</code>
    </summary>
    
> ```java
>  OkHttpClient client = new OkHttpClient().newBuilder()
>    .build();
>  Request request = new Request.Builder()
>    .url("https://poste-388415.uc.r.appspot.com/users/id/1")
>    .build();
>  Response response = client.newCall(request).execute();
> ```
</details>

<details>
    <summary>
        <code>Java - Unirest</code>
    </summary>
    
> ```java
> Unirest.setTimeouts(0, 0);
> HttpResponse<String> response = Unirest.get("https://poste-388415.uc.r.appspot.com/users/id/1")
>   .asString();
> ```
</details>

<details>
    <summary>
        <code>C# - HttpClient</code>
    </summary>
    
> ```cs
> var client = new HttpClient();
> var request = new HttpRequestMessage(HttpMethod.Get, "https://poste-388415.uc.r.appspot.com/users/id/1");
> var response = await client.SendAsync(request);
> response.EnsureSuccessStatusCode();
> Console.WriteLine(await response.Content.ReadAsStringAsync());
> ```
</details>

<details>
    <summary>
        <code>C# - RestSharp</code>
    </summary>
    
> ```cs
> var options = new RestClientOptions("https://poste-388415.uc.r.appspot.com")
> {
>   MaxTimeout = -1,
> };
> var client = new RestClient(options);
> var request = new RestRequest("/users/id/1", Method.Get);
> RestResponse response = await client.ExecuteAsync(request);
> Console.WriteLine(response.Content);
> ```
</details>

</details>

<details>
    <summary>
        <code>GET</code>
        <code><b>/users/email/{email}</b></code>
        <code>(gets a user with the specified email from the database)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `email` |  required | string | The specific user email |

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `200` | `text/plain;charset=UTF-8` | JSON String |
> | `400` | `application/json` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
>  curl --location 'https://poste-388415.uc.r.appspot.com/users/email/example@email.com'
> ```
</details>

<details>
    <summary>
        <code>Java - OkHttp</code>
    </summary>
    
> ```java
> OkHttpClient client = new OkHttpClient().newBuilder()
>   .build();
> Request request = new Request.Builder()
>   .url("https://poste-388415.uc.r.appspot.com/users/email/example@email.com")
>   .build();
> Response response = client.newCall(request).execute();
> ```
</details>

<details>
    <summary>
        <code>Java - Unirest</code>
    </summary>
    
> ```java
> Unirest.setTimeouts(0, 0);
> HttpResponse<String> response = Unirest.get("https://poste-388415.uc.r.appspot.com/users/email/example@email.com")
>   .asString();
> ```
</details>

<details>
    <summary>
        <code>C# - HttpClient</code>
    </summary>
    
> ```cs
> var client = new HttpClient();
> var request = new HttpRequestMessage(HttpMethod.Get, "https://poste-388415.uc.r.appspot.com/users/email/example@email.com");
> var response = await client.SendAsync(request);
> response.EnsureSuccessStatusCode();
> Console.WriteLine(await response.Content.ReadAsStringAsync());
> ```
</details>

<details>
    <summary>
        <code>C# - RestSharp</code>
    </summary>
    
> ```cs
> var options = new RestClientOptions("https://poste-388415.uc.r.appspot.com")
> {
>   MaxTimeout = -1,
> };
> var client = new RestClient(options);
> var request = new RestRequest("/users/email/example@email.com", Method.Get);
> RestResponse response = await client.ExecuteAsync(request);
> Console.WriteLine(response.Content);
> ```
</details>

</details>

<details>
    <summary>
        <code>GET</code>
        <code><b>/users/login/{email}/{password}</b></code>
        <code>(validates a provided set of login credentials)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `email` |  required | string | The specific user email |
> | `password` |  required | string | The specific user password |

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `200` | `text/plain;charset=UTF-8` | JSON String |
> | `400` | `application/json` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
>  curl --location 'https://poste-388415.uc.r.appspot.com/users/login/example@email.com/passw0rd'
> ```
</details>

<details>
    <summary>
        <code>Java - OkHttp</code>
    </summary>
    
> ```java
> OkHttpClient client = new OkHttpClient().newBuilder()
>   .build();
> Request request = new Request.Builder()
>   .url("https://poste-388415.uc.r.appspot.com/users/login/example@email.com/passw0rd")
>   .build();
> Response response = client.newCall(request).execute();
> ```
</details>

<details>
    <summary>
        <code>Java - Unirest</code>
    </summary>
    
> ```java
> Unirest.setTimeouts(0, 0);
> HttpResponse<String> response = Unirest.get("https://poste-388415.uc.r.appspot.com/users/login/example@email.com/passw0rd")
>   .asString();
> ```
</details>

<details>
    <summary>
        <code>C# - HttpClient</code>
    </summary>
    
> ```cs
> var client = new HttpClient();
> var request = new HttpRequestMessage(HttpMethod.Get, "https://poste-388415.uc.r.appspot.com/users/login/example@email.com/passw0rd");
> var response = await client.SendAsync(request);
> response.EnsureSuccessStatusCode();
> Console.WriteLine(await response.Content.ReadAsStringAsync());
> ```
</details>

<details>
    <summary>
        <code>C# - RestSharp</code>
    </summary>
    
> ```cs
> var options = new RestClientOptions("https://poste-388415.uc.r.appspot.com")
> {
>   MaxTimeout = -1,
> };
> var client = new RestClient(options);
> var request = new RestRequest("/users/login/example@email.com/passw0rd", Method.Get);
> RestResponse response = await client.ExecuteAsync(request);
> Console.WriteLine(response.Content);
> ```
</details>

</details>

<details>
    <summary>
        <code>POST</code>
        <code><b>/users/add</b></code>
        <code>(adds a new user)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `username` |  required | string | New user's username |
> | `email` |  required | string | New user's email |
> | `password` |  required | string | New user's password |

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `201` | `text/plain;charset=UTF-8` | JSON String |
> | `200` | `text/plain;charset=UTF-8` | JSON String |
> | `400` | `application/json` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
> curl --location 'https://poste-388415.uc.r.appspot.com/users/add' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'email=example@email.com' \
> --data-urlencode 'username=DemoUser' \
> --data-urlencode 'password=passw0rd'
> ```
</details>

</details>

<details>
    <summary>
        <code>POST</code>
        <code><b>/users/update</b></code>
        <code>(updates a user's username or password)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `username` |  required | string | The user's new username |
> | `email` |  required | string | The email of the targeted user |
> | `password` |  required | string | New user's new password |

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `201` | `text/plain;charset=UTF-8` | JSON String |
> | `400` | `application/json` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
> curl --location 'https://poste-388415.uc.r.appspot.com/users/update' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'email=example@email.com' \
> --data-urlencode 'username=DemoUser' \
> --data-urlencode 'password=passw0rd'
> ```
</details>

</details>

<details>
    <summary>
        <code>POST</code>
        <code><b>/users/delete</b></code>
        <code>(deletes a user)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `email` |  required | string | The email of the targeted user |
> | `password` |  required | string | The password of the targeted user |

##### Responses

> | http code | content-type | response |
> |---|---|---|
> | `201` | `text/plain;charset=UTF-8` | JSON String |
> | `400` | `application/json` | JSON String |

##### Code Snippets

<details>
    <summary>
        <code>cURL</code>
    </summary>
    
> ```javascript
> curl --location 'https://poste-388415.uc.r.appspot.com/users/delete' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'email=example@email.com' \
> --data-urlencode 'password=passw0rd'
> ```
</details>

</details>

------------------------------------------------------------------------------------------