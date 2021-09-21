import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

abstract class BaseTest extends Specification {

    static JSONObject txInputData

    static {
        def path = Paths.get("src", "test", "resources", "txInputData.json")
        def bufferedReader = Files.newBufferedReader(path)
        def tokener = new JSONTokener(bufferedReader)
        txInputData = new JSONObject(tokener)
    }
}
