import React, { useState,useEffect } from 'react';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
var W3CWebSocket = require('websocket').w3cwebsocket;



export default function SysAnaliticsDay({ route, navigation }) {
    const {date}= route.params;
    const [guestsCounter, setGuestsCounter] = useState(0);
    const [normalUsersCounter, setNormalUsersCounter] = useState(0);
    const [managersCounter, setManagersCounter] = useState(0);
    const [ownersCounter, setOwnersCounter] = useState(0);

    useEffect(() => {
        var client = new W3CWebSocket(`ws://localhost:4567/AdminWebSocketDay`);
        
    
        client.onopen = function () {
          client.send(JSON.stringify({ "type": "GET_SYS_STAT_DAY", "date":date}));    
        };
    
        client.onmessage = function (e) {
          const parsedMessage = JSON.parse(e.data);
          console.log(parsedMessage);
          if (parsedMessage.type == "GET_SYS_STAT_DAY") {
            setGuestsCounter(parsedMessage.GuestsCounter);
            setNormalUsersCounter(parsedMessage.NormalUsersCounter);
            setManagersCounter(parsedMessage.ManagersCounter);
            setOwnersCounter(parsedMessage.OwnersCounter);
          }else if(parsedMessage.type == "UPDATE_GUESTS") {
            console.log(parsedMessage);
            setGuestsCounter(parsedMessage.number);
          }else if(parsedMessage.type == "UPDATE_REGISTERED") {
            console.log(parsedMessage);
            setNormalUsersCounter(parsedMessage.number);
          }else if(parsedMessage.type == "UPDATE_OWNERS") {
            console.log(parsedMessage);
            setOwnersCounter(parsedMessage.number);
          }else if(parsedMessage.type == "UPDATE_MANAGERS") {
            console.log(parsedMessage);
            setManagersCounter(parsedMessage.number);
          }
        };
    
    
      }, [])

    return (
        <View>
            <Text>Show analitics for day : {date}</Text>
            <View style={{padding:20, paddingRight:100}}>
                <View style={{ padding: 5, borderWidth: 1, borderColor: 'grey', borderRadius: 3 }}>
                    <Text>Number Of Guests:     {guestsCounter}</Text>
                </View>
                <View style={{ padding: 5, borderWidth: 1, borderColor: 'grey', borderRadius: 3 }}>
                    <Text>Number Of Registered Users:       {normalUsersCounter}</Text>

                </View>
                <View style={{ padding: 5, borderWidth: 1, borderColor: 'grey', borderRadius: 3 }}>
                    <Text>Number Of Managers:       {managersCounter}</Text>
                </View>
                <View style={{ padding: 5, borderWidth: 1, borderColor: 'grey', borderRadius: 3 }}>
                    <Text>Number Of Owners:     {ownersCounter}</Text>

                </View>
            </View>

            <View style={{ flexDirection: 'row', position: 'absolute', right: 0, padding: 5 }}>
                <Button title='Back' color={'red'} onPress={() => { navigation.pop() }} />
            </View>
        </View>
    )

};

