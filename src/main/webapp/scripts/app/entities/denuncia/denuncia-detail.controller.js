'use strict';

angular.module('denunciasApp')
    .controller('DenunciaDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Denuncia, Canton, $location, uiGmapGoogleMapApi) {
        $scope.denuncia = entity;
        $scope.cantons = [];
        $scope.loadSanciones = function(){
            $scope.question= new Object();
            $scope.question.choices = [];
            $scope.question.choices[0] = Object.create({}, { text: { value: "Es válido"}, sancionable : {value:true}});
            $scope.question.choices[1] = Object.create({}, { text: { value: "No es válido"}, sancionable : {value:false}});
        };
        $scope.loadSanciones();
        var unsubscribe = $rootScope.$on('denunciasApp:denunciaUpdate', function(event, result) {
            $scope.denuncia = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;

        Canton.query(function(result) {
            $scope.cantons= result;
        });

        $scope.save = function () {
            $scope.denuncia.estado = "Enviada";
            $scope.isSaving = true;
            if ($scope.denuncia.id != null) {
                Denuncia.update($scope.denuncia, onSaveSuccess, onSaveError);
            } else {
                Denuncia.save($scope.denuncia, onSaveSuccess, onSaveError);
            }
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('denunciasApp:denunciaUpdate', result);
            $scope.isSaving = false;
            $location.path( "/denuncias" );
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        uiGmapGoogleMapApi.then(function(maps) {
            $scope.denuncia.$promise.then(function(data) {
                $scope.coords = {"center": {"latitude": data.latitud, "longitude": data.longitud}};
            });

        });
    });
