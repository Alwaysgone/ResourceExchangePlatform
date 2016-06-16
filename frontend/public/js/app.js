'use strict';

// Declare app level module which depends on filters, and services


angular.module('rep-frontend', [
  'ngRoute',
  'ngAria',
  'ngMaterial',
  'md.data.table',
  'rep-frontend.controllers',
  'rep-frontend.filters',
  'rep-frontend.services',
  'rep-frontend.directives'
]).
constant("endpoint","http://192.168.99.100:8080/api").
config(function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('!');
	$locationProvider.html5Mode(true);
	$routeProvider
	.when('/nominations', {
		templateUrl: 'content/nominations.html',
		controller: 'NominationController'
	})
	.when('/input', {
		templateUrl: 'content/input.html',
		controller: 'InputController'
	})
	.otherwise({
		redirectTo: '/nominations'
	});	
});
