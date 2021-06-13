import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import Store from '../Components/Store';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function StoreScreen({ route, navigation }) {
    const { userId, storeId,registered } = route.params;
    const [products, setProducts] = useState([]);
    // const [client,setClient] =useState(null);
    useEffect(() => {
        var client= new W3CWebSocket('ws://localhost:4567/Store/currentStore');
        client.onerror = function () {
            console.log('Connection Error');
        };

        client.onopen = function () {
            console.log('WebSocket Client Connected /Store/currentStore');
            client.send(JSON.stringify({ "type": "GET_PRODUCTS", "storeId": storeId }));

        };

        client.onclose = function () {
            console.log('echo-protocol Client Closed');
        };


        client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);
            console.log(parsedMessage);
            if (parsedMessage.type == "PRODUCTS") {
                console.log(parsedMessage.items);
                setProducts(parsedMessage.items);

            }
            else if (parsedMessage.type == "LOGOUT") {
                result = parsedMessage.result;
                if (result)
                    window.location.href = "Login";
                else
                    alert("Error Log Out");
            } else if (parsedMessage.type == "ADD_PRODUCT") {
                result = parsedMessage.result;
                console.log(result);
                if (result) {
                    alert("Product Added Successfully");
                } else {
                    alert("Error with Adding Product");
                }

            }

        };
    }, []);

    
    

    return (
        <View>
            <BannerRegister navigation={navigation} registered={registered} userId={userId} />
            <View >
                <Store userId={userId} storeId={storeId} products={products} />
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
