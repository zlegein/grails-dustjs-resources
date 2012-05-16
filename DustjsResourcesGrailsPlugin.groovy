class DustjsResourcesGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [resources:'1.1.6 > *']
    def loadAfter = ['resources']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "DustJs  Resource Plugin" // Headline display name of the plugins
    def author = "Zach Legein"
    def authorEmail = "zlegein@gmail.com"
    def description = "This plugins supports server-side compilation of .dust template files to their .js counterparts."


    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-dustjs-resource"

}
