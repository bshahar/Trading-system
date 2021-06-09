import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Receipt from '../Components/Receipt';
import BannerRegister from '../Components/BannerRegister';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function StorePurchaseHistoryScreen({ route, navigation }) {
    const { userId, storeId, storeName } = route.params;
    const [purchases, setPurchases] = useState([]);
    useEffect(() => {
        var client = new W3CWebSocket('ws://localhost:4567/myStores/StorePermissions/action');
        client.onerror = function () {
            console.log('Connection Error');
        };

        client.onopen = function (event) {
            client.send(JSON.stringify({ "type": "GET_PURCHASES", "userId": userId, "storeId": storeId }));
        };

        client.onclose = function () {
            console.log('echo-protocol Client Closed');
        };

        client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);
            console.log(parsedMessage);
            if (parsedMessage.type == "GET_PURCHASES") {
                if (parsedMessage.result) {
                    setPurchases(parsedMessage.data);
                } else {
                    alert(parsedMessage.message);
                }
            }
        };
    }, [])

    return (
        <View style={{ padding: 5 }}>
            
            <Text>Purchases for store: {storeName}</Text>
            <View style={{paddingTop: 20 }}>
                {purchases.map((item) => {
                    return (
                        <View style={{ padding: 5 }}>
                            <Receipt userName={item.userName} storeName={item.storeName} lines={item.lines} totalCost={item.totalCost} />
                        </View>
                    )
                })}


            </View>
            <View style={{ flexDirection: 'row', position: 'absolute', right: 0, padding: 5 }}>
                <Button title='Back' color={'red'} onPress={() => { navigation.pop() }} />
            </View>



        </View>
    );
};






const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
});
