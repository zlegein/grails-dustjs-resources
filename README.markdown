#dust JS Resource plugin#
This plugin is designed to optimize the use of <a href="https://github.com/linkedin/dustjs">.dust</a> js files. The processing will compile specified .dust files into their .js counterparts, and place the javascript into the processing chain to be available to the other resource plugin features. The plugin uses the <a href="http://www.grails.org/plugin/resources">Resources plugin</a> and plays nicely with both the zipped and cached resources plugins. It will also include the core of the dust library which is required to render dust templates on the client.

## Installation

    plugins {
        runtime: 'dustjs-resources:0.3'
    }

## Usage

### Declaring Resources

    modules = {
      dust {
        dependsOn 'dustjs'
        resource url: 'dust/example.dust'
      }
    }

#### Settings

*   **dependsOn**: `dustjs`.
*   **url**: location of the dust template file.
*   **attrs[type]**: must be `js`.
*   **bundle**: must be set as will not default correctly. To add to default bundle use `bundle_<module name>`.

## Template Names

Template names are based on the resource URL. If the URL is `templates/foo.dust`, then the template name will be `templates/foo`.
Note that the `.dust` extension is removed.

The default path separator is `/`. If you want to change it, you can specify a value for `templatesPathSeparator` in the configuration. For example,
adding

    grails.resources.mappers.dustjs.templatesPathSeparator = '_'

will change the template name to `templates_foo`.

If you specify a value for `templatesRoot` in the configuration, then that value will be stripped from the template name. For example, adding

    grails.resources.mappers.dustjs.templatesRoot = 'templates'

will change the template name to just `foo`.

## Configuration

All configuration variables should be relative to:

    grails.resources.mappers.dustjs

*   **templatesRoot**: The root folder of the templates relative to `web-app`. This value will be stripped from template paths when calculating the template name. Default is none.
*   **templatesPathSeparator**: The delimiter to use for template names. Default is `/`


##Special Thanks##
* To Paul Fairless for the <a href="https://github.com/paulfairless/grails-lesscss-resources">grails-lesscss-resources</a> plugin for showing me how to pull this off
* To Matt Sheehan for the <a href="https://github.com/sheehan/grails-handlebars-resources">grails-handlebars-resources</a> plugin for greatly improving this plugin
