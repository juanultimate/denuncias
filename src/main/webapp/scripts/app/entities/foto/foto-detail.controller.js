'use strict';

angular.module('denunciasApp')
    .controller('FotoDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Foto) {
        $scope.foto = entity;
        $scope.load = function (id) {
            Foto.get({id: id}, function(result) {
                $scope.foto = result;
            });
        };
        var unsubscribe = $rootScope.$on('denunciasApp:fotoUpdate', function(event, result) {
            $scope.foto = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
