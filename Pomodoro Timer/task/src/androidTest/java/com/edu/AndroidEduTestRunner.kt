package com.edu

import androidx.test.runner.AndroidJUnitRunner

class AndroidEduTestRunner : AndroidJUnitRunner() {
    override fun sendStatus(resultCode: Int, results: android.os.Bundle) {
        testNum++
        if (resultCode < 0) {
            val stack = results.getString("stack")
            if (stack != null) {
                val errorMessage = stack.substringAfter(":").trim().substringBefore("\n")
                results.putString("stack", "#educational_plugin FAILED + " +
                        "Wrong answer in test #${testNum / 2} $errorMessage")
            }
        }
        super.sendStatus(resultCode, results)
    }

    companion object {
        var testNum = 0
    }
}
