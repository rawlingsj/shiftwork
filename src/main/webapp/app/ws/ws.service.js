(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .service('WS', WS);

    WS.$inject = ['$rootScope'];

    function WS ($rootScope) {

        var stompClient = null;

        function setConnected(connected) {
            $("#connect").prop("disabled", connected);
            $("#disconnect").prop("disabled", !connected);
            if (connected) {
                $("#conversation").show();
            }
            else {
                $("#conversation").hide();
            }
            $("#greetings").html("");
        }

        function connect() {
            var socket = new SockJS('/gs-guide-websocket');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/greetings', function (greeting) {
                    var jobStatusUpdate = JSON.parse(greeting.body);
                    showScore(jobStatusUpdate);
                });
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }

        function sendName() {
            stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
        }

        function showScore(jobStatusUpdate) {
            $rootScope.$broadcast('score', jobStatusUpdate);
        }

        $(function () {
            $("form").on('submit', function (e) {
                e.preventDefault();
            });
            $( "#connect" ).click(function() { connect(); });
            $( "#disconnect" ).click(function() { disconnect(); });
            $( "#send" ).click(function() { sendName(); });
        });

    }
})();