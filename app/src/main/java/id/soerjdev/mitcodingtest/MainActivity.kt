package id.soerjdev.mitcodingtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.soerjdev.mitcodingtest.databinding.ActivityMainBinding
import id.soerjdev.mitcodingtest.model.CharKeyboard
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.apply {
            buttonTransform.setOnClickListener {
                transform()
            }
        }
    }

    private fun transform() {
        val char = binding.editTextInputChar.text.toString().trim().toLowerCase(Locale.getDefault())
        val operator = binding.editTextInputOperation.text.toString().trim().toUpperCase(Locale.getDefault())

        if (char.isEmpty()) {
            Toast.makeText(this, "Please fill your char", Toast.LENGTH_SHORT).show()
            return
        }

        if (operator.isEmpty()) {
            Toast.makeText(this, "Please fill your operator", Toast.LENGTH_SHORT).show()
            return
        }

        val arrayChar = char.split(",").toTypedArray()
        val arrayOperator = operator.split(",").toTypedArray()

        var result = arrayOf("")

        val filteredCharArray = getCharFromArray(arrayChar)

        filteredCharArray.forEach { value ->
            arrayOperator.forEach { operator ->
                var output = ""
                if (operator == "H") {
                    output = transformHorizontal(char = value)
                } else if (operator == "V") {
                    output = transformVertical(char = value)
                } else if (operator.toIntOrNull() != null && operator.toInt() < 10){
                    output = shift(char = value, shiftStep = operator.toInt())
                }
                result += output
            }
        }

        var text = ""
        result.forEach { string ->
            text += string
        }

        binding.textViewResult.text = text
    }

    private fun shift(char: Map<String, Any>, shiftStep: Int): String {
        val keyboardChar = CharKeyboard()

        var index = char["rowIndex"].toString().toInt()
        var indexInArray = char["indexInArray"].toString().toInt() + shiftStep

        if (indexInArray.toString().toInt() > 9) {
            index += 1
            indexInArray += -10
        }

        if (index > 3) {
            index -= 4
        }

        return keyboardChar.keyboardChar[index][indexInArray]
    }

    private fun getCharFromArray(arrayChar: Array<String>): Array<MutableMap<String, Any>> {
        val result = arrayOf(mutableMapOf<String, Any>())

        val charKeyboard = CharKeyboard()

        arrayChar.forEachIndexed { index, char ->
            charKeyboard.keyboardChar.forEachIndexed { indexRow, row ->
                if (row.contains(char)) {
                    val indexInRow = row.indexOf(char)

                    result[index]["rowIndex"] = indexRow
                    result[index]["char"] = char
                    result[index]["indexInArray"] = indexInRow
                }
            }
        }

        return result
    }

    private fun transformVertical(char: Map<String, Any>): String {
        val keyboardChar = CharKeyboard()

        val index = char["rowIndex"]
        val indexInArray = char["indexInArray"]

        return keyboardChar.keyboardChar[3 - index.toString().toInt()][indexInArray.toString().toInt()]
    }

    private fun transformHorizontal(char: Map<String, Any>): String {
        val keyboardChar = CharKeyboard()

        val index = char["rowIndex"]
        val indexInArray = char["indexInArray"]

        return keyboardChar.keyboardChar[index.toString().toInt()][9 - indexInArray.toString().toInt()]
    }
}