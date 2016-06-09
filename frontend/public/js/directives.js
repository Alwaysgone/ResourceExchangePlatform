'use strict';

/* Directives */

angular.module('rep-frontend.directives', []).
  directive('appVersion', function (version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  });
