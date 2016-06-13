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
.controller('NominationController', ['$scope', '$http', 'endpoint', function ($scope, $http, endpoint) {
    /*$http.get('http://192.168.99.100:8080/api' + '/nominations')
    .then(function(response) {
        console.log('Successfully retrieved nominations');
		$scope.nominations = response.data.nominations;
    }, function(response) {
		console.log('An error occurred while fetching nominations status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
		$scope.nominations = [];
	});*/
	function success(response) {
		$scope.nominations = response.data.nominations;
	}
	$scope.query = {
		limit: 5,
		page: 1
	};
	$scope.getNominations = function () {
		$scope.promise =  $http.get(endpoint + '/nominations',{params: $scope.query}).then(success);
	};
	
	$scope.getNominations();
}])
.controller('InputController', function($scope, $http, endpoint) {
	$http.get(endpoint + '/regions')
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
		var resourceDTO = {participant: 'participant1', nominations: nominations};
		$http.post(endpoint + '/nominations', resourceDTO)
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
	};
	$scope.submitNomination = function(){
		var nominations = [];
		nominations.push($scope.nomination);
		//TODO use login name
		var resourceDTO = {participant: 'participant1', nominations: nominations};
		$http.post(endpoint + '/nominations', resourceDTO)
		.then(function(response) {
			console.log('Successfully submitted nominations response.data: ' + response.data + ', response.data.length: ' + response.data.length);
			$scope.submittedNominationIds=response.data;
		}, function(response) {
			console.log('An error occurred while submitting nominations status:' + response.status + ', data: ' + response.data + ', statusText : ' + response.statusText);
			$scope.submittedNominationIds=[];
		});
	};
	$scope.addNomination = function() {
		$scope.nominations.push(angular.extend({}, $scope.nomination));
	};
});
