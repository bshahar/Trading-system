import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function Product({ product, userId, storeId }) {
  const [amount, setAmount] = useState('');
  var client = new W3CWebSocket('wss://localhost:4567/Store/currentStore');

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

    }

  };

  const addProduct = (productId, amount, userId, storeId) => {
    client.send(JSON.stringify({ "type": "ADD_PRODUCT", "productId": productId, "amount": amount, "userId": userId, "storeId": storeId }));
  }

  return (
    <View style={styles.container}>
      <Text>{product.productName}, </Text>
      <Text>Price: {product.price}, </Text>
      <Text>In stock: {product.amount}:  </Text>
      <TextInput
        style={{ borderColor: 'black', borderWidth: 1, height: 25, width: 25 }}
        placeholder={amount}
        value={amount}
        onChangeText={(text) => { setAmount(text) }} />
      <Text>  </Text>

      <Button title={'Add to Cart'} onPress={() => { setAmount(""); addProduct(product.productId, amount, userId, storeId) }} />

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
