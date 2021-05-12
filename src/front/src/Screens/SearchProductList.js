import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import Store from '../Components/Store';
import Product from '../Components/Product'
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function SearchProductList({ route, navigation }) {
    const { products, registered, userId } = route.params;
    console.log(products);
    return (
        <View>
            <BannerRegister navigation={navigation} registered={registered} userId={userId} />
            <Text>user id: {userId}</Text>
            {products.map((item) => { return (<Product product={item} userId={userId} storeId={item.storeId} />) })}
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
