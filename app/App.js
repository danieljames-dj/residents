import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Icon from 'react-native-vector-icons/Ionicons';
import { createMaterialBottomTabNavigator } from '@react-navigation/material-bottom-tabs';

export default function App() {

  function HomeScreen() {
    const color = "#900";
    return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <Text>
          Try editing me dude! 🎉
          <Icon name="home" size={30} color="#900" />
          <Icon name="ios-home" color={color} size={26} />
        </Text>
      </View>
    );
  }
  
  const Stack = createStackNavigator();
  const Tab = createMaterialBottomTabNavigator();
  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <Text>
        Try editing me dude! 🎉
        <Icon name="home" size={30} color="#900" />
      </Text>
    </View>
    // <NavigationContainer>
    //   <Tab.Navigator
    //     activeColor="#e91e63"
    //     barStyle={{ backgroundColor: 'tomato' }}
    //   >
    //     <Tab.Screen
    //       name="Feed"
    //       options={{
    //         tabBarLabel: 'Home',
    //         tabBarIcon: ({ color }) => (
    //           <MaterialCommunityIcons name="home" color={color} size={26} />
    //         ),
    //       }}
    //     />
    //   </Tab.Navigator>
    // </NavigationContainer>
    // <NavigationContainer>
    //   <Stack.Navigator>
    //     <Stack.Screen name="Home" component={HomeScreen} />
    //   </Stack.Navigator>
    // </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
