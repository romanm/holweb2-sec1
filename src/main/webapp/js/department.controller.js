var cuwyApp = angular.module('holweb2secApp', ['ngSanitize']);
cuwyApp.controller('departmentController', [ '$scope', '$http', '$filter', function ($scope, $http, $filter) {
	console.log('departmentController');
	$scope.departmentName = document.getElementById("departmentName").value
	console.log($scope.departmentName);
	$scope.orders2 = document.getElementById("orders2").value
	console.log($scope.orders2);
	$scope.seek = document.getElementById("seekParam").value;
	console.log($scope.seek);

	$http({ method : 'GET', url : '/model/department/v.' + $scope.departmentName + '.json.js'
	}).success(function(data, status, headers, config) {
		$scope.department = data
		console.log($scope.department);
//		inits2index();
	}).error(function(data, status, headers, config) {
		$scope.error = data
	});

	if($scope.orders2 == "personal"){
		$http({ method : 'GET', url : '/model/personalListHolWeb.json.js'
		}).success(function(data, status, headers, config) {
			$scope.personalList = data
			pullOutDepartmentPersonal();
		}).error(function(data, status, headers, config) {
			$scope.error = data
		});
	}

	pullOutDepartmentPersonal = function(){
		$scope.department.dpl = [];
		$scope.personalList.pl.forEach(function(personal) {
			if($scope.department.department_id === personal.department_id
				|| $scope.department.department_id2 === personal.department_id
			){
				$scope.department.dpl.push(personal);
			}
		})
		$scope.department.dpl = $filter('orderBy')($scope.department.dpl, 'personal_username')

	}

	inits2index = function(){
		angular.forEach($scope.department.docBlock, function(s2, s2index){
			s2.idx = "section-"+s2index ;
			console.log(s2);
			angular.forEach(s2.docBlock, function(s3, s3index){
				if(s3.p){
					s3.idx = "p-"+s2index+"-"+s3index ;
				}else
				if(s3.p){
					
				}
			} );
		} );
	}

}

]);
