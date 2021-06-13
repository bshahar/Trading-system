import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, CheckBox, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import OpenStore from '../Components/OpenStore';
import { Picker } from '@react-native-picker/picker'

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function PurchasePolicyScreen({ route, navigation }) {
    const { userId, storeId, basedOn, registered } = route.params;
    const [list, setList] = useState([]);
    const [type, setType] = useState("Min Amount");
    const [prodIdMinAmount, setProdIdMinAmount] = useState("");
    const [prodIdMaxAmount, setProdIdMaxAmount] = useState("");
    const [MinAmount, setMinAmount] = useState("");
    const [MaxAmount, setMaxAmount] = useState("");

    const [xor, setXor] = useState(false);
    const [or, setOr] = useState(false);
    const [and, setAnd] = useState(false);
    const [operation, setOperation] = useState("");
    const [age, setAge] = useState("");
    const [hour, setHour] =useState("");

    const MinAmountView =
        <View style={{ flexDirection: 'row' }}>
            <TextInput value={prodIdMinAmount} onChangeText={(text) => setProdIdMinAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Product Id'} />
            <TextInput value={MinAmount} onChangeText={(text) => setMinAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Amount'} />

        </View>

    const MaxAmountView =
        <View style={{ flexDirection: 'row' }}>
            <TextInput value={prodIdMaxAmount} onChangeText={(text) => setProdIdMaxAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Product Id'} />
            <TextInput value={MaxAmount} onChangeText={(text) => setMaxAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Amount'} />

        </View>


    const AgeLimitView =
        <View style={{ flexDirection: 'row' }}>
            <TextInput value={age} onChangeText={(text) => setAge(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set age limit'} />
        </View>

    const TimeLimitView =
        <View style={{ flexDirection: 'row' }}>
            <TextInput value={hour} onChangeText={(text) => setHour(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Hour'} />
        </View>


    const getCorrectView = (type) => {
        if (type == 'Min Amount') {
            return MinAmountView;
        } else if (type == 'Max Amount') {
            return MaxAmountView;
        } else if (type == 'Age Limit') {
            return AgeLimitView;
        } else if (type == 'Time Limit') {
            return TimeLimitView;
        }
    }

    const checkNecessary = () => {
        return true;
        // return ((xor || or || and) && (beginTime != "") && (endTime != "") && (presentage != "") && (policyName != "") &&
        //     ((prodIdMinAmount != "" && amount != "") || cost != "" || (startHour != "" && endHour != "")))
    }

    return (
        <View style={{ backgroundColor: '#fff', paddingTop: 10 }} >
            <View style={{ flexDirection: 'row' }}>
                <View style={{ alignSelf: 'center' }}>
                    <Text>Choose Operation:</Text>
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5 }}>
                        <Text>XOR</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <CheckBox style={{ alignSelf: 'center' }} value={xor} onValueChange={(value) => { setXor(value); setOr(false); setAnd(false); setOperation("Xor") }} />
                    </View>
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5 }}>
                        <Text>OR</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <CheckBox style={{ alignSelf: 'center' }} value={or} onValueChange={(value) => { setXor(false); setOr(value); setAnd(false); setOperation("Or") }} />
                    </View>
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5 }}>
                        <Text>AND</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <CheckBox style={{ alignSelf: 'center' }} value={and} onValueChange={(value) => { setXor(false), setOr(false), setAnd(value); setOperation("And") }} />
                    </View>
                </View>
            </View>
           
            
            <View style={{ padding: 5, paddingLeft: 10, paddingRight: 10, borderColor: 'grey', borderBottomWidth: 2, }}>

            </View>
            <View style={{ padding: 5, flexDirection: 'row' }} >
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5 }}>
                        <Picker
                            style={{ padding: 5, borderWidth: 1, fontSize: 12, width: 200 }}
                            selectedValue={type}
                            onValueChange={(value, index) => setType(value)}>
                            <Picker.Item key={1} label={'Min Amount'} value={'Min Amount'} />
                            <Picker.Item key={2} label={'Max Amount'} value={'Max Amount'} />
                            <Picker.Item key={3} label={'Age Limit'} value={'Age Limit'} />
                            <Picker.Item key={3} label={'Time Limit'} value={'Time Limit'} />

                        </Picker>
                    </View>
                    <View style={{ padding: 5 }}>
                        {getCorrectView(type)}
                    </View>
                </View>
            </View>
            <View style={{ padding: 5, width: 150 }}>
                <Button title={'Set And Add'} onPress={() => {
                    if (!checkNecessary()) {
                        alert("Necessary parametes can't be empty");
                    } else {
                        if (type == 'Min Amount') {
                            setList([...list, { policyName: "Min Amount", params: [MinAmount, prodIdMinAmount] }])
                        } else if (type == 'Max Amount') {
                            setList([...list, { policyName: "Max Amount", params: [MaxAmount,prodIdMaxAmount] }])
                        } else if (type == 'Age Limit') {
                            setList([...list, { policyName: "Age Limit", params: [age] }])
                        }else if (type == 'Time Limit') {
                            setList([...list, { policyName: "Time Limit", params: [hour] }])
                        }
                        setProdIdMinAmount("");
                        setProdIdMaxAmount("");
                        setAge("");
                        setHour("");
                    }
                }} />
            </View>
            <View style={{ padding: 5, width: 150 }}>
                <Button color={'red'} title={'Undo'} onPress={() => { list.pop() }} />
            </View>
            {list.map((item) => { return (<View style={{ padding: 5 }}><Text>Policy Name: {item.policyName}, Condition: {item.params[0]}</Text></View>) })}
            <View style={{ padding: 5, width: 150 }}>
                <Button color={'green'} title={'Finish And Update Policy'} onPress={() => {
                    var client = new W3CWebSocket(`ws://localhost:4567/discountAndPurchasesPolicies`);
                    client.onopen = function () {
                        if (!checkNecessary()) {

                        } else {
                            if (basedOn == "product") {
                                client.send(JSON.stringify({
                                    "type": "ADD_PURCHASE_POLICY_PRODUCT",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "productId": route.params.productId,
                                    "list": list,
                                    "operation": operation
                                }));
                            } else if (basedOn == "category") {
                                client.send(JSON.stringify({
                                    "type": "ADD_PURCHASE_POLICY_CATEGORY",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "category": route.params.category,
                                    "list": list,
                                    "operation": operation
                                }));

                            } else if (basedOn == "store") {
                                client.send(JSON.stringify({
                                    "type": "ADD_PURCHASE_POLICY_STORE",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "list": list,
                                    "operation": operation
                                }));
                            }
                        }

                    }

                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);
                        if (parsedMessage.type == "ADD_PURCHASE_POLICY_PRODUCT") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(parsedMessage.message);
                                navigation.pop();
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        } else if (parsedMessage.type == "ADD_PURCHASE_POLICY_CATEGORY") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(parsedMessage.message);
                                navigation.pop();
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        } else if (parsedMessage.type == "ADD_PURCHASE_POLICY_STORE") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(parsedMessage.message);
                                navigation.pop();
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        }
                    }
                }
                } />
            </View>

            <View style={{ flexDirection: 'row', position: 'absolute', right: 0, padding: 5 }}>
                <Button title='Back' color={'red'} onPress={() => { navigation.pop() }} />
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
