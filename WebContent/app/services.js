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