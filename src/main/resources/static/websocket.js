'use strict';

var SockJS = require('sockjs-client'); // <1>
require('stompjs'); // <2>

var lametimeSC;

function register(registrations, reconnectionDelay) {
    console.log("connecting...");
    if(reconnectionDelay === undefined) {
        reconnectionDelay = 1000;
    } else {
        if(reconnectionDelay <30000) {
            reconnectionDelay = reconnectionDelay*2;
        } else {
            reconnectionDelay = 30000;
        }
    }

	var socket = SockJS('/chat'); // <3>
	lametimeSC = Stomp.over(socket);
	var stompSuccessCallback = function(frame) {
        registrations.forEach(function (registration) { // <4>
            lametimeSC.subscribe(registration.route, registration.callback);
        });
        lametimeSC.send("/app/state", {}, {});
    };
    var stompFailureCallback = function(error) {
        console.log("STOMP: " + error);
        setTimeout(function() {register(registrations, reconnectionDelay)}, reconnectionDelay);
    }
	lametimeSC.connect({}, stompSuccessCallback, stompFailureCallback);
}

function send(message) {
    lametimeSC.send("/app/chat", {},
                JSON.stringify({'text':message}));
}

module.exports.register = register;
module.exports.send = send;
