import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Receipt from '../Components/Receipt';
import BannerRegister from '../Components/BannerRegister';
import AddProductScreen from './AddProductScreen';
import AppointManager from '../Components/AppointManager';
import AppointOwner from '../Components/AppointOwner';
import RemoveProductScreen from './RemoveProductsScreen';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function MyStoreScreen({ route, navigation }) {
  const { userId, storeId, storeName, isOwner, registered } = route.params;
  const [pemissions, setPermissions] = useState([]);
  const [menagerName, setMenagerName] = useState("");


  useEffect(() => {
    var client = new W3CWebSocket('ws://localhost:4567/myStores/StorePermissions');
    client.onerror = function () {
      console.log('Connection Error');
    };

    client.onopen = function (event) {
      client.send(JSON.stringify({ "type": "GET_PERMISSIONS", "id": userId, "storeId": storeId }));
    }

    client.onclose = function () {
      console.log('echo-protocol Client Closed');
    };

    client.onmessage = function (e) {
      const parsedMessage = JSON.parse(e.data);
      console.log(parsedMessage);
      if (parsedMessage.type == "GET_PERMISSIONS") {
        if (parsedMessage.result) {
          console.log(parsedMessage.data)
          setPermissions(parsedMessage.data);
        } else {
          alert(parsedMessage.message);
        }
      }
    };
  }, []);

  const permissionsFunc =
    <View style={styles.permissinList}>
      {pemissions.map((item) => {
        switch (item) {
          case 'AddProduct':
            return (
              <View style={{ padding: 5 }}>
                <Button title={'Add Product'} onPress={() => { navigation.navigate('AddProduct', { userId: userId, storeId: storeId, storeName: storeName }) }} />
              </View>
            );
          case 'AppointManager':
            return (
              <AppointManager storeName={storeName} userId={userId} storeId={storeId} />
            );
          case 'AppointOwner':
            return (
              <AppointOwner storeName={storeName} userId={userId} storeId={storeId} />
            )

          case 'CloseStore':
            return (
              <View style={{ padding: 5 }}>
                <Button title={'Close Store'} color={'red'} onPress={() => { }} />
              </View>
            );
          case 'GetWorkersInfo':
            return (
              <View style={{ padding: 5 }}>
                <Button title={'Workers Info'} onPress={() => { navigation.navigate('WorkersInfo', { userId: userId, storeId: storeId, storeName: storeName }) }} />
              </View>
            );
          case 'EditProduct':
            return (
              <View style={{ padding: 5 }}>
                <Button title={`Store's Products`} onPress={() => { navigation.navigate('MyStoreProducts', { userId: userId, storeId: storeId }) }} />
              </View>);
          case 'ViewPurchaseHistory':
            return (
              <View style={{ padding: 5 }}>
                <Button title={'Store Purchase History'} onPress={() => { navigation.navigate('StorePurchaseHistory', { userId: userId, storeId: storeId, storeName: storeName }) }} />
              </View>
            );
          case 'DefineDiscountPolicy':
            return (
              <View>
                <View style={{ padding: 5 }}>
                  <Button title={'Add Store Discount Policy'} onPress={() => { navigation.navigate('AddStoreDiscountPolicy', { userId: userId, storeId: storeId, registered: registered }) }} />
                </View>
                <View style={{ padding: 5 }}>
                  <Button title={'Edit Store Discount Policy'} onPress={() => { navigation.navigate('EditStoreDiscountPolicy', { userId: userId, storeId: storeId, registered: registered }) }} />
                </View>
              </View>

            );
          case 'DefinePurchasePolicy':
            return (
              <View>
                <View style={{ padding: 5 }}>
                  <Button title={'Add Store Purchase Policy'} onPress={() => { navigation.navigate('AddStorePurchasePolicy', { userId: userId, storeId: storeId, registered: registered }) }} />
                </View>
                <View style={{ padding: 5 }}>
                  <Button title={'Edit Store Purchase Policy'} onPress={() => { navigation.navigate('EditStorePurchasePolicy', { userId: userId, storeId: storeId, registered: registered }) }} />
                </View>
              </View>
            );
          case 'ResponedToOffer':
            return (
              <View>
                <View style={{ padding: 5 }}>
                  <Button title={"Menage Store's Bids "} onPress={() => { navigation.navigate('StoreBids', { userId: userId, storeId: storeId, registered: registered }) }} />
                </View>
              </View>
            );
          default:
            break;
        }
      })}

    </View>


  return (
    <View >
      <BannerRegister navigation={navigation} registered={registered} userId={userId} />
      <Text>Permissions for user {userId} in store {storeName}</Text>
      <Text>user id: {userId}</Text>

      {permissionsFunc}
      {isOwner ?
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          <View style={{ padding: 5, alignItems: 'center' }}>
            <TextInput style={{ borderWidth: 1, alignSelf: 'center' }} value={menagerName} onChangeText={(text) => setMenagerName(text)} placeholder={'Menager Name'} />
          </View>
          <View style={{ padding: 5 }}>
            <Button title={'Update Menager Permissions'} onPress={() => { navigation.navigate('MenagerPermissions', { userId: userId, storeId: storeId, userName: menagerName, registered: registered }) }} />
          </View>
        </View>
        : <View />}

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
