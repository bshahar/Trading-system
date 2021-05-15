import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Receipt from '../Components/Receipt';
import BannerRegister from '../Components/BannerRegister';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function MyPurchasesScreen({ route, navigation }) {
  const { userId, registered } = route.params;
  const [purchases, setPurchases] = useState([]);
  useEffect(() => {
    var client = new W3CWebSocket('wss://localhost:4567/myPurchases');
    client.onerror = function () {
      console.log('Connection Error');
    };

    client.onopen = function (event) {
      client.send(JSON.stringify({ "type": "GET_PURCHASES", "userId": userId }));
    }


    client.onclose = function () {
      console.log('echo-protocol Client Closed');
    };

    client.onmessage = function (e) {
      const parsedMessage = JSON.parse(e.data);
      console.log(parsedMessage);
      if (parsedMessage.type == "GET_PURCHASES") {
        if (parsedMessage.result) {
          // console.log(parsedMessage.data)
            setPurchases(parsedMessage.data);
        } else {
          alert(parsedMessage.message);
        }


      }

    };
  }, [])

  return (
    <View >
      <BannerRegister navigation={navigation} registered={registered} userId={userId} />
      <Text>My Purchases screen</Text>
      <Text>user id: {userId}</Text>
      {purchases.map((item) => {
        return (
          <View style={{ padding: 5 }}>
            <Receipt storeName={item.storeName} lines={item.lines} totalCost={item.totalCost} />
          </View>
        )
      })}

     

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
