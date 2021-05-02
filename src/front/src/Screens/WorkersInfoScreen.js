import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function WorkersIfoScreen({ route, navigation }) {
    const { userId, storeId, storeName } = route.params;
    const [users, setUsers] = useState([]);
    useEffect(() => {
        var client = new W3CWebSocket('wss://localhost:4567/myStores/StorePermissions/action');

        client.onerror = function () {
            console.log('Connection Error');
        };

        client.onopen = function (event) {
            client.send(JSON.stringify({ "type": "GET_WORKERS", "userId": userId, "storeId": storeId }));
        }


        client.onclose = function () {
            console.log('echo-protocol Client Closed');
        };

        client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);
            if (parsedMessage.result) {
                if (parsedMessage.type == "GET_WORKERS") {
                    console.log(parsedMessage.users);
                    setUsers(parsedMessage.data);
                }
            } else {
                alert(parsedMessage.message);
                navigation.pop();
            }
        };
    }, [])


    return (
        <View>

            <Text>Workers for Store: "{storeName}"</Text>
            <View style={{ padding: 5, paddingTop: 20 }}>
                <View style={{ backgroundColor: '#fff', borderColor: 'blue', borderRadius: 5, borderWidth: 1 }}>
                    {users.map((item) => {
                        return (
                            <View style={{ padding: 5 }}>
                                <Text>User id: {item.userId},   userName: {item.userName}.</Text>
                            </View>
                        )
                    })}
                </View>
            </View>
            <View style={{ flexDirection: 'row', position: 'absolute', right: 0, padding: 5 }}>
                <Button title='Back' color={'red'} onPress={() => { navigation.pop() }} />
            </View>

        </View>
    );
}
