var app = angular.module('app', ['ngRoute', 'ngSanitize', 'pascalprecht.translate','mgcrea.ngStrap']);

app.value('varRestBase', 'rest/');
app.value('availableLanguages', ['en','no']);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'app/templates/home.html',
            controller: 'HomeController'
        })
        .when('/start', {
            templateUrl: 'app/templates/start.html',
            controller: 'StartController'
        })
        .when('/mood', {
            templateUrl: 'app/templates/mood.html',
            controller: 'MoodController'
        })
        .when('/admin', {
            templateUrl: 'app/templates/admin.html',
            controller: 'AdminController' ,
            resolve: {
                'handshake': ['RestService', function (RestService) {
                    return RestService.get('handshake.json');
                }]
            }
        })
        .when('/login', {
            templateUrl: 'app/templates/login.html',
            controller: 'LoginController' ,
            resolve: {
                'handshake': ['RestService', function (RestService) {
                    return RestService.get('handshake.json');
                }]
            }
        });

    $locationProvider.html5Mode(true);
}]);

app.config(['$translateProvider',function ($translateProvider) {
    var defaultLanguage=window.localStorage.defaultLanguage || 'en';    // you could set up browser language detection here. And i use window instead of $window because $window is not availabe at this point yet.
    $translateProvider.useLoader('TranslationLoaderService', {});
    $translateProvider.use(defaultLanguage);
}]);

