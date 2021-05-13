import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function AppointManager({storeName,userId,storeId}) {
    const [userNameManager, setUserNameManager] = useState("");

    return (
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>

            <View style={{ padding: 5, alignItems: 'center' }}>
                <TextInput value={userNameManager} placeholder='Enter User Name' style={{ borderWidth: 1, alignSelf: 'center' }} onChangeText={(text) => { setUserNameManager(text) }} />
            </View>
            <View style={{ padding: 5 }}>
                <Button title={'Add manager'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/myStores/StorePermissions/action`);
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);

                        if (parsedMessage.type == "ADD_MANAGER") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(`${userNameManager} is now manager of store: "${storeName}"`);
                                setUserNameManager("");
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        }
                    }
                    client.onopen = function () {
                        client.send(JSON.stringify({
                            "type": "ADD_MANAGER",
                            "ownerId": userId,
                            "userName": userNameManager,
                            "storeId": storeId

                        }));
                    }
                }} />
            </View>

            <View style={{ padding: 5 }}>
                <Button title={'Remove manager'} color={'red'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/myStores/StorePermissions/action`);
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);

                        if (parsedMessage.type == "REMOVE_MANAGER") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(`${userNameManager} has been removed from store: "${storeName}"`);
                                setUserNameManager("");
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        }
                    }
                    client.onopen = function () {
                        client.send(JSON.stringify({
                            "type": "REMOVE_MANAGER",
                            "ownerId": userId,
                            "userName": userNameManager,
                            "storeId": storeId

                        }));
                    }
                }} />
            </View>

        </View>
    );
}
