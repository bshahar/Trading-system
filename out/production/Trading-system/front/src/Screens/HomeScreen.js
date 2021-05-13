import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button, SliderComponent } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function HomeScreen({ route, navigation }) {
  const { userId, registered,reload } = route.params;
  const [stores, setStores] = useState([]);
  const [isReload,setIsReload]= useState(reload)
  const [firstTime, setFirstTime] = useState(true);

  useEffect(() => {
    var client = new W3CWebSocket(`wss://localhost:4567/Main?userId=${userId}`);




    client.onerror = function () {
      console.log('Connection Error');
    };

    client.onopen = function () {
      console.log('WebSocket Client Connected Home screen');
      client.send(JSON.stringify({ "type": "GET_STORES", "id": userId }));
      if (firstTime) {
        console.log('send notification request');
        client.send(JSON.stringify({ "type": "GET_NOTIFICATIONS", "id": userId }));
        setFirstTime(false);
      }

    };

    client.onclose = function () {
      console.log('echo-protocol Client Closed');
    };


    client.onmessage = function (e) {
      console.log(e);
      const parsedMessage = JSON.parse(e.data);
      console.log(parsedMessage);
      if (parsedMessage.type == "GET_STORES") {
        setStores(parsedMessage.stores);

      }
      else if (parsedMessage.type == "LOGOUT") {
        result = parsedMessage.result;
        if (result)
          window.location.href = "Login";
        else
          alert("Error Log Out");
      }
      else if (parsedMessage.type == "GUEST_REGISTER") {
        result = parsedMessage.result;
        if (result) {
          console.log("register succefully");
          window.location.href = "Login";
        }
        else {
          alert(parsedMessage.data);
        }
      } else if (parsedMessage.type == "GET_NOTIFICATIONS") {
        console.log('got notification answer');

        var messages = parsedMessage.data;
        console.log(messages);
        if (messages.length > 0) {
          let oneMessage = "";
          for (let i = 0; i < messages.length; i++) {
            oneMessage = oneMessage.concat(messages[i]);
            oneMessage = oneMessage.concat('\n');
          }
          console.log(oneMessage);
          alert(oneMessage);
        }
      } else if (parsedMessage.type == "ALERT") {
        console.log(parsedMessage.data);
        alert(parsedMessage.data);
      }
    };


  }, [])




  return (
    <View style={{ flex: 1 }}>
      <BannerRegister navigation={navigation} userId={userId} registered={registered} />
      <View style={styles.container}>
        <Text>Home Screen</Text>
        <Text>User connected: {registered}</Text>
        <Text>{registered}</Text>

        <View style={styles.storeList}>
          {stores.map((item) => {
            return (
              <View style={{padding:5}}>
                <Button color={'red'} title={item.storeName} onPress={() => { navigation.navigate('Store', { userId: userId, storeId: item.storeId, registered: registered }) }} />
              </View>
            )
          })}
        </View>


      </View>

    </View>

  );
}






const styles = StyleSheet.create({
  modalView: {
    position: 'absolute',
    alignSelf: 'center'
  },
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  storeList: {
    alignSelf: 'flex-start',
    flex: '1',
    left: 0,
    padding: 5,
  }
});
