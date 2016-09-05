'use strict';

angular.module('denunciasApp')
    .controller('DenunciaAntDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Denuncia, Canton, uiGmapGoogleMapApi) {
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

        $scope.incializarBotonDescarga = function () {
            html2canvas($(".container-fluid"), {
                onrendered: function (canvas) {
                    $scope.img    = canvas.toDataURL();
                }
            });
        };


        $scope.loadSanciones();
        $scope.loadPagados();
        $scope.incializarBotonDescarga();

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



        $scope.zip= function() {
            var zip = new JSZip();
            html2canvas($(".container-fluid"), {
                onrendered: function (canvas) {
                    var image = new Image();
                    image.src = canvas.toDataURL("image/png");
                    zip.file("general.png", image.src.substr(image.src.indexOf(',')+1), {base64: true});

                    html2canvas($(".foto"), {
                        onrendered: function (canvas) {
                            var image = new Image();
                            image.src = canvas.toDataURL("image/png");
                            zip.file("alerta.png", image.src.substr(image.src.indexOf(',') + 1), {base64: true});


                            html2canvas($(".mapa"), {
                                onrendered: function (canvas) {
                                    var image = new Image();
                                    image.src = canvas.toDataURL("image/png");
                                    zip.file("mapa.png", image.src.substr(image.src.indexOf(',')+1), {base64: true});
                                    zip.generateAsync({type: "blob"})
                                        .then(function (content) {
                                            saveAs(content, $scope.denuncia.codigo + ".zip");
                                        });

                                }
                            });


                        }
                    });


                }
            });
        }

        // $scope.map = {
        //     "zoom": 18,
        //     "marker": {
        //         "id":0,
        //         "options": {
        //             "icon": {
        //                 "anchor": new google.maps.Point(36,36),
        //                 "origin": new google.maps.Point(0,0),
        //                 "scaledSize": new google.maps.Size(36,36),
        //                 "url": 'https://maps.google.com/mapfiles/ms/icons/green-dot.png'
        //             }
        //         }
        //     }
        // };
        uiGmapGoogleMapApi.then(function(maps) {
            $scope.denuncia.$promise.then(function(data) {
                $scope.coords = {"center": {"latitude": data.latitud, "longitude": data.longitud}};
            });
        });


    });
