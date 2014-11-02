describe('Unit test for controller: StartTest', function () {

    var scope;

    var RestTriggerError = false;
    var RestServiceRegistry = null;
    var RestExpect = null;

    var RestServiceMock = {
        get: function (url) {
            RestServiceRegistry = {method: 'get', url: url};
            return {
                success: function (callback) {
                    if (!RestTriggerError) callback(RestExpect);
                },
                error: function (callback) {
                    if (RestTriggerError) callback(RestExpect);
                }

            }
        }
    };

    beforeEach(function () {
        module('app');

        RestExpect = null;
        RestTriggerError = false;
        RestServiceRegistry = null;

        inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            $controller("StartController", { $scope: scope, RestService: RestServiceMock});
        });
    });

    it('should have data placeholder with predefined data', function () {
        expect(scope.data).toBe('data will be here');
    });

    it('should call REST service and return data', function () {
        RestExpect = {text: 'SUCCESS TEXT'};
        scope.go.fetch();
        expect(RestServiceRegistry.method).toBe('get');
        expect(RestServiceRegistry.url).toBe('test.json');
        expect(scope.data).toBe('SUCCESS TEXT');
    });

    it('should call REST service and display error if fail', function () {
        RestExpect = {text: 'SUCCESS TEXT'};
        RestTriggerError = true;
        scope.go.fetch();
        expect(RestServiceRegistry.method).toBe('get');
        expect(RestServiceRegistry.url).toBe('test.json');
        expect(scope.data).toBe('Some error occured');
    });


});
