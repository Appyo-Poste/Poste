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

$app->get('/users', function (Request $request, Response $response) use ($container) {
    $token = (int) $request->getUri()->getQuery('page_token');
    $userList = $container->get('cloudsql')->listUsers(10, $token);

    console_log($userList);

    foreach ($userList as &$value){
        echo $value . "<br>";
    }

})->setName('users');

$app->post('/users/add', function (Request $request, Response $response) use ($container) {
    $user = $request->getParsedBody();

    $id = $container->get('cloudsql')->create($user, "users");

    $response->getBody()->write($id);
        return $response;
});

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