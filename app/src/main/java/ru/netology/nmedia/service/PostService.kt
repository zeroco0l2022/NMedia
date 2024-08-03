package ru.netology.nmedia.service


fun DisplayNumber(num: Int): String {
    var numDelta: Float = (num / 100).toFloat() / 10
    var coef: String = "K"
    if (numDelta >= 1000) {
        numDelta = (numDelta / 100).toInt().toFloat() / 10
        coef = "M"
    }

    return if (num < 1000) {
        num.toString()
    } else if (numDelta >= 10 || numDelta == numDelta.toInt().toFloat()) {
        String.format("%d%s", numDelta.toInt(), coef)
    } else {
        String.format("%.${1}f%s", numDelta, coef)
    }

}

