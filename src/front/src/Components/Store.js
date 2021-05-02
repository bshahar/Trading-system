import React  from 'react';
import { StyleSheet, Text, View,TextInput, Button } from 'react-native';
import Product from './Product';



export default function Store({userId,storeId,products}) {
  
  return (
    <View >
        <Text>user id: {userId}</Text>
        <Text>store id: {storeId}</Text>
        {products.map((item)=>{return (<Product product={item} userId={userId} storeId={storeId}/>)})}
     
    </View>
  );
};



const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection:'row',
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
