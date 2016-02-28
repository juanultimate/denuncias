'use strict';

angular.module('denunciasApp')
    .controller('PersonaController', function ($scope, $state, Persona) {

        $scope.personas = [];
        $scope.loadAll = function() {
            Persona.query(function(result) {
               $scope.personas = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.persona = {
                nombre: null,
                edad: null,
                id: null
            };
        };
    });
