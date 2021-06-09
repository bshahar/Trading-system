import React,{useState} from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import {Feather} from '@expo/vector-icons'
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function EditProductComponent({ userId, storeId, productName, productId, price, amount,navigation }) {
    const [newPrice, setProductPrice] = useState(price);
    const [newAmount, setAmount] = useState(amount);

    return (
        <View style={{padding:5, flexDirection: 'row' ,alignItems:'center' }}>
            <Feather name='trash-2' color="red" size={25} onPress={()=>{
                var client = new W3CWebSocket(`ws://localhost:4567/myStores/StorePermissions/action`);
                client.onmessage = function (event) {
                    const parsedMessage = JSON.parse(event.data);

                    if (parsedMessage.type == "REMOVE_PRODUCT") {
                        var result = parsedMessage.result;
                        if (result) {
                            alert(`Product has been removed`);
                            navigation.pop();
                            
                        }
                        else {
                            alert(parsedMessage.message);
                        }

                    }
                }
                client.onopen = function () {
                    client.send(JSON.stringify({
                        "type": "REMOVE_PRODUCT",
                        "userId": userId,
                        "storeId": storeId,
                        "productId":productId,
                     
                    }));
                }
            }}/>
            <Text>  {productName}:     Price: </Text>
            <TextInput style={{ width: 30, height: 30, borderWidth: 1, borderRadius:2 }} value={newPrice} onChangeText={(text) => setProductPrice(text)} placeholder={'Price'} />
            <Text>      Amount: </Text>
            <TextInput style={{ width: 30, height: 30, borderWidth: 1, borderRadius:2 }} value={newAmount} onChangeText={(text) => setAmount(text)} placeholder={'Amount'} />
            <Text>      </Text>
            <Button title={'Edit'} onPress={() => {
                var client = new W3CWebSocket(`ws://localhost:4567/myStores/StorePermissions/action`);
                client.onmessage = function (event) {
                    const parsedMessage = JSON.parse(event.data);

                    if (parsedMessage.type == "EDIT_PRODUCT") {
                        var result = parsedMessage.result;
                        if (result) {
                            alert(`Product has been edited`);
                            
                        }
                        else {
                            alert(parsedMessage.message);
                        }

                    }
                }
                client.onopen = function () {
                    client.send(JSON.stringify({
                        "type": "EDIT_PRODUCT",
                        "userId": userId,
                        "storeId": storeId,
                        "productId":productId,
                        "price":newPrice,
                        "amount":newAmount,
                    }));
                }
            }} />
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
