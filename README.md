# Trading-system

Members:
Elad Solomon,
Erez Shmueli,
Or Kandabi,
Shahar Bardugo,
Dorin Matzrafi

Trading system , where you can open stores and manage them , buy products ,show statics and more.


# Getting started
Optional argumnets to start the system.
1. load - inittial the system with optional data (the data  store in the file -"appConfigTest.json")
## How to use
    tradingSystem load



# Config file
There are some option how you can initial the system.
you should go to the file "appConfigTest.json" that store at /resource.
Then you can fill the file in json format with the command you want to execute.

# Register Command
## paramters:
1. username - The Username that you want to insert the system with.
2. password - that will attach to the user.
3. age - User age (for purchase policy).
## how to use
     "register": [
    {
      "username": "kandabior",
      "age": 20,
      "password": "or321654"
    },
    {
      "username": "elad",
      "age": 16,
      "password": "elad321654"
    }




Configuration:  
The system have two modes: one for testing and one for regular run. In order to initialize the system in test mode insert as program argument the string "test". When in test mode, the system will load the "appConfigTest.json" using the local database. Otherwise, the remote database will be used.  
  


The system also has the option of loading data automaticlly when it initializes. To do that, insert as program argument the string "load".
Then, the system will load the json file "requestedTests.json".  In this file, you can execute the following functionalities: register, login, logout, open store, add product to store, remove product from store and appiont store manager. To each function you will need to give the proper parameters, see examples to functions' names and parameters in the current json file. Once this option is active, the system will load the scenario described by the data in the json file.
