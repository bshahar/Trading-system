import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import { Feather } from '@expo/vector-icons'
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function CounterBid({ storeId, userId, productId, productName, offerId, price, amount, storeName }) {
    const [show, setShow] = useState("true");

    const approve = () => {
        var client = new W3CWebSocket(`ws://localhost:4567/myStores/bids`);
        client.onmessage = function (event) {
            const parsedMessage = JSON.parse(event.data);
            if (parsedMessage.type == "APPROVE_COUNTER_OFFER") {
                var result = parsedMessage.result;
                if (result) {
                    alert(parsedMessage.message);
                    setShow("false");
                }
                else {
                    alert(parsedMessage.message);
                }
            }
        }
        client.onopen = function () {
            client.send(JSON.stringify({
                "type": "APPROVE_COUNTER_OFFER",
                "userId": userId,
                "storeId": storeId,
                "productId": productId,
                "offerId": offerId,
            }));
        }
    };

    const decline = () => {
        var client = new W3CWebSocket(`ws://localhost:4567/myStores/bids`);
        client.onmessage = function (event) {
            const parsedMessage = JSON.parse(event.data);
            if (parsedMessage.type == "DECLINE_COUNTER_OFFER") {
                var result = parsedMessage.result;
                if (result) {
                    alert(parsedMessage.message);
                    setShow("false");
                }
                else {
                    alert(parsedMessage.message);
                }
            }
        }
        client.onopen = function () {
            client.send(JSON.stringify({
                "type": "DECLINE_COUNTER_OFFER",
                "userId": userId,
                "storeId": storeId,
                "productId": productId,
                "offerId": offerId,
            }));
        }
    };


    if (show == "true") {
        return (
            <View style={{ borderRadius: 3, borderColor: 'grey', borderWidth: 1 }}>
                <View style={{ padding: 5, flexDirection: 'row', alignItems: 'center' }}>
                    <View style={{ padding: 5 }}>
                        <Text>store: {storeName},</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <Text>product: {productName},</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <Text>amount: {amount},</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <Text>offered price: {price}:</Text>
                    </View>
                </View>

                <View style={{ padding: 5, flexDirection: 'row', alignItems: 'center' }}>
                    <View style={{ padding: 5 }}>
                        <Button title={'Approve'} color={'green'} onPress={approve}/>
                    </View>
                    <View style={{ padding: 5 }}>
                        <Button title={'Decline'} color={'red'} onPress={decline}/>
                    </View>
                </View>
            </View>

        );
    }else {
        return <View></View>
    }

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
