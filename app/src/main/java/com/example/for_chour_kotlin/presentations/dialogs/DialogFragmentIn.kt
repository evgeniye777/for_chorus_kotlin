package com.example.for_chour_kotlin.presentations.dialogs
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import com.example.for_chour_kotlin.R

class DialogFragmentIn(private val context: Context) {
    fun showDialog(onResult: (login: String, password: String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_fragment_in, null)
        val loginEditText = dialogView.findViewById<EditText>(R.id.dialogEditTextLogin)
        val passwordEditText = dialogView.findViewById<EditText>(R.id.dialogEditTextPassword)
        AlertDialog.Builder(context)
            .setTitle("Введите данные")
            .setView(dialogView)
            .setPositiveButton("Войти") { _, _ ->
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()
                onResult(login, password)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}