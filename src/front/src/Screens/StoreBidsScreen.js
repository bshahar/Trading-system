import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Bid from '../Components/Bid';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function StoreBidsScreen({ route, navigation }) {
    const { userId, storeId } = route.params;
    const [bids, setBids] = useState([]);
    useEffect(() => {
        var client = new W3CWebSocket('ws://localhost:4567/myStores/bids');
        client.onerror = function () {
            console.log('Connection Error');
        };

        client.onopen = function () {
            console.log('WebSocket Client Connected /myStores/bids');
            client.send(JSON.stringify({ "type": "GET_BIDS", "storeId": storeId,"userId":userId}));

        };

        client.onclose = function () {
            console.log('echo-protocol Client Closed');
        };


        client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);
            console.log(parsedMessage);
            if (parsedMessage.type == "GET_BIDS") {
                console.log(parsedMessage.items);
                if(parsedMessage.result){
                    setBids(parsedMessage.offers);
                }else{
                    alert(parsedMessage.message);
                    navigation.pop();
                }    
            }
        };
    }, []);




    return (
        <View>
            
            <View style={{marginTop:20}}>
                {bids.map((item) => {
                    return (
                        <View style={{ padding: 5 }}>
                            <Bid
                                userId={userId}
                                storeId={storeId}
                                offerId={item.offerId}
                                productId={item.productId}
                                productName={item.productName}
                                amount={item.amount}
                                price={item.price}
                                userName={item.userName}
                                
                                />
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

