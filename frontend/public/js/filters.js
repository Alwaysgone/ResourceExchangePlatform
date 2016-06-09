'use strict';

/* Filters */

angular.module('rep-frontend.filters', []).
  filter('interpolate', function (version) {
    return function (text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    };
  })
  .filter('rangeFilter', function() {
		return function(item, scope) {
			var lowerBound = scope.resourceFilter.lowerBound;
			var upperBound = scope.resourceFilter.upperBound;
			if(typeof lowerBound !== 'undefined' && lowerBound !== '') {
				if(items.amount < lowerBound) {
					console.log('Skipping amount ' + items[i].amount + ' because it is too small');
				}
			}
			if(typeof upperBound !== 'undefined' && upperBound !== '') {
				if(items[i].amount > upperBound) {
					console.log('Skipping amount ' + items[i].amount + ' because it is too large');
				}
			}
			return item;
		};
	});
  /*
  .filter('rangeFilter', function() {
    return function(items, lowerBound, upperBound) {
		var result = [];
		if((typeof lowerBound === 'undefined' || lowerBound === '') && (typeof upperBound === 'undefined' || upperBound === '')) {
			console.log('Returning items without filtering becase lowerBound and upperBound are not set');
			return items;
		}
		if(typeof items !== 'undefined') {
			console.log('Filtering ' + items.length + ' items lowerBound: ' + lowerBound + ', upperBound: ' + upperBound);
			for (var i=0; i<items.length; i++){
				if(typeof lowerBound !== 'undefined' && lowerBound !== '') {
					if(items[i].amount < lowerBound) {
						console.log('Skipping amount ' + items[i].amount + ' because it is too small');
						continue;
					}
				}
				if(typeof upperBound !== 'undefined' && upperBound !== '') {
					if(items[i].amount > upperBound) {
						console.log('Skipping amount ' + items[i].amount + ' because it is too large');
						continue;
					}
				}
				console.log('Pushing item ' + items[i].id + ' to result');
				result.push(items[i]);
			}
		} else {
			console.log('Items where undefined');
		}
        return result;
	};
  });*/
