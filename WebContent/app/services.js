app.service('RestService', ['$http', 'varRestBase', function($http, varRestBase) {
        return {
            get: function(url, args, callback) {
                var rest = $http.get(varRestBase + url, args);
                if (typeof (callback) == 'function') callback();
                return rest;
            },
            post: function(url, data, headers, callback) {
                var rest = $http.post(varRestBase + url, data, headers);
                if (typeof (callback) == 'function') callback();
                return rest;
            }
        }
}]);

app.service('DevicesService', ['RestService', '$q', 'Device', 'DeviceCollection', function(RestService, $q, Device, DeviceCollection) {
        return {
            get: function(category) {
                var restCall,
                    url = 'device/list',
                    deferred = $q.defer();
                deferred.notify('Fetching the list for category ' + category + '.');
                restCall = RestService.post(url, {
                    category: category
                }, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    });
                restCall.success(function(data) {
                    deferred.resolve(new DeviceCollection(data.devices));
                });
                restCall.error(function(data) {
                    deferred.reject('Error while receiving the list');
                });
                return deferred.promise;
            },
            discover: function(category) {
                var restCall,
                    url = 'device/search',
                    deferred = $q.defer();
                deferred.notify('Discovering Devices');
                restCall = RestService.post(url, {
                    shouldShowKnownDevices: true
                }, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    });
                restCall.success(function(data) {
                    deferred.resolve(data.devices);
                });
                restCall.error(function(data) {
                    deferred.reject(data);
                });
                return deferred.promise;
            },
            trigger: function(device, dimLevel) {
                dimLevel = parseInt(dimLevel);
                var restCall,
                    url = 'device/command',
                    deferred = $q.defer(),
                    data = {
                        type: '',
                        sensorId: device.sensorId,
                        unitCode: device.unitCode,
                        dimLevel: dimLevel
                    };
                switch (dimLevel) {
                    case 0:
                        data.type = 'OFF';
                        break;
                    case 100:
                        data.type = 'ON';
                        break;
                    default:
                        data.type = 'DIM';
                        break;
                }
                deferred.notify('Triggering the device ' + device.name + '.');
                restCall = RestService.post(url, data, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });
                restCall.success(function(data) {
                    deferred.resolve(data.devices);
                });
                restCall.error(function(data) {
                    deferred.reject('Error while triggering the device');
                });
                return deferred.promise;
            },
            register: function(device, dimLevel) {
                dimLevel = parseInt(dimLevel);
                var restCall,
                    url = 'device/update',
                    deferred = $q.defer(),
                    data = {
                        type: 'ADD',
                        device: device
                    };
                deferred.notify('Registrating the device ' + device.name + '.');
                restCall = RestService.post(url, data, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });
                restCall.success(function(data) {
                    deferred.resolve('Registration success');
                });
                restCall.error(function(data) {
                    deferred.reject(data);
                });
                return deferred.promise;
            }

        }
}]);

app.service('DeviceRuleService', ['RestService', '$q', 'Device', 'DeviceCollection', function(RestService, $q, Device, DeviceCollection) {
        return {
            get: function(category) {
                var restCall,
                    url = 'rule/list',
                    deferred = $q.defer();
                deferred.notify('Fetching the list or rules  ' + category + '.');
                restCall = RestService.post(url, {
                    category: category
                }, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    });
                restCall.success(function(data) {
                    deferred.resolve(new DeviceCollection(data.devices));
                });
                restCall.error(function(data) {
                    deferred.reject('Error while receiving the list');
                });
                return deferred.promise;
            },
            register: function(device, dimLevel) {
                var restCall,
                    url = 'device/update',
                    deferred = $q.defer(),
                    data = {
                        type: 'ADD',
                        device: device
                    };
                deferred.notify('Registrating the device ' + device.name + '.');
                restCall = RestService.post(url, data, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });
                restCall.success(function(data) {
                    deferred.resolve('Registration success');
                });
                restCall.error(function(data) {
                    deferred.reject(data);
                });
                return deferred.promise;
            }

        }
}]);