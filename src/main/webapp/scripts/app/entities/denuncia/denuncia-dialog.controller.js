'use strict';

angular.module('denunciasApp').controller('DenunciaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Denuncia',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Denuncia) {

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

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        $scope.datePickerForFecha = {};

        $scope.datePickerForFecha.status = {
            opened: false
        };

        $scope.datePickerForFechaOpen = function($event) {
            $scope.datePickerForFecha.status.opened = true;
        };

        $scope.setFoto = function ($file, denuncia) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        denuncia.foto = base64Data;
                        denuncia.fotoContentType = $file.type;
                    });
                };
            }
        };
}]);
