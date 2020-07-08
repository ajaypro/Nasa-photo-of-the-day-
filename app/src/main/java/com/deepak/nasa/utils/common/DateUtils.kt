package com.deepak.nasa.utils.common

import java.text.DecimalFormat
import java.util.*


fun defaultDate(decimalFormat: DecimalFormat, calendar: Calendar) =  "${decimalFormat.format(calendar.get(Calendar.YEAR))}-${decimalFormat.format(
    calendar.get(Calendar.MONTH) + 1)}-${decimalFormat.format(calendar.get(Calendar.DAY_OF_MONTH))}"

