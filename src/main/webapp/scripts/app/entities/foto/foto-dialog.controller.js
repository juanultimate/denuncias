'use strict';

angular.module('denunciasApp').controller('FotoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Foto',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Foto) {

        $scope.foto = entity;
        $scope.load = function(id) {
            Foto.get({id : id}, function(result) {
                $scope.foto = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('denunciasApp:fotoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.foto.id != null) {
                Foto.update($scope.foto, onSaveSuccess, onSaveError);
            } else {
                Foto.save($scope.foto, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.setData = function ($file, foto) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        foto.data = base64Data;
                        foto.dataContentType = $file.type;
                    });
                };
            }
        };
}]);
