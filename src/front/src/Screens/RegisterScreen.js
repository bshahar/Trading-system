import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button, TouchableOpacity } from 'react-native';

var W3CWebSocket = require('websocket').w3cwebsocket;




export default function RegisterScreen({ route, navigation }) {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [age, setAge] = useState('');

  const { back, userId } = route.params;

  var client = new W3CWebSocket('ws://localhost:4567/Login');
  client.onerror = function () {
    console.log('Connection Error');
  };

  client.onopen = function () {
    console.log('WebSocket Client Connected Register screen');
  };

  client.onclose = function () {
    console.log('echo-protocol Client Closed');
  };

  client.onmessage = function (e) {
    const parsedMessage = JSON.parse(e.data);
    console.log(parsedMessage);
    if (parsedMessage.result == "true" || parsedMessage.result == true) {
      if (parsedMessage.type == "REGISTER") {
        navigation.navigate('Login');
      } else if (parsedMessage.type == "GUEST_REGISTER") {
        client.send(JSON.stringify({ "type": "LOGIN", "email": userName, "password": password }));

      } else if (parsedMessage.type == "LOGIN") {
        console.log('login');
        navigation.navigate("Home", {
          userId: userId,
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
      <Text style={styles.title}>Register</Text>
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
        <TextInput
        style={{ borderWidth: 1, padding: 5 }}
        placeholder={'Age'}
        value={age}
        onChangeText={(text) => setAge(text)} />
      <View style={{ padding: 5 }}>
        <Button title={'Register'} onPress={() => Register(userName, password, client, back, userId,age)} />
        {back ?
          <TouchableOpacity onPress={() => navigation.pop()}>
            <Text style={styles.login}>Cancel Registration</Text>
          </TouchableOpacity> :
          <TouchableOpacity onPress={() => navigation.navigate('Login')}>
            <Text style={styles.login}>Log In insted</Text>
          </TouchableOpacity>}

      </View>

    </View>
  );
}

const Register = (userName, password, client, back, userId,age) => {
  if (back) {
    client.send(JSON.stringify({ "type": "GUEST_REGISTER", "email": userName, "password": password, "userId": userId,"age":age }));

  } else {
    client.send(JSON.stringify({ "type": "REGISTER", "email": userName, "password": password,"age":age }));
  }



}

const styles = StyleSheet.create({
  login: {
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
