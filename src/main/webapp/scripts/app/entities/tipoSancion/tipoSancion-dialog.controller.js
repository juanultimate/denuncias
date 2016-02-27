'use strict';

angular.module('denunciasApp').controller('TipoSancionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TipoSancion',
        function($scope, $stateParams, $uibModalInstance, entity, TipoSancion) {

        $scope.tipoSancion = entity;
        $scope.load = function(id) {
            TipoSancion.get({id : id}, function(result) {
                $scope.tipoSancion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('denunciasApp:tipoSancionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tipoSancion.id != null) {
                TipoSancion.update($scope.tipoSancion, onSaveSuccess, onSaveError);
            } else {
                TipoSancion.save($scope.tipoSancion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
