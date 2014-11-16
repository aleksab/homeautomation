app.controller('RulesController', ['$scope', 'DeviceRuleService', 'DevicesService', 'DeviceRule', 'DeviceRuleThenListItem', function($scope, DeviceRuleService, DevicesService, DeviceRule, DeviceRuleThenListItem) {

        $scope.feedback = {};
        $scope.whenActions = [
            {
                code: 'ON',
                name: 'Is turned on'
            },
            {
                code: 'OFF',
                name: 'Is turned off'
            },
            {
                code: 'TIME',
                name: 'And the time is...'
            },

        ];
        $scope.thenActions = [
            {
                code: 'ON',
                name: 'Turn on'
            },
            {
                code: 'OFF',
                name: 'Turn off'
            },
            {
                code: 'WAIT_OFF',
                name: 'Turn off after'
            },
            {
                code: 'DIM',
                name: 'Dim'
            }

        ];
        $scope.getDevices = function() {

            $scope.devices = [];

            var devicesPromise = DevicesService.get();

            devicesPromise.then(function(data) {
                $scope.devices = data;
            }, function(data) {
                    $scope.feedback.error = data;
                }, function(data) {
                    $scope.feedback.notification = data;
                }
            );

        };

        $scope.ruleManager = {
            get: function() {
                $scope.rules = [];
                var rulePromise = DeviceRuleService.get();

                rulePromise.then(function(data) {
                    $scope.rules = data;
                }, function(data) {
                        $scope.feedback.error = data;
                    }, function(data) {
                        $scope.feedback.notification = data;
                    }
                );
            },
            add: function() {
                var newDevice = new DeviceRule();
                newDevice.expanded = true;
                $scope.rules.push(newDevice);
            },
            remove: function(index) {
                $scope.rules.splice(index, 1);
                rule.feedback = {};
                var rulePromise = DeviceRuleService.save(rule);

                rulePromise.then(function(data) {
                    rule.feedback.success = data;
                }, function(data) {
                        rule.feedback.error = data;
                    }, function(data) {
                        rule.feedback.notification = data;
                    }
                );
            },
            save: function(rule) {

                rule.feedback = {};
                var rulePromise = DeviceRuleService.save(rule);

                rulePromise.then(function(data) {
                    rule.feedback.success = data;
                }, function(data) {
                        rule.feedback.error = data;
                    }, function(data) {
                        rule.feedback.notification = data;
                    }
                );

            },
            addDevice: function(rule) {
                var newRuleDevice = new DeviceRuleThenListItem();
                rule.thenList.push(newRuleDevice);
            },
            removeDevice: function(rule, index) {
                rule.splice(index, 1);
            }
        };

        (function() {
            $scope.getDevices();
            $scope.ruleManager.get();
            $scope.ruleManager.add();
            $scope.thenActionValues = [];
            for (var i=0;i<=100;i++){
            $scope.thenActionValues.push(i);

            }
        }());

}]);

app.controller('HomeController', ['$scope', 'DevicesService', function($scope, DevicesService) {

        var clearInputs = function() {
            $scope.feedback = {};
        };

        $scope.getDevices = function() {

            clearInputs();

            $scope.devices = [];

            var devicesPromise = DevicesService.get();

            devicesPromise.then(function(data) {
                $scope.devices = data;
            }, function(data) {
                    $scope.feedback.error = data;
                }, function(data) {
                    $scope.feedback.notification = data;
                }
            );

        };

        $scope.triggerDevice = function(device, dimLevel) {

            clearInputs();

            var devicesPromise = DevicesService.trigger(device, dimLevel);

            devicesPromise.then(function(data) {
                $scope.feedback.success = data;
            }, function(data) {
                    $scope.feedback.error = data;
                }, function(data) {
                    $scope.feedback.notification = data;
                }
            );

        };

        (function() {
            $scope.getDevices();
        }())

}]);




app.controller('DiscoverController', ['$scope', 'DevicesService', function($scope, DevicesService) {

        $scope.feedback = {
            loading: true
        };

        $scope.devices = null;

        $scope.registerDevice = function(device) {

            var devicesPromise = DevicesService.register(device);

            devicesPromise.then(function(data) {
                device.registrated = true;
                $scope.feedback.success = data;
            }, function(data) {
                    $scope.feedback.error = data;
                }, function(data) {
                    $scope.feedback.notification = data;
                }
            );

        };

        $scope.startDiscoverDevices = function() {
            $scope.feedback.loading = true;
            $scope.discoverDevices();
        };

        $scope.stopDiscoverDevices = function() {
            $scope.feedback.loading = false;
        };

        $scope.discoverDevices = function() {

            var devicesPromise = DevicesService.discover();

            devicesPromise.then(function(data) {
                if (data.length > 0 && $scope.devices === null) {
                    $scope.devices = {};
                }
                for (var i in data) {
                    $scope.devices[data[i].sensorId + data[i].unitCode] = data[i];
                }
                ;
                if ($scope.feedback.loading == true) {
                    $scope.discoverDevices();
                }
            }, function(data) {
                    $scope.feedback.error = data;
                }, function(data) {
                    $scope.feedback.notification = data;
                }
            );

        };

        (function() {
            $scope.discoverDevices();
        }());


}]);