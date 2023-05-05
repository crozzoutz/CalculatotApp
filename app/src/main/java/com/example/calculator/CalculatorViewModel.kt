package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class CalculatorViewModel : ViewModel() {

    // variables to store numbers
    private var number1: Double? = null
    private var pendingOperation = "="

    private val result = MutableLiveData<Double?>()
    val stringResult: LiveData<String>
        get() = result.map{result->result.toString()}

    private val newNumber = MutableLiveData<String>()
    val stringNewNumber: LiveData<String>
        get() = newNumber

    private val displayOperation = MutableLiveData<String>()
    val stringDisplayOperation: LiveData<String>
        get() = displayOperation


    fun numberPressed(caption: String) {
        if (newNumber.value != null) {
            newNumber.value = newNumber.value + caption
        } else {
            newNumber.value = caption
        }
    }

    fun operandPressed(operand: String) {
        try {
            val value = newNumber.value?.toDouble()
            if (value != null) {
                performOperation(value, operand)
            }
        } catch (e: NumberFormatException) {
            newNumber.value = ""

        }
        pendingOperation = operand
        displayOperation.value = pendingOperation
    }

    fun negPressed() {
        val value = newNumber.value

        if (value == null || value.isEmpty()) {
            newNumber.value = "-"
        } else {
            try {
                var doubleValue = value.toDouble()
                doubleValue *= -1
                newNumber.value = doubleValue.toString()

            } catch (e: NumberFormatException) {
                //newNumber was "-" or "." , so cleaning it
                newNumber.value = ""
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (number1 == null) {
            number1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> number1 = value
                "/" -> number1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    number1!! / value
                }

                "*" -> number1 = number1!! * value
                "-" -> number1 = number1!! - value
                "+" -> number1 = number1!! + value
            }
        }
        result.value = number1
        newNumber.value = ""
    }

}