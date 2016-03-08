'use strict';

angular.module('denunciasApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('denuncia-ant', {
                parent: 'entity',
                url: '/denuncias-ant',
                data: {
                    authorities: ['ROLE_ANT'],
                    pageTitle: 'Denuncias'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/denuncia-ant/denuncias-ant.html',
                        controller: 'DenunciaAntController'
                    }
                },
                resolve: {
                }
            })
            .state('denuncia-ant.detail', {
                parent: 'entity',
                url: '/denuncia-ant/{id}',
                data: {
                    authorities: ['ROLE_ANT'],
                    pageTitle: 'Denuncia'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/denuncia-ant/denuncia-ant-detail.html',
                        controller: 'DenunciaAntDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Denuncia', function($stateParams, Denuncia) {
                        return Denuncia.get({id : $stateParams.id});
                    }]
                }
            })
            .state('denuncia-ant.new', {
                parent: 'denuncia-ant',
                url: '/new',
                data: {
                    authorities: ['ROLE_ANT'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/denuncia-ant/denuncia-ant-dialog.html',
                        controller: 'DenunciaAntDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    codigo: null,
                                    fecha: null,
                                    sancionable: null,
                                    latitud: null,
                                    longitud: null,
                                    placa: null,
                                    estado: null,
                                    foto: null,
                                    fotoContentType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('denuncia-ant', null, { reload: true });
                    }, function() {
                        $state.go('denuncia-ant');
                    })
                }]
            })
            .state('denuncia-ant.edit', {
                parent: 'denuncia-ant',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ANT'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/denuncia-ant/denuncia-ant-dialog.html',
                        controller: 'DenunciaAntDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Denuncia', function(Denuncia) {
                                return Denuncia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('denuncia-ant', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('denuncia-ant.delete', {
                parent: 'denuncia-ant',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ANT'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/denuncia-ant/denunciaant--delete-dialog.html',
                        controller: 'DenunciaAntDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Denuncia', function(Denuncia) {
                                return Denuncia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('denuncia-ant', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
