import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import OpenStore from '../Components/OpenStore';
import { Picker } from '@react-native-picker/picker'

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function SearchProductScreen({ route, navigation }) {
  const { userId, registered } = route.params;
  const [type, setType] = useState('Name');
  const [param, setParam] = useState("");
  const [min, setMin] = useState("");
  const [max, setMax] = useState("");
  const [prodRank, setProdRank] = useState("");
  const [storeRank, setStoreRank] = useState("");
  const [category, setCategory] = useState("");
 



  return (
    <View >
      <BannerRegister navigation={navigation} registered={registered} userId={userId} />
      <Text>search screen</Text>
      <Text>user id: {userId}</Text>
      <View style={{ padding: 5, flexDirection: 'row' }}>
        <View style={{ alignItems: 'center' }}>
          <Text>Search By: </Text>
        </View>
        <Picker
          style={{ borderWidth: 1, fontSize: 12 }}
          selectedValue={type}
          onValueChange={(value, index) => setType(value)}>
          <Picker.Item key={1} label={'Name'} value={'Name'} />
          <Picker.Item key={2} label={'Category'} value={'Category'} />
          <Picker.Item key={3} label={'Keywords'} value={'Keywords'} />
        </Picker>
      </View>
      <View style={{ padding: 5 }}>
        <TextInput value={param} style={{ width: 100, borderWidth: 1, padding: 5 }} placeholder={'Parameter'} onChangeText={(text) => setParam(text)} />
      </View>
      <View style={{ flexDirection: 'row' }}>
        <View style={{ padding: 5 }}>
          <TextInput value={min} style={{ width: 100, borderWidth: 1, padding: 5 }} placeholder={'Min Price'} onChangeText={(text) => setMin(text)} />
        </View>
        <View style={{ padding: 5 }}>
          <TextInput value={max} style={{ width: 100, borderWidth: 1, padding: 5 }} placeholder={'Max Price'} onChangeText={(text) => setMax(text)} />
        </View>
      </View>
      <View style={{ flexDirection: 'row' }}>
        <View style={{ padding: 5 }}>
          <TextInput value={prodRank} style={{ width: 100, borderWidth: 1, padding: 5 }} placeholder={'Product Rank'} onChangeText={(text) => setProdRank(text)} />
        </View>
        <View style={{ padding: 5 }}>
          <TextInput value={category} style={{ width: 100, borderWidth: 1, padding: 5 }} placeholder={'Product Category'} onChangeText={(text) => setCategory(text)} />
        </View>
      </View>

      <View style={{ padding: 5 }}>
        <TextInput value={storeRank} style={{ width: 100, borderWidth: 1, padding: 5 }} placeholder={'Store Rank'} onChangeText={(text) => setStoreRank(text)} />
      </View>
      <View>
        <Button title={'SEARCH'} onPress={() => {
          var client = new W3CWebSocket('wss://localhost:4567/search');
          client.onerror = function () {
            console.log('Connection Error');
          };

          client.onopen = function (event) {
            client.send(JSON.stringify({
              "type": "SEARCH_PRODUCTS",
              "userId":userId,
              "searchType": type,
              "param":param,
              "maxPrice": max,
              "minPrice": min,
              "prodRank":prodRank,
              "storeRank":storeRank,
              "category":category
            }));
          }
      
          client.onclose = function () {
            console.log('echo-protocol Client Closed');
          };

          client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);

            if (parsedMessage.type == "SEARCH_PRODUCTS") {
              if(parsedMessage.result){
                console.log(parsedMessage.products);
             
                navigation.navigate("SearchProductList",{registered:registered,userId:userId,products:parsedMessage.products})
              }else{
                alert(parsedMessage.message);
              }             
            }
          };
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
  storeList: {
    alignSelf: 'flex-start',
    flex: '1',
    left: 0,
    padding: 5,
  }
});


