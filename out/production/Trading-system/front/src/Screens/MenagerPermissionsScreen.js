import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, CheckBox, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import OpenStore from '../Components/OpenStore';

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function MenagerPermissionsScreen({ route, navigation }) {
    const { userName, userId, storeId, registered } = route.params;
    // const [permissions, setPermissions] = useState([]);
    const [addProduct, setAddProduct] = useState(false);
    const [addManager, setAddManager] = useState(false);
    const [removeManager, setRemoveManager] = useState(false);
    const [addOwner, setAddOwner] = useState(false);
    const [removeOwner, setRemoveOwner] = useState(false);
    const [editProduct, setEditProduct] = useState(false);
    const [workersInfo, setWorkersInfo] = useState(false);
    const [storePurchases, setStorePurchases] = useState(false);


    useEffect(() => {
        var client = new W3CWebSocket('wss://localhost:4567/myStores/UpdatePermissions');
        client.onerror = function () {
            console.log('Connection Error');
        };

        client.onopen = function (event) {
            client.send(JSON.stringify({ "type": "GET_PERMISSIONS", "userId": userId, "managerName": userName, "storeId": storeId }));
        }


        client.onclose = function () {
            console.log('echo-protocol Client Closed');
        };

        
        client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);
            if (parsedMessage.result) {
                if (parsedMessage.type == "GET_PERMISSIONS") {
                    console.log(parsedMessage.data);
                    const permissions = parsedMessage.data;
                    for (let i = 0; i < permissions.length; i++) {
                        console.log(permissions[i].permission);
                        switch (permissions[i].permission) {
                            case "AddProduct":
                                setAddProduct(permissions[i].allowed);
                                break;
                            case "AppointManager":
                                setAddManager(permissions[i].allowed);
                                break;
                            case "RemoveManagerAppointmen":
                                setRemoveManager(permissions[i].allowed);
                                break;
                            case "AppointOwner":
                                setAddOwner(permissions[i].allowed);
                                break;
                            case "RemoveOwnerAppointmen":
                                setRemoveOwner(permissions[i].allowed);
                                break;
                            case "EditProduct":
                                setEditProduct(permissions[i].allowed);
                                break;
                            case "ViewPurchaseHistory":
                                setStorePurchases(permissions[i].allowed);
                                break;
                            case "GetWorkersInfo":
                                setWorkersInfo(permissions[i].allowed);
                                break;

                        }
                    }
                }
            } else {
                alert(parsedMessage.message);
            }
        };
    }, [])

    return (
        <View >
            <BannerRegister navigation={navigation} registered={registered} userId={userId} />
            <Text>store screen</Text>
            <Text>user id: {userId}</Text>
            <Text>permissions for menager {userName}</Text>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={addProduct} onValueChange={setAddProduct} />
                <Text style={{ padding: 5 }}>Add Product Pemission</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={editProduct} onValueChange={setEditProduct} />
                <Text style={{ padding: 5 }}>Edit Product Pemission</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={storePurchases} onValueChange={setStorePurchases} />
                <Text style={{ padding: 5 }}>View Store's Purchase History Pemission</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={addManager} onValueChange={setAddManager} />
                <Text style={{ padding: 5 }}>Appoint Manager Pemissions</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={removeManager} onValueChange={setRemoveManager} />
                <Text style={{ padding: 5 }}>Remove Manager Appointment  Pemissions</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={addOwner} onValueChange={setAddOwner} />
                <Text style={{ padding: 5 }}>Appoint Owner Pemissions</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={removeOwner} onValueChange={setRemoveOwner} />
                <Text style={{ padding: 5 }}>Remove Owner Appointment  Pemissions</Text>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <CheckBox style={{ alignSelf: 'center' }} value={workersInfo} onValueChange={setWorkersInfo} />
                <Text style={{ padding: 5 }}>Access To Workers Information</Text>
            </View>
            <View style={{ padding:5 }}>
                <Button title={'Update Permissions'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/myStores/UpdatePermissions`);
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);

                        if (parsedMessage.type == "UPDATE_PERMISSIONS") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert("permissions updated successfully");
                                navigation.pop();
                            }
                            else {
                                alert(parsedMessage.message);
                            }
                        }
                    }

                    client.onopen = function () {
                        client.send(JSON.stringify({
                            "type": "UPDATE_PERMISSIONS",
                            "ownerId": userId,
                            "userName": userName,
                            "storeId": storeId,
                            "permissions": [
                                { "per": "AddProduct", "isAllowed":addProduct },
                                { "per": "AppointManager", "isAllowed":addManager },
                                { "per": "RemoveManagerAppointmen", "isAllowed":removeManager },
                                { "per": "AppointOwner","isAllowed": addOwner },
                                { "per": "RemoveManagerAppointmen", "isAllowed":removeOwner },
                                { "per": "EditProduct","isAllowed": editProduct },
                                { "per": "ViewPurchaseHistory", "isAllowed":storePurchases },
                                { "per": "GetWorkersInfo", "isAllowed":workersInfo },
                            ]
                        }));
                    }

                }} />
            </View>


        </View>




    );
};






const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        backgroundColor: '#fff',

    },
    storeList: {
        alignSelf: 'flex-start',
        flex: '1',
        left: 0,
        padding: 5,
    }
});
