import React from  'react';
import {View,Text,StyleSheet} from 'react-native'; 


const HomeScreen = ({navigation}) =>{  
    return(
        <View style={styles.container}>
            <Text style={styles.text}>Home Screen</Text>     
        </View>
    );
}; 

export default HomeScreen ;

const styles = StyleSheet.create({
    container:{
        flex:1,
        alignItems:'center',
        justifyContent:'center'
    },
    text:{
        fontSize:30
    },
});