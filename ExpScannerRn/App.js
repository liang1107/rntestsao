/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View, Button, PermissionsAndroid, NativeModules } from 'react-native';

async function requestCameraPermission() {
  try {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.CAMERA,
      {
        'title': 'Cool Photo App Camera Permission',
        'message': 'Cool Photo App needs access to your camera ' +
          'so you can take awesome pictures.'
      }
    )
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log("You can use the camera")
    } else {
      console.log("Camera permission denied")
    }
  } catch (err) {
    console.warn(err)
  }
}
async function requestWritePermission() {
  try {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
      {
        'title': 'Cool Photo App WRITE_EXTERNAL_STORAGE Permission',
        'message': 'Cool Photo App needs access to your camera ' +
          'so you can take awesome pictures.'
      }
    )
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log("You can use the WRITE_EXTERNAL_STORAGE")
    } else {
      console.log("Camera permission denied")
    }
  } catch (err) {
    console.warn(err)
  }
}

type Props = {};
export default class App extends Component<Props> {



  constructor(props) {
    super(props);
    this.state = {
      result: 'N/A',
      resultpp: 'N/A',
    };
    requestCameraPermission();

    this.onButtonPress = this.onButtonPress.bind(this);
  }

  onButtonPress() {
    requestWritePermission();
    NativeModules.ExpScannerReaderManager.scannerExp('eab5374580b10338a47e30f991-vagfvtRfftRgrfg', (msg) => {
      this.setState({ result: msg });
    },
      err => {
        console.log(err);
      });
  };

 
  render() {
    return (
      <View style={styles.container}>
        <Button title='Exp Scanner' onPress={this.onButtonPress} />

        <Text style={styles.display}>
          Exp Result: {this.state.result}
        </Text>
      


      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  display: {
    textAlign: 'center',
    color: '#333333',
    marginTop: 30,
    marginBottom: 30
  },
});
