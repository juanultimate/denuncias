'use strict';

angular.module('denunciasApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('foto', {
                parent: 'entity',
                url: '/fotos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Fotos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/foto/fotos.html',
                        controller: 'FotoController'
                    }
                },
                resolve: {
                }
            })
            .state('foto.detail', {
                parent: 'entity',
                url: '/foto/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Foto'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/foto/foto-detail.html',
                        controller: 'FotoDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Foto', function($stateParams, Foto) {
                        return Foto.get({id : $stateParams.id});
                    }]
                }
            })
            .state('foto.new', {
                parent: 'foto',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/foto/foto-dialog.html',
                        controller: 'FotoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    codigo: null,
                                    data: null,
                                    dataContentType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('foto', null, { reload: true });
                    }, function() {
                        $state.go('foto');
                    })
                }]
            })
            .state('foto.edit', {
                parent: 'foto',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/foto/foto-dialog.html',
                        controller: 'FotoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Foto', function(Foto) {
                                return Foto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('foto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('foto.delete', {
                parent: 'foto',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/foto/foto-delete-dialog.html',
                        controller: 'FotoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Foto', function(Foto) {
                                return Foto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('foto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
