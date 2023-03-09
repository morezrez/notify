package mamali.qa.notify.Utils

fun String.toPersianDigit(): String {
    var result = ""
    var text: Char
    for (character in this) {
        text = character
        when (character) {
            '0' -> text = '۰'
            '1' -> text = '۱'
            '2' -> text = '۲'
            '3' -> text = '۳'
            '4' -> text = '۴'
            '5' -> text = '۵'
            '6' -> text = '۶'
            '7' -> text = '۷'
            '8' -> text = '۸'
            '9' -> text = '۹'
        }
        result += text
    }
    return result
}
