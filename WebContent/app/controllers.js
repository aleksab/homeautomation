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
		$scope.getDevices();
	}())

}]);

app.controller('DiscoverController', ['$scope', 'DevicesService',  function ($scope, DevicesService) {        

	$scope.feedback = {
		loading:true
	};

	$scope.devices = null;

	$scope.registerDevice = function(device){

		var devicesPromise = DevicesService.register(device);

		devicesPromise.then(
			function(data){
				device.registrated = true;
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

	$scope.startDiscoverDevices = function(){
		$scope.feedback.loading=true;
		$scope.discoverDevices();
	};

	$scope.stopDiscoverDevices = function(){
		$scope.feedback.loading=false;
	};

	$scope.discoverDevices = function(){

		var devicesPromise = DevicesService.discover();

		devicesPromise.then(
			function(data){
				if (data.length>0 && $scope.devices===null){
					$scope.devices={};
				}
				for(var i in data){
					$scope.devices[data[i].sensorId+data[i].unitCode]=data[i];
				};
				if ($scope.feedback.loading==true){
					$scope.discoverDevices();
				}
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
		$scope.discoverDevices();
	}());


}]);