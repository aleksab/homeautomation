app.service('RestService', ['$http', 'varRestBase', function ($http, varRestBase) {
    return {
        get: function (url, args, callback) {
            var rest = $http.get(varRestBase + url, args);
            if (typeof(callback) == 'function')callback();
            return rest;
        },
        post: function (url, data, callback) {
            var rest = $http.post(varRestBase + url, data);
            if (typeof(callback) == 'function')callback();
            return rest;
        }
    }
}]
);

app.factory('AuthFactory', ['RestService', function (RestService) {
    var credentials = {};
    window.credentials = credentials;
    return {
        reset: function () {
            credentials.username = '';
            credentials.password = '';
            return credentials;
        },
        login: function (loginCredentials) {
            credentials.username = (loginCredentials && loginCredentials.hasOwnProperty('username')) ? loginCredentials.username : null;
            credentials.password = (loginCredentials && loginCredentials.hasOwnProperty('password')) ? loginCredentials.password : null;
            return this.authInREST();
        },
        authInREST: function () {
            return RestService.post('auth/' + credentials.username + '.json');   // this called mocked json response by username.
        }
    }
}]
);


app.factory('TranslationLoaderService',['RestService','$q', function (RestService, $q) {
    return function (options) {
        var deferred = $q.defer();
        var restCall = RestService.get('i18n/locale-' + options.key + '.json');
        restCall.success(function (data) {
            deferred.resolve(data);
        });
        restCall.error(function () {
            deferred.reject(options.key);
        });
        return deferred.promise;
    };
}]) ;