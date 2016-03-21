'use strict';

angular.module('denunciasApp')
    .controller('DenunciaAntController', function ($scope, $state, DataUtils, Denuncia, ParseLinks) {

        $scope.denuncias = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Denuncia.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id'], estado:'Enviada'}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.denuncias = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.denuncia = {
                codigo: null,
                fecha: null,
                sancionable: null,
                latitud: null,
                longitud: null,
                placa: null,
                estado: null,
                foto: null,
                fotoContentType: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;









    });
