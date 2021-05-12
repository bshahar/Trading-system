import React, { useState, useEffect } from 'react';
import { Modal, StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function BannerRegister({ navigation, userId, registered }) {
  const props = {
    userId: userId,
    registered: registered,
  }

  const logoutButton =
    <View style={{ padding: 5 }}>
      <Button color={'red'} title='Logout' onPress={() => {
        var client = new W3CWebSocket(`wss://localhost:4567/Main?userId=${userId}`);
        client.onmessage = function (event) {
          const parsedMessage = JSON.parse(event.data);

          if (parsedMessage.type == "LOGOUT") {
            var result = parsedMessage.result;
            if (result)
              navigation.navigate('Login');
            else
              alert("Error Log Out");
          }
        }
        client.onopen = function () {
          client.send(JSON.stringify({ "type": "LOGOUT", "id": userId }));
        }
      }} />
    </View>


  const registerButton =
    <View style={{ flexDirection: 'row' }}>
      <View style={{ padding: 5 }}>
        <Button color={'red'} title='Register' onPress={() => { navigation.navigate('Register', { back: true, userId: userId }) }} />
      </View>
      <View style={{ padding: 5 }}>
        <Button color={'grey'} title={'EXIT'} onPress={() => { navigation.navigate('Login') }} />
      </View>
    </View>

  return (
    <View style={styles.container}>
      <View style={styles.container}>
        <View style={{ padding: 5 }}>
          <Button title='Home' onPress={() => navigation.navigate('Home', props)} />
        </View>
        <View style={{ padding: 5 }}>
          <Button title='My Stores' onPress={() => navigation.navigate('MyStores', props)} />
        </View>
        <View style={{ padding: 5 }}>
          <Button title='Cart' onPress={() => navigation.navigate('MyCart', props)} />
        </View>
        <View style={{ padding: 5 }}>
          <Button title='Purchases' onPress={() => navigation.navigate('MyPurchases', props)} />
        </View>
        <View style={{ padding: 5 }}>
          <Button title='Search' onPress={() => navigation.navigate('SearchProductScreen', props)} />
        </View>






      </View>

      <View style={{ flexDirection: 'row', position: 'absolute', right: 0 }}>
        <Text>{registered}</Text>
        {registered == "true" ? logoutButton : registerButton}
      </View>


    </View>


  );
};






const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    backgroundColor: '#fff',

  },
});
