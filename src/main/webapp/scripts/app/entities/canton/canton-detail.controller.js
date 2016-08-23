'use strict';

angular.module('denunciasApp')
    .controller('CantonDetailController', function ($scope, $rootScope, $stateParams, entity, Canton) {
        $scope.canton = entity;
        $scope.load = function (id) {
            Canton.get({id: id}, function(result) {
                $scope.canton = result;
            });
        };
        var unsubscribe = $rootScope.$on('denunciasApp:cantonUpdate', function(event, result) {
            $scope.canton = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
