import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function Product({ product, userId, storeId }) {
  const [amount, setAmount] = useState('');
  const [price, setPrice] = useState('');
  var client = new W3CWebSocket('ws://localhost:4567/Store/currentStore');

  client.onmessage = function (e) {
    const parsedMessage = JSON.parse(e.data);
    console.log(parsedMessage);
    if (parsedMessage.type == "PRODUCTS") {
      console.log(parsedMessage.items);
      setProducts(parsedMessage.items);

    }
    else if (parsedMessage.type == "LOGOUT") {
      const result = parsedMessage.result;
      if (result)
        window.location.href = "Login";
      else
        alert("Error Log Out");
    } else if (parsedMessage.type == "ADD_PRODUCT") {
      const result = parsedMessage.result;
      console.log(result);
      if (result) {
        alert("Product Added Successfully");
      } else {
        alert("Error with Adding Product");
      }

    }else if(parsedMessage.type == "ADD_PRODUCT"){
      alert(parsedMessage.message);
    }

  };

  const addProduct = (productId, amount, userId, storeId) => {
    client.send(JSON.stringify({ "type": "ADD_PRODUCT", "productId": productId, "amount": amount, "userId": userId, "storeId": storeId }));
  }

  const setBid = (productId, amount,price, userId, storeId) => {
    client.send(JSON.stringify({ "type": "SET_OFFER", "productId": productId, "amount": amount,"price":price, "userId": userId, "storeId": storeId }));
  }

  return (

    <View style={{ padding: 5, flexDirection: 'row', borderColor: 'grey', borderWidth: 1, borderRadius: 3,flex:1 }}>
      <View style={{ padding: 5, alignSelf: 'center' }}>
        <Text>{product.productName}, </Text>
      </View>

      <View style={{ padding: 5, alignSelf: 'center' }}>
        <Text>Price: {product.price}, </Text>
      </View>

      <View style={{ padding: 5, alignSelf: 'center' }}>
        <Text>In stock: {product.amount}:  </Text>
      </View>
      <View style={{ padding: 5, alignSelf: 'center' }}>
        <TextInput
          style={{ borderColor: 'black', borderRadius: 3, borderWidth: 1, height: 25, width: 25 }}
          value={amount}
          onChangeText={(text) => { setAmount(text) }} />
      </View>

      <View style={{ padding: 5, alignSelf: 'center' }}>
        <Button title={'Add to Cart'} onPress={() => { setAmount(""); addProduct(product.productId, amount, userId, storeId) }} />
      </View>

      <View style={{ padding: 15, alignSelf: 'center' }}>
        <Text style={{ fontSize: 40 }}>|</Text>
      </View>
      <View style={{ flexDirection: 'column' }}>
        <View style={{ padding: 5, paddingLeft: 10, alignSelf: 'center' }}>
          <Text style={{ fontSize: 15, color: 'grey' }}>Or maybe set a bid</Text>
        </View>
        <View style={{ flexDirection: 'row' }}>
          <View style={{padding:5,alignSelf:'center'}}>
            <Text style={{ fontSize: 10, color: 'red' }}>price per product:</Text>
          </View>
          <View style={{ padding: 5, alignSelf: 'center' }}>
            <TextInput
              style={{ borderColor: 'black', borderRadius: 3, borderWidth: 1, height: 25, width: 25 }}
              value={price}
              onChangeText={(text) => { setPrice(text) }} />
          </View>

          <View style={{ padding: 5, alignSelf: 'center' }}>
            <Button title={'Set Bid'} onPress={() => { setAmount(""); setPrice(""); setBid(product.productId, amount, price, userId, storeId) }} />
          </View>
        </View>
      </View>
    </View>


  );
};



const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    backgroundColor: '#fff',
    alignItems: 'center'

  },
});
