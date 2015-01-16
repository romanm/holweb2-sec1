cuwyApp.controller('departmentController', [ '$scope', '$http', '$filter', function ($scope, $http, $filter) {
	console.log('departmentController');
	modelAttToAjs($scope);
	$http({ method : 'GET', url : '/model/department/v.' + $scope.departmentName + '.json.js'
	}).success(function(data, status, headers, config) {
		$scope.department = data
		console.log($scope.department);
	}).error(function(data, status, headers, config) {
		$scope.error = data
	});

	$http({ method : 'GET', url : '/model/personalListHolWeb.json.js'
	}).success(function(data, status, headers, config) {
		$scope.personalList = data
		pullOutDepartmentPersonal();
	}).error(function(data, status, headers, config) {
		$scope.error = data
	});

	pullOutDepartmentPersonal = function(){
		$scope.department.dspl = [];
		$scope.department.dpl = [];
		$scope.personalList.pl.forEach(function(personal) {
			if($scope.department.department_id === personal.department_id
				|| $scope.department.department_id2 === personal.department_id
			){
				if(personal.position_id == 3){//sheff
					$scope.department.dspl.splice(0,0,personal);
				}else if(personal.position_id == 2){//admin
					$scope.department.dspl.push(personal);
				}else{
					$scope.department.dpl.push(personal);
				}
			}
		})
		console.log($scope.department.dspl);
		$scope.department.dpl = $filter('orderBy')($scope.department.dpl, 'personal_username')
	}

}]);
