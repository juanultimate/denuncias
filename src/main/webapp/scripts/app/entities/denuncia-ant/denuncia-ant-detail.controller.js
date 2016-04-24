'use strict';

angular.module('denunciasApp')
    .controller('DenunciaAntDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Denuncia, Canton) {
        $scope.denuncia = entity;
        $scope.cantons = [];
        $scope.loadSanciones = function(){
            $scope.question= new Object();
            $scope.question.choices = [];
            $scope.question.choices[0] = Object.create({}, { text: { value: "Es válido"}, sancionable : {value:true}});
            $scope.question.choices[1] = Object.create({}, { text: { value: "No es válido"}, sancionable : {value:false}});
        };
        $scope.loadPagados = function(){
            $scope.pago= new Object();
            $scope.pago.choices = [];
            $scope.pago.choices[0] = Object.create({}, { text: { value: "Pagado"}, pagado : {value:true}});
            $scope.pago.choices[1] = Object.create({}, { text: { value: "No Pagado"}, pagado : {value:false}});
        };


        $scope.loadSanciones();
        $scope.loadPagados();

        $scope.load = function (id) {
            Denuncia.get({id: id}, function(result) {
                $scope.denuncia = result;
            });

        };
        var unsubscribe = $rootScope.$on('denunciasApp:denunciaAntUpdate', function(event, result) {
            $scope.denuncia = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;

        Canton.query(function(result) {
            $scope.cantons= result;
        });

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.denuncia.id != null) {
                Denuncia.update($scope.denuncia, onSaveSuccess, onSaveError);
            } else {
                Denuncia.save($scope.denuncia, onSaveSuccess, onSaveError);
            }
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('denunciasApp:denunciaAntUpdate', result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.descargarr=function(){
            console.log("dfasdasdf");
            html2canvas($(".mapa"), {
                onrendered: function(canvas) {
                    document.body.appendChild(canvas);
                    canvas.toBlob(function(blob) {
                        saveAs(blob, "Dashboard.png");
                    });
                }
            });
        };


        $scope.descargar = function ($event) {
            html2canvas($(".container-fluid"), {
                onrendered: function (canvas) {
                    //theCanvas = canvas;
                    document.body.appendChild(canvas);
                    var img    = canvas.toDataURL();
                    $event.target.href=img;
                    $event.target.download="testing.png";
                    //document.write('<img src="'+img+'"/>');

                    //$("#img-out").append(canvas);
                    // Clean up
                    //document.body.removeChild(canvas);
                }
            });
        };





    });
