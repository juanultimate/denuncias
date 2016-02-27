'use strict';

angular.module('denunciasApp')
    .controller('DenunciaDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Denuncia) {
        $scope.denuncia = entity;
        $scope.load = function (id) {
            Denuncia.get({id: id}, function(result) {
                $scope.denuncia = result;
            });
        };
        var unsubscribe = $rootScope.$on('denunciasApp:denunciaUpdate', function(event, result) {
            $scope.denuncia = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
