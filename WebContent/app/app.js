var app = angular.module('kiwi', ['ngRoute', 'ngSanitize']);

app.value('varRestBase', 'rest/');

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'app/templates/home.html',
            controller: 'HomeController'
        });

    $locationProvider.html5Mode(true);
}]);
