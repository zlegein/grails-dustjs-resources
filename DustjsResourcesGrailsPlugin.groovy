import org.grails.plugin.resource.ResourceTagLib
import org.grails.plugin.resource.ResourceProcessor

class DustjsResourcesGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "DustJs  Resource Plugin" // Headline display name of the plugins
    def author = "Zach Legein"
    def authorEmail = "zlegein@gmail.com"
    def description = "This plugins supports server-side compilation of .dust template files to their .js counterparts."

    def watchedResources = [ "file:./src/dust/*.dust", "file:./dust/*.dust", "file:./src/dust/**/*.dust","file:./dust/**/*.dust"  ]
    def documentation = "http://github.com/zlegein/grails-dustjs-resources"
    def issueManagement = [ system: "GitHub", url: "http://github.com/zlegein/grails-dustjs-resources/issues" ]

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def doWithApplicationContext = {
    }

    def doWithSpring = { ->
        ResourceTagLib.SUPPORTED_TYPES['dust'] = [type:'text/javascript', writer:'js']
        ResourceProcessor.DEFAULT_MODULE_SETTINGS['dust'] = [
                disposition: 'defer'
        ]
    }

}
