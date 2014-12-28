
//var cuwyApp = angular.module('cuwyApp', ['ui.bootstrap', 'ngSanitize', 'textAngular']);
//cuwyApp.controller('drugCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {


var cuwyApp = angular.module('holweb2secApp', ['ngSanitize']);
cuwyApp.controller('departmentController', [ '$scope',  function ($scope) {
	console.log("-----------------");
	$scope.department = department;
	console.log($scope.department);
}]);
/*
var cuwyApp = angular.module('holweb2secApp', ['ngSanitize']);
cuwyApp.controller('departmentController', [ '$scope', '$sce', function ($scope, $sce) {
	console.log("-----------------");
	$scope.department = department;
	console.log($scope.department);
}]);
 * */
