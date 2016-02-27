'use strict';

angular.module('denunciasApp')
	.controller('DenunciaDeleteController', function($scope, $uibModalInstance, entity, Denuncia) {

        $scope.denuncia = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Denuncia.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
