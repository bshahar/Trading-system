import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, Button, Touchable, TouchableOpacity } from 'react-native';
import { EvilIcons } from '@expo/vector-icons'
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function PurchaseScreen({ route, navigation }) {
    const { storeId, products, storeName, userId, registered } = route.params;
    const [CreditNum, setCreditNum] = useState("");
    const [month, setMonth] = useState("");
    const [year, setYear] = useState("");
    const [holder, setHolder] = useState("");
    const [CVV, setCVV] = useState("");
    const [id, setId] = useState("");
    const [name, setName] = useState("");
    const [address, setAddress] = useState("");
    const [city, setCity] = useState("");
    const [country, setCountry] = useState("");
    const [zip, setZip] = useState("");

    return (
        <View style={styles.container} >
            <View>
                <Text>Purchase for Store: {storeName}</Text>
                <View style={{ borderWidth: 1, padding: 5 }}>
                    {products}
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5, borderColor: 'grey', borderRadius: 5, borderWidth: 2 }}>
                        <View style={{ flexDirection: 'row' }}>
                            <View style={{ padding: 5 }}>
                                <EvilIcons name='location' color="balck" size={25} />
                            </View>
                            <View style={{ padding: 5, alignSelf: 'center' }}>
                                <Text>Please Enter Shipment Address: </Text>
                            </View>
                        </View>

                        <View style={{ padding: 5 }}>
                            <TextInput value={name} style={{ borderWidth: 1, padding: 5 }} placeholder={'Full name'} onChangeText={(text) => setName(text)} />
                        </View>
                        <View style={{ flexDirection: 'row' }}>
                            <View style={{ padding: 5 }}>
                                <TextInput value={address} style={{ borderWidth: 1, padding: 5 }} placeholder={'Address'} onChangeText={(text) => setAddress(text)} />
                            </View>
                            <View style={{ padding: 5 }}>
                                <TextInput value={city} style={{ borderWidth: 1, padding: 5 }} placeholder={'City'} onChangeText={(text) => setCity(text)} />
                            </View>
                        </View>
                        <View style={{ flexDirection: 'row' }}>
                            <View style={{ padding: 5 }}>
                                <TextInput value={country} style={{ borderWidth: 1, padding: 5 }} placeholder={'Country'} onChangeText={(text) => setCountry(text)} />
                            </View>
                            <View style={{ padding: 5 }}>
                                <TextInput value={zip} style={{ borderWidth: 1, padding: 5 }} placeholder={'Zip'} onChangeText={(text) => setZip(text)} />
                            </View>
                        </View>

                    </View>
                </View>


                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5, borderColor: 'grey', borderRadius: 5, borderWidth: 2 }}>
                        <View style={{ flexDirection: 'row' }}>
                            <View style={{ padding: 5 }}>
                                <EvilIcons name='credit-card' color="balck" size={25} />
                            </View>
                            <View style={{ padding: 5, alignSelf: 'center' }}>
                                <Text>Please Enter Credit Info: </Text>
                            </View>
                        </View>

                        <View style={{ padding: 5 }}>
                            <TextInput value={CreditNum} style={{ borderWidth: 1, padding: 5 }} placeholder={'Credit card number'} onChangeText={(text) => setCreditNum(text)} />
                        </View>

                        <View style={{ flexDirection: 'row' }}>
                            <View style={{ padding: 5, width: 100 }}>
                                <TextInput value={month} style={{ borderWidth: 1, padding: 5 }} placeholder={'Month'} onChangeText={(text) => setMonth(text)} />
                            </View>
                            <View style={{ padding: 5, width: 100 }}>
                                <TextInput value={year} style={{ borderWidth: 1, padding: 5 }} placeholder={'Year'} onChangeText={(text) => setYear(text)} />
                            </View>
                            <View style={{ padding: 5, width: 100 }}>
                                <TextInput value={CVV} style={{ borderWidth: 1, padding: 5 }} placeholder={'CVV'} onChangeText={(text) => setCVV(text)} />
                            </View>
                        </View>

                        <View style={{ padding: 5 }}>
                            <TextInput value={holder} style={{ borderWidth: 1, padding: 5 }} placeholder={'Holder name'} onChangeText={(text) => setHolder(text)} />
                        </View>

                        <View style={{ padding: 5 }}>
                            <TextInput value={id} style={{ borderWidth: 1, padding: 5 }} placeholder={'Holder ID'} onChangeText={(text) => setId(text)} />
                        </View>

                    </View>

                    <View style={{ padding: 5 }}>
                        <Button title='Buy' onPress={() => {
                            var client = new W3CWebSocket('ws://localhost:4567/Cart/purchase');
                            client.onmessage = function (event) {
                                const parsedMessage = JSON.parse(event.data);
                                console.log(parsedMessage);
                                if (parsedMessage.type == "BUY_PRODUCT") {
                                    var result = parsedMessage.result;
                                    if (result) {
                                        alert("purchase confirmed successfully!")
                                        navigation.navigate('Home', { userId: userId, registered: registered });
                                    }
                                    else {
                                        alert(parsedMessage.message);
                                    }

                                }
                            }
                            client.onopen = function () {
                                client.send(JSON.stringify({
                                    "type": "BUY_PRODUCT",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "paymentData": {
                                        "card_number": CreditNum,
                                        "month": month,
                                        "year": year,
                                        "holder": holder,
                                        "cvv": CVV,
                                        "id": id
                                    },
                                    "supplementData": {
                                        "name": name,
                                        "address": address,
                                        "city": city,
                                        "country": country,
                                        "zip": zip
                                    }
                                }));
                            }
                        }} />

                    </View>
                </View>


            </View>
            <View style={{ flexDirection: 'row', position: 'absolute', right: 0, padding: 5 }}>
                <Button title='Back' color={'red'} onPress={() => { navigation.pop() }} />
            </View>



        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        flexDirection: 'row',
        padding: 5

    },

});
