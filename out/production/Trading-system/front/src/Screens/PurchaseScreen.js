import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button, Touchable, TouchableOpacity } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function PurchaseScreen({ route, navigation }) {
    const { storeId, products, storeName, userId,registered } = route.params;
    const [CreditNum,setCreditNum]= useState("");
    const [CreditExp,setCreditExp]= useState("");
    const [CVV,setCVV]= useState("");
    return (
        <View style={styles.container} >
            <View>
                <Text>Purchase for Store: {storeName}</Text>
                <View style={{ borderWidth: 1 }}>
                    {products}
                </View>

                <Text>Please Insert Credit Info: </Text>
                <TextInput value={CreditNum} style={{ borderWidth: 1, padding: 5 }} placeholder={'Credit card number'} onChangeText={(text)=>setCreditNum(text)} />
                <View style={{ flexDirection: 'row' }}>
                    <TextInput value={CreditExp} style={{ borderWidth: 1, padding: 5 }} placeholder={'Exp Date'} onChangeText={(text)=>setCreditExp(text)}/>
                    <TextInput value={CVV} style={{ borderWidth: 1, padding: 5 }} placeholder={'CVV'} onChangeText={(text)=>setCVV(text)}/>
                </View>
                <Button title='Buy' onPress={() => {
                    var client = new W3CWebSocket('wss://localhost:4567/Cart/purchase');
                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);
                        console.log(parsedMessage);
                        if (parsedMessage.type == "BUY_PRODUCT") {
                            var result = parsedMessage.result;
                            if (result){
                                alert(parsedMessage.message)
                                navigation.navigate('Home',{userId:userId,registered:registered});
                            }
                            else{
                                alert(parsedMessage.message);
                            }
                                
                        }
                    }
                    client.onopen = function () {
                        client.send(JSON.stringify({ "type": "BUY_PRODUCT", "userId": userId, "storeId": storeId,"creditInfo":CreditNum }));
                    }
                }} />

            </View>
            <View style={{ flexDirection: 'row', position: 'absolute', right: 0, padding: 5 }}>
                <Button title='Back' color={'red'} onPress={() => { navigation.pop() }} />
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
        flex: 1,
        backgroundColor: '#fff',
        flexDirection: 'row',
        padding: 5

    },

});
