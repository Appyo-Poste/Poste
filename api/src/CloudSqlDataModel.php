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
    // Setting the bellow to true will drop all tables on startup
    private $debug_nuke = true;
    
    private $dsn;
    private $user;
    private $password;

    /**
     * Creates the SQL user table if it doesn't already exist.
     */
    public function __construct(PDO $pdo)
    {
        $this->pdo = $pdo;

        // -----[ Users | Start ]-----
        $userColumns = array(
            'id INT PRIMARY KEY AUTO_INCREMENT',
            'email VARCHAR(255) UNIQUE NOT NULL',
            'username VARCHAR(255) NOT NULL',
            'password VARCHAR(255) NOT NULL',
            'password_salt VARCHAR(255)',
        );

        $this->userColumnNames = array_map(function ($columnDefinition) {
            return explode(' ', $columnDefinition)[0];
        }, $userColumns);
        $userColumnsText = implode(', ', $userColumns);

        if ($debug_nuke) 
        {
            $this->pdo->query("DROP TABLE IF EXISTS users");
        }
        $this->pdo->query("CREATE TABLE IF NOT EXISTS users ($userColumnsText)");
        // -----[ Users | End ]-----


        // -----[ Posts | Start ]-----
        $postColumns = array(
            'id INT PRIMARY KEY AUTO_INCREMENT',
            'name VARCHAR(255) NOT NULL',
            'link longtext NOT NULL',
            'ownerId INT NOT NULL REFERENCES users(id)',
        );

        $this->postColumnNames = array_map(function ($columnDefinition) {
            return explode(' ', $columnDefinition)[0];
        }, $postColumns);
        $postColumnsText = implode(', ', $postColumns);

        if ($debug_nuke) 
        {
            $this->pdo->query("DROP TABLE IF EXISTS posts");
        }
        $this->pdo->query("CREATE TABLE IF NOT EXISTS posts ($postColumnsText)");
        // -----[ Posts | End ]-----


        // -----[ Folders | Start ]-----
        $folderColumns = array(
            'id INT PRIMARY KEY AUTO_INCREMENT',
            'name VARCHAR(50) NOT NULL',
            'ownerId INT NOT NULL REFERENCES users(id)',
        );

        $this->folderColumnNames = array_map(function ($columnDefinition) {
            return explode(' ', $columnDefinition)[0];
        }, $folderColumns);
        $folderColumnsText = implode(', ', $folderColumns);

        if ($debug_nuke) 
        {
            $this->pdo->query("DROP TABLE IF EXISTS folders");
        }
        $this->pdo->query("CREATE TABLE IF NOT EXISTS folders ($folderColumnsText)");
        // -----[ Folders | End ]-----


        // -----[ Posts-Folders | Start ]-----
        $postFolderColumns = array(
            'postId INT NOT NULL REFERENCES posts(id)',
            'folderId INT NOT NULL REFERENCES folders(id)',
        );

        $this->postFolderColumnNames = array_map(function ($columnDefinition) {
            return explode(' ', $columnDefinition)[0];
        }, $postFolderColumns);
        $postFolderColumnsText = implode(', ', $postFolderColumns);

        if ($debug_nuke) 
        {
            $this->pdo->query("DROP TABLE IF EXISTS posts_folders");
        }
        $this->pdo->query("CREATE TABLE IF NOT EXISTS posts_folders ($postFolderColumnsText, PRIMARY KEY (postId, folderId))");
        // -----[ Posts-Folders | End ]-----


        // -----[ Users-Folders | Start ]-----
        /**
         * Access ints:
         *      0 - No Access
         *      1 - View Access
         *      2 - Manage Access
         *      3 - Owner Access
         */
        $userFolderColumns = array(
            'userId INT NOT NULL REFERENCES users(id)',
            'folderId INT NOT NULL REFERENCES folders(id)',
            'access int NOT NULL',
        );

        $this->userFolderColumnNames = array_map(function ($columnDefinition) {
            return explode(' ', $columnDefinition)[0];
        }, $userFolderColumns);
        $userFolderColumnsText = implode(', ', $userFolderColumns);

        if ($debug_nuke) 
        {
            $this->pdo->query("DROP TABLE IF EXISTS users_folders");
        }
        $this->pdo->query("CREATE TABLE IF NOT EXISTS users_folders ($userFolderColumnsText, PRIMARY KEY (userId, folderId))");
        // -----[ Users-Folders | End ]-----




        $TokensColumns = array(
            'acccess_code VARCHAR(255) PRIMARY KEY ',
            'user_id VARCHAR(255)',
            'type VARCHAR(255)',
        );

        $this->tokensColumnNames = array_map(function ($tokensDefinition) {
            return explode(' ', $tokensDefinition)[0];
        }, $TokensColumns);
        $tokensText = implode(', ', $TokensColumns);

        $this->pdo->query("CREATE TABLE IF NOT EXISTS tokens ($tokensText)");
    }


    // -----[ Users | Start ]-----
        /**
         * /users endpoint
         */
        public function getAllUsers() 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM users';
            
            $statement = $pdo->prepare($query);
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($result, $row);
            }

            return $result;
        }

        /**
         * /users endpoint with email param
         */
        public function getUserByEmail($email) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM users WHERE email = :email';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':email', $email, PDO::PARAM_STR);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result;
        }

        /**
         * /users endpoint with id param
         */
        public function getUserById($id) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM users WHERE id = :id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':id', $id, PDO::PARAM_INT);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result;
        }

        /**
         * /users/add endpoint
         */
        public function addUser($email, $username, $password) 
        {
            $pdo = $this->pdo;
            $query = 'INSERT INTO users (email, username, password) VALUES (:email, :username, :password)';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':email', $email, PDO::PARAM_STR);
            $statement->bindValue(':username', $username, PDO::PARAM_STR);
            $statement->bindValue(':password', $password, PDO::PARAM_STR);
            $statement->execute();
            // $result = $statement->fetch(PDO::FETCH_ASSOC);

            // return $result;
            return $this->pdo->lastInsertId();
        }

        /**
         * /users/update endpoint
         */
        public function updateUser($email, $username, $password) 
        {
            $pdo = $this->pdo;
            $query = 'UPDATE users SET username=:username, password=:password WHERE email=:email';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':email', $email, PDO::PARAM_STR);
            $statement->bindValue(':username', $username, PDO::PARAM_STR);
            $statement->bindValue(':password', $password, PDO::PARAM_STR);
            // $statement->execute();
            // $result = $statement->fetch(PDO::FETCH_ASSOC);

            // return $result;
            return $statement->execute();
        }

        /**
         * /users/delete endpoint
         */
        public function deleteUser($email, $password) 
        {
            $pdo = $this->pdo;
            $query = 'DELETE FROM users WHERE email=:email AND password=:password';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':email', $email, PDO::PARAM_STR);
            $statement->bindValue(':password', $password, PDO::PARAM_STR);
            $statement->execute();
            // $result = $statement->fetch(PDO::FETCH_ASSOC);

            // return $result;
            return $statement->rowCount();
        }
    // -----[ Users | End ]-----

    // -----[ Posts | Start ]-----
        /**
         * /posts endpoint
         */
        public function getAllPosts() 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM posts';
            
            $statement = $pdo->prepare($query);
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($result, $row);
            }

            return $result;
        }

        /**
         * /posts/id/{id} endpoint 
         */
        public function getPostById($id) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM posts WHERE id = :id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':id', intval($id), PDO::PARAM_INT);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result;
        }

        /**
         * /posts/user/{id} endpoint
         */
        public function getPostsForUserId($ownerId) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM posts WHERE ownerId = :ownerId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':ownerId', intval($ownerId), PDO::PARAM_INT);
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($result, $row);
            }

            return $result;
        }

        /**
         * /posts/add endpoint
         */
        public function addPost($name, $link, $ownerId) 
        {
            $pdo = $this->pdo;
            $query = 'INSERT INTO posts (name, link, ownerId) VALUES (:name, :link, :ownerId)';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':name', $name, PDO::PARAM_STR);
            $statement->bindValue(':link', $link, PDO::PARAM_STR);
            $statement->bindValue(':ownerId', intval($ownerId), PDO::PARAM_INT);
            $statement->execute();

            return $this->pdo->lastInsertId();
        }

        /**
         * /posts/update endpoint
         */
        public function updatePost($id, $name, $link, $ownerId) 
        {
            $pdo = $this->pdo;
            $query = 'UPDATE posts SET name=:name, link=:link, ownerId=:ownerId WHERE id=:id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':name', $name, PDO::PARAM_STR);
            $statement->bindValue(':link', $link, PDO::PARAM_STR);
            $statement->bindValue(':ownerId', intval($ownerId), PDO::PARAM_INT);
            $statement->bindValue(':id', intval($id), PDO::PARAM_INT);

            return $statement->execute();
        }

        /**
         * /posts/delete endpoint
         */
        public function deletePost($id) 
        {
            $pdo = $this->pdo;
            $query = 'DELETE FROM posts WHERE id=:id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':id', intval($id), PDO::PARAM_INT);
            $statement->execute();

            return $statement->rowCount();
        }

        /**
         * no accosiated endpoint
         */
        public function getArrayOfPosts($idArray) 
        {
            if (empty($idArray)) 
            {
                return array();
            }

            $pdo = $this->pdo;
            $statmentVars = array();
            $whereClause = "";
            
            foreach ($idArray as $id) {
                $idIndex = array_search($id, $idArray);
                if ($idIndex == 0) 
                {
                    $whereClause .= "id = :id{$idIndex}";
                }
                else 
                {
                    $whereClause .= " OR id = :id{$idIndex}";
                }
            }
            
            $query = "SELECT * FROM posts WHERE {$whereClause}";
            
            $statement = $pdo->prepare($query);
            foreach ($idArray as $id) {
                $idIndex = array_search($id, $idArray);
                $statement->bindValue(":id{$idIndex}", intval($id), PDO::PARAM_INT);
            }
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($result, $row);
            }

            return $result;
        }
    // -----[ Posts | End ]-----

    // -----[ Folders | Start ]-----
        /**
         * /folders endpoint
         */
        public function getAllFolders() 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM folders';
            
            $statement = $pdo->prepare($query);
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($result, $row);
            }

            return $result;
        }

        /**
         * /folders/id/{id} endpoint 
         */
        public function getFolderById($id) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM folders WHERE id = :id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':id', intval($id), PDO::PARAM_INT);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result;
        }

        /**
         * /folders/user/{id} endpoint
         */
        public function getFoldersForUserId($ownerId) 
        {
            $pdo = $this->pdo;
            // $query = 'SELECT * FROM folders WHERE ownerId = :ownerId';
            
            // $statement = $pdo->prepare($query);
            // $statement->bindValue(':ownerId', intval($ownerId), PDO::PARAM_INT);
            // $statement->execute();

            // $result = array();
            // while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
            //     $access = $this->getUserAccessForFolder($row["id"], $ownerId);
                
            //     array_push($result, array(
            //         "id" => $row["id"],
            //         "name" => $row["name"],
            //         "ownerId" => $row["ownerId"],
            //         "access" => $access
            //     ));
            // }

            $query = 'SELECT * FROM users_folders WHERE userId = :userId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':userId', intval($ownerId), PDO::PARAM_INT);
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                $folder = $this->getFolderById($row["folderId"]);
                
                array_push($result, array(
                    "id" => $folder["id"],
                    "name" => $folder["name"],
                    "ownerId" => $folder["ownerId"],
                    "access" => $row["access"]
                ));
            }

            return $result;
        }

        /**
         * /folders/add endpoint
         */
        public function addFolder($name, $ownerId) 
        {
            $pdo = $this->pdo;
            $query = 'INSERT INTO folders (name, ownerId) VALUES (:name, :ownerId)';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':name', $name, PDO::PARAM_STR);
            $statement->bindValue(':ownerId', intval($ownerId), PDO::PARAM_INT);
            $statement->execute();

            return $this->pdo->lastInsertId();
        }

        /**
         * /folders/update endpoint
         */
        public function updateFolder($id, $name, $ownerId) 
        {
            $pdo = $this->pdo;
            $query = 'UPDATE folders SET name=:name, ownerId=:ownerId WHERE id=:id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':name', $name, PDO::PARAM_STR);
            $statement->bindValue(':ownerId', intval($ownerId), PDO::PARAM_INT);
            $statement->bindValue(':id', intval($id), PDO::PARAM_INT);

            return $statement->execute();
        }

        /**
         * /folder/delete endpoint
         */
        public function deleteFolder($id) 
        {
            $pdo = $this->pdo;
            $query = 'DELETE FROM folders WHERE id=:id';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':id', intval($id), PDO::PARAM_INT);
            $statement->execute();

            return $statement->rowCount();
        }
    // -----[ Folders | End ]-----

    // -----[ Posts-Folders | Start ]-----
        /**
         * /folders/posts/{id} endpoint 
         */
        public function getPostsInFolder($folderId) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM posts_folders WHERE folderId = :folderId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->execute();

            $postIds = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($postIds, $row);
            }

            return $this->getArrayOfPosts($postIds);
        }

        /**
         * /folders/posts/add endpoint
         */
        public function addPostToFolder($folderId, $postId) 
        {
            $pdo = $this->pdo;
            $query = 'INSERT INTO posts_folders (postId, folderId) VALUES (:postId, :folderId)';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':postId', intval($postId), PDO::PARAM_INT);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result;
        }

        /**
         * /folders/posts/delete endpoint
         */
        public function deletePostFromFolder($folderId, $postId) 
        {
            $pdo = $this->pdo;
            $query = 'DELETE FROM posts_folders WHERE folderId=:folderId AND postId=:postId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':postId', intval($postId), PDO::PARAM_INT);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->execute();

            return $statement->rowCount();
        }
    // -----[ Posts-Folders | End ]-----

    // -----[ Users-Folders | Start ]-----
        private function getUserAccessForFolder($folderId, $userId) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT access FROM users_folders WHERE folderId = :folderId and userId = :userId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->bindValue(':userId', intval($userId), PDO::PARAM_INT);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result["access"];
        }

        /**
         * /folders/users/{id} endpoint 
         */
        public function getUsersInFolder($folderId) 
        {
            $pdo = $this->pdo;
            $query = 'SELECT * FROM users_folders WHERE folderId = :folderId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->execute();

            $result = array();
            while ($row = $statement->fetch(PDO::FETCH_ASSOC)) {
                array_push($result, $row);
            }

            return $result;
        }

        /**
         * /folders/users/add endpoint
         */
        public function addUserToFolder($folderId, $userId, $access) 
        {
            $pdo = $this->pdo;
            $query = 'INSERT INTO users_folders (userId, folderId, access) VALUES (:userId, :folderId, :access)';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':userId', intval($userId), PDO::PARAM_INT);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->bindValue(':access', intval($access), PDO::PARAM_INT);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            return $result;
        }

        /**
         * /folders/users/update endpoint
         */
        public function updateUserInFolder($folderId, $userId, $access) 
        {
            $pdo = $this->pdo;
            $query = 'UPDATE users_folders SET access=:access WHERE folderId=:folderId AND userId=:userId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':userId', intval($userId), PDO::PARAM_INT);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->bindValue(':access', intval($access), PDO::PARAM_INT);

            return $statement->execute();
        }

        /**
         * /folders/users/delete endpoint
         */
        public function deleteUserFromFolder($folderId, $userId) 
        {
            $pdo = $this->pdo;
            $query = 'DELETE FROM users_folders WHERE folderId=:folderId AND userId=:userId';
            
            $statement = $pdo->prepare($query);
            $statement->bindValue(':userId', intval($userId), PDO::PARAM_INT);
            $statement->bindValue(':folderId', intval($folderId), PDO::PARAM_INT);
            $statement->execute();

            return $statement->rowCount();
        }
    // -----[ Users-Folders | End ]-----

















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
