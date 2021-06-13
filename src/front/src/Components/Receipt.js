import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button, Touchable, TouchableOpacity } from 'react-native';
import CartProduct from './CartProduct';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function Receipt({ userName, storeName, lines, totalCost, showCancel, receiptId,navigation }) {

    return (
        <View style={styles.container} >
            <View>
                <Text>Store Name: {storeName}</Text>
                <Text>User Name: {userName}</Text>
                {lines.map((item) => {
                    // setTotal(total+(item.price*item.amount));
                    return (
                        <View style={{ flexDirection: 'row' }}>
                            <Text>{item.prodName}  </Text>
                            <Text>{item.price} NIS     </Text>
                            <Text>{item.amount} units    </Text>
                            <Text>:     {item.price * item.amount}</Text>
                        </View>
                    )
                })}
                <Text>---------------------------</Text>
                <Text>Total Price: {totalCost} NIS</Text>


            </View>
            <View  style={{position:'absolute', right:5, alignSelf:'center'}}>
                {showCancel ? <View>
                    <Button title={'Cancel Purchase'} color={'red'} onPress={() => {
                        var client = new W3CWebSocket('ws://localhost:4567/myPurchases');
                        client.onerror = function () {
                            console.log('Connection Error');
                        };

                        client.onopen = function (event) {
                            client.send(JSON.stringify({ "type": "CANCEL_PURCHASE",  "receiptId": receiptId }));
                        }


                        client.onclose = function () {
                            console.log('echo-protocol Client Closed');
                        };

                        client.onmessage = function (e) {
                            const parsedMessage = JSON.parse(e.data);
                            console.log(parsedMessage);
                            if (parsedMessage.type == "CANCEL_PURCHASE") {
                                if (parsedMessage.result) {
                                    // console.log(parsedMessage.data)
                                    alert(parsedMessage.message);
                                    navigation.pop();
                                } else {
                                    alert(parsedMessage.message);
                                }


                            }

                        };

                    }} />
                </View> : <View />}
            </View>


        </View>
    );
};

const styles = StyleSheet.create({
    container: {

        backgroundColor: '#fff',
        flexDirection: 'row',
        borderWidth: 1,
        borderColor: 'blue',
        borderRadius: 5,
        padding: 5

    },

});
