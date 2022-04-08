package com.bink.localhero.utils

import java.util.*

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

fun String.dateValidation(): Boolean {
    val new = formatDate()
    if (new.isNotEmpty()) {
        val split = new.split(SEPARATOR_SLASH)
        if (split.size > 1 &&
            !split[0].isBlank() &&
            !split[1].isBlank()
        ) {
            val month = split[0].toInt()
            val year = split[1].toInt() + 2000
            if (month < 1 ||
                month > 12
            ) {
                return false
            }
            val cal = Calendar.getInstance()
            // presuming that a card can't expire more than 10 years in the future
            // the average expiry is about 3 years, but giving more in case
            if (year < cal.get(Calendar.YEAR) ||
                year > cal.get(Calendar.YEAR) + 10
            ) {
                return false
            } else if (year == cal.get(Calendar.YEAR) &&
                month <= cal.get(Calendar.MONTH)
            ) {
                return false
            }
            return true
        }
    }
    return false
}

fun String.formatDate(): String {
    val builder = StringBuilder()
    try {
        val new = replace(REGEX_DECIMAL_OR_SLASH.toRegex(), EMPTY_STRING)
        if (new.isNotEmpty()) {
            val parts = new.split(SEPARATOR_SLASH)
            val year: String
            var month: String
            if (parts.size == 1) {
                val len = kotlin.math.max(0, length - 2)
                month = new.substring(0, len)
                year = new.substring(len)
            } else {
                month = parts[0]
                year = parts[1]
            }
            month = "00$month"
            builder.append(month.substring(month.length - 2))
            builder.append(SEPARATOR_SLASH)
            builder.append(year)
        }
        return builder.toString()
    } catch (e: StringIndexOutOfBoundsException) {
        return ""
    }
}

