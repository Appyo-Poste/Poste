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

<br>
<br>

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

<br>
<br>

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

<br>
<br>

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

<br>
<br>

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

<br>
<br>

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

<br>
<br>

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

<br>
<br>

</details>

------------------------------------------------------------------------------------------

### Post Interactions

<details>
    <summary>
        <code>GET</code>
        <code><b>/posts</b></code>
        <code>(gets all posts from the database)</code>
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
>  curl --location 'https://poste-388415.uc.r.appspot.com/posts'
> ```
</details>

<br>
<br>

</details>



<details>
    <summary>
        <code>GET</code>
        <code><b>/posts/id/{id}</b></code>
        <code>(gets a posts with the specified id from the database)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int ($int64) | The specific post id |

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
>  curl --location 'https://poste-388415.uc.r.appspot.com/posts/id/1'
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
>    .url("https://poste-388415.uc.r.appspot.com/posts/id/1")
>    .build();
>  Response response = client.newCall(request).execute();
> ```
</details>

<br>
<br>

</details>



<details>
    <summary>
        <code>GET</code>
        <code><b>/posts/user/{id}</b></code>
        <code>(gets all posts owned by the specified user id from the database)</code>
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
>  curl --location 'https://poste-388415.uc.r.appspot.com/posts/user/1'
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
>    .url("https://poste-388415.uc.r.appspot.com/posts/user/1")
>    .build();
>  Response response = client.newCall(request).execute();
> ```
</details>

<br>
<br>

</details>



<details>
    <summary>
        <code>POST</code>
        <code><b>/posts/add</b></code>
        <code>(adds a new post)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `name` |  required | string | The name of the new post |
> | `link` |  required | string | The link for the new post |
> | `ownerId` |  required | int | The user ID of the post owner |

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

<br>
<br>

</details>



<details>
    <summary>
        <code>POST</code>
        <code><b>/posts/update</b></code>
        <code>(updates a post's name or link)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the post to update |
> | `name` |  required | string | The post's new name |
> | `link` |  required | string | The post's new link |
> | `ownerId` |  required | int | The post's new ownerId |

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

<br>
<br>

</details>

<details>
    <summary>
        <code>POST</code>
        <code><b>/posts/delete</b></code>
        <code>(deletes a post)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the post to delete |

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

<br>
<br>

</details>

------------------------------------------------------------------------------------------

### Folder Interactions


<details>
    <summary>
        <code>GET</code>
        <code><b>/folders</b></code>
        <code>(retrieves all folders from the database)</code>
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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>GET</code>
        <code><b>/folders/id/{id}</b></code>
        <code>(retrieves a specific folders from the database)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the folder to retrieve |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/id/1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>GET</code>
        <code><b>/folders/user/{id}</b></code>
        <code>(retrieves all folders owned by a specific user from the database)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the user to find folders for |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/user/1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>GET</code>
        <code><b>/folders/posts/{id}</b></code>
        <code>(posts for a specific folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the folder |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/posts/1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>GET</code>
        <code><b>/folders/users/{id}</b></code>
        <code>(retrieves all users with access to a specific folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the folder |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/users/1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/add</b></code>
        <code>(adds a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `name` |  required | string | The name of the folder |
> | `ownerId` |  required | int | The id of the folder owner |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/add' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'name=Folder Name' \
> --data-urlencode 'ownerId=1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/update</b></code>
        <code>(updates a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the folder |
> | `name` |  required | string | The name of the folder |
> | `ownerId` |  required | int | The id of the folder owner |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/update' \
> --data-urlencode 'id=1' \
> --data-urlencode 'name=Folder Name' \
> --data-urlencode 'ownerId=1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/delete</b></code>
        <code>(deletes a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `id` |  required | int | The id of the folder |

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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/delete' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'id=1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/posts/add</b></code>
        <code>(adds a post to a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `folderId` |  required | int | The id of the folder |
> | `postId` |  required | int | The id of the post |


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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/posts/add' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'folderId=1' \
> --data-urlencode 'postId=1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/posts/delete</b></code>
        <code>(removes a post from a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `folderId` |  required | int | The id of the folder |
> | `postId` |  required | int | The id of the post |


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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/posts/delete' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'folderId=1' \
> --data-urlencode 'postId=1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/users/add</b></code>
        <code>(adds a user to a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `folderId` |  required | int | The id of the folder |
> | `userId` |  required | int | The id of the user |
> | `access` |  required | int | The level of access for the user |


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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/users/add' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'folderId=1' \
> --data-urlencode 'userId=1' \
> --data-urlencode 'access=3'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/users/update</b></code>
        <code>(updates a user's access to a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `folderId` |  required | int | The id of the folder |
> | `userId` |  required | int | The id of the user |
> | `access` |  required | int | The level of access for the user |


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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/users/update' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'folderId=1' \
> --data-urlencode 'userId=1' \
> --data-urlencode 'access=1'
> ```
</details>

<br>
<br>

</details>


<details>
    <summary>
        <code>POST</code>
        <code><b>/folders/users/delete</b></code>
        <code>(removes a user from a folder)</code>
    </summary>

##### Parameters

> | name | type | data type | description |
> |---|---|---|---|
> | `folderId` |  required | int | The id of the folder |
> | `userId` |  required | int | The id of the user |


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
> curl --location 'https://poste-388415.uc.r.appspot.com/folders/users/delete' \
> --header 'Content-Type: application/x-www-form-urlencoded' \
> --data-urlencode 'folderId=1' \
> --data-urlencode 'userId=1'
> ```
</details>

<br>
<br>

</details>

------------------------------------------------------------------------------------------
