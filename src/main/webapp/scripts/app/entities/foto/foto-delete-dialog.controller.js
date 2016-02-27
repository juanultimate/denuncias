'use strict';

angular.module('denunciasApp')
	.controller('FotoDeleteController', function($scope, $uibModalInstance, entity, Foto) {

        $scope.foto = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Foto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
