<?php

/*
 * Copyright 2018 Google LLC All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace Google\Cloud\Samples\Bookshelf;

/*
 * Adds all the controllers to Slim PHP $app.
 */
use Psr\Http\Message\ServerRequestInterface as Request;
use Psr\Http\Message\ResponseInterface as Response;
use Google\Cloud\Storage\Bucket;

$container = $app->getContainer();

function console_log($output, $with_script_tags = true) {
    $js_code = 'console.log(' . json_encode($output, JSON_PRETTY_PRINT) . 
');';
    if ($with_script_tags) {
        $js_code = '<script>' . $js_code . '</script>';
    }
    echo $js_code;
}

/**
 * Base URL - Forward to /users
 */
$app->get('/', function (Request $request, Response $response) use ($container) {
    return $response
        ->withHeader('Location', '/users')
        ->withStatus(302);
})->setName('home');


// -----[ Users | Start ]-----

/**
 * GET /users
 */
$app->get('/users', function (Request $request, Response $response) use ($container) {
    $data = $container->get('cloudsql')->getAllUsers();

    $response->getBody()->write(json_encode(array(
        "code" => 200,
        "message" => "Successfully retreived all users",
        "result" => $data
    )));
    return $response
        ->withStatus(200, "Successfully retreived all users");
});

/**
 * GET /users/id/{id}
 */
$app->get('/users/id/{id}', function (Request $request, Response $response, $args) use ($container) {
    $missingProperties = array();
    if (!array_key_exists("id", $args)) { array_push($missingProperties, "id"); }
    
    if (!empty($missingProperties)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Missing the following properties: " . implode(", ", $missingProperties),
            "result" => "{}"
        )));
        
        return $response
            ->withStatus(400, "Missing the following properties: " . implode(", ", $missingProperties));
    }
    
    $data = $container->get('cloudsql')->getUserById($args["id"]);
    
    $response->getBody()->write(json_encode(array(
        "code" => 200,
        "message" => "Successfully retreived user with id " . $args["id"],
        "result" => $data
    )));
    return $response
        ->withStatus(200, "Successfully retreived user with id " . $args["id"]);
});

/**
 * GET /users/email/{email}
 */
$app->get('/users/email/{email}', function (Request $request, Response $response, $args) use ($container) {
    $missingProperties = array();
    if (!array_key_exists("email", $args)) { array_push($missingProperties, "email"); }
    
    if (!empty($missingProperties)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Missing the following properties: " . implode(", ", $missingProperties),
            "result" => "{}"
        )));
        
        return $response
            ->withStatus(400, "Missing the following properties: " . implode(", ", $missingProperties));
    }
    
    $data = $container->get('cloudsql')->getUserByEmail($args["email"]);
    
    $response->getBody()->write(json_encode(array(
        "code" => 200,
        "message" => "Successfully retreived user with email " . $args["email"],
        "result" => $data
    )));
    return $response
        ->withStatus(200, "Successfully retreived user with email " . $args["email"]);
});

/**
 * GET /users/login/{email}/{password}
 */
$app->get('/users/login/{email}/{password}', function (Request $request, Response $response, $args) use ($container) {
    $missingProperties = array();
    if (!array_key_exists("email", $args)) { array_push($missingProperties, "email"); }
    if (!array_key_exists("password", $args)) { array_push($missingProperties, "password"); }
    
    if (!empty($missingProperties)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Missing the following properties: " . implode(", ", $missingProperties),
            "result" => "{}"
        )));
        
        return $response
            ->withStatus(400, "Missing the following properties: " . implode(", ", $missingProperties));
    }
    
    $data = $container->get('cloudsql')->getUserByEmail($args["email"]);
    
    if ($data == false) {
        $response->getBody()->write(json_encode(array(
            "code" => 200,
            "message" => "Requets OK",
            "result" => array(
                "success" => true,
                "message" => "No user with email '" . $args["email"] . "' exisits"
            )
        )));
        
        return $response
            ->withStatus(200, "No user with email '" . $args["email"] . "' exisits");
    }

    if ($data["password"] != $args["password"]) {
        $response->getBody()->write(json_encode(array(
            "code" => 200,
            "message" => "Requets OK",
            "result" => array(
                "success" => false,
                "message" => "Incorrect password provided for user '" . $args["email"] . "'"
            )
        )));
        
        return $response
            ->withStatus(200, "Incorrect password provided for user '" . $args["email"] . "'");
    }

    $response->getBody()->write(json_encode(array(
        "code" => 200,
        "message" => "Requets OK",
        "result" => array(
            "success" => true,
            "message" => "Login for user '" . $args["email"] . "' succeeded"
        )
    )));
    return $response
        ->withStatus(200, "Login for user '" . $args["email"] . "' succeeded");
});

/**
 * POST /users/add
 */
$app->post('/users/add', function (Request $request, Response $response) use ($container) {
    $body = $request->getParsedBody();

    if (empty($body)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Cannot process empty body",
            "result" => "{}"
        )));
        return $response->withStatus(400, "Cannot process empty body");
    }

    $missingProperties = array();
    if (!array_key_exists("email", $body)) { array_push($missingProperties, "email"); }
    if (!array_key_exists("username", $body)) { array_push($missingProperties, "username"); }
    if (!array_key_exists("password", $body)) { array_push($missingProperties, "password"); }
    
    if (!empty($missingProperties)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Missing the following properties: " . implode(", ", $missingProperties),
            "result" => "{}"
        )));
        
        return $response
            ->withStatus(400, "Missing the following properties: " . implode(", ", $missingProperties));
    }
    
    $duplicate = $container->get('cloudsql')->getUserByEmail($body["email"]);
    if ($duplicate != false) {
        $response->getBody()->write(json_encode(array(
            "code" => 200,
            "message" => "Requets OK",
            "result" => array(
                "success" => false,
                "message" => "Email already in use",
                "newUserId" => ""
            )
        )));
        
        return $response
            ->withStatus(200, "Email already in use");
    }

    $data = $container->get('cloudsql')->addUser($body["email"], $body["username"], $body["password"]);

    $response->getBody()->write(json_encode(array(
        "code" => 201,
        "message" => "New user created",
        "result" => array(
            "success" => true,
            "message" => "Successfully created new user",
            "newUserId" => $data
        )
    )));
    return $response
        ->withStatus(201);
});

/**
 * POST /users/update
 */
$app->post('/users/update', function (Request $request, Response $response) use ($container) {
    $body = $request->getParsedBody();

    if (empty($body)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Cannot process empty body",
            "result" => "{}"
        )));
        return $response->withStatus(400, "Cannot process empty body");
    }

    $missingProperties = array();
    if (!array_key_exists("email", $body)) { array_push($missingProperties, "email"); }
    if (!array_key_exists("username", $body)) { array_push($missingProperties, "username"); }
    if (!array_key_exists("password", $body)) { array_push($missingProperties, "password"); }
    
    if (!empty($missingProperties)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Missing the following properties: " . implode(", ", $missingProperties),
            "result" => "{}"
        )));
        
        return $response
            ->withStatus(400, "Missing the following properties: " . implode(", ", $missingProperties));
    }

    $data = $container->get('cloudsql')->updateUser($body["email"], $body["username"], $body["password"]);

    $response->getBody()->write(json_encode(array(
        "code" => 201,
        "message" => "User updated",
        "result" => array(
            "success" => true,
            "message" => "Successfully updated user"
        )
    )));
    return $response
        ->withStatus(201);
});

/**
 * POST /users/delete
 */
$app->post('/users/delete', function (Request $request, Response $response) use ($container) {
    $body = $request->getParsedBody();

    if (empty($body)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Cannot process empty body",
            "result" => "{}"
        )));
        return $response->withStatus(400, "Cannot process empty body");
    }

    $missingProperties = array();
    if (!array_key_exists("email", $body)) { array_push($missingProperties, "email"); }
    if (!array_key_exists("password", $body)) { array_push($missingProperties, "password"); }
    
    if (!empty($missingProperties)) {
        $response->getBody()->write(json_encode(array(
            "code" => 400,
            "message" => "Missing the following properties: " . implode(", ", $missingProperties),
            "result" => "{}"
        )));
        
        return $response
            ->withStatus(400, "Missing the following properties: " . implode(", ", $missingProperties));
    }

    $data = $container->get('cloudsql')->deleteUser($body["email"], $body["password"]);

    $response->getBody()->write(json_encode(array(
        "code" => 201,
        "message" => "User deleted",
        "result" => array(
            "success" => true,
            "message" => "Successfully deleted user"
        )
    )));
    return $response
        ->withStatus(201);
});

// -----[ Users | End ]-----



$app->post('/twitter/auth', function (Request $request, Response $response) use ($container) {
    $body = $request->getParsedBody();
    $response->getBody()->write(implode(" ", $body));
    return $response;
});

$app->get('/twitter/auth', function (Request $request, Response $response) use ($container) {
    $body = $request->getParsedBody();
    
    $code = $_GET['code'];
    $state = $_GET['state'];

    $token['acccess_code'] = $code;
    $token['user_id'] = $state;
    $token['type'] = "Twitter";
   
/*
    foreach ($_GET as &$value){
        echo $value . "<br>";
    }
*/
    //$id = $container->get('cloudsql')->create($token, "tokens");
    $response->getBody()->write($code);
        return $response;
});

$app->post('/users/login', function (Request $request, Response $response, $args) use ($container) {
    if(isset($_POST['username']) && isset($_POST['password']))
     {
		// Innitialize Variable
	   	$username = $_POST['username'];
        $password = $_POST['password'];
        $result = $container->get('cloudsql')->login($username, $password);
        $response->getBody()->write($result);
        return $response;
     }
});

$app->get('/users/login', function (Request $request, Response $response, $args) use ($container) {
    if(isset($_GET['username']) && isset($_GET['password']))
     {
		// Innitialize Variable
	   	$username = urldecode($_GET['username']);
        $password = urldecode($_GET['password']);
        //console_log($username . "\n" . $password);
        //console_log($container->get('cloudsql')->login($username, $password));
        $result = $container->get('cloudsql')->login($username, $password);
     }
     $response->getBody()->write($result);
    return $response;
});

/**
 * GET /{file} - Load a static asset.
 *
 * THIS MUST BE PLACED AT THE VERY BOTTOM OF YOUR SLIM APPLICATION, JUST BEFORE
 * $app->run()!!!
 */
$app->get('/.well-known/assetlinks.json', function (Request $request, Response $response, $args) {

    $newResponse = $response->withHeader('Content-Type', "application/json" . '; charset=UTF-8');

    $newResponse->getBody()->write('[{
        "relation": ["delegate_permission/common.handle_all_urls"],
        "target" : { "namespace": "android_app", "package_name": "com.example.poste",
                     "sha256_cert_fingerprints": ["11:15:AD:57:E1:57:F6:6D:33:A8:65:77:08:10:86:42:0B:02:3B:65:86:2A:F1:D4:2A:99:16:D0:AF:20:A3:13"] }
      }]');

    return $newResponse;
});