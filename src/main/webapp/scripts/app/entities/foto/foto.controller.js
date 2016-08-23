'use strict';

angular.module('denunciasApp')
    .controller('FotoController', function ($scope, $state, DataUtils, Foto) {

        $scope.fotos = [];
        $scope.loadAll = function() {
            Foto.query(function(result) {
               $scope.fotos = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.foto = {
                codigo: null,
                data: null,
                dataContentType: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
