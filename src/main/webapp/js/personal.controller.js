var cuwyApp = angular.module('holweb2secApp', ['ngSanitize']);
cuwyApp.controller('personalController', [ '$scope', '$http', '$filter', function ($scope, $http, $filter) {
	console.log('personalController');
	$scope.personalUrl = document.getElementById("personalUrl").value;
	console.log($scope.personalUrl);

	$http({ method : 'GET', url : '/model/personalListHolWeb.json.js'
	}).success(function(data, status, headers, config) {
		$scope.personalList = data;
		if($scope.personalUrl){
			initSpkPersonal();
		}else{
			orderABC()
		}
		console.log($scope.spkPersonal);
	}).error(function(data, status, headers, config) {
		$scope.error = data
	});

	orderABC = function(){
		var oAbc = $filter('orderBy')($scope.personalList.pl, 'personal_username')
		$scope.personalList.pl = oAbc;
	}
	initSpkPersonal = function(){
		$scope.personalList.pl.forEach(function(p) {
			if(p.personal_url.toLowerCase() == $scope.personalUrl.toLowerCase()){
				$scope.spkPersonal = p;
			}
		})
	};

}]);
