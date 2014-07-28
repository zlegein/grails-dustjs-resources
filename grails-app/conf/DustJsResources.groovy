modules = {

    dustjs100 {
      resource url: [plugin: 'dustjs-resources', dir:'js', file:'dust-core-1.0.0.js']
    }

    dustjs111 {
        resource url: [plugin: 'dustjs-resources', dir:'js', file:'dust-core-1.1.1.js']
        resource url: [plugin: 'dustjs-resources', dir:'js', file:'dust-helpers-1.1.0.js']
    }

    dustjs210 {
        resource url: [plugin: 'dustjs-resources', dir:'js', file:'dust-core-2.1.0.js']
        resource url: [plugin: 'dustjs-resources', dir:'js', file:'dust-helpers-1.1.1.js']
    }

    dustjs {
        dependsOn 'dustjs210'
    }

}
