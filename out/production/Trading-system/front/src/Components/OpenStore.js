import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function OpenStore({userId,navigation}) {
    const [storeName,setStoreName] = useState("");

    return (
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>

            <View style={{ padding: 5, alignItems: 'center' }}>
                <TextInput value={storeName} placeholder='Enter Store Name' style={{ borderWidth: 1, alignSelf: 'center' }} onChangeText={(text) => { setStoreName(text) }} />
            </View>
            <View style={{ padding: 5 }}>
                <Button title={'Open Store'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/myStores/openStore`);
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);

                        if (parsedMessage.type == "OPEN_STORE") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(`"${storeName}" has been opened successfuly`);
                                setStoreName("");
                                navigation.navigate("Home",{userId:userId,registered:"true",reload:true})
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        }
                    }
                    client.onopen = function () {
                        client.send(JSON.stringify({
                            "type": "OPEN_STORE",
                            "userId": userId,
                            "storeName": storeName

                        }));
                    }
                }} />
            </View>

        </View>
    );
}
