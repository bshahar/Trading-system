import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import { Feather } from '@expo/vector-icons'
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function Bid({ storeId, userId, productId, productName, offerId, price, amount, userName }) {
    const [counterPrice, setCounterPrice] = useState("");
    const [show, setShow] = useState("true");

    const approve = () => {
        var client = new W3CWebSocket(`ws://localhost:4567/myStores/bids`);
        client.onmessage = function (event) {
            const parsedMessage = JSON.parse(event.data);
            if (parsedMessage.type == "APPROVE_OFFER") {
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
                "type": "APPROVE_OFFER",
                "userId": userId,
                "storeId": storeId,
                "productId": productId,
                "offerId": offerId,
            }));
            setShow("false");

        }
    };

    const decline = () => {
        var client = new W3CWebSocket(`ws://localhost:4567/myStores/bids`);
        client.onmessage = function (event) {
            const parsedMessage = JSON.parse(event.data);
            if (parsedMessage.type == "DECLINE_OFFER") {
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
                "type": "DECLINE_OFFER",
                "userId": userId,
                "storeId": storeId,
                "productId": productId,
                "offerId": offerId,
            }));
        }
    };

    const counterOffer = () => {
        var client = new W3CWebSocket(`ws://localhost:4567/myStores/bids`);
        client.onmessage = function (event) {
            const parsedMessage = JSON.parse(event.data);
            if (parsedMessage.type == "COUNTER_OFFER") {
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
                "type": "COUNTER_OFFER",
                "userId": userId,
                "storeId": storeId,
                "productId": productId,
                "offerId": offerId,
                "price": counterPrice
            }));
        }
    };

    if (show == "true") {
        return (
            <View style={{ borderRadius: 3, borderColor: 'grey', borderWidth: 1 }}>
                <View style={{ padding: 5, flexDirection: 'row', alignItems: 'center' }}>
                    <View style={{ padding: 5 }}>
                        <Text>user: {userName},</Text>
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
                    <View style={{ padding: 5 }}>
                        <TextInput
                            style={{ borderColor: 'black', borderRadius: 3, borderWidth: 1, height: 25, width: 25 }}
                            value={counterPrice} onChangeText={(text) => setCounterPrice(text)} />
                    </View>
                    <View style={{ padding: 5 }}>
                        <Button title={'Counter Offer'} onPress={counterOffer} />
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
