'use strict';

angular.module('denunciasApp')
    .controller('DenunciaController', function ($scope, $state, Denuncia, ParseLinks) {

        $scope.denuncias = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Denuncia.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
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
                canton: null,
                fecha: null,
                sancion: null,
                estado: null,
                distrito: null,
                tipoSancion: null,
                placa: null,
                id: null
            };
        };
    });
