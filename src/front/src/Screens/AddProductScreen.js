import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Receipt from '../Components/Receipt';
import BannerRegister from '../Components/BannerRegister';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function AddProductScreen({ route, navigation }) {
  const { userId, storeId, storeName } = route.params;
  const [prodName, setProdName] = useState("");
  const [categories, setCategories] = useState("");
  const [price, setPrice] = useState("");
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");

  return (
    <View style={{ padding: 5, flex: 1 }}>
      <View style={{ flexDirection: 'row', position: 'absolute', right: 0 }}>
        <Button title='Cancel' color='red' onPress={() => navigation.pop()} />
      </View>
      <Text>Adding product to store: "{storeName}"</Text>
      <Text>user id: {userId}</Text>
      <View style={{ padding: 5 }}>
        <TextInput value={prodName} onChangeText={(text) => setProdName(text)} style={{ borderWidth: 1, padding: 5 }} placeholder="Set Product Name" />
      </View>

      <View style={{ padding: 5 }}>
        <TextInput value={categories} onChangeText={(text) => setCategories(text)} style={{ borderWidth: 1, padding: 5 }} placeholder="Set Product Categories" />
        <Text style={{ fontSize:10,color:'red' }}>Categories must be saperated by ","</Text>
      </View>


      <View style={{ padding: 5 }}>
        <TextInput value={price} onChangeText={(text) => setPrice(text)} style={{ borderWidth: 1, padding: 5 }} placeholder="Set Product Price" />
      </View>

      <View style={{ padding: 5 }}>
        <TextInput value={description} onChangeText={(text) => setDescription(text)} style={{ borderWidth: 1, padding: 5 }} placeholder="Set Product Description" />
      </View>

      <View style={{ padding: 5 }}>
        <TextInput value={amount} onChangeText={(text) => setAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder="Set Product Amount" />
      </View>

      <View style={{ padding: 5 }}>
        <Button title='Add Product' onPress={() => {
          var client = new W3CWebSocket(`ws://localhost:4567/myStores/StorePermissions/action`);
          client.onmessage = function (event) {
            const parsedMessage = JSON.parse(event.data);

            if (parsedMessage.type == "ADD_PRODUCT") {
              var result = parsedMessage.result;
              if (result) {
                alert(`Product added successfully to store ${storeName}`);
                navigation.pop()
              }
              else {
                alert(parsedMessage.message);
              }

            }
          }
          client.onopen = function () {
            client.send(JSON.stringify({
              "type": "ADD_PRODUCT",
              "userId": userId,
              "storeId": storeId,
              "prodName": prodName,
              "categories": categories,
              "price": price,
              "description": description,
              "amount": amount
            }));
          }
        }} />
      </View>



    </View>
  );
};






const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    backgroundColor: '#fff',

  },
  permissinList: {
    alignSelf: 'flex-start',
    flex: '1',
    left: 0,
    padding: 5,
  }
});
