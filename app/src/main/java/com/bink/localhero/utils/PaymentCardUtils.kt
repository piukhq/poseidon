package com.bink.localhero.utils

fun String.cardValidation(): PaymentCardType {
    if (!isValidLuhnFormat()) {
        return PaymentCardType.NONE
    }
    val sanitizedInput = ccSanitize()
    val paymentType = sanitizedInput.presentedCardType()
    return if (sanitizedInput.length == paymentType.len &&
        sanitizedInput.isValidLuhnFormat()
    ) {
        paymentType
    } else {
        PaymentCardType.NONE
    }
}

fun String.numberSanitize() = replace(REGEX_DECIMAL_ONLY.toRegex(), EMPTY_STRING)

fun String.ccSanitize() = replace(SPACE, EMPTY_STRING)

fun String.isValidLuhnFormat(): Boolean {
    val sanitizedInput = ccSanitize()
    return when {
        sanitizedInput != numberSanitize() -> false
        sanitizedInput.luhnLengthInvalid() -> false
        sanitizedInput.luhnValidPopulated() -> sanitizedInput.luhnChecksum() % 10 == 0
        else -> false
    }
}

fun String.luhnValidPopulated() = all(Char::isDigit) && length > 1

fun String.luhnLengthInvalid() =
    !(length == DIGITS_VISA_MASTERCARD ||
            length == DIGITS_AMERICAN_EXPRESS)

fun String.luhnChecksum() = luhnMultiply().sum()

fun String.luhnMultiply() = digits().mapIndexed { i, j ->
    when {
        (length - i + 1) % 2 == 0 -> j
        j >= 5 -> j * 2 - 9
        else -> j * 2
    }
}

fun String.digits() = map(Character::getNumericValue)

fun String.presentedCardType(): PaymentCardType {
    val sanitizedInput = numberSanitize()
    if (sanitizedInput.isEmpty()) {
        return PaymentCardType.NONE
    }
    PaymentCardType.values().forEach {
        if (it != PaymentCardType.NONE &&
            it.len >= sanitizedInput.length
        ) {
            val splits = it.prefix.split(SEPARATOR_PIPE)
            for (prefix in splits) {
                if (splits.size <= 1 ||
                    prefix.length != 1 ||
                    sanitizedInput.length <= prefix.length
                ) {
                    val range = prefix.split(SEPARATOR_HYPHEN)
                    if (range.size > 1 &&
                        sanitizedInput >= range[0] &&
                        sanitizedInput <= range[1]
                    ) {
                        return it
                    } else if (sanitizedInput.length >= prefix.length &&
                        sanitizedInput.substring(0, prefix.length) == prefix
                    ) {
                        return it
                    }
                }
            }
        }
    }
    return PaymentCardType.NONE
}

