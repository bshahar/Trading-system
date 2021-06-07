# Trading-system

Members:
Elad Solomon,
Erez Shmueli,
Or Kandabi,
Shahar Bardugo,
Dorin Matzrafi

# Corona-API

This project include python service that allowed you to send request and recive answer in json format.
Using Jenkins and Docker.
The service will run on localhost:8080


## Get Status
Returns a value of success / fail to contact the backend API.
### Request
    curl localhost:8080/status

### Response

    {“status”: “success”}

  

Configuration:  
The system have two modes: one for testing and one for regular run. In order to initialize the system in test mode insert as program argument the string "test". When in test mode, the system will load the "appConfigTest.json" using the local database. Otherwise, the remote database will be used.  
  


The system also has the option of loading data automaticlly when it initializes. To do that, insert as program argument the string "load".
Then, the system will load the json file "requestedTests.json".  In this file, you can execute the following functionalities: register, login, logout, open store, add product to store, remove product from store and appiont store manager. To each function you will need to give the proper parameters, see examples to functions' names and parameters in the current json file. Once this option is active, the system will load the scenario described by the data in the json file.
