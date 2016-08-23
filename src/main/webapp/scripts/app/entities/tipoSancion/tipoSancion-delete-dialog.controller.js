'use strict';

angular.module('denunciasApp')
	.controller('TipoSancionDeleteController', function($scope, $uibModalInstance, entity, TipoSancion) {

        $scope.tipoSancion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TipoSancion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
