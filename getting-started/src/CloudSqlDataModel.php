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

namespace Google\Cloud\Samples\AppEngine\GettingStarted;

use PDO;

/**
 * Class CloudSql is a wrapper for making calls to a Cloud SQL MySQL database.
 */
class CloudSqlDataModel
{
    private $dsn;
    private $user;
    private $password;

    /**
     * Creates the SQL user table if it doesn't already exist.
     */
    public function __construct(PDO $pdo)
    {
        $this->pdo = $pdo;

        $userColumns = array(
            'id serial PRIMARY KEY ',
            'nickname VARCHAR(255)',
            'username VARCHAR(255)',
            'password VARCHAR(255)',
            'password_salt VARCHAR(255)',
        );

        $TokensColumns = array(
            'acccess_code VARCHAR(255) PRIMARY KEY ',
            'user_id VARCHAR(255)',
            'type VARCHAR(255)',
        );

        $this->userColumnNames = array_map(function ($columnDefinition) {
            return explode(' ', $columnDefinition)[0];
        }, $userColumns);
        $columnText = implode(', ', $userColumns);

        $this->pdo->query("CREATE TABLE IF NOT EXISTS users ($columnText)");

        
        $this->tokensColumnNames = array_map(function ($tokensDefinition) {
            return explode(' ', $tokensDefinition)[0];
        }, $TokensColumns);
        $tokensText = implode(', ', $TokensColumns);

        $this->pdo->query("CREATE TABLE IF NOT EXISTS tokens ($tokensText)");
    }

    /**
     * Throws an exception if $book contains an invalid key.
     *
     * @param $user array
     *
     * @throws \Exception
     */
    private function verifyUser($user)
    {
        if ($invalid = array_diff_key($user, array_flip($this->userColumnNames))) {
            throw new \Exception(sprintf(
                'unsupported user properties: "%s"',
                implode(', ', $invalid)
            ));
        }
    }

    public function listUsers($limit = 10, $cursor = 0)
    {
        $pdo = $this->pdo;
        $query = 'SELECT * FROM users WHERE id > :cursor ORDER BY id LIMIT :limit';
        $statement = $pdo->prepare($query);
        $statement->bindValue(':cursor', $cursor, PDO::PARAM_INT);
        $statement->bindValue(':limit', $limit, PDO::PARAM_INT);
        $statement->execute();
        // Uncomment this while loop to output the results
        // while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
        //     var_dump($row);
        // }
        $rows = array();
        $nextCursor = null;
        while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
            array_push($rows, $row);
            if (count($rows) == $limit) {
                $nextCursor = $row['id'];
                break;
            }
        }

        return ['users' => $rows, 'cursor' => $nextCursor];
    }

    public function create($object, $table = "users")
    {
        //$this->verifyUser($user);

        $names = array_keys($object);
        $placeHolders = array_map(function ($key) {
            return ":$key";
        }, $names);
        $pdo = $this->pdo;
        $sql = sprintf(
            'INSERT INTO ' . $table . '(%s) VALUES (%s)',
            implode(', ', $names),
            implode(', ', $placeHolders)
        );

        $statement = $pdo->prepare($sql);
        $statement->execute($object);
        return $this->pdo->lastInsertId();
    }

    public function login($username, $password)
    {
        $pdo = $this->pdo;
        // [START gae_php_app_cloudsql_query]
        $statement = $pdo->prepare('SELECT * FROM users WHERE username = :username AND password = :password');
        $statement->bindValue('username', $username, PDO::PARAM_STR);
        $statement->bindValue('password', $password, PDO::PARAM_STR);
        $statement->execute();
        $result = $statement->fetch(PDO::FETCH_ASSOC);
        if($result['username'] == $username)
          {
			 $result="true";	
          }  
          else
          {
			  	$result="false";
          }
        // [END gae_php_app_cloudsql_query]
        return $result;
    }

    public function update($book)
    {
        $this->verifyBook($book);
        $assignments = array_map(
            function ($column) {
                return "$column=:$column";
            },
            $this->columnNames
        );
        $assignmentString = implode(',', $assignments);
        $sql = "UPDATE books SET $assignmentString WHERE id = :id";
        $statement = $this->pdo->prepare($sql);
        $values = array_merge(
            array_fill_keys($this->columnNames, null),
            $book
        );
        return $statement->execute($values);
    }

    public function delete($id)
    {
        $statement = $this->pdo->prepare('DELETE FROM books WHERE id = :id');
        $statement->bindValue('id', $id, PDO::PARAM_INT);
        $statement->execute();

        return $statement->rowCount();
    }
}
