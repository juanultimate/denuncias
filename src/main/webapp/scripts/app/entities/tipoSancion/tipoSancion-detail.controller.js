'use strict';

angular.module('denunciasApp')
    .controller('TipoSancionDetailController', function ($scope, $rootScope, $stateParams, entity, TipoSancion) {
        $scope.tipoSancion = entity;
        $scope.load = function (id) {
            TipoSancion.get({id: id}, function(result) {
                $scope.tipoSancion = result;
            });
        };
        var unsubscribe = $rootScope.$on('denunciasApp:tipoSancionUpdate', function(event, result) {
            $scope.tipoSancion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
