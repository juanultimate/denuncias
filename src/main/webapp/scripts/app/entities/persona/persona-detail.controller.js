'use strict';

angular.module('denunciasApp')
    .controller('PersonaDetailController', function ($scope, $rootScope, $stateParams, entity, Persona) {
        $scope.persona = entity;
        $scope.load = function (id) {
            Persona.get({id: id}, function(result) {
                $scope.persona = result;
            });
        };
        var unsubscribe = $rootScope.$on('denunciasApp:personaUpdate', function(event, result) {
            $scope.persona = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
