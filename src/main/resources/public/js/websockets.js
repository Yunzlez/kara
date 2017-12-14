function connect() {
    var socket = new SockJS('/requestws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        //todo current bin name in uri
        stompClient.subscribe('/topic/newrequests/544ab807-df52-4e64-a488-8729ee175680', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function setConnected(connected) {
    console.log('connected:' + connected);
}

function showMessageOutput(messageOutput) {
    //todo update page here, update counters
    //https://github.com/janl/mustache.js
    //replace body: null with "No body"
    console.log('MESSAGE:' + messageOutput);
}