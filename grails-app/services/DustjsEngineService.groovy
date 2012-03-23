import javax.script.ScriptEngineManager
import javax.script.ScriptEngine

class DustjsEngineService {

    def compile(File input, File target) {
        ScriptEngineManager manager = new ScriptEngineManager()
        ScriptEngine engine = manager.getEngineByName("JavaScript")
        // need to add this to the project that you are using the plugin from
        File dustjs = new File(this.class.getResource('dust-full-0.3.0-modified.js').path)

        if (!dustjs.exists()) {
            throw new FileNotFoundException("dustjs file needed for compilation not found");
        }
        try {
            String data = input.text.replaceAll("\n", "")
            data = data.replaceAll("'", '\'')
            def filename = "${input.parentFile.name}_${input.name.replaceAll(/.dust/, '')}"
            log.debug "Compiling dust file under name: ${filename}"
            String script = dustjs.text + "var compiled = dust.compile('${data}', '${filename}');"
            // run the javascript
            engine.eval(script);
            String compiled = engine.get("compiled")
            target.write(compiled)
        } catch (Exception ex) {
            throw new DustjsCompilerException ("Error compiling file: ${target}", ex)
        }
    }
}
