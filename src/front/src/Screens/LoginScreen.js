import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button, Touchable, TouchableOpacity } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

var W3CWebSocket = require('websocket').w3cwebsocket;
var client = new W3CWebSocket('ws://localhost:4567/Login');

export default function LoginScreen({ route, navigation }) {
  const { userId, back } = route.params;
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');

  client.onerror = function () {
    console.log('Connection Error');
  };

  client.onopen = function () {
    console.log('WebSocket Client Connected login screen');
  };

  client.onclose = function () {
    console.log('echo-protocol Client Closed');
  };

  client.onmessage = async function (e) {
    const parsedMessage = JSON.parse(e.data);
    console.log(parsedMessage);
    if (parsedMessage.result == "true") {
      if (parsedMessage.type == "LOGIN") {
        console.log('login');
        await AsyncStorage.setItem(`systemManager${parsedMessage.id}`, parsedMessage.systemManager ? "true" : "false");
        navigation.navigate("Home", {
          userId: parsedMessage.id,
          registered: "true",
          reload: true,


        });
      } else if (parsedMessage.type == "GUEST_LOGIN") {
        console.log('guest login')
        navigation.navigate("Home", {
          userId: parsedMessage.id,
          registered: "false",
          reload: true,

        });
      } else if (parsedMessage.type == "LOGGED_GUEST_LOGIN") {
        navigation.navigate("Home", {
          userId: parsedMessage.data,
          registered: "true",
          reload: true,

        });

      }
    } else {
      alert(parsedMessage.message);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Log In</Text>
      <TextInput
        style={{ borderWidth: 1, padding: 5 }}
        placeholder={'Username'}
        value={userName}
        onChangeText={(text) => setUserName(text)} />
      <TextInput
        secureTextEntry={true}
        style={{ borderWidth: 1, padding: 5 }}
        placeholder={'Password'}
        value={password}
        onChangeText={(text) => setPassword(text)} />
      <View style={{ padding: 5 }}>
        <Button title={'Login'} onPress={() => login(userName, password, client, back, userId)} />
      </View>

      {back ? <View style={{ flexDirection: 'row', position: 'absolute', top: 0, right: 0, padding: 5 }}>
        <Button title='Back' color={'red'} onPress={() => {
          navigation.navigate("Home", {
            userId: userId,
            registered: "false",
            reload: true,

          });
        }} />
      </View>
        :
        <View>
          <View style={{ padding: 5 }}>
            <Button title={'Login as Guest'} onPress={() => loginGuest(client)} />
          </View>
          <View style={{ padding: 5 }}>
            <TouchableOpacity onPress={() => navigation.navigate('Register', { back: false, userId: -1 })}>
              <Text style={styles.register}>Don't have an account yet?</Text>
            </TouchableOpacity>
          </View>
        </View>


      }



    </View>
  );
}

const loginGuest = (client) => {
  client.send(JSON.stringify({ "type": "GUEST_LOGIN" }));

}
const login = (userName, password, client, back, userId) => {
  if (back) {
    client.send(JSON.stringify({ "type": "LOGGED_GUEST_LOGIN", "email": userName, "password": password, "userId": userId }));
  } else {
    client.send(JSON.stringify({ "type": "LOGIN", "email": userName, "password": password }));
  }


}

const styles = StyleSheet.create({
  register: {
    color: 'blue',
  },
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    padding: 5,
    fontWeight: 'bold',
    fontSize: 30
  }
});
