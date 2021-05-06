import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import EditProductComponent from '../Components/EditProductComponent'
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function RemoveProductScreen({ route, navigation }) {
    const { userId, storeId } = route.params;
    const [products, setProducts] = useState([]);
    useEffect(() => {
        var client = new W3CWebSocket('wss://localhost:4567/Store/currentStore');
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
        };
    }, []);




    return (
        <View>
            
            <View style={{marginTop:20}}>
                {products.map((item) => {
                    return (
                        <View style={{ padding: 5 }}>

                            <EditProductComponent
                                productId={item.productId}
                                productName={item.productName}
                                price={item.price}
                                amount={item.amount}
                                userId={userId}
                                storeId={storeId}
                                navigation={navigation} />
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
