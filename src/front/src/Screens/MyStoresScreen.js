import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import OpenStore from '../Components/OpenStore';

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function MyStoresScreen({ route, navigation }) {
  const { userId, registered  } = route.params;
  const [stores, setStores] = useState([]);

  useEffect(() => {
    var client = new W3CWebSocket('ws://localhost:4567/myStores');
    client.onerror = function () {
      console.log('Connection Error');
    };

    client.onopen = function (event) {
      client.send(JSON.stringify({ "type": "GET_STORES", "id": userId }));
    }


    client.onclose = function () {
      console.log('echo-protocol Client Closed');
    };

    client.onmessage = function (e) {
      const parsedMessage = JSON.parse(e.data);

      if (parsedMessage.type == "GET_STORES") {
        console.log(parsedMessage.stores);
        setStores(parsedMessage.stores);

      }
      else if (parsedMessage.type == "LOGOUT") {
        result = parsedMessage.result;
        if (result)
          window.location.href = "Login";
        else
          alert("Error Log Out");
      }
    };

  }, [])

  return (
    <View >
      <BannerRegister navigation={navigation} registered={registered} userId={userId} />
      <Text>store screen</Text>
      <Text>user id: {userId}</Text>
      <View style={styles.storeList}>
        {stores.map((item) => {
          return (
            <View style={{ padding: 5 }}>
              <Button color={'red'} title={item.storeName} onPress={() => { navigation.navigate('MyStore', { registered: registered, storeId: item.storeId, userId: userId, storeName: item.storeName,isOwner:true }) }} />
            </View>
          )
        })}
      </View>

      <View style={{ padding: 5 }}>
        <OpenStore userId={userId} navigation={navigation} />
      </View>

    </View>
  );
};






const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    backgroundColor: '#fff',

  },
  storeList: {
    alignSelf: 'flex-start',
    flex: '1',
    left: 0,
    padding: 5,
  }
});
