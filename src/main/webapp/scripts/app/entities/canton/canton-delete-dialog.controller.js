'use strict';

angular.module('denunciasApp')
	.controller('CantonDeleteController', function($scope, $uibModalInstance, entity, Canton) {

        $scope.canton = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Canton.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
