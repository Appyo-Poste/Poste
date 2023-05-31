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

$app->get('/', function (Request $request, Response $response) use ($container) {
    return $response
        ->withHeader('Location', '/books')
        ->withStatus(302);
})->setName('home');

$app->get('/books', function (Request $request, Response $response) use ($container) {
    $token = (int) $request->getUri()->getQuery('page_token');
    $bookList = $container->get('cloudsql')->listUsers(10, $token);

    console_log($bookList);

    return $container->get('view')->render($response, 'list.html.twig', [
        'users' => $bookList['users'],
        'next_page_token' => $bookList['cursor'],
    ]);
})->setName('books');

function console_log($output, $with_script_tags = true) {
    $js_code = 'console.log(' . json_encode($output, JSON_PRETTY_PRINT) . 
');';
    if ($with_script_tags) {
        $js_code = '<script>' . $js_code . '</script>';
    }
    echo $js_code;
}

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

$app->get('/books/{id}', function (Request $request, Response $response, $args) use ($container) {
    $book = $container->get('cloudsql')->read($args['id']);
    if (!$book) {
        return $response->withStatus(404);
    }
    return $container->get('view')->render($response, 'view.html.twig', ['book' => $book]);
});

$app->get('/books/{id}/edit', function (Request $request, Response $response, $args) use ($container) {
    $book = $container->get('cloudsql')->read($args['id']);
    if (!$book) {
        return $response->withStatus(404);
    }

    return $container->get('view')->render($response, 'form.html.twig', [
        'action' => 'Edit',
        'book' => $book,
    ]);
});

$app->post('/books/{id}/edit', function (Request $request, Response $response, $args) use ($container) {
    if (!$container->get('cloudsql')->read($args['id'])) {
        return $response->withStatus(404);
    }
    $book = $request->getParsedBody();
    $book['id'] = $args['id'];
    $files = $request->getUploadedFiles();
    if ($files['image']->getSize()) {
        $image = $files['image'];
        $bucket = $container->get('bucket');
        $imageStream = $image->getStream();
        $imageContentType = $image->getClientMediaType();
        // [START gae_php_app_upload_image]
        // Set your own image file path and content type below to upload an
        // image to Cloud Storage.
        // $imageStream = fopen('/path/to/your_image.jpg', 'r');
        // $imageContentType = 'image/jpg';
        $object = $bucket->upload($imageStream, [
            'metadata' => ['contentType' => $imageContentType],
            'predefinedAcl' => 'publicRead',
        ]);
        $imageUrl = $object->info()['mediaLink'];
        // [END gae_php_app_upload_image]
        $book['image_url'] = $imageUrl;
    }
    if ($container->get('cloudsql')->update($book)) {
        return $response
            ->withHeader('Location', "/books/$args[id]")
            ->withStatus(302);
    }

    $response->getBody()->write('Could not update book');
    return $response;
});

$app->post('/books/{id}/delete', function (Request $request, Response $response, $args) use ($container) {
    $book = $container->get('cloudsql')->read($args['id']);
    if ($book) {
        $container->get('cloudsql')->delete($args['id']);
        if (!empty($book['image_url'])) {
            $objectName = parse_url(basename($book['image_url']), PHP_URL_PATH);
            $bucket = $container->get('bucket');
            // get bucket name from image
            // [START gae_php_app_delete_image]
            $object = $bucket->object($objectName);
            $object->delete();
            // [END gae_php_app_delete_image]
        }
        return $response
            ->withHeader('Location', '/books')
            ->withStatus(302);
    }

    return $response->withStatus(404);
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