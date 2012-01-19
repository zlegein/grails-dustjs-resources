class DustjsGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [resources:'1.1.3 > *']
    def loadAfter = ['resources']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/*.gsp",
        "web-app/dust/*"
    ]

    // TODO Fill in these fields
    def title = "Dustjs  Plugin" // Headline display name of the plugin
    def author = "Zach Legein & Zach Lendon"
    def authorEmail = ""
    def description = '''\\
This plugin supports server-side compilation of .dust template files to their .js counterparts.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/dustjs-plugin"

}
