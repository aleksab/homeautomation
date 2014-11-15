var app = angular.module('kiwi', ['ngRoute', 'ngSanitize', 'ngTouch']);

app.value('varRestBase', 'http://192.168.1.190:5300/');

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
        
        .when('/', {
            templateUrl: 'app/templates/home.html',
            controller: 'HomeController'
        })

        .when('/discover', {
            templateUrl: 'app/templates/discover.html',
            controller: 'DiscoverController'
        })

        .when('/rules', {
            templateUrl: 'app/templates/rules.html',
            controller: 'RulesController'
        });

}]);
