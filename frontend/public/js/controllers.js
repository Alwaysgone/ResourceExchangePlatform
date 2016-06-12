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
	$http.get('http://192.168.99.100:8080/api' + '/regions')
    .then(function(response) {
        console.log('Successfully retrieved regions');
		$scope.regions = response.data;
    }, function(response) {
		console.log('An error occurred while fetching regions status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
        $scope.regions = [];
	});
	$scope.nominations=[];
	$scope.nomination={id: null, resource: null, quantity: null, unit: null, direction: null};
	$scope.submittedNominationIds=[];
	$scope.submitNominations = function(nominations){
		$http.post('http://192.168.99.100:8080/api' + '/nominations', nominations)
		.then(function(response) {
			// TODO
			console.log('Successfully submitted nominations');
			$scope.allocations = response.data.allocations;
			$scope.nominations = response.data.nominations;
		}, function(response) {
			console.log('An error occurred while submitting nominations status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
			$scope.allocations = [];
			$scope.nominations = [];
		});
	}
	$scope.submitNomination = function(){
		var nominations = [];
		nominations.push($scope.nomination);
		//TODO use login name
		var resourceDTO = {participant: 'participant1', nominations: nominations};
		$http.post('http://192.168.99.100:8080/api' + '/nominations', resourceDTO)
		.then(function(response) {
			console.log('Successfully submitted nominations response.data: ' + response.data + ', response.data.length: ' + response.data.length);
			$scope.submittedNominationIds=response.data;
		}, function(response) {
			console.log('An error occurred while submitting nominations status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
			$scope.submittedNominationIds=[];
		});
	}
});
