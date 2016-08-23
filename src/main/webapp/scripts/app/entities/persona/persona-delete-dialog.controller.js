'use strict';

angular.module('denunciasApp')
	.controller('PersonaDeleteController', function($scope, $uibModalInstance, entity, Persona) {

        $scope.persona = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Persona.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
