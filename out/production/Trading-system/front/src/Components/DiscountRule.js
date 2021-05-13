import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, CheckBox, TextInput, Button } from 'react-native';
import BannerRegister from './BannerRegister';
import OpenStore from './OpenStore';
import { Picker } from '@react-native-picker/picker'

var W3CWebSocket = require('websocket').w3cwebsocket;



export default function DiscountRule() {
    const [type, setType] = useState("");
    const [amount, setAmount] = useState("");
    const [cost, setCost] = useState("");
    const [timeType, setTimeType] = useState("");
    const [dayWeek, setDayWeek] = useState("");
    const [dayMonth, setDayMonth] = useState("");
    const [startHour, setStartHour] = useState("");
    const [endHour, setEndHour] = useState("");

    const byMinAmountView = <View>
        <TextInput value={amount} onChangeText={(text) => setAmount(text)} style={{ borderWidth: 1 }} placeholder={'Set Amount'} />
    </View>

    const byMinCostView = <View>
        <TextInput value={cost} onChangeText={(text) => setCost(text)} style={{ borderWidth: 1 }} placeholder={'Set Total Cost'} />
    </View>

    const weekArr = [
        { key: 1, value: '1' },
        { key: 2, value: '2' },
        { key: 3, value: '3' },
        { key: 4, value: '4' },
        { key: 5, value: '5' },
        { key: 6, value: '6' },
        { key: 7, value: '7' }];
    const dayOfWeekView = <View>
        <Picker
            style={{ flex:1,borderWidth: 1, fontSize: 12 }}
            selectedValue={dayWeek}
            onValueChange={(value, index) => setDayWeek(value)}>
            <Picker.Item key={1} lable={'1'} value={'1'} />
            <Picker.Item key={2} lable={'2'} value={'2'} />
            <Picker.Item key={3} lable={'3'} value={'3'} />
            <Picker.Item key={4} lable={'4'} value={'4'} />
            <Picker.Item key={5} lable={'5'} value={'5'} />
            <Picker.Item key={6} lable={'6'} value={'6'} />
            <Picker.Item key={7} lable={'7'} value={'7'} />


        </Picker>
    </View>

    const monthArr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31];
    const dayOfMonthView = <View>
        <Picker
            style={{ borderWidth: 1, fontSize: 12 }}
            selectedValue={dayMonth}
            onValueChange={(value, index) => setDayMonth(value)}>
            {monthArr.map((item) => { return (<Picker.Item key={item} lable={item} value={item} />) })}
        </Picker>
    </View>

    const hourArr = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,];
    const hoursView = <View>
        <Picker
            style={{ borderWidth: 1, fontSize: 12 }}
            selectedValue={startHour}
            onValueChange={(value, index) => setStartHour(value)}>
            {hourArr.map((item) => { return (<Picker.Item key={item} lable={item} value={item} />) })}
        </Picker>
        <Picker
            style={{ borderWidth: 1, fontSize: 12 }}
            selectedValue={endHour}
            onValueChange={(value, index) => setEndHour(value)}>
            {hourArr.map((item) => { return (<Picker.Item key={item} lable={item} value={item} />) })}
        </Picker>
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

    const byTimeView = <View>
        <Picker
            style={{ borderWidth: 1, fontSize: 12, width: 200 }}
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

    return (
        <View  >
            <View syle={{ padding: 5 }} >
                <Picker
                    style={{ padding: 5, borderWidth: 1, fontSize: 12, width: 200 }}
                    selectedValue={type}
                    onValueChange={(value, index) => setType(value)}>
                    <Picker.Item key={1} label={'By Min Amount'} value={'By Min Amount'} />
                    <Picker.Item key={2} label={'By Min Total Cost'} value={'By Min Total Cost'} />
                    <Picker.Item key={3} label={'By Time'} value={'By Time'} />
                </Picker>
            </View>

            {getCorrectView(type)}

            <Text>{dayWeek}</Text>
            <Text>{dayMonth}</Text>
            <Text>{startHour}</Text>
            <Text>{endHour}</Text>

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
