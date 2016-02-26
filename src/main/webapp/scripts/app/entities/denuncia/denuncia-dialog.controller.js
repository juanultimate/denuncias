'use strict';

angular.module('denunciasApp').controller('DenunciaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Denuncia',
        function($scope, $stateParams, $uibModalInstance, entity, Denuncia) {

        $scope.denuncia = entity;
        $scope.load = function(id) {
            Denuncia.get({id : id}, function(result) {
                $scope.denuncia = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('denunciasApp:denunciaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.denuncia.id != null) {
                Denuncia.update($scope.denuncia, onSaveSuccess, onSaveError);
            } else {
                Denuncia.save($scope.denuncia, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForFecha = {};

        $scope.datePickerForFecha.status = {
            opened: false
        };

        $scope.datePickerForFechaOpen = function($event) {
            $scope.datePickerForFecha.status.opened = true;
        };
}]);
