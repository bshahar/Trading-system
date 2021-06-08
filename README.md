# Trading-system

Members:
Elad Solomon,
Erez Shmueli,
Or Kandabi,
Shahar Bardugo,
Dorin Matzrafi

Trading system- where you can open stores and manage them, buy products, show statics and more!


# Getting started
Optional argumnets to start the system
1. load - initialize the system with data (the data is stored in the file -"requestedTests.json")
2. test - initialize the system in test mode.
Explenation on each mode later in this document.
## How to use
    tradingSystem test load



# Config file
The system has two modes: one for testing and one for regular run. In order to initialize the system in test mode insert as program argument the string "test". When in test mode, the system will load the "appConfigTest.json" using the local database. Otherwise, the system will load the file "appConfig.json", and remote database will be used. The configuration files are located at /resources. Please note that you wrote the correct database name & password for the system to be able to run.

# Register Command
## parameters:
1. username - the user name you wish to log in to the system with.
2. password - that will attach to that user.
3. age - user age (for purchase policies).
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






The system also has the option of loading data automaticlly when it initializes. To do that, insert as program argument the string "load".
Then, the system will load the json file "requestedTests.json".  In this file, you can execute the following functionalities: RegisterAndLoginUser, register, login, logout, open store, add product to store, remove product from store and appiont store manager. To each function you will need to give the proper parameters, see examples to functions' names and parameters in the current json file. Once this option is active, the system will load the scenario described by the data in the json file.

Permission numbers:


"addProduct" - 1
"appointManager" - 2
"appointOwner" - 3
"closeStore" - 4
"defineDiscountFormat" - 5
"defineDiscountPolicy" - 6
"definePurchaseFormat" - 7
"definePurchasePolicy" - 8
"editDiscountFormat" - 9
"editDiscountPolicy" - 10
"editProduct" - 11
"editPurchaseFormat" - 12
"disableEeditPurchasePolicy" - 13
"getWorkersInfo" - 14
"openStore" - 15
"removeManagerAppointment" - 16
"removeOwnerAppointment" - 17
"removeProduct" - 19
"reopenStore" - 20
"replayMessages" - 21
"viewMessages" - 21
"viewPurchaseHistory" - 22
"responedToOffer" - 23


