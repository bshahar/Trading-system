import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button, Touchable, TouchableOpacity } from 'react-native';
import CartProduct from './CartProduct';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function Bag({ navigation, storeId, storeName, products, userId, registered }) {
    const renderedProducts =
        <View>
            {products.map((item) => {
                return <CartProduct productName={item.productName} productAmount={item.productAmount} productPrice={item.productPrice} />
            })}
        </View>
    return (
        <View style={styles.container} >
            <View>
                <Text>Store Name: {storeName}</Text>
                {renderedProducts}
            </View>
            <View style={{ position: 'relative', alignSelf: 'center', paddingLeft: 10 }}>
                <Button title={'Purchase'} onPress={() => { navigation.navigate('PurchaseScreen', { storeId: storeId, storeName: storeName, userId: userId, products: renderedProducts,registered:registered }) }} />
            </View>


        </View>
    );
};



// var client = new W3CWebSocket('ws://localhost:4567/Cart/purchase');
//                     client.onmessage = function (event) {
//                       const parsedMessage = JSON.parse(event.data);

//                       if (parsedMessage.type == "BUY_PRODUCT") {
//                         var result = parsedMessage.result;
//                         if (result)
//                           navigation.navigate('Login');
//                         else
//                           alert("Error Log Out");
//                       }
//                     }
//                     client.onopen = function () {
//                       client.send(JSON.stringify({ "type": "LOGOUT", "id": userId }));
//                     }



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
