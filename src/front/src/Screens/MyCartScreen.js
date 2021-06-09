import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Bag from '../Components/Bag';
import BannerRegister from '../Components/BannerRegister';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function MyCartScreen({ route, navigation }) {
  const { userId,registered } = route.params;
  const [bags, setBags] = useState([]);

  useEffect(() => {
    var client = new W3CWebSocket('ws://localhost:4567/Cart');
    client.onerror = function () {
      console.log('Connection Error');
    };

    client.onopen = function (event) {
      client.send(JSON.stringify({ "type": "GET_CART", "id": userId }));
    }


    client.onclose = function () {
      console.log('echo-protocol Client Closed');
    };

    client.onmessage = function (e) {
      const parsedMessage = JSON.parse(e.data);
      console.log(parsedMessage);
      if (parsedMessage.type == "GET_CART") {
        setBags(parsedMessage.bags);
      }

    };
  }, [])

  return (
    <View >
      <BannerRegister navigation={navigation} registered={registered} userId={userId} />
      <Text>Cart screen</Text>
      <Text>user id: {userId}</Text>
      {bags.map((item) => {
        return (
        <View style={{padding:5}}>
          <Bag navigation={navigation} registered={registered} userId={userId}storeId={item.storeId} storeName={item.storeName} products={item.products} />
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
