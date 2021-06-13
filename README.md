# Trading-system

Members:
Elad Solomon,
Erez Shmueli,
Or Kandabi,
Shahar Bardugo,
Dorin Matzrafi

Trading system- where you can open stores and manage them, sale & buy products, show statics and more!


# Getting started
Optional argumnets to start the system:
1. test - initialize the system in test mode.
2. load - initialize the system with data in it (the data is stored in the file -"requestedTests.json").  
Explenation on each mode later in this document.
## How to use
    tradingSystem test load_FILENAME.json




# Configuration file
The system has two modes: one for testing and one for regular run. In order to initialize the system in test mode insert as program argument the string "test". When in test mode, the system will load the "appConfigTest.json" using the local database. Otherwise, the system will load the file "appConfig.json", and remote database will be used. The configuration files are located at /resources.  
Please note that you wrote the correct database name & password for the system to be able to run properly.


# Load data
The system has the option of loading data automaticlly when it initializes. To do that, insert as program argument the string "load_FILENAME.json". Then, the system will load the json file "FILENAME.json". In this file, you can execute the following functionalities: register & login (seperatly or combined), logout, open store, add product to store, remove product from store, appiont store manager and appiont store owner. To each function you will need to give the proper parameters, see examples to functions' names and parameters below. Once this option is active, the system will load the scenario described by the data in the json file.

## Register
### parameters:
1. username - the user name you wish to log in to the system with.
2. age - user age (for purchase policies).
3. password - that will attach to that user.
### How to use
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
        },
        {
          "username": "erez",
          "age": 26,
          "password": "erez321654"
        }
      ]

## Login
### parameters:
1. username - the user name you wish to log in to the system with.
2. password - user's matching password.
### How to use
     "login": [
        {
          "username": "kandabior",
          "password": "or321654"
        }
      ]

## Register & login combined
### parameters:
Same for Login function.
### How to use
     "RegisterAndLoginUser": [
        {
          "username": "dorin",
          "age": 26,
          "password": "dorin321654"
        },
        {
          "username": "shahar",
          "age": 25,
          "password": "shahar321654"
        }
     ]


## Logout
### parameters:
1. username - user name you wish to logout from.
### How to use
     "logout" : ["kandabior"]

## Open store
### parameters:
1. userOwnerName - the owner user name in the system.
2. storeName - name of the store to be shown in the system.
### How to use
    "openStore": [
      {
        "userOwnerName": "elad",
        "storeName": "storeNameTest1"
      },
      {
        "userOwnerName": "elad",
        "storeName": "storeNameTest2"
      }
    ]

## Add product to a store
### parameters:
1. storeOwnerName - the owner user name in the system.
2. storeName - name of the store as it is in the system.
3. name - prduct name.
4. categories - list of categories the product is associated to.
5. price
6. description - optional description of the product.
7. quantity
### How to use
    "addProduct": [
      {
        "storeOwnerName": "elad",
        "storeName": "storeNameTest1",
        "name": "Bamba",
        "categories": [
          "food"
        ],
        "price": 20,
        "description": "snack",
        "quantity": 100
      }
    ]

## Remove product from a store
### parameters:
1. managerUserName - the owner/ manager user name in the system.
2. storeName - name of the store as it is in the system.
3. prodName - prduct name.
### How to use
    "removeProduct" : [
      {
        "managerUserName" : "elad",
        "storeName" : "storeNameTest1",
        "prodName" : "Corn"
      }
    ]

## Add store manager
### parameters:
1. appointerUserName - the store owner user name in the system.
2. appointeeUserName - the user name of the appointee.
3. storeName - name of the store as it is in the system.
### How to use
    "addStoreManager": [
      {
        "appointerUserName": "elad",
        "appointeeUserName": "kandabior",
        "storeName": "storeNameTest1"
      }
    ]
    
## Add store Owner
### parameters:
1. appointerUserName - the store owner user name in the system.
2. appointeeUserName - the user name of the appointee.
3. storeName - name of the store as it is in the system.
### How to use
    "addStoreOwner": [
      {
        "appointerUserName": "elad",
        "appointeeUserName": "erez",
        "storeName": "storeNameTest1"
      }
    ]




  
  
# Now you're ready to use our Trading System! 


