package org.grails.plugins.dustjs

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.springframework.core.io.ClassPathResource

class DustjsEngine {

    def Scriptable globalScope
    def ClassLoader classLoader

    def DustjsEngine(){
        try {
            classLoader = getClass().getClassLoader()

            def dustJsResource = (new ClassPathResource('org/grails/plugins/dustjs/dust-full-1.0.0.js', getClass().classLoader))
            assert dustJsResource.exists() : "DustJs resource not found"

            def dustJsStream = dustJsResource.inputStream

            Context cx = Context.enter()
            cx.setOptimizationLevel(-1)
            globalScope = cx.initStandardObjects()
            cx.evaluateReader(globalScope, new InputStreamReader(dustJsStream, 'UTF-8'), dustJsResource.filename, 0, null)
        } catch (Exception e) {
            throw new Exception("DustJs Engine initialization failed.", e)
        } finally {
            try {
                Context.exit()
            } catch (java.lang.IllegalStateException ise) {
                log.error ise
            }
        }
    }

    def compile(File input, File target, String templateName) {
        try {
            def cx = Context.enter()
            def compileScope = cx.newObject(globalScope)
            compileScope.setParentScope(globalScope)
            compileScope.put("templateInput", compileScope, input.text)
            compileScope.put("templateName", compileScope, templateName)
            log.debug "Compiling dust file under name: ${templateName}"
            String output = cx.evaluateString(compileScope, "dust.compile(templateInput, templateName)", "DustJs compile command", 0, null)
            target.write(output)
        } catch (Exception e) {
            throw new Exception("DustJs Engine compilation of dustjs to javascript failed.", e)
        } finally {
            try {
                Context.exit()
            } catch (java.lang.IllegalStateException ise) {
                log.error ise
            }
        }
    }
}