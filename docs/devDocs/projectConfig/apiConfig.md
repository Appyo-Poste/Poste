# Developer Documentation - API Config
*[Click here to return to table of contents](../../home.md)*

We used Google Cloud for our API deployment but you may use any service of your choice.

### Step 1 - Create a Project
Follow [this guide](https://cloud.google.com/resource-manager/docs/creating-managing-projects) by google to create a new project, I recommend naming it "Poste"

### Step 2 - Enable Access to API
Go to this [link](https://console.cloud.google.com/apis/enableflow?apiid=cloudbuild.googleapis.com&_ga=2.24214778.1347067486.1685475824-239984206.1685475824) and select your project that was made in step 1 and follow the prompts to enable API access.

### Step 3 - Initialize The GCloud CLI
Run the following command to initialize the CLI:
```
gcloud init
```

### Step 4 - Install Composer
We need to install composer globally. Composer is a PHP dependency management tool.
Run the following commands in the CLI:
```
php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"
php -r "if (hash_file('sha384', 'composer-setup.php') === '55ce33d7678c5a611085589f1f3ddf8b3c52d662cd01d4ba75c0ee0459970c2200a51f492d557530c71c15d8dba01eae') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"
php composer-setup.php
php -r "unlink('composer-setup.php');"
sudo mv composer.phar /usr/local/bin/composer
```

### Step 5 - Download and Upload The Code
From this repo, download it as a zip file.
Once downloaded unzip the contents into a folder named `poste`
In the Google Cloud terminal, click the 3 dot icon and select upload, check the folder option and upload the `api` folder located inside the poste folder we just made.

### Step 6 - Install Composer
In the Google Cloud terminal run the following command to install composer:
```
cd api && composer install
```

### Step 7 - Configure Cloud SQL
In the Google Cloud terminal run the following commands to create and configure a Cloud SQL instance:
*Replace "passw0rd!" with a secure password*
```
gcloud sql instances create poste --tier=db-n1-standard-2 --region=us-central1
gcloud sql users set-password root --host=% --instance=poste --password=passw0rd!
gcloud sql databases create poste-data --instance=poste
``` 
This will create a Cloud SQL instance named `poste`, set the root password, and create a new database named `poste-data`

Use the following command to find your connection name:
```
gcloud sql instances describe poste | grep connectionName
```

Update the `app.yaml` file with the new password and connection name.

### Step 8 - Configure Cloud Storage Bucket
In the Google Cloud terminal run the following command to create a Cloud Storage Bucket:
```
gsutil mb -l us-central1 gs://poste-picture-storage/
```

### Step 9 - Deploying The App
In the Google Cloud terminal run the following command to deploy the app:
```
cd ~/api && gcloud app deploy
```
When prompted enter `y`

### Step 10 - Update Code
In the code in the API class located in the com.example.poste.api.poste package update the string on line 45 to be using the new URL.
<img src="https://i.imgur.com/1xcvILI.png">
