#dust JS Resource plugin#
This plugin is designed to optimize the use of <a href="https://github.com/linkedin/dustjs">.dust</a> js files. The processing will compile specified .dust files into their .js counterparts, and place the javascript into the processing chain to be available to the other resource plugin features. The plugin uses the <a href="http://www.grails.org/plugin/resources">Resources plugin</a> and plays nicely with both the zipped and cached resources plugins.

##Usage##
An Example of using both .dust and .js files together in a bundle
<pre><code>'dust' {
        resource url:'js/dust-core-0.4.0.js'
        resource url: 'dust/test.dust', attrs: [rel: "javascript/dust", type: 'js'], bundle: 'bundle_dust'        
    }
</code></pre>



###Required Settings for DUST###
<ul>
<li><b>url</b>: The location of the .dust file</li>
<li><b>attrs[rel]</b>: should be set to javascript/dust for compatibility reasons</li>
<li><b>attrs[type]</b>: must be set to js for resources to process</li>
<li><b>bundle</b>: Must be set as will not default correctly. To add to default bundle use 'bundle_<module name>'</li>
</ul>

See the <a href="http://www.grails.org/plugin/resources">Resources plugin</a> for more details on available configurations

##Issues##
<ul>
    <li>The plugin uses a modified version of the <a href="https://github.com/linkedin/dustjs/blob/master/dist/dust-full-0.4.0.js">dust-full-0.4.0.js</a> for compilation. The <b>window</b> object in the script is out of scope and has been replaced with <b>this</b> instead. This plugin will need to be updated when new versions of <a href="https://github.com/linkedin/dustjs">dustjs</a> are released.</li>
    <li>Must specify the default bundle manually as this is calculated based on file extension by default.</li>
    <li>When debug is switched on there is currently no way to fall back to the standard DUST javascript support. The dust files will be rendered unprocessed</li>
</ul>

##Special Thanks##
To Paul Fairless for the <a href="https://github.com/paulfairless/grails-lesscss-resources">grails-lesscss-resources</a> plugin for showing me how to pull this off with a complete rip of his code
