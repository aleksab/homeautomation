var app = angular.module('kiwi', ['ngRoute', 'ngSanitize', 'ngTouch']);

app.value('varRestBase', 'http://192.168.1.190:5300/');

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'app/templates/home.html',
            controller: 'HomeController'
        });

    $locationProvider.html5Mode(true);
}]);
