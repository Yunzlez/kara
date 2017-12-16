var ws_connected_bin = false;
function connect() {
    var socket = new SockJS('/requestws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        //todo current bin name in uri
        stompClient.subscribe('/topic/newrequests/' + window.location.pathname.split('/')[2], function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function setConnected(connected) {
    console.log('connected:' + connected);
    ws_connected_bin = true;
}

function showMessageOutput(messageOutput) {
    //todo update page here, update counters
    //https://github.com/janl/mustache.js
    if (messageOutput.hasOwnProperty("headers") && messageOutput.headers !== null) {
        messageOutput.headers = flatten(messageOutput.headers, "headers");
    }
    if (messageOutput.hasOwnProperty("queryParams") && messageOutput.queryParams !== null) {
        messageOutput.headers = flatten(messageOutput.queryParams, "queryParams");
    }
    console.log(messageOutput);
    $.get('/js/mustache-template.html', function (template) {
        var rendered = Mustache.render(template, messageOutput);
        $("#").prependTo(rendered);
    });
    //todo update numbers
}

function flatten(object, propname) {
    console.log("Flattening " + propname);
    var headersKV = [];
    for (var k in object) {
        console.log(k);
        if (object.hasOwnProperty(k)) {
            var obj = {};
            obj.key = k;
            obj.value = object[k];
            console.log(obj);
            headersKV.push(obj);
        } else {
            console.log("Nope")
        }
    }
    return headersKV;
}

function addCount(type) {
    switch (type) {
        case "GET":
            break;
        case "POST":
            break;
        case "PUT":
            break;
        case "PATCH":
            break;
        case "DELETE":
            break;
    }
}

function addOne(id) {
    $(id)
}

document.onload = function () {
    connect();
    //todo fix
};