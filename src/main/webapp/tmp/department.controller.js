var cuwyApp = angular.module('holweb2secApp', ['ngSanitize']);
cuwyApp.controller('departmentController', [ '$scope', '$http', function ($scope, $http) {
	console.log('departmentController');
	var departmentName = document.getElementById("departmentName").value
	console.log(departmentName);
	$http({ method : 'GET', url : '/model/department/v.' + departmentName + '.json.js'
	}).success(function(data, status, headers, config) {
		$scope.department = data
		console.log($scope.department);
	}).error(function(data, status, headers, config) {
		$scope.error = data
		console.log(data);
	});
}]);
