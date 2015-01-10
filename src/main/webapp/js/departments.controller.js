var cuwyApp = angular.module('holweb2secApp', ['ngSanitize']);
cuwyApp.controller('departmentsController', [ '$scope', '$http', function ($scope, $http) {
	console.log('departmentsController');
	$http({ method : 'GET', url : '/model/vids.json.js'
	}).success(function(data, status, headers, config) {
		$scope.vids = data;
		console.log($scope.vids);
	}).error(function(data, status, headers, config) {
		$scope.error = data;
	});
}]);
