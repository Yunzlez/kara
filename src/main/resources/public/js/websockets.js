var ws_connected_bin = false;
function connect() {
    var socket = new SockJS('/requestws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/newrequests/' + window.location.pathname.split('/')[2], function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function setConnected(connected) {
    ws_connected_bin = true;
}

function showMessageOutput(messageOutput) {
    //https://github.com/janl/mustache.js
    messageOutput.methodLower = messageOutput.method.toLowerCase();
    if (messageOutput.hasOwnProperty("headers") && messageOutput.headers !== null) {
        messageOutput.headers = flatten(messageOutput.headers, "headers");
    }
    if (messageOutput.hasOwnProperty("queryParams") && messageOutput.queryParams !== null) {
        messageOutput.queryParams = flatten(messageOutput.queryParams, "queryParams");
    }

    console.log(messageOutput);
    $.get('/js/mustache-template.html', function (template) {
        var rendered = Mustache.render(template, messageOutput);
        $("#reqLog").prepend(rendered);
    });
    addCount(messageOutput.method);

    //todo optimize
    $('pre code').first(function(i, block) {
        hljs.highlightBlock(block);
    });
}

function flatten(object, propname) {
    var headersKV = [];
    for (var k in object) {
        if (object.hasOwnProperty(k)) {
            var obj = {};
            obj.key = k;
            obj.value = object[k];
            headersKV.push(obj);
        }
    }
    return headersKV;
}

function addCount(type) {
    addOne("#requestCount");
    switch (type) {
        case "GET":
            addOne("#getCount");
            break;
        case "POST":
            addOne("#postCount");
            break;
        case "PUT":
            addOne("#putCount");
            break;
        case "PATCH":
            addOne("#patchCount");
            break;
        case "DELETE":
            addOne("#deleteCount");
            break;
    }
}

function addOne(id) {
    console.log("Adding 1 to " + id);
    var num = +$(id).text() + 1;
    $(id).text(num);
}

window.onload = function () {
    connect();
};