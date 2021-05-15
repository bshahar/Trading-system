import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button, Touchable, TouchableOpacity } from 'react-native';
import CartProduct from './CartProduct';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function Receipt({ storeName, lines, totalCost}) {
    
    return (
        <View style={styles.container} >
            <View>
                <Text>Store Name: {storeName}</Text>
                {lines.map((item)=>{
                    // setTotal(total+(item.price*item.amount));
                    return (
                    <View style={{flexDirection:'row'}}>
                        <Text>{item.prodName}  </Text>
                        <Text>{item.price} NIS     </Text>
                        <Text>{item.amount} units    </Text>
                        <Text>:     {item.price*item.amount}</Text>
                    </View>
                )})}
                <Text>---------------------------</Text>
                <Text>Total Price: {totalCost} NIS</Text>

            
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
