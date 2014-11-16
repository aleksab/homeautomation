(function() {
    'use strict';
    app.factory('DeviceRuleThenListItem', [function() {

            var Scheme = function() {
                return {
                    id: null, // id of then
                    action: 'ON', // ON|OFF|DIM|WAIT_OFF,	// which action to apply. Turn light on, off, dim the light of wait a specific time before turning off
                    deviceId: 0, // which device this should be applied for
                    value: 0 // dim level or minutes to wait (WAIT_OFF)
                };
            };

            return function(rawItem) {
                var item = new Scheme();
                if (rawItem) {
                    for (var k in rawItem) {
                        if (item.hasOwnProperty(k)) {
                            Scheme[k] = rawItem[k];
                        }
                    }
                }
                return item;
            }

        }
    ]);

    app.factory('DeviceRuleConditionListItem', [function() {
            var Scheme = function() {
                return {
                    id: null, // id of condition
                    condition: 'TIME_OF_DAY', // TIME_OF_DAY|DAY_OF_WEEK|DELAY_MINUTES|FROM_TO_TIME|FROM_TO_DAY_OF_WEEK,	// which condition to use.
                    timeOfDay: "00:00:00", // what time of day
                    dayOfWeek: "1", // which day of the week
                    fromTime: "00:00:00", // from time
                    toTime: "00:00:00", // to time
                    fromDayOfWeek: "1", //from day of the week
                    toDayOfWeek: "1" // to day of the week
                }
            }

            return function(rawItem) {
                var item = new Scheme();
                if (rawItem) {
                    for (var k in rawItem) {
                        if (item.hasOwnProperty(k)) {
                            Scheme[k] = rawItem[k];
                        }
                    }
                }
                return item;
            }

    }]);

    app.factory('DeviceRule', [function() {

            var Scheme = function() {
                return {
                    id: null, // if for rule (for delete and edit)
                    name: "New rule", // name of rule
                    active: false, // if rule is enabled or not
                    whenDeviceId: 0, // which device to trigger rule on
                    whenAction: 'ON', // ON|OFF|TIME,	// when the trigger should kick in. On light on, off or a specific time
                    whenTime: "00:00:00", // if TIME is used for action then this specified the time of day when trigger should kick in
                    thenList: [], // list of actions to apply when rule is triggered
                    conditionList: [] // list of condition that must be valid for actions to be applied
                }
            }

            return function(rawItem) {
                var item = new Scheme();
                if (rawItem) {
                    for (var k in rawItem) {
                        if (item.hasOwnProperty(k)) {
                            Scheme[k] = rawItem[k];
                        }
                    }
                }
                return item;
            }

    }]);

    app.factory('Device', [function() {

            var Scheme = function() {
                return {
                    sensorId: 0,
                    unitCode: 0,
                    name: ''
                }
            }

            return function(rawItem) {
                var item = new Scheme();
                if (rawItem) {
                    if (rawItem.hasOwnProperty('sensorId'))
                        item.sensorId = rawItem.sensorId;
                    if (rawItem.hasOwnProperty('name'))
                        item.name = rawItem.name;
                    if (rawItem.hasOwnProperty('unitCode'))
                        item.unitCode = rawItem.unitCode;
                }
                return item;
            }

    }]);

    app.factory('DeviceCollection', ['Device', function(Device) {

            return function(rawCollection) {
                var list = [];
                if (rawCollection && angular.isArray(rawCollection)) {
                    for (var i in rawCollection) {
                        if (rawCollection[i]) {
                            list.push(new Device(rawCollection[i]));
                        }
                    }
                }
                return list;
            }

    }]);
}());