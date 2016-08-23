'use strict';

angular.module('denunciasApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('denuncia', {
                parent: 'entity',
                url: '/denuncias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Denuncias'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/denuncia/denuncias.html',
                        controller: 'DenunciaController'
                    }
                },
                resolve: {
                }
            })
            .state('denuncia.detail', {
                parent: 'entity',
                url: '/denuncia/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Denuncia'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/denuncia/denuncia-detail.html',
                        controller: 'DenunciaDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Denuncia', function($stateParams, Denuncia) {
                        return Denuncia.get({id : $stateParams.id});
                    }]
                }
            })
            .state('denuncia.new', {
                parent: 'denuncia',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/denuncia/denuncia-dialog.html',
                        controller: 'DenunciaDialogController',
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
                        $state.go('denuncia', null, { reload: true });
                    }, function() {
                        $state.go('denuncia');
                    })
                }]
            })
            .state('denuncia.edit', {
                parent: 'denuncia',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/denuncia/denuncia-dialog.html',
                        controller: 'DenunciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Denuncia', function(Denuncia) {
                                return Denuncia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('denuncia', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('denuncia.delete', {
                parent: 'denuncia',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/denuncia/denuncia-delete-dialog.html',
                        controller: 'DenunciaDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Denuncia', function(Denuncia) {
                                return Denuncia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('denuncia', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
