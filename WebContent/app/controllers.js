app.controller('HomeController', ['$scope', 'DevicesService',  function ($scope, DevicesService) {        

	var clearInputs = function(){
		$scope.feedback = {};
	};


	$scope.getDevices = function(){

		clearInputs();

		$scope.devices = [];

		var devicesPromise = DevicesService.get();

		devicesPromise.then(
			function(data){
				$scope.devices = data;
			},
			function(data){
				$scope.feedback.error = data;
			},
			function(data){
				$scope.feedback.notification = data;
			}
		);

	};

	$scope.triggerDevice = function(device, dimLevel){

		clearInputs();

		var devicesPromise = DevicesService.trigger(device, dimLevel);

		devicesPromise.then(
			function(data){
				$scope.feedback.success = data;
			},
			function(data){
				$scope.feedback.error = data;
			},
			function(data){
				$scope.feedback.notification = data;
			}
		);

	};

	(function(){
		clearInputs();
	}())

}]);