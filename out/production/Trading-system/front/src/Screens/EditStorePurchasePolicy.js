import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, CheckBox, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import OpenStore from '../Components/OpenStore';

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function EditStorePurchasePolicy({ route, navigation }) {
    const { userId, storeId, registered } = route.params;
    const [productId, setProductId] = useState('');
    const [category, setCategory] = useState('');
    return (
        <View >
            <BannerRegister navigation={navigation} registered={registered} userId={userId} />
            <View style={{ padding: 5, flexDirection: 'row' }}>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5, borderWidth: 2, borderRadius: 3, borderColor: 'grey' }}>
                        <View style={{ padding: 5 }}>
                            <TextInput placeholder={'Enter Product Id'} value={productId} onChangeText={(text) => { setProductId(text) }} style={{ borderWidth: 1, padding: 5 }} />
                        </View>
                        <View style={{ padding: 5 }}>
                            <Button title={'On Product'} onPress={() => { navigation.navigate("EditPurchaseScreen", { userId: userId, storeId: storeId, basedOn: 'product',productId:productId, registered: registered }) }} />
                        </View>
                    </View>
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5, borderWidth: 2, borderRadius: 3, borderColor: 'grey' }}>
                        <View style={{ padding: 5 }} >
                            <TextInput placeholder={'Enter Category'} value={category} onChangeText={(text) => { setCategory(text) }} style={{ borderWidth: 1, padding: 5 }} />
                        </View>
                        <View style={{ padding: 5 }}>
                            <Button title={'On Category'} onPress={() => { navigation.navigate("EditPurchaseScreen", { userId: userId, storeId: storeId, basedOn: 'category',category:category, registered: registered }) }} />
                        </View>
                    </View>
                </View>
                <View style={{ alignSelf:'center', padding: 5 }}>
                    <View style={{ padding: 5, borderWidth: 2, borderRadius: 3, borderColor: 'grey' }}>
                        <View style={{ alignSelf: 'center', padding: 5 }}>
                            <Button title={'On Store'} onPress={() => { navigation.navigate("EditPurchaseScreen", { userId: userId, storeId: storeId, basedOn: 'store', registered: registered }) }} />
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
    storeList: {
        alignSelf: 'flex-start',
        flex: '1',
        left: 0,
        padding: 5,
    }
});
