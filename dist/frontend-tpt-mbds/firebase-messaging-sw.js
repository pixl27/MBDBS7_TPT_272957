importScripts('https://www.gstatic.com/firebasejs/8.7.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.7.0/firebase-messaging.js');
importScripts('https://www.gstatic.com/firebasejs/8.7.0/firebase-analytics.js');


firebase.initializeApp({
    apiKey: "AIzaSyBElHiQCyNDiL-qWRLED8gNWmZcJh0BOVc",
    authDomain: "dotabet-eeeb0.firebaseapp.com",
    projectId: "dotabet-eeeb0",
    storageBucket: "dotabet-eeeb0.appspot.com",
    messagingSenderId: "651843946289",
    appId: "1:651843946289:web:fd1ae79ccf36e25d36d210",
    measurementId: "G-3B8109JYSP"
  });

  const messaging = firebase.messaging();
  messaging.setBackgroundMessageHandler(function(payload) {

    console.log(payload);

    return self.registration.showNotification("title",  { body: 'salbat'});
});