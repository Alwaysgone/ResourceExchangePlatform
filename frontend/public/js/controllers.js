'use strict';

/* Controllers */

angular.module('rep-frontend.controllers', [
	//'app.config'
])
.controller('AppCtrl', function ($scope, $http) {
	$scope.name = 'Mike';
  })
.controller('MainController', function($scope, $location) {
	$scope.changeView = function(view){
		$location.path(view);
	}
})
.controller('NominationController', ['$scope', '$http', function ($scope, $http) {
    $http.get('http://192.168.99.100:8080/api' + '/nominations')
    .then(function(response) {
        console.log('Successfully retrieved nominations');
		$scope.allocations = response.data.allocations;
		$scope.nominations = response.data.nominations;
    }, function(response) {
		console.log('An error occurred while fetching nominations status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
        $scope.allocations = [];
		$scope.nominations = [];
	});
}])
.controller('InputController', function($scope, $http) {
	$scope.submitNomination = function(nominations){
		$http.get('http://192.168.99.100:8080/api' + '/nominations')
		.then(function(response) {
			console.log('Successfully retrieved nominations');
			$scope.allocations = response.data.allocations;
			$scope.nominations = response.data.nominations;
		}, function(response) {
			console.log('An error occurred while fetching nominations status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
			$scope.allocations = [];
			$scope.nominations = [];
		});
	}
});
