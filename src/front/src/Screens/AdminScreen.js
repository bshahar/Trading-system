import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import Receipt from '../Components/Receipt';
import BannerRegister from '../Components/BannerRegister';
import AddProductScreen from './AddProductScreen';
import AppointManager from '../Components/AppointManager';
import AppointOwner from '../Components/AppointOwner';
import RemoveProductScreen from './RemoveProductsScreen';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function AdminScreen({ route, navigation }) {
    const { userId, registered } = route.params;
    const [date, setDate] = useState('');

    return (
        <View >
            <BannerRegister navigation={navigation} registered={registered} userId={userId} />
            <View style={{
                alignSelf: 'flex-start',
                flex: '1',
                left: 0,
                padding: 5,
            }}>
                <View style={{ padding: 5 }}>
                    <Button title={"Get Purchase History"} onPress={() => { navigation.navigate('GlobalPurchases', { userId: userId, registered: registered }) }} />
                </View>
                <View style={{ padding: 5 }}>
                    <Button title={"Watch System Analitics"} onPress={() => { navigation.navigate('SysAnalitics', { userId: userId, registered: registered }) }} />
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ flexDirection: 'row' }}>
                        <View style={{ padding: 5, alignSelf:'center'}}>
                            <TextInput style={{borderWidth:1,height:30,}} placeholder='Enter Date' value={date} onChangeText={(text) => setDate(text)} />
                            <Text style={{color:'red', fontSize:12}}>Enter Date In DD/MM/YYYY</Text>
                        </View>
                        <View style={{ padding: 5 }}>
                            <Button title={"Analitics Per date"} onPress={() => { navigation.navigate('SysAnaliticsDay', { userId: userId, registered: registered,date:date }) }} />
                        </View>

                    </View>

                </View>
            </View>
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
