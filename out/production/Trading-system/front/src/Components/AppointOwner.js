import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function AppointOwner({ storeName, userId, storeId }) {
    const [userNameOwner, setUserNameOwner] = useState("");

    return (
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>

            <View style={{ padding: 5, alignItems: 'center' }}>
                <TextInput value={userNameOwner} placeholder='Enter User Name' style={{ borderWidth: 1, alignSelf: 'center' }} onChangeText={(text) => { setUserNameOwner(text) }} />
            </View>
            <View style={{ padding: 5 }}>
                <Button title={'Add Owner'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/myStores/StorePermissions/action`);
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);

                        if (parsedMessage.type == "ADD_OWNER") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(`${userNameOwner} is now owner of store: "${storeName}"`);
                                setUserNameOwner("");
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        }
                    }
                    client.onopen = function () {
                        client.send(JSON.stringify({
                            "type": "ADD_OWNER",
                            "ownerId": userId,
                            "userName": userNameOwner,
                            "storeId": storeId

                        }));
                    }
                }} />
            </View>

            <View style={{ padding: 5 }}>
                <Button title={'Remove Owner'} color={'red'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/myStores/StorePermissions/action`);
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);

                        if (parsedMessage.type == "REMOVE_OWNER") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(`${userNameOwner} has been removed from store: "${storeName}"`);
                                setUserNameOwner("");
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        }
                    }
                    client.onopen = function () {
                        client.send(JSON.stringify({
                            "type": "REMOVE_OWNER",
                            "ownerId": userId,
                            "userName": userNameOwner,
                            "storeId": storeId

                        }));
                    }
                }} />
            </View>

        </View>
    );
}
