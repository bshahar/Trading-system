import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function CartProduct({ productName, productAmount,productPrice }) {

    return (
        <View style={styles.container}>
            <Text>{productName},    </Text>
            <Text>Amount:   {productAmount} </Text>
            <Text>Price:   {productPrice} </Text>
        </View>
    );
};







const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection:'row',
        backgroundColor: '#fff',
        paddingLeft:20,
    },
});
