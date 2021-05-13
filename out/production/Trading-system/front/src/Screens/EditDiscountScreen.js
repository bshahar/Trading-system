import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, CheckBox, TextInput, Button } from 'react-native';
import BannerRegister from '../Components/BannerRegister';
import OpenStore from '../Components/OpenStore';
import { Picker } from '@react-native-picker/picker'
import { set } from 'react-native-reanimated';

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function EditDiscountScreen({ route, navigation }) {
    const { userId, storeId, basedOn, registered } = route.params;
    const [list, setList] = useState([]);
    const [policyName, setPolicyName] = useState("");
    const [type, setType] = useState("By Min Amount");
    const [amount, setAmount] = useState("");
    const [prodIdMinAmount, setProdIdMinAmount] = useState("");
    const [cost, setCost] = useState("");
    const [timeType, setTimeType] = useState("Day of week");
    const [dayWeek, setDayWeek] = useState("");
    const [dayMonth, setDayMonth] = useState("");
    const [startHour, setStartHour] = useState("");
    const [endHour, setEndHour] = useState("");
    const [beginTime, setBeginTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [presentage, setPresentage] = useState("");
    const [xor, setXor] = useState(false);
    const [or, setOr] = useState(false);
    const [and, setAnd] = useState(false);
    const [operation, setOperation] = useState("");
    const [sum, setSum] = useState(false);
    const [max, setMax] = useState(false);
    const [mathOp, setMathOp] = useState("");

    useEffect(() => {
        var client = new W3CWebSocket(`wss://localhost:4567/getDiscountAndPurchasesPolicies`);
        client.onerror = function () {
            console.log('Connection Error');
        };

        client.onopen = function () {
            if (basedOn == 'product') {
                client.send(JSON.stringify({ "type": "GET_DISCOUNT_POLICY_PRODUCT", "userId": userId, "storeId": storeId, "productId": route.params.productId }));
            } else if (basedOn == 'category') {
                client.send(JSON.stringify({ "type": "GET_DISCOUNT_POLICY_CATEGORY", "userId": userId, "storeId": storeId, "category": route.params.category }));
            } else if (basedOn == 'store') {
                client.send(JSON.stringify({ "type": "GET_DISCOUNT_POLICY_STORE", "userId": userId, "storeId": storeId }));
            }
        };

        client.onclose = function () {
            console.log('echo-protocol Client Closed');
        };


        client.onmessage = function (e) {
            const parsedMessage = JSON.parse(e.data);
            console.log(parsedMessage);
            if (parsedMessage.type == 'GET_DISCOUNT_POLICY') {
                if (parsedMessage.result) {
                    const mathOp = parsedMessage.data.mathOp;
                    setMathOp(mathOp);
                    if (mathOp == "Sum")
                        setSum(true);
                    if (mathOp == "Max")
                        setMax(true);
                    const operator = parsedMessage.data.operator;
                    setOperation(operator);
                    if (operator == "Xor")
                        setXor(true);
                    if (operator == "And")
                        setAnd(true);
                    if (operator == "Or")
                        setOr(true);
                    setBeginTime(parsedMessage.data.begin);
                    setEndTime(parsedMessage.data.end);
                    setPresentage(parsedMessage.data.percentage);
                    console.log(parsedMessage.data.list);
                    setList(parsedMessage.data.list);
                    
                   



                } else {
                    alert(parsedMessage.message);
                    navigation.pop()
                }
            }

        };
    }, []);


    const byMinAmountView =
        <View style={{ flexDirection: 'row' }}>
            <TextInput value={prodIdMinAmount} onChangeText={(text) => setProdIdMinAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Product Id'} />
            <TextInput value={amount} onChangeText={(text) => setAmount(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Amount'} />

        </View>

    const byMinCostView = <View>
        <TextInput value={cost} onChangeText={(text) => setCost(text)} style={{ borderWidth: 1, padding: 5 }} placeholder={'Set Total Cost'} />
    </View>

    const dayOfWeekView =
        <View style={{ padding: 5 }}>
            <TextInput placeholder={'Day'} value={dayWeek} onChangeText={(text) => { setDayWeek(text) }} style={{ borderWidth: 1, padding: 5 }} />
        </View>

    const dayOfMonthView = <View style={{ padding: 5 }}>
        <TextInput placeholder={'Day'} value={dayMonth} onChangeText={(text) => { setDayMonth(text) }} style={{ borderWidth: 1, padding: 5 }} />
    </View>

    const hoursView = <View style={{ flexDirection: 'row' }}>
        <View style={{ padding: 5 }}>
            <TextInput placeholder={'Start'} value={startHour} onChangeText={(text) => { setStartHour(text) }} style={{ padding: 5, borderWidth: 1, width: 80 }} />
        </View>
        <View style={{ padding: 5 }}>
            <TextInput placeholder={'End'} value={endHour} onChangeText={(text) => { setEndHour(text) }} style={{ borderWidth: 1, width: 80, padding: 5 }} />
        </View>

    </View>



    const getTimeView = (type) => {
        if (timeType == 'Day of week') {
            return dayOfWeekView;
        } else if (timeType == 'Day of month') {
            return dayOfMonthView;
        } else if (timeType == 'Hour of the day') {
            return hoursView;
        }
    }

    const byTimeView =
        <View>
            <Picker
                style={{ padding: 5, borderWidth: 1, fontSize: 12, width: 200 }}
                selectedValue={timeType}
                onValueChange={(value, index) => setTimeType(value)}>
                <Picker.Item key={1} label={'Day of week'} value={'Day of week'} />
                <Picker.Item key={2} label={'Day of month'} value={'Day of month'} />
                <Picker.Item key={3} label={'Hour of the day'} value={'Hour of the day'} />
            </Picker>
            <View style={{ padding: 5, width: 100 }}>
                {getTimeView(type)}
            </View>

        </View>

    const getCorrectView = (type) => {
        if (type == 'By Min Amount') {
            return byMinAmountView;
        } else if (type == 'By Min Total Cost') {
            return byMinCostView;
        } else if (type == 'By Time') {
            return byTimeView;
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
            <View style={{ flexDirection: 'row' }}>
                <View style={{ alignSelf: 'center' }}>
                    <Text>Choose Discount Adding Method:</Text>
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5 }}>
                        <Text>SUM</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <CheckBox style={{ alignSelf: 'center' }} value={sum} onValueChange={(value) => { setSum(value); setMax(false); setMathOp("Sum") }} />
                    </View>
                </View>
                <View style={{ padding: 5 }}>
                    <View style={{ padding: 5 }}>
                        <Text>MAX</Text>
                    </View>
                    <View style={{ padding: 5 }}>
                        <CheckBox style={{ alignSelf: 'center' }} value={max} onValueChange={(value) => { setSum(false); setMax(value); setMathOp("Max") }} />
                    </View>
                </View>

            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <View style={{ alignSelf: 'center' }}>
                    <Text>Begin Date: </Text>
                </View>
                <View>
                    <TextInput title={'Enter Date'} value={beginTime} style={{ padding: 5, borderWidth: 1 }} onChangeText={(text) => { setBeginTime(text) }} />
                </View>
                <View style={{ alignSelf: 'center' }}>
                    <Text style={{ fontSize: 10, color: 'red' }} >Enter DD/MM/YYYY format</Text>
                </View>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <View style={{ alignSelf: 'center' }}>
                    <Text>End Date: </Text>
                </View>
                <View>
                    <TextInput title={'Enter Date'} value={endTime} style={{ padding: 5, borderWidth: 1 }} onChangeText={(text) => { setEndTime(text) }} />
                </View>
                <View style={{ alignSelf: 'center' }}>
                    <Text style={{ fontSize: 10, color: 'red' }}>Enter DD/MM/YYYY format</Text>
                </View>
            </View>
            <View style={{ flexDirection: 'row', padding: 5 }}>
                <View style={{ alignSelf: 'center' }}>
                    <Text>Discount Presentage: </Text>
                </View>
                <View >
                    <TextInput title={'Enter Number'} value={presentage} style={{ padding: 5, borderWidth: 1 }} onChangeText={(text) => { setPresentage(text) }} />
                </View>
                <View style={{ alignSelf: 'center' }}>
                    <Text>%</Text>
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
                            <Picker.Item key={1} label={'By Min Amount'} value={'By Min Amount'} />
                            <Picker.Item key={2} label={'By Min Total Cost'} value={'By Min Total Cost'} />
                            <Picker.Item key={3} label={'By Time'} value={'By Time'} />
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
                        if (type == 'By Min Amount') {
                            setList([...list, { policyName: "Minimal Amount", params: [amount, prodIdMinAmount] }])
                        } else if (type == 'By Min Total Cost') {
                            setList([...list, { policyName: "Minimal Cost", params: [cost] }])
                        } else if (type == 'By Time') {
                            setList([...list, {
                                policyName: "Purchase Time",
                                params: [
                                    timeType == 'Day of week',
                                    timeType == 'Day of month',
                                    timeType == 'Hour of the day',
                                    dayWeek, dayMonth, startHour, endHour]
                            }])
                        }
                        setPolicyName("");
                        setAmount("");
                        setProdIdMinAmount("");
                        setCost("");
                        setDayWeek("");
                        setDayMonth("");
                        setStartHour("");
                        setEndHour("");
                    }
                }} />
            </View>
            <View style={{ padding: 5, width: 150 }}>
                <Button color={'red'} title={'Undo'} onPress={() => { list.pop() }} />
            </View>
            {list.map((item) => { return (<View style={{ padding: 5 }}><Text>Policy Name: {item.policyName}, params: {item.params}</Text></View>) })}
            <View style={{ padding: 5, width: 150 }}>
                <Button color={'green'} title={'Finish And Update Policy'} onPress={() => {
                    var client = new W3CWebSocket(`wss://localhost:4567/discountAndPurchasesPolicies`);
                    client.onopen = function () {
                        if (!checkNecessary()) {

                        } else {
                            if (basedOn == "product") {
                                client.send(JSON.stringify({
                                    "type": "ADD_DISCOUNT_POLICY_PRODUCT",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "productId": route.params.productId,
                                    "list": list,
                                    "begin": beginTime,
                                    "end": endTime,
                                    "presentage": presentage,
                                    "mathOp": mathOp,
                                    "operation": operation
                                }));
                            } else if (basedOn == "category") {
                                client.send(JSON.stringify({
                                    "type": "ADD_DISCOUNT_POLICY_CATEGORY",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "category": route.params.category,
                                    "list": list,
                                    "begin": beginTime,
                                    "end": endTime,
                                    "presentage": presentage,
                                    "mathOp": mathOp,
                                    "operation": operation
                                }));

                            } else if (basedOn == "store") {
                                client.send(JSON.stringify({
                                    "type": "ADD_DISCOUNT_POLICY_STORE",
                                    "userId": userId,
                                    "storeId": storeId,
                                    "list": list,
                                    "begin": beginTime,
                                    "end": endTime,
                                    "presentage": presentage,
                                    "mathOp": mathOp,
                                    "operation": operation
                                }));
                            }
                        }

                    }

                    client.onmessage = function (event) {
                        const parsedMessage = JSON.parse(event.data);
                        if (parsedMessage.type == "ADD_DISCOUNT_POLICY_PRODUCT") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(parsedMessage.message);
                                navigation.pop();
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        } else if (parsedMessage.type == "ADD_DISCOUNT_POLICY_CATEGORY") {
                            var result = parsedMessage.result;
                            if (result) {
                                alert(parsedMessage.message);
                                navigation.pop();
                            }
                            else {
                                alert(parsedMessage.message);
                            }

                        } else if (parsedMessage.type == "ADD_DISCOUNT_POLICY_STORE") {
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
