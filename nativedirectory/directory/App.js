import React, { useEffect } from 'react'; 
import { StyleSheet, Text, View } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { 
  NavigationContainer, 
  DefaultTheme as NavigationDefaultTheme,
  DarkTheme as NavigationDarkTheme
} from '@react-navigation/native';
import { createDrawerNavigator } from '@react-navigation/drawer';
import { DrawerContent } from './screens/DrawerContent';


import MainTabScreen from './screens/MainTabScreen';
import RootStackScreen from './screens/RootStackScreen';




const Drawer = createDrawerNavigator();

const App = () => {
  return ( 
   
     <NavigationContainer > 
       
 <RootStackScreen>
   
 </RootStackScreen>
   
        {/* <Drawer.Navigator drawerContent={props=> <DrawerContent {...props}/>}>
         <Drawer.Screen name="HomeDrawer" component={MainTabScreen} />
          
         </Drawer.Navigator> 
     */}
    
    </NavigationContainer>
    
  );
}

export default App;

